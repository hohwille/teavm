/*
 *  Copyright 2020 adopted to TeaVM by Joerg Hohwiller
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.MinguoChronology;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
public class TestChronoLocalDate {
    Chronology[][] data_of_calendars() {

        return new Chronology[][] { { HijrahChronology.INSTANCE }, { IsoChronology.INSTANCE },
        { JapaneseChronology.INSTANCE }, { MinguoChronology.INSTANCE }, { ThaiBuddhistChronology.INSTANCE } };
    }

    @Test
    public void test_badWithAdjusterChrono() {

        for (Object[] data : data_of_calendars()) {
            Chronology chrono = (Chronology) data[0];

            LocalDate refDate = LocalDate.of(1900, 1, 1);
            ChronoLocalDate date = chrono.date(refDate);
            for (Chronology[] clist : data_of_calendars()) {
                Chronology chrono2 = clist[0];
                ChronoLocalDate date2 = chrono2.date(refDate);
                TemporalAdjuster adjuster = new FixedAdjuster(date2);
                if (chrono != chrono2) {
                    try {
                        date.with(adjuster);
                        Assert.fail("WithAdjuster should have thrown a ClassCastException");
                    } catch (ClassCastException cce) {
                        // Expected exception; not an error
                    }
                } else {
                    // Same chronology,
                    ChronoLocalDate result = date.with(adjuster);
                    assertEquals("WithAdjuster failed to replace date", result, date2);
                }
            }
        }
    }

    @Test
    public void test_badPlusAdjusterChrono() {

        for (Object[] data : data_of_calendars()) {
            Chronology chrono = (Chronology) data[0];

            LocalDate refDate = LocalDate.of(1900, 1, 1);
            ChronoLocalDate date = chrono.date(refDate);
            for (Chronology[] clist : data_of_calendars()) {
                Chronology chrono2 = clist[0];
                ChronoLocalDate date2 = chrono2.date(refDate);
                TemporalAmount adjuster = new FixedAdjuster(date2);
                if (chrono != chrono2) {
                    try {
                        date.plus(adjuster);
                        Assert.fail("WithAdjuster should have thrown a ClassCastException");
                    } catch (ClassCastException cce) {
                        // Expected exception; not an error
                    }
                } else {
                    // Same chronology,
                    ChronoLocalDate result = date.plus(adjuster);
                    assertEquals("WithAdjuster failed to replace date", result, date2);
                }
            }
        }
    }

    @Test
    public void test_badMinusAdjusterChrono() {

        for (Object[] data : data_of_calendars()) {
            Chronology chrono = (Chronology) data[0];

            LocalDate refDate = LocalDate.of(1900, 1, 1);
            ChronoLocalDate date = chrono.date(refDate);
            for (Chronology[] clist : data_of_calendars()) {
                Chronology chrono2 = clist[0];
                ChronoLocalDate date2 = chrono2.date(refDate);
                TemporalAmount adjuster = new FixedAdjuster(date2);
                if (chrono != chrono2) {
                    try {
                        date.minus(adjuster);
                        Assert.fail("WithAdjuster should have thrown a ClassCastException");
                    } catch (ClassCastException cce) {
                        // Expected exception; not an error
                    }
                } else {
                    // Same chronology,
                    ChronoLocalDate result = date.minus(adjuster);
                    assertEquals("WithAdjuster failed to replace date", result, date2);
                }
            }
        }
    }

    @Test
    public void test_badPlusPeriodUnitChrono() {

        for (Object[] data : data_of_calendars()) {
            Chronology chrono = (Chronology) data[0];

            LocalDate refDate = LocalDate.of(1900, 1, 1);
            ChronoLocalDate date = chrono.date(refDate);
            for (Chronology[] clist : data_of_calendars()) {
                Chronology chrono2 = clist[0];
                ChronoLocalDate date2 = chrono2.date(refDate);
                TemporalUnit adjuster = new FixedPeriodUnit(date2);
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
                    ChronoLocalDate result = date.plus(1, adjuster);
                    assertEquals("WithAdjuster failed to replace date", result, date2);
                }
            }
        }
    }

    @Test
    public void test_badMinusPeriodUnitChrono() {

        for (Object[] data : data_of_calendars()) {
            Chronology chrono = (Chronology) data[0];

            LocalDate refDate = LocalDate.of(1900, 1, 1);
            ChronoLocalDate date = chrono.date(refDate);
            for (Chronology[] clist : data_of_calendars()) {
                Chronology chrono2 = clist[0];
                ChronoLocalDate date2 = chrono2.date(refDate);
                TemporalUnit adjuster = new FixedPeriodUnit(date2);
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
                    ChronoLocalDate result = date.minus(1, adjuster);
                    assertEquals("WithAdjuster failed to replace date", result, date2);
                }
            }
        }
    }

    @Test
    public void test_badDateTimeFieldChrono() {

        for (Object[] data : data_of_calendars()) {
            Chronology chrono = (Chronology) data[0];

            LocalDate refDate = LocalDate.of(1900, 1, 1);
            ChronoLocalDate date = chrono.date(refDate);
            for (Chronology[] clist : data_of_calendars()) {
                Chronology chrono2 = clist[0];
                ChronoLocalDate date2 = chrono2.date(refDate);
                TemporalField adjuster = new FixedDateTimeField(date2);
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
                    ChronoLocalDate result = date.with(adjuster, 1);
                    assertEquals("WithAdjuster failed to replace date", result, date2);
                }
            }
        }
    }

    @Test
    public void test_date_comparisons() {

        for (Object[] data : data_of_calendars()) {
            Chronology chrono = (Chronology) data[0];

            List<ChronoLocalDate> dates = new ArrayList<ChronoLocalDate>();

            ChronoLocalDate date = chrono.date(LocalDate.of(1900, 1, 1));

            // Insert dates in order, no duplicates
            if (chrono != JapaneseChronology.INSTANCE) {
                dates.add(date.minus(1000, ChronoUnit.YEARS));
                dates.add(date.minus(100, ChronoUnit.YEARS));
            }
            dates.add(date.minus(10, ChronoUnit.YEARS));
            dates.add(date.minus(1, ChronoUnit.YEARS));
            dates.add(date.minus(1, ChronoUnit.MONTHS));
            dates.add(date.minus(1, ChronoUnit.WEEKS));
            dates.add(date.minus(1, ChronoUnit.DAYS));
            dates.add(date);
            dates.add(date.plus(1, ChronoUnit.DAYS));
            dates.add(date.plus(1, ChronoUnit.WEEKS));
            dates.add(date.plus(1, ChronoUnit.MONTHS));
            dates.add(date.plus(1, ChronoUnit.YEARS));
            dates.add(date.plus(10, ChronoUnit.YEARS));
            dates.add(date.plus(100, ChronoUnit.YEARS));
            dates.add(date.plus(1000, ChronoUnit.YEARS));

            // Check these dates against the corresponding dates for every calendar
            for (Chronology[] clist : data_of_calendars()) {
                List<ChronoLocalDate> otherDates = new ArrayList<>();
                Chronology chrono2 = clist[0];
                if (chrono2 == JapaneseChronology.INSTANCE) {
                    continue;
                }
                for (ChronoLocalDate d : dates) {
                    otherDates.add(chrono2.date(d));
                }

                // Now compare the sequence of original dates with the sequence of converted dates
                for (int i = 0; i < dates.size(); i++) {
                    ChronoLocalDate a = dates.get(i);
                    for (int j = 0; j < otherDates.size(); j++) {
                        ChronoLocalDate b = otherDates.get(j);
                        int cmp = ChronoLocalDate.timeLineOrder().compare(a, b);
                        if (i < j) {
                            assertTrue(a + " compare " + b, cmp < 0);
                            assertEquals(a + " isBefore " + b, a.isBefore(b), true);
                            assertEquals(a + " isAfter " + b, a.isAfter(b), false);
                            assertEquals(a + " isEqual " + b, a.isEqual(b), false);
                        } else if (i > j) {
                            assertTrue(a + " compare " + b, cmp > 0);
                            assertEquals(a + " isBefore " + b, a.isBefore(b), false);
                            assertEquals(a + " isAfter " + b, a.isAfter(b), true);
                            assertEquals(a + " isEqual " + b, a.isEqual(b), false);
                        } else {
                            assertTrue(a + " compare " + b, cmp == 0);
                            assertEquals(a + " isBefore " + b, a.isBefore(b), false);
                            assertEquals(a + " isAfter " + b, a.isAfter(b), false);
                            assertEquals(a + " isEqual " + b, a.isEqual(b), true);
                        }
                    }
                }
            }
        }
    }

    static class FixedAdjuster implements TemporalAdjuster, TemporalAmount {
        private Temporal datetime;

        FixedAdjuster(Temporal datetime) {

            this.datetime = datetime;
        }

        @Override
        public Temporal adjustInto(Temporal ignore) {

            return this.datetime;
        }

        @Override
        public Temporal addTo(Temporal ignore) {

            return this.datetime;
        }

        @Override
        public Temporal subtractFrom(Temporal ignore) {

            return this.datetime;
        }

        @Override
        public List<TemporalUnit> getUnits() {

            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long get(TemporalUnit unit) {

            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    static class FixedPeriodUnit implements TemporalUnit {
        private Temporal dateTime;

        FixedPeriodUnit(Temporal dateTime) {

            this.dateTime = dateTime;
        }

        @Override
        public String toString() {

            return "FixedPeriodUnit";
        }

        @Override
        public Duration getDuration() {

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
        public boolean isSupportedBy(Temporal dateTime) {

            throw new UnsupportedOperationException("Not supported yet.");
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R extends Temporal> R addTo(R dateTime, long periodToAdd) {

            return (R) this.dateTime;
        }

        @Override
        public long between(Temporal temporal1, Temporal temporal2) {

            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    static class FixedDateTimeField implements TemporalField {
        private Temporal dateTime;

        FixedDateTimeField(Temporal dateTime) {

            this.dateTime = dateTime;
        }

        @Override
        public String toString() {

            return "FixedDateTimeField";
        }

        @Override
        public TemporalUnit getBaseUnit() {

            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public TemporalUnit getRangeUnit() {

            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ValueRange range() {

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
        public boolean isSupportedBy(TemporalAccessor dateTime) {

            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ValueRange rangeRefinedBy(TemporalAccessor dateTime) {

            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long getFrom(TemporalAccessor dateTime) {

            throw new UnsupportedOperationException("Not supported yet.");
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R extends Temporal> R adjustInto(R dateTime, long newValue) {

            return (R) this.dateTime;
        }

        @Override
        public String getDisplayName(Locale locale) {

            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public TemporalAccessor resolve(Map<TemporalField, Long> fieldValues, TemporalAccessor partialTemporal,
                ResolverStyle resolverStyle) {

            return null;
        }
    }
}
