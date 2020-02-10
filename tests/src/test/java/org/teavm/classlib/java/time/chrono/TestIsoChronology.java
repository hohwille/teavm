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

import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoChronology;
import java.time.chrono.IsoEra;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
public class TestIsoChronology {

    @Test
    public void test_chrono_byName() {

        Chronology c = IsoChronology.INSTANCE;
        Chronology test = Chronology.of("ISO");
        Assert.assertNotNull("The ISO calendar could not be found byName", test);
        Assert.assertEquals("ID mismatch", test.getId(), "ISO");
        Assert.assertEquals("Type mismatch", test.getCalendarType(), "iso8601");
        Assert.assertEquals(test, c);
    }

    @Test
    public void instanceNotNull() {

        assertNotNull(IsoChronology.INSTANCE);
    }

    @Test
    public void test_eraOf() {

        assertEquals(IsoChronology.INSTANCE.eraOf(0), IsoEra.BCE);
        assertEquals(IsoChronology.INSTANCE.eraOf(1), IsoEra.CE);
    }

    Object[][] data_samples() {

        return new Object[][] { { IsoChronology.INSTANCE.date(1, 7, 8), LocalDate.of(1, 7, 8) },
        { IsoChronology.INSTANCE.date(1, 7, 20), LocalDate.of(1, 7, 20) },
        { IsoChronology.INSTANCE.date(1, 7, 21), LocalDate.of(1, 7, 21) },

        { IsoChronology.INSTANCE.date(2, 7, 8), LocalDate.of(2, 7, 8) },
        { IsoChronology.INSTANCE.date(3, 6, 27), LocalDate.of(3, 6, 27) },
        { IsoChronology.INSTANCE.date(3, 5, 23), LocalDate.of(3, 5, 23) },
        { IsoChronology.INSTANCE.date(4, 6, 16), LocalDate.of(4, 6, 16) },
        { IsoChronology.INSTANCE.date(4, 7, 3), LocalDate.of(4, 7, 3) },
        { IsoChronology.INSTANCE.date(4, 7, 4), LocalDate.of(4, 7, 4) },
        { IsoChronology.INSTANCE.date(5, 1, 1), LocalDate.of(5, 1, 1) },
        { IsoChronology.INSTANCE.date(1727, 3, 3), LocalDate.of(1727, 3, 3) },
        { IsoChronology.INSTANCE.date(1728, 10, 28), LocalDate.of(1728, 10, 28) },
        { IsoChronology.INSTANCE.date(2012, 10, 29), LocalDate.of(2012, 10, 29) }, };
    }

    @Test
    public void test_toLocalDate() {

        for (Object[] data : data_samples()) {
            ChronoLocalDate isoDate = (ChronoLocalDate) data[0];
            LocalDate iso = (LocalDate) data[1];

            assertEquals(LocalDate.from(isoDate), iso);
        }
    }

    @Test
    public void test_fromCalendrical() {

        for (Object[] data : data_samples()) {
            ChronoLocalDate isoDate = (ChronoLocalDate) data[0];
            LocalDate iso = (LocalDate) data[1];

            assertEquals(IsoChronology.INSTANCE.date(iso), isoDate);
        }
    }

    Object[][] data_badDates() {

        return new Object[][] { { 2012, 0, 0 },

        { 2012, -1, 1 }, { 2012, 0, 1 }, { 2012, 14, 1 }, { 2012, 15, 1 },

        { 2012, 1, -1 }, { 2012, 1, 0 }, { 2012, 1, 32 },

        { 2012, 12, -1 }, { 2012, 12, 0 }, { 2012, 12, 32 }, };
    }

    @Test
    public void test_badDates() {

        for (Object[] data : data_badDates()) {
            int year = (int) data[0];
            int month = (int) data[1];
            int dom = (int) data[2];

            try {
                IsoChronology.INSTANCE.date(year, month, dom);
                fail("Expected DateTimeException");
            } catch (DateTimeException e) {
                // expected
            }
        }
    }

    @Test
    public void test_date_withEra() {

        int year = 5;
        int month = 5;
        int dayOfMonth = 5;
        ChronoLocalDate test = IsoChronology.INSTANCE.date(IsoEra.BCE, year, month, dayOfMonth);
        assertEquals(test.getEra(), IsoEra.BCE);
        assertEquals(test.get(ChronoField.YEAR_OF_ERA), year);
        assertEquals(test.get(ChronoField.MONTH_OF_YEAR), month);
        assertEquals(test.get(ChronoField.DAY_OF_MONTH), dayOfMonth);

        assertEquals(test.get(YEAR), 1 + (-1 * year));
        assertEquals(test.get(ERA), 0);
        assertEquals(test.get(YEAR_OF_ERA), year);
    }

