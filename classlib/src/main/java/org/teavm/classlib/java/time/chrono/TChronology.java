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
package org.teavm.classlib.java.time.chrono;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import org.teavm.classlib.java.util.TLocale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.teavm.classlib.java.time.TClock;
import org.teavm.classlib.java.time.TDateTimeException;
import org.teavm.classlib.java.time.TInstant;
import org.teavm.classlib.java.time.TLocalDate;
import org.teavm.classlib.java.time.TLocalTime;
import org.teavm.classlib.java.time.TZoneId;
import org.teavm.classlib.java.time.format.TDateTimeFormatterBuilder;
import org.teavm.classlib.java.time.format.TResolverStyle;
import org.teavm.classlib.java.time.format.TTextStyle;
import org.teavm.classlib.java.time.jdk8.TDefaultInterfaceTemporalAccessor;
import org.teavm.classlib.java.time.jdk8.TJdk8Methods;
import org.teavm.classlib.java.time.temporal.TChronoField;
import org.teavm.classlib.java.time.temporal.TTemporal;
import org.teavm.classlib.java.time.temporal.TTemporalAccessor;
import org.teavm.classlib.java.time.temporal.TTemporalField;
import org.teavm.classlib.java.time.temporal.TTemporalQueries;
import org.teavm.classlib.java.time.temporal.TTemporalQuery;
import org.teavm.classlib.java.time.temporal.TUnsupportedTemporalTypeException;
import org.teavm.classlib.java.time.temporal.TValueRange;

public abstract class TChronology implements Comparable<TChronology> {

    public static final TTemporalQuery<TChronology> FROM = new TTemporalQuery<TChronology>() {
        @Override
        public TChronology queryFrom(TTemporalAccessor temporal) {
            return TChronology.from(temporal);
        }
    };

    private static final ConcurrentHashMap<String, TChronology> CHRONOS_BY_ID = new ConcurrentHashMap<String, TChronology>();
    private static final ConcurrentHashMap<String, TChronology> CHRONOS_BY_TYPE = new ConcurrentHashMap<String, TChronology>();
    private static final Method LOCALE_METHOD;
    static {
        Method method = null;
        try {
            method = TLocale.class.getMethod("getUnicodeLocaleType", String.class);
        } catch (Throwable ex) {
            // ignore
        }
        LOCALE_METHOD = method;
    }

    //-----------------------------------------------------------------------
    public static TChronology from(TTemporalAccessor temporal) {
        TJdk8Methods.requireNonNull(temporal, "temporal");
        TChronology obj = temporal.query(TTemporalQueries.chronology());
        return (obj != null ? obj : TIsoChronology.INSTANCE);
    }

    //-----------------------------------------------------------------------
    public static TChronology ofLocale(TLocale locale) {
        init();
        TJdk8Methods.requireNonNull(locale, "locale");
        String type = "iso";
        if (LOCALE_METHOD != null) {
            // JDK 7: locale.getUnicodeLocaleType("ca");
            try {
                type = (String) LOCALE_METHOD.invoke(locale, "ca");
            } catch (IllegalArgumentException ex) {
                // ignore
            } catch (IllegalAccessException ex) {
                // ignore
            } catch (InvocationTargetException ex) {
                // ignore
            }
        } else if (locale.equals(TJapaneseChronology.LOCALE)) {
            type = "japanese";
        }
        if (type == null || "iso".equals(type) || "iso8601".equals(type)) {
            return TIsoChronology.INSTANCE;
        } else {
            TChronology chrono = CHRONOS_BY_TYPE.get(type);
            if (chrono == null) {
                throw new TDateTimeException("Unknown calendar system: " + type);
            }
            return chrono;
        }
    }

    //-----------------------------------------------------------------------
    public static TChronology of(String id) {
        init();
        TChronology chrono = CHRONOS_BY_ID.get(id);
        if (chrono != null) {
            return chrono;
        }
        chrono = CHRONOS_BY_TYPE.get(id);
        if (chrono != null) {
            return chrono;
        }
        throw new TDateTimeException("Unknown chronology: " + id);
    }

