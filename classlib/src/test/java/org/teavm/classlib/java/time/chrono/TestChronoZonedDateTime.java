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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.teavm.classlib.java.util.TLocale;
import java.util.Map;

import org.junit.Assert;
import org.testng.annotations.DataProvider;
import org.junit.Test;
import org.teavm.classlib.java.time.TDuration;
import org.teavm.classlib.java.time.TLocalDate;
import org.teavm.classlib.java.time.TLocalTime;
import org.teavm.classlib.java.time.TZoneId;
import org.teavm.classlib.java.time.TZoneOffset;
import org.teavm.classlib.java.time.TZonedDateTime;
import org.teavm.classlib.java.time.format.TResolverStyle;
import org.teavm.classlib.java.time.temporal.TChronoUnit;
import org.teavm.classlib.java.time.temporal.TTemporal;
import org.teavm.classlib.java.time.temporal.TTemporalAccessor;
import org.teavm.classlib.java.time.temporal.TTemporalAdjuster;
import org.teavm.classlib.java.time.temporal.TTemporalAmount;
import org.teavm.classlib.java.time.temporal.TTemporalField;
import org.teavm.classlib.java.time.temporal.TTemporalUnit;
import org.teavm.classlib.java.time.temporal.TValueRange;

@SuppressWarnings("rawtypes")
@Test
public class TestChronoZonedDateTime {
    //-----------------------------------------------------------------------
    // regular data factory for names and descriptions of available calendars
    //-----------------------------------------------------------------------
    @DataProvider(name = "calendars")
    TChronology[][] data_of_calendars() {
        return new TChronology[][]{
                    {THijrahChronology.INSTANCE},
                    {TIsoChronology.INSTANCE},
                    {TJapaneseChronology.INSTANCE},
                    {TMinguoChronology.INSTANCE},
                    {TThaiBuddhistChronology.INSTANCE},
        };
    }

