/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.teavm.classlib.java.time.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.teavm.classlib.java.util.TLocale;
import java.util.Map;

import org.teavm.classlib.java.time.TPeriod;
import org.teavm.classlib.java.time.TZoneId;
import org.teavm.classlib.java.time.chrono.TChronology;
import org.teavm.classlib.java.time.chrono.TIsoChronology;
import org.teavm.classlib.java.time.format.TDateTimeFormatterBuilder.ReducedPrinterParser;
import org.teavm.classlib.java.time.jdk8.TDefaultInterfaceTemporalAccessor;
import org.teavm.classlib.java.time.jdk8.TJdk8Methods;
import org.teavm.classlib.java.time.temporal.TTemporalField;
import org.teavm.classlib.java.time.temporal.TTemporalQueries;
import org.teavm.classlib.java.time.temporal.TTemporalQuery;
import org.teavm.classlib.java.time.temporal.TUnsupportedTemporalTypeException;

final class TDateTimeParseContext {

    private TLocale locale;
    private TDecimalStyle symbols;
    private TChronology overrideChronology;
    private TZoneId overrideZone;
    private boolean caseSensitive = true;
    private boolean strict = true;
    private final ArrayList<Parsed> parsed = new ArrayList<Parsed>();

    TDateTimeParseContext(TDateTimeFormatter formatter) {
        super();
        this.locale = formatter.getLocale();
        this.symbols = formatter.getDecimalStyle();
        this.overrideChronology = formatter.getChronology();
        this.overrideZone = formatter.getZone();
        parsed.add(new Parsed());
    }

    // for testing
    TDateTimeParseContext(TLocale locale, TDecimalStyle symbols, TChronology chronology) {
        super();
        this.locale = locale;
        this.symbols = symbols;
        this.overrideChronology = chronology;
        this.overrideZone = null;
        parsed.add(new Parsed());
    }

    TDateTimeParseContext(TDateTimeParseContext other) {
        super();
        this.locale = other.locale;
        this.symbols = other.symbols;
        this.overrideChronology = other.overrideChronology;
        this.overrideZone = other.overrideZone;
        this.caseSensitive = other.caseSensitive;
        this.strict = other.strict;
        parsed.add(new Parsed());
    }

    TDateTimeParseContext copy() {
        return new TDateTimeParseContext(this);
    }

    //-----------------------------------------------------------------------
    TLocale getLocale() {
        return locale;
    }

    TDecimalStyle getSymbols() {
        return symbols;
    }

    TChronology getEffectiveChronology() {
        TChronology chrono = currentParsed().chrono;
        if (chrono == null) {
            chrono = overrideChronology;
            if (chrono == null) {
                chrono = TIsoChronology.INSTANCE;
            }
        }
        return chrono;
    }

    //-----------------------------------------------------------------------
    boolean isCaseSensitive() {
        return caseSensitive;
    }