    public static Set<TChronology> getAvailableChronologies() {
        init();
        return new HashSet<TChronology>(CHRONOS_BY_ID.values());
    }

    private static void init() {
        if (CHRONOS_BY_ID.isEmpty()) {
            register(TIsoChronology.INSTANCE);
            register(TThaiBuddhistChronology.INSTANCE);
            register(TMinguoChronology.INSTANCE);
            register(TJapaneseChronology.INSTANCE);
            register(THijrahChronology.INSTANCE);
            CHRONOS_BY_ID.putIfAbsent("Hijrah", THijrahChronology.INSTANCE);
            CHRONOS_BY_TYPE.putIfAbsent("islamic", THijrahChronology.INSTANCE);
            ServiceLoader<TChronology> loader =  ServiceLoader.load(TChronology.class, TChronology.class.getClassLoader());
            for (TChronology chrono : loader) {
                CHRONOS_BY_ID.putIfAbsent(chrono.getId(), chrono);
                String type = chrono.getCalendarType();
                if (type != null) {
                    CHRONOS_BY_TYPE.putIfAbsent(type, chrono);
                }
            }
        }
    }

    private static void register(TChronology chrono) {
        CHRONOS_BY_ID.putIfAbsent(chrono.getId(), chrono);
        String type = chrono.getCalendarType();
        if (type != null) {
            CHRONOS_BY_TYPE.putIfAbsent(type, chrono);
        }
    }

    //-----------------------------------------------------------------------
    protected TChronology() {
    }

    //-----------------------------------------------------------------------
    <D extends TChronoLocalDate> D ensureChronoLocalDate(TTemporal temporal) {
        @SuppressWarnings("unchecked")
        D other = (D) temporal;
        if (this.equals(other.getChronology()) == false) {
            throw new ClassCastException("Chrono mismatch, expected: " + getId() + ", actual: " + other.getChronology().getId());
        }
        return other;
    }

    <D extends TChronoLocalDate> TChronoLocalDateTimeImpl<D> ensureChronoLocalDateTime(TTemporal temporal) {
        @SuppressWarnings("unchecked")
        TChronoLocalDateTimeImpl<D> other = (TChronoLocalDateTimeImpl<D>) temporal;
        if (this.equals(other.toLocalDate().getChronology()) == false) {
            throw new ClassCastException("Chrono mismatch, required: " + getId()
                    + ", supplied: " + other.toLocalDate().getChronology().getId());
        }
        return other;
    }

    <D extends TChronoLocalDate> ChronoZonedDateTimeImpl<D> ensureChronoZonedDateTime(TTemporal temporal) {
        @SuppressWarnings("unchecked")
        ChronoZonedDateTimeImpl<D> other = (ChronoZonedDateTimeImpl<D>) temporal;
        if (this.equals(other.toLocalDate().getChronology()) == false) {
            throw new ClassCastException("Chrono mismatch, required: " + getId()
                    + ", supplied: " + other.toLocalDate().getChronology().getId());
        }
        return other;
    }

    //-----------------------------------------------------------------------
    public abstract String getId();

    public abstract String getCalendarType();

    //-----------------------------------------------------------------------
    public TChronoLocalDate date(TEra era, int yearOfEra, int month, int dayOfMonth) {
        return date(prolepticYear(era, yearOfEra), month, dayOfMonth);
    }

    public abstract TChronoLocalDate date(int prolepticYear, int month, int dayOfMonth);

    public TChronoLocalDate dateYearDay(TEra era, int yearOfEra, int dayOfYear) {
        return dateYearDay(prolepticYear(era, yearOfEra), dayOfYear);
    }

    public abstract TChronoLocalDate dateYearDay(int prolepticYear, int dayOfYear);

    public abstract TChronoLocalDate dateEpochDay(long epochDay);

    public abstract TChronoLocalDate date(TTemporalAccessor temporal);

    //-----------------------------------------------------------------------
    public TChronoLocalDate dateNow() {
        return dateNow(TClock.systemDefaultZone());
    }

    public TChronoLocalDate dateNow(TZoneId zone) {
        return dateNow(TClock.system(zone));
    }