    @Test(dataProvider="calendars")
    public void test_badWithAdjusterChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoZonedDateTime czdt = chrono.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoZonedDateTime<?> czdt2 = chrono2.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
            TTemporalAdjuster adjuster = new FixedAdjuster(czdt2);
            if (chrono != chrono2) {
                try {
                    czdt.with(adjuster);
                    Assert.fail("WithAdjuster should have thrown a ClassCastException, "
                            + "required: " + czdt + ", supplied: " + czdt2);
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                TChronoZonedDateTime<?> result = czdt.with(adjuster);
                assertEquals(result, czdt2, "WithAdjuster failed to replace date");
            }
        }
    }

    @Test(dataProvider="calendars")
    public void test_badPlusAdjusterChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoZonedDateTime czdt = chrono.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoZonedDateTime<?> czdt2 = chrono2.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
            TTemporalAmount adjuster = new FixedAdjuster(czdt2);
            if (chrono != chrono2) {
                try {
                    czdt.plus(adjuster);
                    Assert.fail("WithAdjuster should have thrown a ClassCastException, "
                            + "required: " + czdt + ", supplied: " + czdt2);
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoZonedDateTime<?> result = czdt.plus(adjuster);
                assertEquals(result, czdt2, "WithAdjuster failed to replace date time");
            }
        }
    }

    @Test(dataProvider="calendars")
    public void test_badMinusAdjusterChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoZonedDateTime czdt = chrono.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoZonedDateTime<?> czdt2 = chrono2.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
            TTemporalAmount adjuster = new FixedAdjuster(czdt2);
            if (chrono != chrono2) {
                try {
                    czdt.minus(adjuster);
                    Assert.fail("WithAdjuster should have thrown a ClassCastException, "
                            + "required: " + czdt + ", supplied: " + czdt2);
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoZonedDateTime<?> result = czdt.minus(adjuster);
                assertEquals(result, czdt2, "WithAdjuster failed to replace date");
            }
        }
    }

    @Test(dataProvider="calendars")
    public void test_badPlusPeriodUnitChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoZonedDateTime czdt = chrono.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoZonedDateTime<?> czdt2 = chrono2.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
            TTemporalUnit adjuster = new FixedPeriodUnit(czdt2);
            if (chrono != chrono2) {
                try {
                    czdt.plus(1, adjuster);
                    Assert.fail("PeriodUnit.doPlus plus should have thrown a ClassCastException, " + czdt
                            + " can not be cast to " + czdt2);
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoZonedDateTime<?> result = czdt.plus(1, adjuster);
                assertEquals(result, czdt2, "WithAdjuster failed to replace date");
            }
        }
    }

    @Test(dataProvider="calendars")
    public void test_badMinusPeriodUnitChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoZonedDateTime czdt = chrono.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoZonedDateTime<?> czdt2 = chrono2.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
            TTemporalUnit adjuster = new FixedPeriodUnit(czdt2);
            if (chrono != chrono2) {
                try {
                    czdt.minus(1, adjuster);
                    Assert.fail("PeriodUnit.doPlus minus should have thrown a ClassCastException, " + czdt.getClass()
                            + " can not be cast to " + czdt2.getClass());
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoZonedDateTime<?> result = czdt.minus(1, adjuster);
                assertEquals(result, czdt2, "WithAdjuster failed to replace date");
            }
        }
    }

    @Test(dataProvider="calendars")
    public void test_badDateTimeFieldChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoZonedDateTime czdt = chrono.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoZonedDateTime<?> czdt2 = chrono2.date(refDate).atTime(TLocalTime.NOON).atZone(TZoneOffset.UTC);
            TTemporalField adjuster = new FixedDateTimeField(czdt2);
            if (chrono != chrono2) {
                try {
                    czdt.with(adjuster, 1);
                    Assert.fail("DateTimeField adjustInto() should have thrown a ClassCastException, " + czdt.getClass()
                            + " can not be cast to " + czdt2.getClass());
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoZonedDateTime<?> result = czdt.with(adjuster, 1);
                assertEquals(result, czdt2, "DateTimeField adjustInto() failed to replace date");
            }
        }
    }

    //-----------------------------------------------------------------------
    // isBefore, isAfter, isEqual, INSTANT_COMPARATOR  test a Chrono against the other Chronos
    //-----------------------------------------------------------------------
    @SuppressWarnings("unused")
    @Test(dataProvider="calendars")
    public void test_zonedDateTime_comparisons(TChronology chrono) {
        List<TChronoZonedDateTime<?>> dates = new ArrayList<TChronoZonedDateTime<?>>();

        TChronoZonedDateTime<?> date = chrono.date(TLocalDate.of(1900, 1, 1))
                .atTime(TLocalTime.MIN)
                .atZone(TZoneOffset.UTC);

        // Insert dates in order, no duplicates
        if (chrono != TJapaneseChronology.INSTANCE) {
            dates.add(date.minus(100, TChronoUnit.YEARS));
        }
        dates.add(date.minus(1, TChronoUnit.YEARS));
        dates.add(date.minus(1, TChronoUnit.MONTHS));
        dates.add(date.minus(1, TChronoUnit.WEEKS));
        dates.add(date.minus(1, TChronoUnit.DAYS));
        dates.add(date.minus(1, TChronoUnit.HOURS));
        dates.add(date.minus(1, TChronoUnit.MINUTES));
        dates.add(date.minus(1, TChronoUnit.SECONDS));
        dates.add(date.minus(1, TChronoUnit.NANOS));
        dates.add(date);
        dates.add(date.plus(1, TChronoUnit.NANOS));
        dates.add(date.plus(1, TChronoUnit.SECONDS));
        dates.add(date.plus(1, TChronoUnit.MINUTES));
        dates.add(date.plus(1, TChronoUnit.HOURS));
        dates.add(date.plus(1, TChronoUnit.DAYS));
        dates.add(date.plus(1, TChronoUnit.WEEKS));
        dates.add(date.plus(1, TChronoUnit.MONTHS));
        dates.add(date.plus(1, TChronoUnit.YEARS));
        dates.add(date.plus(100, TChronoUnit.YEARS));

        // Check these dates against the corresponding dates for every calendar
        for (TChronology[] clist : data_of_calendars()) {
            List<TChronoZonedDateTime<?>> otherDates = new ArrayList<TChronoZonedDateTime<?>>();
            TChronology chrono2 = TIsoChronology.INSTANCE; //clist[0];
            for (TChronoZonedDateTime<?> d : dates) {
                otherDates.add(chrono2.date(d).atTime(d.toLocalTime()).atZone(d.getZone()));
            }

            // Now compare  the sequence of original dates with the sequence of converted dates
            for (int i = 0; i < dates.size(); i++) {
                TChronoZonedDateTime<?> a = dates.get(i);
                for (int j = 0; j < otherDates.size(); j++) {
                    TChronoZonedDateTime<?> b = otherDates.get(j);
                    int cmp = TChronoZonedDateTime.timeLineOrder().compare(a, b);
                    if (i < j) {
                        assertTrue(cmp < 0, a + " compare " + b);
                        assertEquals(a.isBefore(b), true, a + " isBefore " + b);
                        assertEquals(a.isAfter(b), false, a + " ifAfter " + b);
                        assertEquals(a.isEqual(b), false, a + " isEqual " + b);
                    } else if (i > j) {
                        assertTrue(cmp > 0, a + " compare " + b);
                        assertEquals(a.isBefore(b), false, a + " isBefore " + b);
                        assertEquals(a.isAfter(b), true, a + " ifAfter " + b);
                        assertEquals(a.isEqual(b), false, a + " isEqual " + b);
                    } else {
                        assertTrue(cmp == 0, a + " compare " + b);
                        assertEquals(a.isBefore(b), false, a + " isBefore " + b);
                        assertEquals(a.isAfter(b), false, a + " ifAfter " + b);
                        assertEquals(a.isEqual(b), true, a + " isEqual " + b);
                    }
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    // Test Serialization of ISO via chrono API
    //-----------------------------------------------------------------------
    @Test( dataProvider="calendars")
    public void test_ChronoZonedDateTimeSerialization(TChronology chrono) throws Exception {
        TZonedDateTime ref = TLocalDate.of(2000, 1, 5).atTime(12, 1, 2, 3).atZone(TZoneId.of("GMT+01:23"));
        TChronoZonedDateTime<?> orginal = chrono.date(ref).atTime(ref.toLocalTime()).atZone(ref.getZone());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(orginal);
        out.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);
        TChronoZonedDateTime<?> ser = (TChronoZonedDateTime<?>) in.readObject();
        assertEquals(ser, orginal, "deserialized date is wrong");
    }


    static class FixedAdjuster implements TTemporalAdjuster, TTemporalAmount {
        private TTemporal datetime;

        FixedAdjuster(TTemporal datetime) {
            this.datetime = datetime;
        }

        @Override
        public TTemporal adjustInto(TTemporal ignore) {
            return datetime;
        }

        @Override
        public TTemporal addTo(TTemporal ignore) {
            return datetime;
        }

        @Override
        public TTemporal subtractFrom(TTemporal ignore) {
            return datetime;
        }

        @Override
        public List<TTemporalUnit> getUnits() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long get(TTemporalUnit unit) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    static class FixedPeriodUnit implements TTemporalUnit {
        private TTemporal dateTime;

        FixedPeriodUnit(TTemporal dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        public String toString() {
            return "FixedPeriodUnit";
        }

        @Override
        public TDuration getDuration() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isDurationEstimated() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isDateBased() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isTimeBased() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isSupportedBy(TTemporal dateTime) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R extends TTemporal> R addTo(R dateTime, long periodToAdd) {
            return (R) this.dateTime;
        }

        @Override
        public long between(TTemporal temporal1, TTemporal temporal2) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    static class FixedDateTimeField implements TTemporalField {
        private TTemporal dateTime;
        FixedDateTimeField(TTemporal dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        public String toString() {
            return "FixedDateTimeField";
        }

        @Override
        public TTemporalUnit getBaseUnit() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public TTemporalUnit getRangeUnit() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public TValueRange range() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isDateBased() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isTimeBased() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isSupportedBy(TTemporalAccessor dateTime) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public TValueRange rangeRefinedBy(TTemporalAccessor dateTime) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long getFrom(TTemporalAccessor dateTime) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R extends TTemporal> R adjustInto(R dateTime, long newValue) {
            return (R) this.dateTime;
        }

        @Override
        public String getDisplayName(TLocale locale) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public TTemporalAccessor resolve(Map<TTemporalField, Long> fieldValues,
                        TTemporalAccessor partialTemporal, TResolverStyle resolverStyle) {
            return null;
        }
    }
}