    @Test(expected = ClassCastException.class)
    public void test_date_withEra_withWrongEra() {

        IsoChronology.INSTANCE.date(HijrahEra.AH, 1, 1, 1);
    }

    @Test
    public void test_adjust1() {

        ChronoLocalDate base = IsoChronology.INSTANCE.date(1728, 10, 28);
        ChronoLocalDate test = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(test, IsoChronology.INSTANCE.date(1728, 10, 31));
    }

    @Test
    public void test_adjust2() {

        ChronoLocalDate base = IsoChronology.INSTANCE.date(1728, 12, 2);
        ChronoLocalDate test = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(test, IsoChronology.INSTANCE.date(1728, 12, 31));
    }

    @Test
    public void test_adjust_toLocalDate() {

        ChronoLocalDate isoDate = IsoChronology.INSTANCE.date(1726, 1, 4);
        ChronoLocalDate test = isoDate.with(LocalDate.of(2012, 7, 6));
        assertEquals(test, IsoChronology.INSTANCE.date(2012, 7, 6));
    }

    @Test
    public void test_adjust_toMonth() {

        ChronoLocalDate isoDate = IsoChronology.INSTANCE.date(1726, 1, 4);
        assertEquals(IsoChronology.INSTANCE.date(1726, 4, 4), isoDate.with(Month.APRIL));
    }

    @Test
    public void test_LocalDate_adjustToISODate() {

        ChronoLocalDate isoDate = IsoChronology.INSTANCE.date(1728, 10, 29);
        LocalDate test = LocalDate.MIN.with(isoDate);
        assertEquals(test, LocalDate.of(1728, 10, 29));
    }

    @Test
    public void test_LocalDateTime_adjustToISODate() {

        ChronoLocalDate isoDate = IsoChronology.INSTANCE.date(1728, 10, 29);
        LocalDateTime test = LocalDateTime.MIN.with(isoDate);
        assertEquals(test, LocalDateTime.of(1728, 10, 29, 0, 0));
    }

    Object[][] leapYearInformation() {

        return new Object[][] { { 2000, true }, { 1996, true }, { 1600, true },

        { 1900, false }, { 2100, false },

        { -500, false }, { -400, true }, { -300, false }, { -100, false }, { -5, false }, { -4, true }, { -3, false },
        { -2, false }, { -1, false }, { 0, true }, { 1, false }, { 3, false }, { 4, true }, { 5, false },
        { 100, false }, { 300, false }, { 400, true }, { 500, false }, };
    }

    @Test
    public void test_isLeapYear() {

        for (Object[] data : leapYearInformation()) {
            int year = (int) data[0];
            boolean isLeapYear = (boolean) data[1];

            assertEquals(IsoChronology.INSTANCE.isLeapYear(year), isLeapYear);
        }
    }

    @Test
    public void test_now() {

        assertEquals(LocalDate.from(IsoChronology.INSTANCE.dateNow()), LocalDate.now());
    }

    Object[][] data_toString() {

        return new Object[][] { { IsoChronology.INSTANCE.date(1, 1, 1), "0001-01-01" },
        { IsoChronology.INSTANCE.date(1728, 10, 28), "1728-10-28" },
        { IsoChronology.INSTANCE.date(1728, 10, 29), "1728-10-29" },
        { IsoChronology.INSTANCE.date(1727, 12, 5), "1727-12-05" },
        { IsoChronology.INSTANCE.date(1727, 12, 6), "1727-12-06" }, };
    }

    @Test
    public void test_toString() {

        for (Object[] data : data_toString()) {
            ChronoLocalDate isoDate = (ChronoLocalDate) data[0];
            String expected = (String) data[1];

            assertEquals(isoDate.toString(), expected);
        }
    }

    @Test
    public void test_equals_true() {

        assertTrue(IsoChronology.INSTANCE.equals(IsoChronology.INSTANCE));
    }

    @Test
    public void test_equals_false() {

        assertFalse(IsoChronology.INSTANCE.equals(HijrahChronology.INSTANCE));
    }

}