    public TChronoLocalDate dateNow(TClock clock) {
        TJdk8Methods.requireNonNull(clock, "clock");
        return date(TLocalDate.now(clock));
    }

    //-----------------------------------------------------------------------
    public TChronoLocalDateTime<?> localDateTime(TTemporalAccessor temporal) {
        try {
            TChronoLocalDate date = date(temporal);
            return date.atTime(TLocalTime.from(temporal));
        } catch (TDateTimeException ex) {
            throw new TDateTimeException("Unable to obtain TChronoLocalDateTime from TTemporalAccessor: " + temporal.getClass(), ex);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TChronoZonedDateTime<?> zonedDateTime(TTemporalAccessor temporal) {
        try {
            TZoneId zone = TZoneId.from(temporal);
            try {
                TInstant instant = TInstant.from(temporal);
                return zonedDateTime(instant, zone);

            } catch (TDateTimeException ex1) {
                TChronoLocalDateTime cldt = localDateTime(temporal);
                TChronoLocalDateTimeImpl cldtImpl = ensureChronoLocalDateTime(cldt);
                return ChronoZonedDateTimeImpl.ofBest(cldtImpl, zone, null);
            }
        } catch (TDateTimeException ex) {
            throw new TDateTimeException("Unable to obtain TChronoZonedDateTime from TTemporalAccessor: " + temporal.getClass(), ex);
        }
    }

    public TChronoZonedDateTime<?> zonedDateTime(TInstant instant, TZoneId zone) {
        TChronoZonedDateTime<? extends TChronoLocalDate> result = ChronoZonedDateTimeImpl.ofInstant(this, instant, zone);
        return result;
    }

    //-----------------------------------------------------------------------
    public TChronoPeriod period(int years, int months, int days) {
        return new TChronoPeriodImpl(this, years, months, days);
    }

    //-----------------------------------------------------------------------
    public abstract boolean isLeapYear(long prolepticYear);

    public abstract int prolepticYear(TEra era, int yearOfEra);

    public abstract TEra eraOf(int eraValue);

    public abstract List<TEra> eras();

    //-----------------------------------------------------------------------
    public abstract TValueRange range(TChronoField field);

    //-----------------------------------------------------------------------
    public String getDisplayName(TTextStyle style, TLocale locale) {
        return new TDateTimeFormatterBuilder().appendChronologyText(style).toFormatter(locale).format(new TDefaultInterfaceTemporalAccessor() {
            @Override
            public boolean isSupported(TTemporalField field) {
                return false;
            }
            @Override
            public long getLong(TTemporalField field) {
                throw new TUnsupportedTemporalTypeException("Unsupported field: " + field);
            }
            @SuppressWarnings("unchecked")
            @Override
            public <R> R query(TTemporalQuery<R> query) {
                if (query == TTemporalQueries.chronology()) {
                    return (R) TChronology.this;
                }
                return super.query(query);
            }
        });
    }

    //-----------------------------------------------------------------------
    public abstract TChronoLocalDate resolveDate(Map<TTemporalField, Long> fieldValues, TResolverStyle resolverStyle);

    void updateResolveMap(Map<TTemporalField, Long> fieldValues, TChronoField field, long value) {
        Long current = fieldValues.get(field);
        if (current != null && current.longValue() != value) {
            throw new TDateTimeException("Invalid state, field: " + field + " " + current + " conflicts with " + field + " " + value);
        }
        fieldValues.put(field, value);
    }

    //-----------------------------------------------------------------------
    @Override
    public int compareTo(TChronology other) {
        return getId().compareTo(other.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
           return true;
        }
        if (obj instanceof TChronology) {
            return compareTo((TChronology) obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ getId().hashCode();
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
        return getId();
    }

    //-----------------------------------------------------------------------
    private Object writeReplace() {
        return new Ser(Ser.CHRONO_TYPE, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput out) throws IOException {
        out.writeUTF(getId());
    }

    static TChronology readExternal(DataInput in) throws IOException {
        String id = in.readUTF();
        return TChronology.of(id);
    }

}
