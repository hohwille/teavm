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
package org.teavm.classlib.java.time.temporal;

import static java.time.Month.AUGUST;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JULY;
import static java.time.Month.JUNE;
import static java.time.Month.MARCH;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.FOREVER;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
public class TestChronoUnit {

    Object[][] data_yearsBetween() {

        return new Object[][] { { date(1939, SEPTEMBER, 2), date(1939, SEPTEMBER, 1), 0 },
        { date(1939, SEPTEMBER, 2), date(1939, SEPTEMBER, 2), 0 },
        { date(1939, SEPTEMBER, 2), date(1939, SEPTEMBER, 3), 0 },

        { date(1939, SEPTEMBER, 2), date(1940, SEPTEMBER, 1), 0 },
        { date(1939, SEPTEMBER, 2), date(1940, SEPTEMBER, 2), 1 },
        { date(1939, SEPTEMBER, 2), date(1940, SEPTEMBER, 3), 1 },

        { date(1939, SEPTEMBER, 2), date(1938, SEPTEMBER, 1), -1 },
        { date(1939, SEPTEMBER, 2), date(1938, SEPTEMBER, 2), -1 },
        { date(1939, SEPTEMBER, 2), date(1938, SEPTEMBER, 3), 0 },

        { date(1939, SEPTEMBER, 2), date(1945, SEPTEMBER, 3), 6 },
        { date(1939, SEPTEMBER, 2), date(1945, OCTOBER, 3), 6 },
        { date(1939, SEPTEMBER, 2), date(1945, AUGUST, 3), 5 }, };
    }

    @Test
    public void test_yearsBetween() {

        for (Object[] data : data_yearsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(YEARS.between(start, end), expected);
        }
    }

    @Test
    public void test_yearsBetweenReversed() {

        for (Object[] data : data_yearsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(YEARS.between(end, start), -expected);
        }
    }

    @Test
    public void test_yearsBetween_LocalDateTimeSameTime() {

        for (Object[] data : data_yearsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(YEARS.between(start.atTime(12, 30), end.atTime(12, 30)), expected);
        }
    }

    @Test
    public void test_yearsBetween_LocalDateTimeLaterTime() {

        for (Object[] data : data_yearsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            if (end.isAfter(start)) {
                assertEquals(YEARS.between(start.atTime(12, 30), end.atTime(12, 31)), expected);
            } else {
                assertEquals(YEARS.between(start.atTime(12, 31), end.atTime(12, 30)), expected);
            }
        }
    }

    @Test
    public void test_yearsBetween_ZonedDateSameOffset() {

        for (Object[] data : data_yearsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(
                    YEARS.between(start.atStartOfDay(ZoneOffset.ofHours(2)), end.atStartOfDay(ZoneOffset.ofHours(2))),
                    expected);
        }
    }

    @Test
    public void test_yearsBetween_ZonedDateLaterOffset() {

        for (Object[] data : data_yearsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            // +01:00 is later than +02:00
            if (end.isAfter(start)) {
                assertEquals(YEARS.between(start.atStartOfDay(ZoneOffset.ofHours(2)),
                        end.atStartOfDay(ZoneOffset.ofHours(1))), expected);
            } else {
                assertEquals(YEARS.between(start.atStartOfDay(ZoneOffset.ofHours(1)),
                        end.atStartOfDay(ZoneOffset.ofHours(2))), expected);
            }
        }
    }

    Object[][] data_monthsBetween() {

        return new Object[][] { { date(2012, JULY, 2), date(2012, JULY, 1), 0 },
        { date(2012, JULY, 2), date(2012, JULY, 2), 0 }, { date(2012, JULY, 2), date(2012, JULY, 3), 0 },

        { date(2012, JULY, 2), date(2012, AUGUST, 1), 0 }, { date(2012, JULY, 2), date(2012, AUGUST, 2), 1 },
        { date(2012, JULY, 2), date(2012, AUGUST, 3), 1 },

        { date(2012, JULY, 2), date(2012, SEPTEMBER, 1), 1 }, { date(2012, JULY, 2), date(2012, SEPTEMBER, 2), 2 },
        { date(2012, JULY, 2), date(2012, SEPTEMBER, 3), 2 },

        { date(2012, JULY, 2), date(2012, JUNE, 1), -1 }, { date(2012, JULY, 2), date(2012, JUNE, 2), -1 },
        { date(2012, JULY, 2), date(2012, JUNE, 3), 0 },

        { date(2012, FEBRUARY, 27), date(2012, MARCH, 26), 0 }, { date(2012, FEBRUARY, 27), date(2012, MARCH, 27), 1 },
        { date(2012, FEBRUARY, 27), date(2012, MARCH, 28), 1 },

        { date(2012, FEBRUARY, 28), date(2012, MARCH, 27), 0 }, { date(2012, FEBRUARY, 28), date(2012, MARCH, 28), 1 },
        { date(2012, FEBRUARY, 28), date(2012, MARCH, 29), 1 },

        { date(2012, FEBRUARY, 29), date(2012, MARCH, 28), 0 }, { date(2012, FEBRUARY, 29), date(2012, MARCH, 29), 1 },
        { date(2012, FEBRUARY, 29), date(2012, MARCH, 30), 1 }, };
    }

