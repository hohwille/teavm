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
import org.teavm.classlib.java.time.format.TResolverStyle;
import org.teavm.classlib.java.time.temporal.TChronoUnit;
import org.teavm.classlib.java.time.temporal.TTemporal;
import org.teavm.classlib.java.time.temporal.TTemporalAccessor;
import org.teavm.classlib.java.time.temporal.TTemporalAdjuster;
import org.teavm.classlib.java.time.temporal.TTemporalAmount;
import org.teavm.classlib.java.time.temporal.TTemporalField;
import org.teavm.classlib.java.time.temporal.TTemporalUnit;
import org.teavm.classlib.java.time.temporal.TValueRange;

@Test
public class TestChronoLocalDate {
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
                    {TThaiBuddhistChronology.INSTANCE}};
    }

    @Test(dataProvider="calendars")
    public void test_badWithAdjusterChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoLocalDate date = chrono.date(refDate);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoLocalDate date2 = chrono2.date(refDate);
            TTemporalAdjuster adjuster = new FixedAdjuster(date2);
            if (chrono != chrono2) {
                try {
                    date.with(adjuster);
                    Assert.fail("WithAdjuster should have thrown a ClassCastException");
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoLocalDate result = date.with(adjuster);
                assertEquals(result, date2, "WithAdjuster failed to replace date");
            }
        }
    }

    @Test(dataProvider="calendars")
    public void test_badPlusAdjusterChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoLocalDate date = chrono.date(refDate);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoLocalDate date2 = chrono2.date(refDate);
            TTemporalAmount adjuster = new FixedAdjuster(date2);
            if (chrono != chrono2) {
                try {
                    date.plus(adjuster);
                    Assert.fail("WithAdjuster should have thrown a ClassCastException");
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoLocalDate result = date.plus(adjuster);
                assertEquals(result, date2, "WithAdjuster failed to replace date");
            }
        }
    }

    @Test(dataProvider="calendars")
    public void test_badMinusAdjusterChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoLocalDate date = chrono.date(refDate);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoLocalDate date2 = chrono2.date(refDate);
            TTemporalAmount adjuster = new FixedAdjuster(date2);
            if (chrono != chrono2) {
                try {
                    date.minus(adjuster);
                    Assert.fail("WithAdjuster should have thrown a ClassCastException");
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoLocalDate result = date.minus(adjuster);
                assertEquals(result, date2, "WithAdjuster failed to replace date");
            }
        }
    }

    @Test(dataProvider="calendars")
    public void test_badPlusPeriodUnitChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoLocalDate date = chrono.date(refDate);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoLocalDate date2 = chrono2.date(refDate);
            TTemporalUnit adjuster = new FixedPeriodUnit(date2);
            if (chrono != chrono2) {
                try {
                    date.plus(1, adjuster);
                    Assert.fail("PeriodUnit.doAdd plus should have thrown a ClassCastException" + date.getClass()
                            + ", can not be cast to " + date2.getClass());
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoLocalDate result = date.plus(1, adjuster);
                assertEquals(result, date2, "WithAdjuster failed to replace date");
            }
        }
    }

    @Test(dataProvider="calendars")
    public void test_badMinusPeriodUnitChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoLocalDate date = chrono.date(refDate);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoLocalDate date2 = chrono2.date(refDate);
            TTemporalUnit adjuster = new FixedPeriodUnit(date2);
            if (chrono != chrono2) {
                try {
                    date.minus(1, adjuster);
                    Assert.fail("PeriodUnit.doAdd minus should have thrown a ClassCastException" + date.getClass()
                            + ", can not be cast to " + date2.getClass());
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoLocalDate result = date.minus(1, adjuster);
                assertEquals(result, date2, "WithAdjuster failed to replace date");
            }
        }
    }

    @Test(dataProvider="calendars")
    public void test_badDateTimeFieldChrono(TChronology chrono) {
        TLocalDate refDate = TLocalDate.of(1900, 1, 1);
        TChronoLocalDate date = chrono.date(refDate);
        for (TChronology[] clist : data_of_calendars()) {
            TChronology chrono2 = clist[0];
            TChronoLocalDate date2 = chrono2.date(refDate);
            TTemporalField adjuster = new FixedDateTimeField(date2);
            if (chrono != chrono2) {
                try {
                    date.with(adjuster, 1);
                    Assert.fail("DateTimeField doSet should have thrown a ClassCastException" + date.getClass()
                            + ", can not be cast to " + date2.getClass());
                } catch (ClassCastException cce) {
                    // Expected exception; not an error
                }
            } else {
                // Same chronology,
                TChronoLocalDate result = date.with(adjuster, 1);
                assertEquals(result, date2, "DateTimeField doSet failed to replace date");
            }
        }
    }

    //-----------------------------------------------------------------------
    // isBefore, isAfter, isEqual, DATE_COMPARATOR
    //-----------------------------------------------------------------------
    @Test(dataProvider="calendars")
    public void test_date_comparisons(TChronology chrono) {
        List<TChronoLocalDate> dates = new ArrayList<TChronoLocalDate>();

        TChronoLocalDate date = chrono.date(TLocalDate.of(1900, 1, 1));

        // Insert dates in order, no duplicates
        if (chrono != TJapaneseChronology.INSTANCE) {
            dates.add(date.minus(1000, TChronoUnit.YEARS));
            dates.add(date.minus(100, TChronoUnit.YEARS));
        }
        dates.add(date.minus(10, TChronoUnit.YEARS));
        dates.add(date.minus(1, TChronoUnit.YEARS));
        dates.add(date.minus(1, TChronoUnit.MONTHS));
        dates.add(date.minus(1, TChronoUnit.WEEKS));
        dates.add(date.minus(1, TChronoUnit.DAYS));
        dates.add(date);
        dates.add(date.plus(1, TChronoUnit.DAYS));
        dates.add(date.plus(1, TChronoUnit.WEEKS));
        dates.add(date.plus(1, TChronoUnit.MONTHS));
        dates.add(date.plus(1, TChronoUnit.YEARS));
        dates.add(date.plus(10, TChronoUnit.YEARS));
        dates.add(date.plus(100, TChronoUnit.YEARS));
        dates.add(date.plus(1000, TChronoUnit.YEARS));

        // Check these dates against the corresponding dates for every calendar
        for (TChronology[] clist : data_of_calendars()) {
            List<TChronoLocalDate> otherDates = new ArrayList<TChronoLocalDate>();
            TChronology chrono2 = clist[0];
            if (chrono2 == TJapaneseChronology.INSTANCE) {
                continue;
            }
            for (TChronoLocalDate d : dates) {
                otherDates.add(chrono2.date(d));
            }

            // Now compare  the sequence of original dates with the sequence of converted dates
            for (int i = 0; i < dates.size(); i++) {
                TChronoLocalDate a = dates.get(i);
                for (int j = 0; j < otherDates.size(); j++) {
                    TChronoLocalDate b = otherDates.get(j);
                    int cmp = TChronoLocalDate.timeLineOrder().compare(a, b);
                    if (i < j) {
                        assertTrue(cmp < 0, a + " compare " + b);
                        assertEquals(a.isBefore(b), true, a + " isBefore " + b);
                        assertEquals(a.isAfter(b), false, a + " isAfter " + b);
                        assertEquals(a.isEqual(b), false, a + " isEqual " + b);
                    } else if (i > j) {
                        assertTrue(cmp > 0, a + " compare " + b);
                        assertEquals(a.isBefore(b), false, a + " isBefore " + b);
                        assertEquals(a.isAfter(b), true, a + " isAfter " + b);
                        assertEquals(a.isEqual(b), false, a + " isEqual " + b);
                    } else {
                        assertTrue(cmp == 0, a + " compare " + b);
                        assertEquals(a.isBefore(b), false, a + " isBefore " + b);
                        assertEquals(a.isAfter(b), false, a + " isAfter " + b);
                        assertEquals(a.isEqual(b), true, a + " isEqual " + b);
                    }
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    // Test Serialization of Calendars
    //-----------------------------------------------------------------------
    @Test( dataProvider="calendars")
    public void test_ChronoSerialization(TChronology chrono) throws Exception {
        TLocalDate ref = TLocalDate.of(1900, 1, 5);
        TChronoLocalDate orginal = chrono.date(ref);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(orginal);
        out.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);
        TChronoLocalDate ser = (TChronoLocalDate) in.readObject();
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
