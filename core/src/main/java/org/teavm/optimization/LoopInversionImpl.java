/*
 *  Copyright 2016 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.optimization;

import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.IntSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.teavm.common.DominatorTree;
import org.teavm.common.Graph;
import org.teavm.common.GraphUtils;
import org.teavm.common.Loop;
import org.teavm.common.LoopGraph;
import org.teavm.model.BasicBlock;
import org.teavm.model.Incoming;
import org.teavm.model.Instruction;
import org.teavm.model.Phi;
import org.teavm.model.Program;
import org.teavm.model.TryCatchBlock;
import org.teavm.model.Variable;
import org.teavm.model.util.BasicBlockMapper;
import org.teavm.model.util.InstructionCopyReader;
import org.teavm.model.util.PhiUpdater;
import org.teavm.model.util.ProgramUtils;

/**
 * Transforms loop in form:
 *
 * ```
 * while (true) {
 *     condition;
 *     body;
 * }
 * ```
 *
 * to form:
 *
 * ```
 * if (condition) {
 *     while (true) {
 *         body;
 *         condition; // copy
 *     }
 * }
 * ```
 *
 * where `condition` is a part of loop that has exits and `body` has no exits.
 * More formally, we define *body start candidate* as a node which 1) dominates all of the "tails" (i.e. nodes
 * that have edges to loop header), 2) does not dominate loop exits. *Body start* is a body start candidate
 * that is not dominated by some other body start candidate. If body start does not exits, loop is
 * not invertible.
 *
 * Therefore, *body* is a set of nodes of the loop that are dominated by body start and
 * all remaining nodes are *condition*.
 */
class LoopInversionImpl {
    private final Program program;
    private final int parameterCount;
    private Graph cfg;
    private DominatorTree dom;
    private boolean postponed;
    private boolean changed;

    LoopInversionImpl(Program program, int parameterCount) {
        this.program = program;
        this.parameterCount = parameterCount;
    }

    void apply() {
        do {
            cfg = ProgramUtils.buildControlFlowGraph(program);
            LoopGraph loopGraph = new LoopGraph(cfg);
            dom = GraphUtils.buildDominatorTree(cfg);
            List<LoopWithExits> loops = getLoopsWithExits(loopGraph);

            postponed = false;
            if (!loops.isEmpty()) {
                for (LoopWithExits loop : loops) {
                    loop.invert();
                }
                if (changed) {
                    Variable[] inputs = new Variable[parameterCount];
                    for (int i = 0; i < inputs.length; ++i) {
                        inputs[i] = program.variableAt(i);
                    }
                    new PhiUpdater().updatePhis(program, inputs);
                }
            }
        } while (postponed);
    }

    private List<LoopWithExits> getLoopsWithExits(LoopGraph cfg) {
        Map<Loop, LoopWithExits> loops = new HashMap<>();

        for (int node = 0; node < cfg.size(); ++node) {
            Loop loop = cfg.loopAt(node);
            while (loop != null) {
                LoopWithExits loopWithExits = getLoopWithExits(loops, loop);
                loopWithExits.nodes.add(node);
                for (int successor : cfg.outgoingEdges(node)) {
                    Loop successorLoop = cfg.loopAt(successor);
                    if (successorLoop == null || !successorLoop.isChildOf(loop)) {
                        loopWithExits.exits.add(node);
                        break;
                    }
                }
                loop = loop.getParent();
            }
        }

        List<LoopWithExits> resultList = new ArrayList<>();
        Set<LoopWithExits> visited = new HashSet<>();
        for (LoopWithExits loop : loops.values()) {
            sortLoops(loop, visited, resultList);
        }
        Collections.reverse(resultList);

        return resultList;
    }

    private LoopWithExits getLoopWithExits(Map<Loop, LoopWithExits> cache, Loop loop) {
        return cache.computeIfAbsent(loop, key ->
                new LoopWithExits(key.getHead(), key.getParent() != null
                        ? getLoopWithExits(cache, key.getParent())
                        : null));
    }

    private void sortLoops(LoopWithExits loop, Set<LoopWithExits> visited, List<LoopWithExits> target) {
        if (!visited.add(loop)) {
            return;
        }
        if (loop.parent != null) {
            sortLoops(loop.parent, visited, target);
        }
        target.add(loop);
    }

    private class LoopWithExits {
        final int head;
        final LoopWithExits parent;
        final IntSet nodes = new IntOpenHashSet();
        final IntSet nodesAndCopies = new IntOpenHashSet();
        final IntSet exits = new IntOpenHashSet();
        int bodyStart;
        int copyStart;
        int headCopy;
        final IntIntMap copiedNodes = new IntIntOpenHashMap();
        boolean shouldSkip;

        LoopWithExits(int head, LoopWithExits parent) {
            this.head = head;
            this.parent = parent;
        }

        void invert() {
            if (tryInvert()) {
                LoopWithExits ancestor = parent;
                while (ancestor != null) {
                    ancestor.shouldSkip = true;
                    ancestor = ancestor.parent;
                }
            }
        }