    @Test
    public void test_monthsBetween() {

        for (Object[] data : data_monthsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(MONTHS.between(start, end), expected);
        }
    }

    @Test
    public void test_monthsBetweenReversed() {

        for (Object[] data : data_monthsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(MONTHS.between(end, start), -expected);
        }
    }

    @Test
    public void test_monthsBetween_LocalDateTimeSameTime() {

        for (Object[] data : data_monthsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(MONTHS.between(start.atTime(12, 30), end.atTime(12, 30)), expected);
        }
    }

    @Test
    public void test_monthsBetween_LocalDateTimeLaterTime() {

        for (Object[] data : data_monthsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            if (end.isAfter(start)) {
                assertEquals(MONTHS.between(start.atTime(12, 30), end.atTime(12, 31)), expected);
            } else {
                assertEquals(MONTHS.between(start.atTime(12, 31), end.atTime(12, 30)), expected);
            }
        }
    }

    @Test
    public void test_monthsBetween_ZonedDateSameOffset() {

        for (Object[] data : data_monthsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(
                    MONTHS.between(start.atStartOfDay(ZoneOffset.ofHours(2)), end.atStartOfDay(ZoneOffset.ofHours(2))),
                    expected);
        }
    }

    @Test
    public void test_monthsBetween_ZonedDateLaterOffset() {

        for (Object[] data : data_monthsBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            // +01:00 is later than +02:00
            if (end.isAfter(start)) {
                assertEquals(MONTHS.between(start.atStartOfDay(ZoneOffset.ofHours(2)),
                        end.atStartOfDay(ZoneOffset.ofHours(1))), expected);
            } else {
                assertEquals(MONTHS.between(start.atStartOfDay(ZoneOffset.ofHours(1)),
                        end.atStartOfDay(ZoneOffset.ofHours(2))), expected);
            }
        }
    }

