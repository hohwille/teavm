/*
 *  Copyright 2020 generated by Joerg Hohwiller.
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
package org.teavm.classlib.java.time.zone;

import java.util.Map;
import org.teavm.classlib.java.time.zone.TZoneOffsetTransitionRule.TimeDefinition;

final class TTzdbInternalPacific {
    static void init(Map<String, TZoneRules> map) {
        map.put("Pacific/Apia", new TStandardZoneRules(new long[] {-2445424384L, -1861878784L, -631110600L, 1325239200L}, new int[] {45184, -41216, -41400, -39600, 46800}, new long[] {-2445424384L, -1861878784L, -631110600L, 1285498800L, 1301752800L, 1316872800L, 1325239200L, 1333202400L, 1348927200L, 1365256800L, 1380376800L}, new int[] {45184, -41216, -41400, -39600, -36000, -39600, -36000, 50400, 46800, 50400, 46800, 50400}, new TZoneOffsetTransitionRule(4,1,7,14400,0,1,46800,50400,46800), new TZoneOffsetTransitionRule(9,24,7,10800,0,1,46800,46800,50400)));
        map.put("Pacific/Auckland", new TStandardZoneRules(new long[] {-3192435544L, -757425600L}, new int[] {41944, 41400, 43200}, new long[] {-3192435544L, -1330335000L, -1320057000L, -1300699800L, -1287396000L, -1269250200L, -1255946400L, -1237800600L, -1224496800L, -1206351000L, -1192442400L, -1174901400L, -1160992800L, -1143451800L, -1125914400L, -1112607000L, -1094464800L, -1081157400L, -1063015200L, -1049707800L, -1031565600L, -1018258200L, -1000116000L, -986808600L, -968061600L, -955359000L, -936612000L, -923304600L, 152632800L, 162309600L, 183477600L, 194968800L, 215532000L, 226418400L, 246981600L, 257868000L, 278431200L, 289317600L, 309880800L, 320767200L, 341330400L, 352216800L, 372780000L, 384271200L, 404834400L, 415720800L, 436284000L, 447170400L, 467733600L, 478620000L, 499183200L, 510069600L, 530632800L, 541519200L, 562082400L, 573573600L, 594136800L, 605023200L, 623772000L, 637682400L, 655221600L, 669132000L, 686671200L, 700581600L, 718120800L, 732636000L, 749570400L, 764085600L, 781020000L, 795535200L, 812469600L, 826984800L, 844524000L, 858434400L, 875973600L, 889884000L, 907423200L, 921938400L, 938872800L, 953388000L, 970322400L, 984837600L, 1002376800L, 1016287200L, 1033826400L, 1047736800L, 1065276000L, 1079791200L, 1096725600L, 1111240800L, 1128175200L, 1142690400L, 1159624800L, 1174140000L, 1191074400L, 1207404000L, 1222524000L, 1238853600L, 1253973600L}, new int[] {41944, 41400, 45000, 41400, 43200, 41400, 43200, 41400, 43200, 41400, 43200, 41400, 43200, 41400, 43200, 41400, 43200, 41400, 43200, 41400, 43200, 41400, 43200, 41400, 43200, 41400, 43200, 41400, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800}, new TZoneOffsetTransitionRule(4,1,7,7200,0,2,43200,46800,43200), new TZoneOffsetTransitionRule(9,24,7,7200,0,2,43200,43200,46800)));
        map.put("Pacific/Bougainville", new TStandardZoneRules(new long[] {-2840178136L, -2366790512L, -868010400L, -768906000L, 1419696000L}, new int[] {37336, 35312, 36000, 32400, 36000, 39600}, new long[] {-2840178136L, -2366790512L, -868010400L, -768906000L, 1419696000L}, new int[] {37336, 35312, 36000, 32400, 36000, 39600}));
        map.put("Pacific/Chatham", new TStandardZoneRules(new long[] {-3192437628L, -757426500L}, new int[] {44028, 44100, 45900}, new long[] {-3192437628L, -757426500L, 152632800L, 162309600L, 183477600L, 194968800L, 215532000L, 226418400L, 246981600L, 257868000L, 278431200L, 289317600L, 309880800L, 320767200L, 341330400L, 352216800L, 372780000L, 384271200L, 404834400L, 415720800L, 436284000L, 447170400L, 467733600L, 478620000L, 499183200L, 510069600L, 530632800L, 541519200L, 562082400L, 573573600L, 594136800L, 605023200L, 623772000L, 637682400L, 655221600L, 669132000L, 686671200L, 700581600L, 718120800L, 732636000L, 749570400L, 764085600L, 781020000L, 795535200L, 812469600L, 826984800L, 844524000L, 858434400L, 875973600L, 889884000L, 907423200L, 921938400L, 938872800L, 953388000L, 970322400L, 984837600L, 1002376800L, 1016287200L, 1033826400L, 1047736800L, 1065276000L, 1079791200L, 1096725600L, 1111240800L, 1128175200L, 1142690400L, 1159624800L, 1174140000L, 1191074400L, 1207404000L, 1222524000L, 1238853600L, 1253973600L}, new int[] {44028, 44100, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500, 45900, 49500}, new TZoneOffsetTransitionRule(4,1,7,9900,0,2,45900,49500,45900), new TZoneOffsetTransitionRule(9,24,7,9900,0,2,45900,45900,49500)));
        map.put("Pacific/Chuuk", new TStandardZoneRules(new long[] {-3944628428L, -2177489228L, -1743674400L, -1606813200L, -907408800L, -770634000L}, new int[] {-49972, 36428, 36000, 32400, 36000, 32400, 36000}, new long[] {-3944628428L, -2177489228L, -1743674400L, -1606813200L, -907408800L, -770634000L}, new int[] {-49972, 36428, 36000, 32400, 36000, 32400, 36000}));
        map.put("Pacific/Easter", new TStandardZoneRules(new long[] {-1178124152L, 384922800L}, new int[] {-26248, -25200, -21600}, new long[] {-1178124152L, -36619200L, -23922000L, -3355200L, 7527600L, 24465600L, 37767600L, 55915200L, 69217200L, 87969600L, 100666800L, 118209600L, 132116400L, 150868800L, 163566000L, 182318400L, 195620400L, 213768000L, 227070000L, 245217600L, 258519600L, 277272000L, 289969200L, 308721600L, 321418800L, 340171200L, 353473200L, 371620800L, 403070400L, 416372400L, 434520000L, 447822000L, 466574400L, 479271600L, 498024000L, 510721200L, 529473600L, 545194800L, 560923200L, 574225200L, 592372800L, 605674800L, 624427200L, 637124400L, 653457600L, 668574000L, 687326400L, 700628400L, 718776000L, 732078000L, 750225600L, 763527600L, 781675200L, 794977200L, 813729600L, 826426800L, 845179200L, 859690800L, 876628800L, 889930800L, 906868800L, 923194800L, 939528000L, 952830000L, 971582400L, 984279600L, 1003032000L, 1015729200L, 1034481600L, 1047178800L, 1065931200L, 1079233200L, 1097380800L, 1110682800L, 1128830400L, 1142132400L, 1160884800L, 1173582000L, 1192334400L, 1206846000L, 1223784000L, 1237086000L, 1255233600L, 1270350000L, 1286683200L, 1304823600L, 1313899200L, 1335668400L, 1346558400L, 1367118000L, 1378612800L, 1398567600L, 1410062400L, 1463281200L, 1471147200L, 1494730800L, 1502596800L, 1526180400L, 1534046400L, 1554606000L, 1567915200L, 1586055600L, 1599364800L}, new int[] {-26248, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -25200, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000, -21600, -18000}, new TZoneOffsetTransitionRule(4,2,7,10800,0,0,-21600,-18000,-21600), new TZoneOffsetTransitionRule(9,2,7,14400,0,0,-21600,-21600,-18000)));
        map.put("Pacific/Efate", new TStandardZoneRules(new long[] {-1829387596L}, new int[] {40396, 39600}, new long[] {-1829387596L, 433256400L, 448977600L, 467298000L, 480427200L, 496760400L, 511876800L, 528210000L, 543931200L, 559659600L, 575380800L, 591109200L, 606830400L, 622558800L, 638280000L, 654008400L, 669729600L, 686062800L, 696340800L, 719931600L, 727790400L}, new int[] {40396, 39600, 43200, 39600, 43200, 39600, 43200, 39600, 43200, 39600, 43200, 39600, 43200, 39600, 43200, 39600, 43200, 39600, 43200, 39600, 43200, 39600}));
        map.put("Pacific/Enderbury", new TStandardZoneRules(new long[] {-2177411740L, 307627200L, 788871600L}, new int[] {-41060, -43200, -39600, 46800}, new long[] {-2177411740L, 307627200L, 788871600L}, new int[] {-41060, -43200, -39600, 46800}));
        map.put("Pacific/Fakaofo", new TStandardZoneRules(new long[] {-2177411704L, 1325242800L}, new int[] {-41096, -39600, 46800}, new long[] {-2177411704L, 1325242800L}, new int[] {-41096, -39600, 46800}));
        map.put("Pacific/Fiji", new TStandardZoneRules(new long[] {-1709985344L}, new int[] {42944, 43200}, new long[] {-1709985344L, 909842400L, 920124000L, 941896800L, 951573600L, 1259416800L, 1269698400L, 1287842400L, 1299333600L, 1319292000L, 1327154400L, 1350741600L, 1358604000L, 1382796000L, 1390050000L, 1414850400L, 1421503200L, 1446300000L, 1452952800L, 1478354400L, 1484402400L, 1509804000L, 1515852000L, 1541253600L, 1547301600L, 1573308000L, 1578751200L, 1604757600L}, new int[] {42944, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800, 43200, 46800}, new TZoneOffsetTransitionRule(1,12,7,10800,0,1,43200,46800,43200), new TZoneOffsetTransitionRule(11,8,7,7200,0,1,43200,43200,46800)));
        map.put("Pacific/Funafuti", new TStandardZoneRules(new long[] {-2177495812L}, new int[] {43012, 43200}, new long[] {-2177495812L}, new int[] {43012, 43200}));
        map.put("Pacific/Galapagos", new TStandardZoneRules(new long[] {-1230746496L, 504939600L}, new int[] {-21504, -18000, -21600}, new long[] {-1230746496L, 504939600L, 722930400L, 728888400L}, new int[] {-21504, -18000, -21600, -18000, -21600}));
        map.put("Pacific/Gambier", new TStandardZoneRules(new long[] {-1806678012L}, new int[] {-32388, -32400}, new long[] {-1806678012L}, new int[] {-32388, -32400}));
        map.put("Pacific/Guadalcanal", new TStandardZoneRules(new long[] {-1806748788L}, new int[] {38388, 39600}, new long[] {-1806748788L}, new int[] {38388, 39600}));
        map.put("Pacific/Guam", new TStandardZoneRules(new long[] {-3944626740L, -2177487540L, -885549600L, -802256400L}, new int[] {-51660, 34740, 36000, 32400, 36000}, new long[] {-3944626740L, -2177487540L, -885549600L, -802256400L, -331891200L, -281610000L, -73728000L, -29415540L, -16704000L, -10659600L, 9907200L, 21394800L, 41356800L, 52844400L, 124819200L, 130863600L, 201888000L, 209487660L, 230659200L, 241542000L}, new int[] {-51660, 34740, 36000, 32400, 36000, 39600, 36000, 39600, 36000, 39600, 36000, 39600, 36000, 39600, 36000, 39600, 36000, 39600, 36000, 39600, 36000}));
        map.put("Pacific/Honolulu", new TStandardZoneRules(new long[] {-2334101314L, -712150200L}, new int[] {-37886, -37800, -36000}, new long[] {-2334101314L, -1157283000L, -1155436200L, -880198200L, -765376200L, -712150200L}, new int[] {-37886, -37800, -34200, -37800, -34200, -37800, -36000}));
        map.put("Pacific/Johnston", new TStandardZoneRules(new long[] {-2334101314L, -712150200L}, new int[] {-37886, -37800, -36000}, new long[] {-2334101314L, -1157283000L, -1155436200L, -880198200L, -765376200L, -712150200L}, new int[] {-37886, -37800, -34200, -37800, -34200, -37800, -36000}));
        map.put("Pacific/Kiritimati", new TStandardZoneRules(new long[] {-2177415040L, 307622400L, 788868000L}, new int[] {-37760, -38400, -36000, 50400}, new long[] {-2177415040L, 307622400L, 788868000L}, new int[] {-37760, -38400, -36000, 50400}));
        map.put("Pacific/Kosrae", new TStandardZoneRules(new long[] {-3944631116L, -2177491916L, -1743678000L, -1606813200L, -1041418800L, -907408800L, -770634000L, -7988400L, 915105600L}, new int[] {-47284, 39116, 39600, 32400, 39600, 36000, 32400, 39600, 43200, 39600}, new long[] {-3944631116L, -2177491916L, -1743678000L, -1606813200L, -1041418800L, -907408800L, -770634000L, -7988400L, 915105600L}, new int[] {-47284, 39116, 39600, 32400, 39600, 36000, 32400, 39600, 43200, 39600}));
        map.put("Pacific/Kwajalein", new TStandardZoneRules(new long[] {-2177492960L, -1041418800L, -907408800L, -817462800L, -7988400L, 745934400L}, new int[] {40160, 39600, 36000, 32400, 39600, -43200, 43200}, new long[] {-2177492960L, -1041418800L, -907408800L, -817462800L, -7988400L, 745934400L}, new int[] {40160, 39600, 36000, 32400, 39600, -43200, 43200}));
        map.put("Pacific/Majuro", new TStandardZoneRules(new long[] {-2177493888L, -1743678000L, -1606813200L, -1041418800L, -907408800L, -818067600L, -7988400L}, new int[] {41088, 39600, 32400, 39600, 36000, 32400, 39600, 43200}, new long[] {-2177493888L, -1743678000L, -1606813200L, -1041418800L, -907408800L, -818067600L, -7988400L}, new int[] {41088, 39600, 32400, 39600, 36000, 32400, 39600, 43200}));
        map.put("Pacific/Marquesas", new TStandardZoneRules(new long[] {-1806676920L}, new int[] {-33480, -34200}, new long[] {-1806676920L}, new int[] {-33480, -34200}));
        map.put("Pacific/Midway", new TStandardZoneRules(new long[] {-2445424632L, -1861879032L}, new int[] {45432, -40968, -39600}, new long[] {-2445424632L, -1861879032L}, new int[] {45432, -40968, -39600}));
        map.put("Pacific/Nauru", new TStandardZoneRules(new long[] {-1545131260L, -862918200L, -767350800L, 287418600L}, new int[] {40060, 41400, 32400, 41400, 43200}, new long[] {-1545131260L, -862918200L, -767350800L, 287418600L}, new int[] {40060, 41400, 32400, 41400, 43200}));
        map.put("Pacific/Niue", new TStandardZoneRules(new long[] {-2177412020L, -599575200L, 276089400L}, new int[] {-40780, -40800, -41400, -39600}, new long[] {-2177412020L, -599575200L, 276089400L}, new int[] {-40780, -40800, -41400, -39600}));
        map.put("Pacific/Norfolk", new TStandardZoneRules(new long[] {-2177493112L, -599656320L, 1443882600L}, new int[] {40312, 40320, 41400, 39600}, new long[] {-2177493112L, -599656320L, 152029800L, 162916200L, 1443882600L, 1570287600L, 1586012400L, 1601737200L}, new int[] {40312, 40320, 41400, 45000, 41400, 39600, 43200, 39600, 43200}, new TZoneOffsetTransitionRule(4,1,7,7200,0,2,39600,43200,39600), new TZoneOffsetTransitionRule(10,1,7,7200,0,2,39600,39600,43200)));
        map.put("Pacific/Noumea", new TStandardZoneRules(new long[] {-1829387148L}, new int[] {39948, 39600}, new long[] {-1829387148L, 250002000L, 257342400L, 281451600L, 288878400L, 849366000L, 857228400L}, new int[] {39948, 39600, 43200, 39600, 43200, 39600, 43200, 39600}));
        map.put("Pacific/Pago_Pago", new TStandardZoneRules(new long[] {-2445424632L, -1861879032L}, new int[] {45432, -40968, -39600}, new long[] {-2445424632L, -1861879032L}, new int[] {45432, -40968, -39600}));
        map.put("Pacific/Palau", new TStandardZoneRules(new long[] {-3944624276L, -2177485076L}, new int[] {-54124, 32276, 32400}, new long[] {-3944624276L, -2177485076L}, new int[] {-54124, 32276, 32400}));
        map.put("Pacific/Pitcairn", new TStandardZoneRules(new long[] {-2177421580L, 893665800L}, new int[] {-31220, -30600, -28800}, new long[] {-2177421580L, 893665800L}, new int[] {-31220, -30600, -28800}));
        map.put("Pacific/Pohnpei", new TStandardZoneRules(new long[] {-3944629972L, -2177490772L, -1743678000L, -1606813200L, -1041418800L, -907408800L, -770634000L}, new int[] {-48428, 37972, 39600, 32400, 39600, 36000, 32400, 39600}, new long[] {-3944629972L, -2177490772L, -1743678000L, -1606813200L, -1041418800L, -907408800L, -770634000L}, new int[] {-48428, 37972, 39600, 32400, 39600, 36000, 32400, 39600}));
        map.put("Pacific/Ponape", new TStandardZoneRules(new long[] {-3944629972L, -2177490772L, -1743678000L, -1606813200L, -1041418800L, -907408800L, -770634000L}, new int[] {-48428, 37972, 39600, 32400, 39600, 36000, 32400, 39600}, new long[] {-3944629972L, -2177490772L, -1743678000L, -1606813200L, -1041418800L, -907408800L, -770634000L}, new int[] {-48428, 37972, 39600, 32400, 39600, 36000, 32400, 39600}));
        map.put("Pacific/Port_Moresby", new TStandardZoneRules(new long[] {-2840176120L, -2366790512L}, new int[] {35320, 35312, 36000}, new long[] {-2840176120L, -2366790512L}, new int[] {35320, 35312, 36000}));
        map.put("Pacific/Rarotonga", new TStandardZoneRules(new long[] {-2177414456L, 279714600L}, new int[] {-38344, -37800, -36000}, new long[] {-2177414456L, 279714600L, 289387800L, 309952800L, 320837400L, 341402400L, 352287000L, 372852000L, 384341400L, 404906400L, 415791000L, 436356000L, 447240600L, 467805600L, 478690200L, 499255200L, 510139800L, 530704800L, 541589400L, 562154400L, 573643800L, 594208800L, 605093400L, 625658400L, 636543000L, 657108000L, 667992600L}, new int[] {-38344, -37800, -34200, -36000, -34200, -36000, -34200, -36000, -34200, -36000, -34200, -36000, -34200, -36000, -34200, -36000, -34200, -36000, -34200, -36000, -34200, -36000, -34200, -36000, -34200, -36000, -34200, -36000}));
        map.put("Pacific/Saipan", new TStandardZoneRules(new long[] {-3944626740L, -2177487540L, -885549600L, -802256400L}, new int[] {-51660, 34740, 36000, 32400, 36000}, new long[] {-3944626740L, -2177487540L, -885549600L, -802256400L, -331891200L, -281610000L, -73728000L, -29415540L, -16704000L, -10659600L, 9907200L, 21394800L, 41356800L, 52844400L, 124819200L, 130863600L, 201888000L, 209487660L, 230659200L, 241542000L}, new int[] {-51660, 34740, 36000, 32400, 36000, 39600, 36000, 39600, 36000, 39600, 36000, 39600, 36000, 39600, 36000, 39600, 36000, 39600, 36000, 39600, 36000}));
        map.put("Pacific/Samoa", new TStandardZoneRules(new long[] {-2445424632L, -1861879032L}, new int[] {45432, -40968, -39600}, new long[] {-2445424632L, -1861879032L}, new int[] {45432, -40968, -39600}));
        map.put("Pacific/Tahiti", new TStandardZoneRules(new long[] {-1806674504L}, new int[] {-35896, -36000}, new long[] {-1806674504L}, new int[] {-35896, -36000}));
        map.put("Pacific/Tarawa", new TStandardZoneRules(new long[] {-2177494324L}, new int[] {41524, 43200}, new long[] {-2177494324L}, new int[] {41524, 43200}));
        map.put("Pacific/Tongatapu", new TStandardZoneRules(new long[] {-2177497160L, -915193200L}, new int[] {44360, 44400, 46800}, new long[] {-2177497160L, -915193200L, 939214800L, 953384400L, 973342800L, 980596800L, 1004792400L, 1012046400L, 1478350800L, 1484398800L}, new int[] {44360, 44400, 46800, 50400, 46800, 50400, 46800, 50400, 46800, 50400, 46800}));
        map.put("Pacific/Truk", new TStandardZoneRules(new long[] {-3944628428L, -2177489228L, -1743674400L, -1606813200L, -907408800L, -770634000L}, new int[] {-49972, 36428, 36000, 32400, 36000, 32400, 36000}, new long[] {-3944628428L, -2177489228L, -1743674400L, -1606813200L, -907408800L, -770634000L}, new int[] {-49972, 36428, 36000, 32400, 36000, 32400, 36000}));
        map.put("Pacific/Wake", new TStandardZoneRules(new long[] {-2177492788L}, new int[] {39988, 43200}, new long[] {-2177492788L}, new int[] {39988, 43200}));
        map.put("Pacific/Wallis", new TStandardZoneRules(new long[] {-2177496920L}, new int[] {44120, 43200}, new long[] {-2177496920L}, new int[] {44120, 43200}));
        map.put("Pacific/Yap", new TStandardZoneRules(new long[] {-3944628428L, -2177489228L, -1743674400L, -1606813200L, -907408800L, -770634000L}, new int[] {-49972, 36428, 36000, 32400, 36000, 32400, 36000}, new long[] {-3944628428L, -2177489228L, -1743674400L, -1606813200L, -907408800L, -770634000L}, new int[] {-49972, 36428, 36000, 32400, 36000, 32400, 36000}));
    }
}