        private boolean tryInvert() {
            if (shouldSkip) {
                postponed = true;
                return false;
            }

            if (!findCondition() || bodyStart < 0) {
                return false;
            }

            collectNodesToCopy();
            copyCondition();
            moveBackEdges();
            removeInternalPhiInputsFromCondition();
            removeExternalPhiInputsFromConditionCopy();

            changed = true;
            return true;
        }

        private boolean findCondition() {
            IntSet tailNodes = new IntOpenHashSet(program.basicBlockCount());
            for (int tailCandidate : cfg.incomingEdges(head)) {
                if (nodes.contains(tailCandidate)) {
                    tailNodes.add(tailCandidate);
                }
            }

            bodyStart = dom.commonDominatorOf(tailNodes.toArray());
            int candidate = bodyStart;
            while (bodyStart != head) {
                int currentCandidate = candidate;
                if (Arrays.stream(exits.toArray()).anyMatch(exit -> dom.dominates(currentCandidate, exit))) {
                    break;
                }
                bodyStart = candidate;
                candidate = dom.immediateDominatorOf(candidate);
            }

            return candidate != bodyStart;
        }

        private void collectNodesToCopy() {
            int[] nodes = this.nodes.toArray();
            Arrays.sort(nodes);
            for (int node : nodes) {
                nodesAndCopies.add(node);
                if (node == head || (node != bodyStart && !dom.dominates(bodyStart, node))) {
                    int copy = program.createBasicBlock().getIndex();
                    if (head == node) {
                        headCopy = copy;
                    }
                    copiedNodes.put(node, copy);
                    nodesAndCopies.add(copy);
                }
            }
        }

        private void copyCondition() {
            BasicBlockMapper blockMapper = new BasicBlockMapper(block -> copiedNodes.getOrDefault(block, block));

            InstructionCopyReader copier = new InstructionCopyReader(program);
            for (int node : copiedNodes.keys().toArray()) {
                BasicBlock sourceBlock = program.basicBlockAt(node);
                BasicBlock targetBlock = program.basicBlockAt(copiedNodes.get(node));
                copier.resetLocation();
                for (int i = 0; i < sourceBlock.instructionCount(); ++i) {
                    sourceBlock.readInstruction(i, copier);
                    Instruction insn = copier.getCopy();
                    insn.acceptVisitor(blockMapper);
                    targetBlock.getInstructions().add(insn);
                }

                for (Phi phi : sourceBlock.getPhis()) {
                    Phi phiCopy = new Phi();
                    phiCopy.setReceiver(phi.getReceiver());
                    for (Incoming incoming : phi.getIncomings()) {
                        Incoming incomingCopy = new Incoming();
                        int source = incoming.getSource().getIndex();
                        incomingCopy.setSource(program.basicBlockAt(copiedNodes.getOrDefault(source, source)));
                        incomingCopy.setValue(incoming.getValue());
                        phiCopy.getIncomings().add(incomingCopy);
                    }
                    targetBlock.getPhis().add(phiCopy);
                }

                for (TryCatchBlock tryCatch : sourceBlock.getTryCatchBlocks()) {
                    TryCatchBlock tryCatchCopy = new TryCatchBlock();
                    int handler = tryCatch.getHandler().getIndex();
                    tryCatchCopy.setExceptionType(tryCatch.getExceptionType());
                    tryCatchCopy.setExceptionVariable(tryCatch.getExceptionVariable());
                    tryCatchCopy.setHandler(program.basicBlockAt(copiedNodes.getOrDefault(handler, handler)));
                    targetBlock.getTryCatchBlocks().add(tryCatchCopy);
                }
            }
        }

        /**
         * Back edges from body are not back edges anymore, instead they point to a copied condition.
         */
        private void moveBackEdges() {
            BasicBlockMapper mapper = new BasicBlockMapper(block -> block == head ? headCopy : block);

            for (int node : nodes.toArray()) {
                BasicBlock block = program.basicBlockAt(node);
                Instruction last = block.getLastInstruction();
                if (last != null) {
                    last.acceptVisitor(mapper);
                }
            }
        }

        /**
         * Original head becomes start of `if (condition)`, it's not loop head anymore.
         * Hence we don't need phi inputs that come from back edges.
         */
        private void removeInternalPhiInputsFromCondition() {
            BasicBlock block = program.basicBlockAt(head);
            for (Phi phi : block.getPhis()) {
                List<Incoming> incomings = phi.getIncomings();
                for (int i = 0; i < incomings.size(); ++i) {
                    Incoming incoming = incomings.get(i);
                    if (nodes.contains(incoming.getSource().getIndex())) {
                        incomings.remove(i--);
                    }
                }
            }
        }

        /**
         * Head copy is not a loop head anymore and there aren't transition from outside of former loop,
         * therefore delete all external phi inputs.
         */
        private void removeExternalPhiInputsFromConditionCopy() {
            BasicBlock block = program.basicBlockAt(headCopy);
            for (Phi phi : block.getPhis()) {
                List<Incoming> incomings = phi.getIncomings();
                for (int i = 0; i < incomings.size(); ++i) {
                    Incoming incoming = incomings.get(i);
                    if (!nodesAndCopies.contains(incoming.getSource().getIndex())) {
                        incomings.remove(i--);
                    }
                }
            }
        }
    }
}