    Object[][] data_weeksBetween() {

        return new Object[][] { { date(2012, JULY, 2), date(2012, JUNE, 25), -1 },
        { date(2012, JULY, 2), date(2012, JUNE, 26), 0 }, { date(2012, JULY, 2), date(2012, JULY, 2), 0 },
        { date(2012, JULY, 2), date(2012, JULY, 8), 0 }, { date(2012, JULY, 2), date(2012, JULY, 9), 1 },

        { date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 21), -1 },
        { date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 22), 0 },
        { date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 28), 0 },
        { date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 29), 0 },
        { date(2012, FEBRUARY, 28), date(2012, MARCH, 1), 0 }, { date(2012, FEBRUARY, 28), date(2012, MARCH, 5), 0 },
        { date(2012, FEBRUARY, 28), date(2012, MARCH, 6), 1 },

        { date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 22), -1 },
        { date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 23), 0 },
        { date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 28), 0 },
        { date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 29), 0 },
        { date(2012, FEBRUARY, 29), date(2012, MARCH, 1), 0 }, { date(2012, FEBRUARY, 29), date(2012, MARCH, 6), 0 },
        { date(2012, FEBRUARY, 29), date(2012, MARCH, 7), 1 }, };
    }

    @Test
    public void test_weeksBetween() {

        for (Object[] data : data_weeksBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(WEEKS.between(start, end), expected);
        }
    }

    @Test
    public void test_weeksBetweenReversed() {

        for (Object[] data : data_weeksBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(WEEKS.between(end, start), -expected);
        }
    }

    Object[][] data_daysBetween() {

        return new Object[][] { { date(2012, JULY, 2), date(2012, JULY, 1), -1 },
        { date(2012, JULY, 2), date(2012, JULY, 2), 0 }, { date(2012, JULY, 2), date(2012, JULY, 3), 1 },

        { date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 27), -1 },
        { date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 28), 0 },
        { date(2012, FEBRUARY, 28), date(2012, FEBRUARY, 29), 1 },
        { date(2012, FEBRUARY, 28), date(2012, MARCH, 1), 2 },

        { date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 27), -2 },
        { date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 28), -1 },
        { date(2012, FEBRUARY, 29), date(2012, FEBRUARY, 29), 0 },
        { date(2012, FEBRUARY, 29), date(2012, MARCH, 1), 1 },

        { date(2012, MARCH, 1), date(2012, FEBRUARY, 27), -3 }, { date(2012, MARCH, 1), date(2012, FEBRUARY, 28), -2 },
        { date(2012, MARCH, 1), date(2012, FEBRUARY, 29), -1 }, { date(2012, MARCH, 1), date(2012, MARCH, 1), 0 },
        { date(2012, MARCH, 1), date(2012, MARCH, 2), 1 },

        { date(2012, MARCH, 1), date(2013, FEBRUARY, 28), 364 }, { date(2012, MARCH, 1), date(2013, MARCH, 1), 365 },

        { date(2011, MARCH, 1), date(2012, FEBRUARY, 28), 364 },
        { date(2011, MARCH, 1), date(2012, FEBRUARY, 29), 365 }, { date(2011, MARCH, 1), date(2012, MARCH, 1), 366 }, };
    }

    @Test
    public void test_daysBetween() {

        for (Object[] data : data_daysBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(DAYS.between(start, end), expected);
        }
    }

    @Test
    public void test_daysBetweenReversed() {

        for (Object[] data : data_daysBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(DAYS.between(end, start), -expected);
        }
    }

    @Test
    public void test_daysBetween_LocalDateTimeSameTime() {

        for (Object[] data : data_daysBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(DAYS.between(start.atTime(12, 30), end.atTime(12, 30)), expected);
        }
    }

    @Test
    public void test_daysBetween_LocalDateTimeLaterTime() {

        for (Object[] data : data_daysBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            if (end.isAfter(start)) {
                assertEquals(DAYS.between(start.atTime(12, 30), end.atTime(12, 31)), expected);
            } else {
                assertEquals(DAYS.between(start.atTime(12, 31), end.atTime(12, 30)), expected);
            }
        }
    }

    @Test
    public void test_daysBetween_ZonedDateSameOffset() {

        for (Object[] data : data_daysBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            assertEquals(
                    DAYS.between(start.atStartOfDay(ZoneOffset.ofHours(2)), end.atStartOfDay(ZoneOffset.ofHours(2))),
                    expected);
        }
    }

    @Test
    public void test_daysBetween_ZonedDateLaterOffset() {

        for (Object[] data : data_daysBetween()) {
            LocalDate start = (LocalDate) data[0];
            LocalDate end = (LocalDate) data[1];
            long expected = ((Number) data[2]).longValue();

            // +01:00 is later than +02:00
            if (end.isAfter(start)) {
                assertEquals(DAYS.between(start.atStartOfDay(ZoneOffset.ofHours(2)),
                        end.atStartOfDay(ZoneOffset.ofHours(1))), expected);
            } else {
                assertEquals(DAYS.between(start.atStartOfDay(ZoneOffset.ofHours(1)),
                        end.atStartOfDay(ZoneOffset.ofHours(2))), expected);
            }
        }
    }

    @Test
    public void test_isDateBased() {

        for (ChronoUnit unit : ChronoUnit.values()) {
            if (unit.getDuration().getSeconds() < 86400) {
                assertEquals(unit.isDateBased(), false);
            } else if (unit == FOREVER) {
                assertEquals(unit.isDateBased(), false);
            } else {
                assertEquals(unit.isDateBased(), true);
            }
        }
    }

    @Test
    public void test_isTimeBased() {

        for (ChronoUnit unit : ChronoUnit.values()) {
            if (unit.getDuration().getSeconds() < 86400) {
                assertEquals(unit.isTimeBased(), true);
            } else if (unit == FOREVER) {
                assertEquals(unit.isTimeBased(), false);
            } else {
                assertEquals(unit.isTimeBased(), false);
            }
        }
    }

    private static LocalDate date(int year, Month month, int dom) {

        return LocalDate.of(year, month, dom);
    }

}