    void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    boolean subSequenceEquals(CharSequence cs1, int offset1, CharSequence cs2, int offset2, int length) {
        if (offset1 + length > cs1.length() || offset2 + length > cs2.length()) {
            return false;
        }
        if (isCaseSensitive()) {
            for (int i = 0; i < length; i++) {
                char ch1 = cs1.charAt(offset1 + i);
                char ch2 = cs2.charAt(offset2 + i);
                if (ch1 != ch2) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < length; i++) {
                char ch1 = cs1.charAt(offset1 + i);
                char ch2 = cs2.charAt(offset2 + i);
                if (ch1 != ch2 && Character.toUpperCase(ch1) != Character.toUpperCase(ch2) &&
                        Character.toLowerCase(ch1) != Character.toLowerCase(ch2)) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean charEquals(char ch1, char ch2) {
        if (isCaseSensitive()) {
            return ch1 == ch2;
        }
        return charEqualsIgnoreCase(ch1, ch2);
    }

    static boolean charEqualsIgnoreCase(char c1, char c2) {
        return c1 == c2 ||
                Character.toUpperCase(c1) == Character.toUpperCase(c2) ||
                Character.toLowerCase(c1) == Character.toLowerCase(c2);
    }

    //-----------------------------------------------------------------------
    boolean isStrict() {
        return strict;
    }

    void setStrict(boolean strict) {
        this.strict = strict;
    }

    //-----------------------------------------------------------------------
    void startOptional() {
        parsed.add(currentParsed().copy());
    }

    void endOptional(boolean successful) {
        if (successful) {
            parsed.remove(parsed.size() - 2);
        } else {
            parsed.remove(parsed.size() - 1);
        }
    }

    //-----------------------------------------------------------------------
    private Parsed currentParsed() {
        return parsed.get(parsed.size() - 1);
    }

    //-----------------------------------------------------------------------
    Long getParsed(TTemporalField field) {
        return currentParsed().fieldValues.get(field);
    }

    int setParsedField(TTemporalField field, long value, int errorPos, int successPos) {
        TJdk8Methods.requireNonNull(field, "field");
        Long old = currentParsed().fieldValues.put(field, value);
        return (old != null && old.longValue() != value) ? ~errorPos : successPos;
    }

    void setParsed(TChronology chrono) {
        TJdk8Methods.requireNonNull(chrono, "chrono");
        Parsed currentParsed = currentParsed();
        currentParsed.chrono = chrono;
        if (currentParsed.callbacks != null) {
            List<Object[]> callbacks = new ArrayList<Object[]>(currentParsed.callbacks);
            currentParsed.callbacks.clear();
            for (Object[] objects : callbacks) {
                ReducedPrinterParser pp = (ReducedPrinterParser) objects[0];
                pp.setValue(this, (Long) objects[1], (Integer) objects[2], (Integer) objects[3]);
            }
        }
    }

    void addChronologyChangedParser(ReducedPrinterParser reducedPrinterParser, long value, int errorPos, int successPos) {
        Parsed currentParsed = currentParsed();
        if (currentParsed.callbacks == null) {
            currentParsed.callbacks = new ArrayList<Object[]>(2);
        }
        currentParsed.callbacks.add(new Object[] {reducedPrinterParser, value, errorPos, successPos});
    }

    void setParsed(TZoneId zone) {
        TJdk8Methods.requireNonNull(zone, "zone");
        currentParsed().zone = zone;
    }

    void setParsedLeapSecond() {
        currentParsed().leapSecond = true;
    }

    //-----------------------------------------------------------------------
    Parsed toParsed() {
        return currentParsed();
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
        return currentParsed().toString();
    }

    //-----------------------------------------------------------------------
    final class Parsed extends TDefaultInterfaceTemporalAccessor {
        TChronology chrono = null;
        TZoneId zone = null;
        final Map<TTemporalField, Long> fieldValues = new HashMap<TTemporalField, Long>();
        boolean leapSecond;
        TPeriod excessDays = TPeriod.ZERO;
        List<Object[]> callbacks;

        private Parsed() {
        }
        protected Parsed copy() {
            Parsed cloned = new Parsed();
            cloned.chrono = this.chrono;
            cloned.zone = this.zone;
            cloned.fieldValues.putAll(this.fieldValues);
            cloned.leapSecond = this.leapSecond;
            return cloned;
        }
        @Override
        public String toString() {
            return fieldValues.toString() + "," + chrono + "," + zone;
        }
        @Override
        public boolean isSupported(TTemporalField field) {
            return fieldValues.containsKey(field);
        }
        @Override
        public int get(TTemporalField field) {
            if (fieldValues.containsKey(field) == false) {
                throw new TUnsupportedTemporalTypeException("Unsupported field: " + field);
            }
            long value = fieldValues.get(field);
            return TJdk8Methods.safeToInt(value);
        }
        @Override
        public long getLong(TTemporalField field) {
            if (fieldValues.containsKey(field) == false) {
                throw new TUnsupportedTemporalTypeException("Unsupported field: " + field);
            }
            return fieldValues.get(field);
        }
        @SuppressWarnings("unchecked")
        @Override
        public <R> R query(TTemporalQuery<R> query) {
            if (query == TTemporalQueries.chronology()) {
                return (R) chrono;
            }
            if (query == TTemporalQueries.zoneId() || query == TTemporalQueries.zone()) {
                return (R) zone;
            }
            return super.query(query);
        }

        TDateTimeBuilder toBuilder() {
            TDateTimeBuilder builder = new TDateTimeBuilder();
            builder.fieldValues.putAll(fieldValues);
            builder.chrono = getEffectiveChronology();
            if (zone != null) {
                builder.zone = zone;
            } else {
                builder.zone = overrideZone;
            }
            builder.leapSecond = leapSecond;
            builder.excessDays = excessDays;
            return builder;
        }
    }

    //-------------------------------------------------------------------------
    // for testing
    void setLocale(TLocale locale) {
        TJdk8Methods.requireNonNull(locale, "locale");
        this.locale = locale;
    }

}
