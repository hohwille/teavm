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
package org.teavm.classlib.java.time;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.AMPM_OF_DAY;
import static java.time.temporal.ChronoField.CLOCK_HOUR_OF_AMPM;
import static java.time.temporal.ChronoField.CLOCK_HOUR_OF_DAY;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.HOUR_OF_AMPM;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MICRO_OF_DAY;
import static java.time.temporal.ChronoField.MICRO_OF_SECOND;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.NANO_OF_DAY;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.PROLEPTIC_MONTH;
import static java.time.temporal.ChronoField.SECOND_OF_DAY;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import static java.time.temporal.ChronoUnit.HALF_DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MICROS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.NANOS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.JulianFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.teavm.classlib.java.time.temporal.MockFieldNoValue;

// @RunWith(TeaVMTestRunner.class)
public class TestLocalDateTime extends AbstractDateTimeTest {

    private static final ZoneOffset OFFSET_PONE = ZoneOffset.ofHours(1);

    private static final ZoneOffset OFFSET_PTWO = ZoneOffset.ofHours(2);

    private static final ZoneOffset OFFSET_MTWO = ZoneOffset.ofHours(-2);

    private static final ZoneId ZONE_PARIS = ZoneId.of("Europe/Paris");

    private static final ZoneId ZONE_GAZA = ZoneId.of("Asia/Gaza");

    private LocalDateTime TEST_2007_07_15_12_30_40_987654321 = LocalDateTime.of(2007, 7, 15, 12, 30, 40, 987654321);

    private LocalDateTime MAX_DATE_TIME;

    private LocalDateTime MIN_DATE_TIME;

    private Instant MAX_INSTANT;

    private Instant MIN_INSTANT;

    @Before
    public void setUp() {

        this.MAX_DATE_TIME = LocalDateTime.MAX;
        this.MIN_DATE_TIME = LocalDateTime.MIN;
        this.MAX_INSTANT = this.MAX_DATE_TIME.atZone(ZoneOffset.UTC).toInstant();
        this.MIN_INSTANT = this.MIN_DATE_TIME.atZone(ZoneOffset.UTC).toInstant();
    }

    @Override
    protected List<TemporalAccessor> samples() {

        TemporalAccessor[] array = { this.TEST_2007_07_15_12_30_40_987654321, LocalDateTime.MAX, LocalDateTime.MIN, };
        return Arrays.asList(array);
    }

    @Override
    protected List<TemporalField> validFields() {

        TemporalField[] array = { NANO_OF_SECOND, NANO_OF_DAY, MICRO_OF_SECOND, MICRO_OF_DAY, MILLI_OF_SECOND,
        MILLI_OF_DAY, SECOND_OF_MINUTE, SECOND_OF_DAY, MINUTE_OF_HOUR, MINUTE_OF_DAY, CLOCK_HOUR_OF_AMPM, HOUR_OF_AMPM,
        CLOCK_HOUR_OF_DAY, HOUR_OF_DAY, AMPM_OF_DAY, DAY_OF_WEEK, ALIGNED_DAY_OF_WEEK_IN_MONTH,
        ALIGNED_DAY_OF_WEEK_IN_YEAR, DAY_OF_MONTH, DAY_OF_YEAR, EPOCH_DAY, ALIGNED_WEEK_OF_MONTH, ALIGNED_WEEK_OF_YEAR,
        MONTH_OF_YEAR, PROLEPTIC_MONTH, YEAR_OF_ERA, YEAR, ERA, JulianFields.JULIAN_DAY,
        JulianFields.MODIFIED_JULIAN_DAY, JulianFields.RATA_DIE, };
        return Arrays.asList(array);
    }

    @Override
    protected List<TemporalField> invalidFields() {

        List<TemporalField> list = new ArrayList<>(Arrays.asList(ChronoField.values()));
        list.removeAll(validFields());
        return list;
    }

    private void check(LocalDateTime dateTime, int y, int m, int d, int h, int mi, int s, int n) {

        assertEquals(dateTime.getYear(), y);
        assertEquals(dateTime.getMonth().getValue(), m);
        assertEquals(dateTime.getDayOfMonth(), d);
        assertEquals(dateTime.getHour(), h);
        assertEquals(dateTime.getMinute(), mi);
        assertEquals(dateTime.getSecond(), s);
        assertEquals(dateTime.getNano(), n);
    }

    private LocalDateTime createDateMidnight(int year, int month, int day) {

        return LocalDateTime.of(year, month, day, 0, 0);
    }

    @Test
    public void test_immutable() {

        Class<LocalDateTime> cls = LocalDateTime.class;
        assertTrue(Modifier.isPublic(cls.getModifiers()));
        assertTrue(Modifier.isFinal(cls.getModifiers()));
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().contains("$") == false) {
                if (Modifier.isStatic(field.getModifiers())) {
                    assertTrue("Field:" + field.getName(), Modifier.isFinal(field.getModifiers()));
                } else {
                    assertTrue("Field:" + field.getName(), Modifier.isPrivate(field.getModifiers()));
                    assertTrue("Field:" + field.getName(), Modifier.isFinal(field.getModifiers()));
                }
            }
        }
    }

    @Test(timeout = 30000) // TODO: remove when time zone loading is faster
    public void now() {

        LocalDateTime expected = LocalDateTime.now(Clock.systemDefaultZone());
        LocalDateTime test = LocalDateTime.now();
        long diff = Math.abs(test.toLocalTime().toNanoOfDay() - expected.toLocalTime().toNanoOfDay());
        if (diff >= 100000000) {
            // may be date change
            expected = LocalDateTime.now(Clock.systemDefaultZone());
            test = LocalDateTime.now();
            diff = Math.abs(test.toLocalTime().toNanoOfDay() - expected.toLocalTime().toNanoOfDay());
        }
        assertTrue(diff < 100000000); // less than 0.1 secs
    }

    @Test(expected = NullPointerException.class)
    public void now_ZoneId_nullZoneId() {

        LocalDateTime.now((ZoneId) null);
    }

    @Test
    public void now_ZoneId() {

        ZoneId zone = ZoneId.of("UTC+01:02:03");
        LocalDateTime expected = LocalDateTime.now(Clock.system(zone));
        LocalDateTime test = LocalDateTime.now(zone);
        for (int i = 0; i < 100; i++) {
            if (expected.equals(test)) {
                return;
            }
            expected = LocalDateTime.now(Clock.system(zone));
            test = LocalDateTime.now(zone);
        }
        assertEquals(test, expected);
    }

    @Test(expected = NullPointerException.class)
    public void now_Clock_nullClock() {

        LocalDateTime.now((Clock) null);
    }

    @Test
    public void now_Clock_allSecsInDay_utc() {

        for (int i = 0; i < (2 * 24 * 60 * 60); i++) {
            Instant instant = Instant.ofEpochSecond(i).plusNanos(123456789L);
            Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
            LocalDateTime test = LocalDateTime.now(clock);
            assertEquals(test.getYear(), 1970);
            assertEquals(test.getMonth(), Month.JANUARY);
            assertEquals(test.getDayOfMonth(), (i < 24 * 60 * 60 ? 1 : 2));
            assertEquals(test.getHour(), (i / (60 * 60)) % 24);
            assertEquals(test.getMinute(), (i / 60) % 60);
            assertEquals(test.getSecond(), i % 60);
            assertEquals(test.getNano(), 123456789);
        }
    }

    @Test
    public void now_Clock_allSecsInDay_offset() {

        for (int i = 0; i < (2 * 24 * 60 * 60); i++) {
            Instant instant = Instant.ofEpochSecond(i).plusNanos(123456789L);
            Clock clock = Clock.fixed(instant.minusSeconds(OFFSET_PONE.getTotalSeconds()), OFFSET_PONE);
            LocalDateTime test = LocalDateTime.now(clock);
            assertEquals(test.getYear(), 1970);
            assertEquals(test.getMonth(), Month.JANUARY);
            assertEquals(test.getDayOfMonth(), (i < 24 * 60 * 60) ? 1 : 2);
            assertEquals(test.getHour(), (i / (60 * 60)) % 24);
            assertEquals(test.getMinute(), (i / 60) % 60);
            assertEquals(test.getSecond(), i % 60);
            assertEquals(test.getNano(), 123456789);
        }
    }

    @Test
    public void now_Clock_allSecsInDay_beforeEpoch() {

        LocalTime expected = LocalTime.MIDNIGHT.plusNanos(123456789L);
        for (int i = -1; i >= -(24 * 60 * 60); i--) {
            Instant instant = Instant.ofEpochSecond(i).plusNanos(123456789L);
            Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
            LocalDateTime test = LocalDateTime.now(clock);
            assertEquals(test.getYear(), 1969);
            assertEquals(test.getMonth(), Month.DECEMBER);
            assertEquals(test.getDayOfMonth(), 31);
            expected = expected.minusSeconds(1);
            assertEquals(test.toLocalTime(), expected);
        }
    }

    @Test
    public void now_Clock_maxYear() {

        Clock clock = Clock.fixed(this.MAX_INSTANT, ZoneOffset.UTC);
        LocalDateTime test = LocalDateTime.now(clock);
        assertEquals(test, this.MAX_DATE_TIME);
    }

    @Test(expected = DateTimeException.class)
    public void now_Clock_tooBig() {

        Clock clock = Clock.fixed(this.MAX_INSTANT.plusSeconds(24 * 60 * 60), ZoneOffset.UTC);
        LocalDateTime.now(clock);
    }

    @Test
    public void now_Clock_minYear() {

        Clock clock = Clock.fixed(this.MIN_INSTANT, ZoneOffset.UTC);
        LocalDateTime test = LocalDateTime.now(clock);
        assertEquals(test, this.MIN_DATE_TIME);
    }

    @Test(expected = DateTimeException.class)
    public void now_Clock_tooLow() {

        Clock clock = Clock.fixed(this.MIN_INSTANT.minusNanos(1), ZoneOffset.UTC);
        LocalDateTime.now(clock);
    }

    @Test
    public void factory_of_4intsMonth() {

        LocalDateTime dateTime = LocalDateTime.of(2007, Month.JULY, 15, 12, 30);
        check(dateTime, 2007, 7, 15, 12, 30, 0, 0);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_4intsMonth_yearTooLow() {

        LocalDateTime.of(Integer.MIN_VALUE, Month.JULY, 15, 12, 30);
    }

    @Test(expected = NullPointerException.class)
    public void factory_of_4intsMonth_nullMonth() {

        LocalDateTime.of(2007, null, 15, 12, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_4intsMonth_dayTooLow() {

        LocalDateTime.of(2007, Month.JULY, -1, 12, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_4intsMonth_dayTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 32, 12, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_4intsMonth_hourTooLow() {

        LocalDateTime.of(2007, Month.JULY, 15, -1, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_4intsMonth_hourTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 15, 24, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_4intsMonth_minuteTooLow() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, -1);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_4intsMonth_minuteTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, 60);
    }

    @Test
    public void factory_of_5intsMonth() {

        LocalDateTime dateTime = LocalDateTime.of(2007, Month.JULY, 15, 12, 30, 40);
        check(dateTime, 2007, 7, 15, 12, 30, 40, 0);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5intsMonth_yearTooLow() {

        LocalDateTime.of(Integer.MIN_VALUE, Month.JULY, 15, 12, 30, 40);
    }

    @Test(expected = NullPointerException.class)
    public void factory_of_5intsMonth_nullMonth() {

        LocalDateTime.of(2007, null, 15, 12, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5intsMonth_dayTooLow() {

        LocalDateTime.of(2007, Month.JULY, -1, 12, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5intsMonth_dayTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 32, 12, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5intsMonth_hourTooLow() {

        LocalDateTime.of(2007, Month.JULY, 15, -1, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5intsMonth_hourTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 15, 24, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5intsMonth_minuteTooLow() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, -1, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5intsMonth_minuteTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, 60, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5intsMonth_secondTooLow() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, 30, -1);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5intsMonth_secondTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, 30, 60);
    }

    @Test
    public void factory_of_6intsMonth() {

        LocalDateTime dateTime = LocalDateTime.of(2007, Month.JULY, 15, 12, 30, 40, 987654321);
        check(dateTime, 2007, 7, 15, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_yearTooLow() {

        LocalDateTime.of(Integer.MIN_VALUE, Month.JULY, 15, 12, 30, 40, 987654321);
    }

    @Test(expected = NullPointerException.class)
    public void factory_of_6intsMonth_nullMonth() {

        LocalDateTime.of(2007, null, 15, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_dayTooLow() {

        LocalDateTime.of(2007, Month.JULY, -1, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_dayTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 32, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_hourTooLow() {

        LocalDateTime.of(2007, Month.JULY, 15, -1, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_hourTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 15, 24, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_minuteTooLow() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, -1, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_minuteTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, 60, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_secondTooLow() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, 30, -1, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_secondTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, 30, 60, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_nanoTooLow() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, 30, 40, -1);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6intsMonth_nanoTooHigh() {

        LocalDateTime.of(2007, Month.JULY, 15, 12, 30, 40, 1000000000);
    }

    @Test
    public void factory_of_5ints() {

        LocalDateTime dateTime = LocalDateTime.of(2007, 7, 15, 12, 30);
        check(dateTime, 2007, 7, 15, 12, 30, 0, 0);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5ints_yearTooLow() {

        LocalDateTime.of(Integer.MIN_VALUE, 7, 15, 12, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5ints_monthTooLow() {

        LocalDateTime.of(2007, 0, 15, 12, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5ints_monthTooHigh() {

        LocalDateTime.of(2007, 13, 15, 12, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5ints_dayTooLow() {

        LocalDateTime.of(2007, 7, -1, 12, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5ints_dayTooHigh() {

        LocalDateTime.of(2007, 7, 32, 12, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5ints_hourTooLow() {

        LocalDateTime.of(2007, 7, 15, -1, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5ints_hourTooHigh() {

        LocalDateTime.of(2007, 7, 15, 24, 30);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5ints_minuteTooLow() {

        LocalDateTime.of(2007, 7, 15, 12, -1);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_5ints_minuteTooHigh() {

        LocalDateTime.of(2007, 7, 15, 12, 60);
    }

    @Test
    public void factory_of_6ints() {

        LocalDateTime dateTime = LocalDateTime.of(2007, 7, 15, 12, 30, 40);
        check(dateTime, 2007, 7, 15, 12, 30, 40, 0);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_yearTooLow() {

        LocalDateTime.of(Integer.MIN_VALUE, 7, 15, 12, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_monthTooLow() {

        LocalDateTime.of(2007, 0, 15, 12, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_monthTooHigh() {

        LocalDateTime.of(2007, 13, 15, 12, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_dayTooLow() {

        LocalDateTime.of(2007, 7, -1, 12, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_dayTooHigh() {

        LocalDateTime.of(2007, 7, 32, 12, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_hourTooLow() {

        LocalDateTime.of(2007, 7, 15, -1, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_hourTooHigh() {

        LocalDateTime.of(2007, 7, 15, 24, 30, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_minuteTooLow() {

        LocalDateTime.of(2007, 7, 15, 12, -1, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_minuteTooHigh() {

        LocalDateTime.of(2007, 7, 15, 12, 60, 40);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_secondTooLow() {

        LocalDateTime.of(2007, 7, 15, 12, 30, -1);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_6ints_secondTooHigh() {

        LocalDateTime.of(2007, 7, 15, 12, 30, 60);
    }

    @Test
    public void factory_of_7ints() {

        LocalDateTime dateTime = LocalDateTime.of(2007, 7, 15, 12, 30, 40, 987654321);
        check(dateTime, 2007, 7, 15, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_yearTooLow() {

        LocalDateTime.of(Integer.MIN_VALUE, 7, 15, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_monthTooLow() {

        LocalDateTime.of(2007, 0, 15, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_monthTooHigh() {

        LocalDateTime.of(2007, 13, 15, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_dayTooLow() {

        LocalDateTime.of(2007, 7, -1, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_dayTooHigh() {

        LocalDateTime.of(2007, 7, 32, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_hourTooLow() {

        LocalDateTime.of(2007, 7, 15, -1, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_hourTooHigh() {

        LocalDateTime.of(2007, 7, 15, 24, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_minuteTooLow() {

        LocalDateTime.of(2007, 7, 15, 12, -1, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_minuteTooHigh() {

        LocalDateTime.of(2007, 7, 15, 12, 60, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_secondTooLow() {

        LocalDateTime.of(2007, 7, 15, 12, 30, -1, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_secondTooHigh() {

        LocalDateTime.of(2007, 7, 15, 12, 30, 60, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_nanoTooLow() {

        LocalDateTime.of(2007, 7, 15, 12, 30, 40, -1);
    }

    @Test(expected = DateTimeException.class)
    public void factory_of_7ints_nanoTooHigh() {

        LocalDateTime.of(2007, 7, 15, 12, 30, 40, 1000000000);
    }

    @Test
    public void factory_of_LocalDate_LocalTime() {

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(2007, 7, 15), LocalTime.of(12, 30, 40, 987654321));
        check(dateTime, 2007, 7, 15, 12, 30, 40, 987654321);
    }

    @Test(expected = NullPointerException.class)
    public void factory_of_LocalDate_LocalTime_nullLocalDate() {

        LocalDateTime.of(null, LocalTime.of(12, 30, 40, 987654321));
    }

    @Test(expected = NullPointerException.class)
    public void factory_of_LocalDate_LocalTime_nullLocalTime() {

        LocalDateTime.of(LocalDate.of(2007, 7, 15), null);
    }

    @Test
    public void factory_ofInstant_zone() {

        LocalDateTime test = LocalDateTime.ofInstant(Instant.ofEpochSecond(86400 + 3600 + 120 + 4, 500), ZONE_PARIS);
        assertEquals(test, LocalDateTime.of(1970, 1, 2, 2, 2, 4, 500)); // offset +01:00
    }

    @Test
    public void factory_ofInstant_offset() {

        LocalDateTime test = LocalDateTime.ofInstant(Instant.ofEpochSecond(86400 + 3600 + 120 + 4, 500), OFFSET_MTWO);
        assertEquals(test, LocalDateTime.of(1970, 1, 1, 23, 2, 4, 500));
    }

    @Test
    public void factory_ofInstant_offsetBeforeEpoch() {

        LocalDateTime test = LocalDateTime.ofInstant(Instant.ofEpochSecond(-86400 + 4, 500), OFFSET_PTWO);
        assertEquals(test, LocalDateTime.of(1969, 12, 31, 2, 0, 4, 500));
    }

    @Test(expected = DateTimeException.class)
    public void factory_ofInstant_instantTooBig() {

        LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.MAX_VALUE), OFFSET_PONE);
    }

    @Test(expected = DateTimeException.class)
    public void factory_ofInstant_instantTooSmall() {

        LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.MIN_VALUE), OFFSET_PONE);
    }

    @Test(expected = NullPointerException.class)
    public void factory_ofInstant_nullInstant() {

        LocalDateTime.ofInstant((Instant) null, ZONE_GAZA);
    }

    @Test(expected = NullPointerException.class)
    public void factory_ofInstant_nullZone() {

        LocalDateTime.ofInstant(Instant.EPOCH, (ZoneId) null);
    }

    @Test
    public void factory_ofEpochSecond_longOffset_afterEpoch() {

        LocalDateTime base = LocalDateTime.of(1970, 1, 1, 2, 0, 0, 500);
        for (int i = 0; i < 100000; i++) {
            LocalDateTime test = LocalDateTime.ofEpochSecond(i, 500, OFFSET_PTWO);
            assertEquals(test, base.plusSeconds(i));
        }
    }

    @Test
    public void factory_ofEpochSecond_longOffset_beforeEpoch() {

        LocalDateTime base = LocalDateTime.of(1970, 1, 1, 2, 0, 0, 500);
        for (int i = 0; i < 100000; i++) {
            LocalDateTime test = LocalDateTime.ofEpochSecond(-i, 500, OFFSET_PTWO);
            assertEquals(test, base.minusSeconds(i));
        }
    }

    @Test(expected = DateTimeException.class)
    public void factory_ofEpochSecond_longOffset_tooBig() {

        LocalDateTime.ofEpochSecond(Long.MAX_VALUE, 500, OFFSET_PONE); // TODO: better test
    }

    @Test(expected = DateTimeException.class)
    public void factory_ofEpochSecond_longOffset_tooSmall() {

        LocalDateTime.ofEpochSecond(Long.MIN_VALUE, 500, OFFSET_PONE); // TODO: better test
    }

    @Test(expected = DateTimeException.class)
    public void factory_ofEpochSecond_badNanos_toBig() {

        LocalDateTime.ofEpochSecond(0, 1000000000, OFFSET_PONE);
    }

    @Test(expected = DateTimeException.class)
    public void factory_ofEpochSecond_badNanos_toSmall() {

        LocalDateTime.ofEpochSecond(0, -1, OFFSET_PONE);
    }

    @Test(expected = NullPointerException.class)
    public void factory_ofEpochSecond_longOffset_nullOffset() {

        LocalDateTime.ofEpochSecond(0L, 500, null);
    }

    @Test
    public void test_from_Accessor() {

        LocalDateTime base = LocalDateTime.of(2007, 7, 15, 17, 30);
        assertEquals(LocalDateTime.from(base), base);
        assertEquals(LocalDateTime.from(ZonedDateTime.of(base, ZoneOffset.ofHours(2))), base);
    }

    @Test(expected = DateTimeException.class)
    public void test_from_Accessor_invalid_noDerive() {

        LocalDateTime.from(LocalTime.of(12, 30));
    }

    @Test(expected = NullPointerException.class)
    public void test_from_Accessor_null() {

        LocalDateTime.from((TemporalAccessor) null);
    }

    @Test
    public void test_parse() {

        for (Object[] data : provider_sampleToString()) {
            int y = (int) data[0];
            int month = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int m = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];
            String text = (String) data[7];

            LocalDateTime t = LocalDateTime.parse(text);
            assertEquals(t.getYear(), y);
            assertEquals(t.getMonth().getValue(), month);
            assertEquals(t.getDayOfMonth(), d);
            assertEquals(t.getHour(), h);
            assertEquals(t.getMinute(), m);
            assertEquals(t.getSecond(), s);
            assertEquals(t.getNano(), n);
        }
    }

    @Test(expected = DateTimeParseException.class)
    public void factory_parse_illegalValue() {

        LocalDateTime.parse("2008-06-32T11:15");
    }

    @Test(expected = DateTimeParseException.class)
    public void factory_parse_invalidValue() {

        LocalDateTime.parse("2008-06-31T11:15");
    }

    @Test(expected = NullPointerException.class)
    public void factory_parse_nullText() {

        LocalDateTime.parse((String) null);
    }

    @Test
    public void factory_parse_formatter() {

        DateTimeFormatter f = DateTimeFormatter.ofPattern("u M d H m s");
        LocalDateTime test = LocalDateTime.parse("2010 12 3 11 30 45", f);
        assertEquals(test, LocalDateTime.of(2010, 12, 3, 11, 30, 45));
    }

    @Test(expected = NullPointerException.class)
    public void factory_parse_formatter_nullText() {

        DateTimeFormatter f = DateTimeFormatter.ofPattern("u M d H m s");
        LocalDateTime.parse((String) null, f);
    }

    @Test(expected = NullPointerException.class)
    public void factory_parse_formatter_nullFormatter() {

        LocalDateTime.parse("ANY", null);
    }

    @Test
    public void test_get_DateTimeField() {

        LocalDateTime test = LocalDateTime.of(2008, 6, 30, 12, 30, 40, 987654321);
        assertEquals(test.getLong(ChronoField.YEAR), 2008);
        assertEquals(test.getLong(ChronoField.MONTH_OF_YEAR), 6);
        assertEquals(test.getLong(ChronoField.DAY_OF_MONTH), 30);
        assertEquals(test.getLong(ChronoField.DAY_OF_WEEK), 1);
        assertEquals(test.getLong(ChronoField.DAY_OF_YEAR), 182);

        assertEquals(test.getLong(ChronoField.HOUR_OF_DAY), 12);
        assertEquals(test.getLong(ChronoField.MINUTE_OF_HOUR), 30);
        assertEquals(test.getLong(ChronoField.SECOND_OF_MINUTE), 40);
        assertEquals(test.getLong(ChronoField.NANO_OF_SECOND), 987654321);
        assertEquals(test.getLong(ChronoField.HOUR_OF_AMPM), 0);
        assertEquals(test.getLong(ChronoField.AMPM_OF_DAY), 1);
    }

    @Test(expected = NullPointerException.class)
    public void test_get_DateTimeField_null() {

        LocalDateTime test = LocalDateTime.of(2008, 6, 30, 12, 30, 40, 987654321);
        test.getLong((TemporalField) null);
    }

    @Test(expected = DateTimeException.class)
    public void test_get_DateTimeField_invalidField() {

        this.TEST_2007_07_15_12_30_40_987654321.getLong(MockFieldNoValue.INSTANCE);
    }

    @Test
    public void test_query() {

        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.query(TemporalQueries.chronology()),
                IsoChronology.INSTANCE);
        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.query(TemporalQueries.localDate()),
                this.TEST_2007_07_15_12_30_40_987654321.toLocalDate());
        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.query(TemporalQueries.localTime()),
                this.TEST_2007_07_15_12_30_40_987654321.toLocalTime());
        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.query(TemporalQueries.offset()), null);
        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.query(TemporalQueries.precision()), ChronoUnit.NANOS);
        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.query(TemporalQueries.zone()), null);
        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.query(TemporalQueries.zoneId()), null);
    }

    @Test(expected = NullPointerException.class)
    public void test_query_null() {

        this.TEST_2007_07_15_12_30_40_987654321.query(null);
    }

    Object[][] provider_sampleDates() {

        return new Object[][] { { 2008, 7, 5 }, { 2007, 7, 5 }, { 2006, 7, 5 }, { 2005, 7, 5 }, { 2004, 1, 1 },
        { -1, 1, 2 }, };
    }

    @Test
    public void test_get_dates() {

        for (Object[] data : provider_sampleDates()) {
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];

            LocalDateTime a = LocalDateTime.of(y, m, d, 12, 30);
            assertEquals(a.getYear(), y);
            assertEquals(a.getMonth(), Month.of(m));
            assertEquals(a.getDayOfMonth(), d);
        }
    }

    @Test
    public void test_getDOY() {

        for (Object[] data : provider_sampleDates()) {
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];

            LocalDateTime a = LocalDateTime.of(y, m, d, 12, 30);
            int total = 0;
            for (int i = 1; i < m; i++) {
                total += Month.of(i).length(isIsoLeap(y));
            }
            int doy = total + d;
            assertEquals(a.getDayOfYear(), doy);
        }
    }

    Object[][] provider_sampleTimes() {

        return new Object[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 0, 1, 0 }, { 0, 0, 1, 1 }, { 0, 1, 0, 0 },
        { 0, 1, 0, 1 }, { 0, 1, 1, 0 }, { 0, 1, 1, 1 }, { 1, 0, 0, 0 }, { 1, 0, 0, 1 }, { 1, 0, 1, 0 }, { 1, 0, 1, 1 },
        { 1, 1, 0, 0 }, { 1, 1, 0, 1 }, { 1, 1, 1, 0 }, { 1, 1, 1, 1 }, };
    }

    @Test
    public void test_get_times() {

        for (Object[] data : provider_sampleTimes()) {
            int h = (int) data[0];
            int m = (int) data[1];
            int s = (int) data[2];
            int ns = (int) data[3];

            LocalDateTime a = LocalDateTime.of(this.TEST_2007_07_15_12_30_40_987654321.toLocalDate(),
                    LocalTime.of(h, m, s, ns));
            assertEquals(a.getHour(), h);
            assertEquals(a.getMinute(), m);
            assertEquals(a.getSecond(), s);
            assertEquals(a.getNano(), ns);
        }
    }

    @Test
    public void test_getDayOfWeek() {

        DayOfWeek dow = DayOfWeek.MONDAY;
        for (Month month : Month.values()) {
            int length = month.length(false);
            for (int i = 1; i <= length; i++) {
                LocalDateTime d = LocalDateTime.of(LocalDate.of(2007, month, i),
                        this.TEST_2007_07_15_12_30_40_987654321.toLocalTime());
                assertSame(d.getDayOfWeek(), dow);
                dow = dow.plus(1);
            }
        }
    }

    @Test
    public void test_with_adjustment() {

        final LocalDateTime sample = LocalDateTime.of(2012, 3, 4, 23, 5);
        TemporalAdjuster adjuster = new TemporalAdjuster() {
            @Override
            public Temporal adjustInto(Temporal dateTime) {

                return sample;
            }
        };
        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.with(adjuster), sample);
    }

    @Test(expected = NullPointerException.class)
    public void test_with_adjustment_null() {

        this.TEST_2007_07_15_12_30_40_987654321.with((TemporalAdjuster) null);
    }

    @Test
    public void test_withYear_int_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.withYear(2008);
        check(t, 2008, 7, 15, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void test_withYear_int_invalid() {

        this.TEST_2007_07_15_12_30_40_987654321.withYear(Year.MIN_VALUE - 1);
    }

    @Test
    public void test_withYear_int_adjustDay() {

        LocalDateTime t = LocalDateTime.of(2008, 2, 29, 12, 30).withYear(2007);
        LocalDateTime expected = LocalDateTime.of(2007, 2, 28, 12, 30);
        assertEquals(t, expected);
    }

    @Test
    public void test_withMonth_int_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.withMonth(1);
        check(t, 2007, 1, 15, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void test_withMonth_int_invalid() {

        this.TEST_2007_07_15_12_30_40_987654321.withMonth(13);
    }

    @Test
    public void test_withMonth_int_adjustDay() {

        LocalDateTime t = LocalDateTime.of(2007, 12, 31, 12, 30).withMonth(11);
        LocalDateTime expected = LocalDateTime.of(2007, 11, 30, 12, 30);
        assertEquals(t, expected);
    }

    @Test
    public void test_withDayOfMonth_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.withDayOfMonth(1);
        check(t, 2007, 7, 1, 12, 30, 40, 987654321);
    }

    @Test(expected = DateTimeException.class)
    public void test_withDayOfMonth_invalid() {

        LocalDateTime.of(2007, 11, 30, 12, 30).withDayOfMonth(32);
    }

    @Test(expected = DateTimeException.class)
    public void test_withDayOfMonth_invalidCombination() {

        LocalDateTime.of(2007, 11, 30, 12, 30).withDayOfMonth(31);
    }

    @Test
    public void test_withDayOfYear_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.withDayOfYear(33);
        assertEquals(t, LocalDateTime.of(2007, 2, 2, 12, 30, 40, 987654321));
    }

    @Test(expected = DateTimeException.class)
    public void test_withDayOfYear_illegal() {

        this.TEST_2007_07_15_12_30_40_987654321.withDayOfYear(367);
    }

    @Test(expected = DateTimeException.class)
    public void test_withDayOfYear_invalid() {

        this.TEST_2007_07_15_12_30_40_987654321.withDayOfYear(366);
    }

    @Test
    public void test_withHour_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321;
        for (int i = 0; i < 24; i++) {
            t = t.withHour(i);
            assertEquals(t.getHour(), i);
        }
    }

    @Test(expected = DateTimeException.class)
    public void test_withHour_hourTooLow() {

        this.TEST_2007_07_15_12_30_40_987654321.withHour(-1);
    }

    @Test(expected = DateTimeException.class)
    public void test_withHour_hourTooHigh() {

        this.TEST_2007_07_15_12_30_40_987654321.withHour(24);
    }

    @Test
    public void test_withMinute_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321;
        for (int i = 0; i < 60; i++) {
            t = t.withMinute(i);
            assertEquals(t.getMinute(), i);
        }
    }

    @Test(expected = DateTimeException.class)
    public void test_withMinute_minuteTooLow() {

        this.TEST_2007_07_15_12_30_40_987654321.withMinute(-1);
    }

    @Test(expected = DateTimeException.class)
    public void test_withMinute_minuteTooHigh() {

        this.TEST_2007_07_15_12_30_40_987654321.withMinute(60);
    }

    @Test
    public void test_withSecond_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321;
        for (int i = 0; i < 60; i++) {
            t = t.withSecond(i);
            assertEquals(t.getSecond(), i);
        }
    }

    @Test(expected = DateTimeException.class)
    public void test_withSecond_secondTooLow() {

        this.TEST_2007_07_15_12_30_40_987654321.withSecond(-1);
    }

    @Test(expected = DateTimeException.class)
    public void test_withSecond_secondTooHigh() {

        this.TEST_2007_07_15_12_30_40_987654321.withSecond(60);
    }

    @Test
    public void test_withNanoOfSecond_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321;
        t = t.withNano(1);
        assertEquals(t.getNano(), 1);
        t = t.withNano(10);
        assertEquals(t.getNano(), 10);
        t = t.withNano(100);
        assertEquals(t.getNano(), 100);
        t = t.withNano(999999999);
        assertEquals(t.getNano(), 999999999);
    }

    @Test(expected = DateTimeException.class)
    public void test_withNanoOfSecond_nanoTooLow() {

        this.TEST_2007_07_15_12_30_40_987654321.withNano(-1);
    }

    @Test(expected = DateTimeException.class)
    public void test_withNanoOfSecond_nanoTooHigh() {

        this.TEST_2007_07_15_12_30_40_987654321.withNano(1000000000);
    }

    @Test
    public void test_plus_adjuster() {

        Duration p = Duration.ofSeconds(62, 3);
        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plus(p);
        assertEquals(t, LocalDateTime.of(2007, 7, 15, 12, 31, 42, 987654324));
    }

    @Test(expected = NullPointerException.class)
    public void test_plus_adjuster_null() {

        this.TEST_2007_07_15_12_30_40_987654321.plus(null);
    }

    @Test
    public void test_plus_Period_positiveMonths() {

        MockSimplePeriod period = MockSimplePeriod.of(7, ChronoUnit.MONTHS);
        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plus(period);
        assertEquals(t, LocalDateTime.of(2008, 2, 15, 12, 30, 40, 987654321));
    }

    @Test
    public void test_plus_Period_negativeDays() {

        MockSimplePeriod period = MockSimplePeriod.of(-25, ChronoUnit.DAYS);
        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plus(period);
        assertEquals(t, LocalDateTime.of(2007, 6, 20, 12, 30, 40, 987654321));
    }

    @Test(expected = NullPointerException.class)
    public void test_plus_Period_null() {

        this.TEST_2007_07_15_12_30_40_987654321.plus((MockSimplePeriod) null);
    }

    @Test(expected = DateTimeException.class)
    public void test_plus_Period_invalidTooLarge() {

        MockSimplePeriod period = MockSimplePeriod.of(1, ChronoUnit.YEARS);
        LocalDateTime.of(Year.MAX_VALUE, 1, 1, 0, 0).plus(period);
    }

    @Test(expected = DateTimeException.class)
    public void test_plus_Period_invalidTooSmall() {

        MockSimplePeriod period = MockSimplePeriod.of(-1, ChronoUnit.YEARS);
        LocalDateTime.of(Year.MIN_VALUE, 1, 1, 0, 0).plus(period);
    }

    @Test
    public void test_plus_longPeriodUnit_positiveMonths() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plus(7, ChronoUnit.MONTHS);
        assertEquals(t, LocalDateTime.of(2008, 2, 15, 12, 30, 40, 987654321));
    }

    @Test
    public void test_plus_longPeriodUnit_negativeDays() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plus(-25, ChronoUnit.DAYS);
        assertEquals(t, LocalDateTime.of(2007, 6, 20, 12, 30, 40, 987654321));
    }

    @Test(expected = NullPointerException.class)
    public void test_plus_longPeriodUnit_null() {

        this.TEST_2007_07_15_12_30_40_987654321.plus(1, (TemporalUnit) null);
    }

    @Test(expected = DateTimeException.class)
    public void test_plus_longPeriodUnit_invalidTooLarge() {

        LocalDateTime.of(Year.MAX_VALUE, 1, 1, 0, 0).plus(1, ChronoUnit.YEARS);
    }

    @Test(expected = DateTimeException.class)
    public void test_plus_longPeriodUnit_invalidTooSmall() {

        LocalDateTime.of(Year.MIN_VALUE, 1, 1, 0, 0).plus(-1, ChronoUnit.YEARS);
    }

    @Test
    public void test_plusYears_int_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusYears(1);
        check(t, 2008, 7, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusYears_int_negative() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusYears(-1);
        check(t, 2006, 7, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusYears_int_adjustDay() {

        LocalDateTime t = createDateMidnight(2008, 2, 29).plusYears(1);
        check(t, 2009, 2, 28, 0, 0, 0, 0);
    }

    @Test(expected = DateTimeException.class)
    public void test_plusYears_int_invalidTooLarge() {

        createDateMidnight(Year.MAX_VALUE, 1, 1).plusYears(1);
    }

    @Test(expected = DateTimeException.class)
    public void test_plusYears_int_invalidTooSmall() {

        LocalDate.of(Year.MIN_VALUE, 1, 1).plusYears(-1);
    }

    @Test
    public void test_plusMonths_int_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusMonths(1);
        check(t, 2007, 8, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusMonths_int_overYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusMonths(25);
        check(t, 2009, 8, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusMonths_int_negative() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusMonths(-1);
        check(t, 2007, 6, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusMonths_int_negativeAcrossYear() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusMonths(-7);
        check(t, 2006, 12, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusMonths_int_negativeOverYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusMonths(-31);
        check(t, 2004, 12, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusMonths_int_adjustDayFromLeapYear() {

        LocalDateTime t = createDateMidnight(2008, 2, 29).plusMonths(12);
        check(t, 2009, 2, 28, 0, 0, 0, 0);
    }

    @Test
    public void test_plusMonths_int_adjustDayFromMonthLength() {

        LocalDateTime t = createDateMidnight(2007, 3, 31).plusMonths(1);
        check(t, 2007, 4, 30, 0, 0, 0, 0);
    }

    @Test(expected = DateTimeException.class)
    public void test_plusMonths_int_invalidTooLarge() {

        createDateMidnight(Year.MAX_VALUE, 12, 1).plusMonths(1);
    }

    @Test(expected = DateTimeException.class)
    public void test_plusMonths_int_invalidTooSmall() {

        createDateMidnight(Year.MIN_VALUE, 1, 1).plusMonths(-1);
    }

    Object[][] provider_samplePlusWeeksSymmetry() {

        return new Object[][] { { createDateMidnight(-1, 1, 1) }, { createDateMidnight(-1, 2, 28) },
        { createDateMidnight(-1, 3, 1) }, { createDateMidnight(-1, 12, 31) }, { createDateMidnight(0, 1, 1) },
        { createDateMidnight(0, 2, 28) }, { createDateMidnight(0, 2, 29) }, { createDateMidnight(0, 3, 1) },
        { createDateMidnight(0, 12, 31) }, { createDateMidnight(2007, 1, 1) }, { createDateMidnight(2007, 2, 28) },
        { createDateMidnight(2007, 3, 1) }, { createDateMidnight(2007, 12, 31) }, { createDateMidnight(2008, 1, 1) },
        { createDateMidnight(2008, 2, 28) }, { createDateMidnight(2008, 2, 29) }, { createDateMidnight(2008, 3, 1) },
        { createDateMidnight(2008, 12, 31) }, { createDateMidnight(2099, 1, 1) }, { createDateMidnight(2099, 2, 28) },
        { createDateMidnight(2099, 3, 1) }, { createDateMidnight(2099, 12, 31) }, { createDateMidnight(2100, 1, 1) },
        { createDateMidnight(2100, 2, 28) }, { createDateMidnight(2100, 3, 1) },
        { createDateMidnight(2100, 12, 31) }, };
    }

    @Test
    public void test_plusWeeks_symmetry() {

        for (Object[] data : provider_samplePlusWeeksSymmetry()) {
            LocalDateTime reference = (LocalDateTime) data[0];

            for (int weeks = 0; weeks < 365 * 8; weeks++) {
                LocalDateTime t = reference.plusWeeks(weeks).plusWeeks(-weeks);
                assertEquals(t, reference);

                t = reference.plusWeeks(-weeks).plusWeeks(weeks);
                assertEquals(t, reference);
            }
        }
    }

    @Test
    public void test_plusWeeks_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusWeeks(1);
        check(t, 2007, 7, 22, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusWeeks_overMonths() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusWeeks(9);
        check(t, 2007, 9, 16, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusWeeks_overYears() {

        LocalDateTime t = LocalDateTime.of(2006, 7, 16, 12, 30, 40, 987654321).plusWeeks(52);
        assertEquals(t, this.TEST_2007_07_15_12_30_40_987654321);
    }

    @Test
    public void test_plusWeeks_overLeapYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusYears(-1).plusWeeks(104);
        check(t, 2008, 7, 12, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusWeeks_negative() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusWeeks(-1);
        check(t, 2007, 7, 8, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusWeeks_negativeAcrossYear() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusWeeks(-28);
        check(t, 2006, 12, 31, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusWeeks_negativeOverYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusWeeks(-104);
        check(t, 2005, 7, 17, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusWeeks_maximum() {

        LocalDateTime t = createDateMidnight(Year.MAX_VALUE, 12, 24).plusWeeks(1);
        check(t, Year.MAX_VALUE, 12, 31, 0, 0, 0, 0);
    }

    @Test
    public void test_plusWeeks_minimum() {

        LocalDateTime t = createDateMidnight(Year.MIN_VALUE, 1, 8).plusWeeks(-1);
        check(t, Year.MIN_VALUE, 1, 1, 0, 0, 0, 0);
    }

    @Test(expected = DateTimeException.class)
    public void test_plusWeeks_invalidTooLarge() {

        createDateMidnight(Year.MAX_VALUE, 12, 25).plusWeeks(1);
    }

    @Test(expected = DateTimeException.class)
    public void test_plusWeeks_invalidTooSmall() {

        createDateMidnight(Year.MIN_VALUE, 1, 7).plusWeeks(-1);
    }

    Object[][] provider_samplePlusDaysSymmetry() {

        return new Object[][] { { createDateMidnight(-1, 1, 1) }, { createDateMidnight(-1, 2, 28) },
        { createDateMidnight(-1, 3, 1) }, { createDateMidnight(-1, 12, 31) }, { createDateMidnight(0, 1, 1) },
        { createDateMidnight(0, 2, 28) }, { createDateMidnight(0, 2, 29) }, { createDateMidnight(0, 3, 1) },
        { createDateMidnight(0, 12, 31) }, { createDateMidnight(2007, 1, 1) }, { createDateMidnight(2007, 2, 28) },
        { createDateMidnight(2007, 3, 1) }, { createDateMidnight(2007, 12, 31) }, { createDateMidnight(2008, 1, 1) },
        { createDateMidnight(2008, 2, 28) }, { createDateMidnight(2008, 2, 29) }, { createDateMidnight(2008, 3, 1) },
        { createDateMidnight(2008, 12, 31) }, { createDateMidnight(2099, 1, 1) }, { createDateMidnight(2099, 2, 28) },
        { createDateMidnight(2099, 3, 1) }, { createDateMidnight(2099, 12, 31) }, { createDateMidnight(2100, 1, 1) },
        { createDateMidnight(2100, 2, 28) }, { createDateMidnight(2100, 3, 1) },
        { createDateMidnight(2100, 12, 31) }, };
    }

    @Test
    public void test_plusDays_symmetry() {

        for (Object[] data : provider_samplePlusDaysSymmetry()) {
            LocalDateTime reference = (LocalDateTime) data[0];

            for (int days = 0; days < 365 * 8; days++) {
                LocalDateTime t = reference.plusDays(days).plusDays(-days);
                assertEquals(t, reference);

                t = reference.plusDays(-days).plusDays(days);
                assertEquals(t, reference);
            }
        }
    }

    @Test
    public void test_plusDays_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusDays(1);
        check(t, 2007, 7, 16, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusDays_overMonths() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusDays(62);
        check(t, 2007, 9, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusDays_overYears() {

        LocalDateTime t = LocalDateTime.of(2006, 7, 14, 12, 30, 40, 987654321).plusDays(366);
        assertEquals(t, this.TEST_2007_07_15_12_30_40_987654321);
    }

    @Test
    public void test_plusDays_overLeapYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusYears(-1).plusDays(365 + 366);
        check(t, 2008, 7, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusDays_negative() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusDays(-1);
        check(t, 2007, 7, 14, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusDays_negativeAcrossYear() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusDays(-196);
        check(t, 2006, 12, 31, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusDays_negativeOverYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusDays(-730);
        check(t, 2005, 7, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_plusDays_maximum() {

        LocalDateTime t = createDateMidnight(Year.MAX_VALUE, 12, 30).plusDays(1);
        check(t, Year.MAX_VALUE, 12, 31, 0, 0, 0, 0);
    }

    @Test
    public void test_plusDays_minimum() {

        LocalDateTime t = createDateMidnight(Year.MIN_VALUE, 1, 2).plusDays(-1);
        check(t, Year.MIN_VALUE, 1, 1, 0, 0, 0, 0);
    }

    @Test(expected = DateTimeException.class)
    public void test_plusDays_invalidTooLarge() {

        createDateMidnight(Year.MAX_VALUE, 12, 31).plusDays(1);
    }

    @Test(expected = DateTimeException.class)
    public void test_plusDays_invalidTooSmall() {

        createDateMidnight(Year.MIN_VALUE, 1, 1).plusDays(-1);
    }

    @Test(expected = ArithmeticException.class)
    public void test_plusDays_overflowTooLarge() {

        createDateMidnight(Year.MAX_VALUE, 12, 31).plusDays(Long.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void test_plusDays_overflowTooSmall() {

        createDateMidnight(Year.MIN_VALUE, 1, 1).plusDays(Long.MIN_VALUE);
    }

    @Test
    public void test_plusHours_one() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = t.toLocalDate();

        for (int i = 0; i < 50; i++) {
            t = t.plusHours(1);

            if ((i + 1) % 24 == 0) {
                d = d.plusDays(1);
            }

            assertEquals(t.toLocalDate(), d);
            assertEquals(t.getHour(), (i + 1) % 24);
        }
    }

    @Test
    public void test_plusHours_fromZero() {

        LocalDateTime base = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = base.toLocalDate().minusDays(3);
        LocalTime t = LocalTime.of(21, 0);

        for (int i = -50; i < 50; i++) {
            LocalDateTime dt = base.plusHours(i);
            t = t.plusHours(1);

            if (t.getHour() == 0) {
                d = d.plusDays(1);
            }

            assertEquals(dt.toLocalDate(), d);
            assertEquals(dt.toLocalTime(), t);
        }
    }

    @Test
    public void test_plusHours_fromOne() {

        LocalDateTime base = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.of(1, 0));
        LocalDate d = base.toLocalDate().minusDays(3);
        LocalTime t = LocalTime.of(22, 0);

        for (int i = -50; i < 50; i++) {
            LocalDateTime dt = base.plusHours(i);

            t = t.plusHours(1);

            if (t.getHour() == 0) {
                d = d.plusDays(1);
            }

            assertEquals(dt.toLocalDate(), d);
            assertEquals(dt.toLocalTime(), t);
        }
    }

    @Test
    public void test_plusMinutes_one() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = t.toLocalDate();

        int hour = 0;
        int min = 0;

        for (int i = 0; i < 70; i++) {
            t = t.plusMinutes(1);
            min++;
            if (min == 60) {
                hour++;
                min = 0;
            }

            assertEquals(t.toLocalDate(), d);
            assertEquals(t.getHour(), hour);
            assertEquals(t.getMinute(), min);
        }
    }

    @Test
    public void test_plusMinutes_fromZero() {

        LocalDateTime base = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = base.toLocalDate().minusDays(1);
        LocalTime t = LocalTime.of(22, 49);

        for (int i = -70; i < 70; i++) {
            LocalDateTime dt = base.plusMinutes(i);
            t = t.plusMinutes(1);

            if (t == LocalTime.MIDNIGHT) {
                d = d.plusDays(1);
            }

            assertEquals(String.valueOf(i), dt.toLocalDate(), d);
            assertEquals(String.valueOf(i), dt.toLocalTime(), t);
        }
    }

    @Test
    public void test_plusMinutes_noChange_oneDay() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusMinutes(24 * 60);
        assertEquals(t.toLocalDate(), this.TEST_2007_07_15_12_30_40_987654321.toLocalDate().plusDays(1));
    }

    @Test
    public void test_plusSeconds_one() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = t.toLocalDate();

        int hour = 0;
        int min = 0;
        int sec = 0;

        for (int i = 0; i < 3700; i++) {
            t = t.plusSeconds(1);
            sec++;
            if (sec == 60) {
                min++;
                sec = 0;
            }
            if (min == 60) {
                hour++;
                min = 0;
            }

            assertEquals(t.toLocalDate(), d);
            assertEquals(t.getHour(), hour);
            assertEquals(t.getMinute(), min);
            assertEquals(t.getSecond(), sec);
        }
    }

    Iterator<Object[]> plusSeconds_fromZero() {

        return new Iterator<Object[]>() {
            int delta = 30;

            int i = -3660;

            LocalDate date = TestLocalDateTime.this.TEST_2007_07_15_12_30_40_987654321.toLocalDate().minusDays(1);

            int hour = 22;

            int min = 59;

            int sec = 0;

            @Override
            public boolean hasNext() {

                return this.i <= 3660;
            }

            @Override
            public Object[] next() {

                final Object[] ret = new Object[] { this.i, this.date, this.hour, this.min, this.sec };
                this.i += this.delta;
                this.sec += this.delta;

                if (this.sec >= 60) {
                    this.min++;
                    this.sec -= 60;

                    if (this.min == 60) {
                        this.hour++;
                        this.min = 0;

                        if (this.hour == 24) {
                            this.hour = 0;
                        }
                    }
                }

                if (this.i == 0) {
                    this.date = this.date.plusDays(1);
                }

                return ret;
            }

            @Override
            public void remove() {

                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void test_plusSeconds_fromZero() {

        Iterator<Object[]> plusSeconds_fromZero = plusSeconds_fromZero();
        while (plusSeconds_fromZero.hasNext()) {
            Object[] data = plusSeconds_fromZero.next();
            int seconds = (int) data[0];
            LocalDate date = (LocalDate) data[1];
            int hour = (int) data[2];
            int min = (int) data[3];
            int sec = (int) data[4];

            LocalDateTime base = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
            LocalDateTime t = base.plusSeconds(seconds);

            assertEquals(date, t.toLocalDate());
            assertEquals(hour, t.getHour());
            assertEquals(min, t.getMinute());
            assertEquals(sec, t.getSecond());
        }
    }

    @Test
    public void test_plusSeconds_noChange_oneDay() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusSeconds(24 * 60 * 60);
        assertEquals(t.toLocalDate(), this.TEST_2007_07_15_12_30_40_987654321.toLocalDate().plusDays(1));
    }

    @Test
    public void test_plusNanos_halfABillion() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = t.toLocalDate();

        int hour = 0;
        int min = 0;
        int sec = 0;
        int nanos = 0;

        for (long i = 0; i < 3700 * 1000000000L; i += 500000000) {
            t = t.plusNanos(500000000);
            nanos += 500000000;
            if (nanos == 1000000000) {
                sec++;
                nanos = 0;
            }
            if (sec == 60) {
                min++;
                sec = 0;
            }
            if (min == 60) {
                hour++;
                min = 0;
            }

            assertEquals(String.valueOf(i), t.toLocalDate(), d);
            assertEquals(t.getHour(), hour);
            assertEquals(t.getMinute(), min);
            assertEquals(t.getSecond(), sec);
            assertEquals(t.getNano(), nanos);
        }
    }

    Iterator<Object[]> plusNanos_fromZero() {

        return new Iterator<Object[]>() {
            long delta = 7500000000L;

            long i = -3660 * 1000000000L;

            LocalDate date = TestLocalDateTime.this.TEST_2007_07_15_12_30_40_987654321.toLocalDate().minusDays(1);

            int hour = 22;

            int min = 59;

            int sec = 0;

            long nanos = 0;

            @Override
            public boolean hasNext() {

                return this.i <= 3660 * 1000000000L;
            }

            @Override
            public Object[] next() {

                final Object[] ret = new Object[] { this.i, this.date, this.hour, this.min, this.sec,
                (int) this.nanos };
                this.i += this.delta;
                this.nanos += this.delta;

                if (this.nanos >= 1000000000L) {
                    this.sec += this.nanos / 1000000000L;
                    this.nanos %= 1000000000L;

                    if (this.sec >= 60) {
                        this.min++;
                        this.sec %= 60;

                        if (this.min == 60) {
                            this.hour++;
                            this.min = 0;

                            if (this.hour == 24) {
                                this.hour = 0;
                                this.date = this.date.plusDays(1);
                            }
                        }
                    }
                }

                return ret;
            }

            @Override
            public void remove() {

                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void test_plusNanos_fromZero() {

        Iterator<Object[]> plusNanos_fromZero = plusNanos_fromZero();
        while (plusNanos_fromZero.hasNext()) {
            Object[] data = plusNanos_fromZero.next();
            long nanoseconds = ((Number) data[0]).longValue();
            LocalDate date = (LocalDate) data[1];
            int hour = (int) data[2];
            int min = (int) data[3];
            int sec = (int) data[4];
            int nanos = (int) data[5];

            LocalDateTime base = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
            LocalDateTime t = base.plusNanos(nanoseconds);

            assertEquals(date, t.toLocalDate());
            assertEquals(hour, t.getHour());
            assertEquals(min, t.getMinute());
            assertEquals(sec, t.getSecond());
            assertEquals(nanos, t.getNano());
        }
    }

    @Test
    public void test_plusNanos_noChange_oneDay() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusNanos(24 * 60 * 60 * 1000000000L);
        assertEquals(t.toLocalDate(), this.TEST_2007_07_15_12_30_40_987654321.toLocalDate().plusDays(1));
    }

    @Test
    public void test_minus_adjuster() {

        Duration p = Duration.ofSeconds(62, 3);
        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minus(p);
        assertEquals(t, LocalDateTime.of(2007, 7, 15, 12, 29, 38, 987654318));
    }

    @Test(expected = NullPointerException.class)
    public void test_minus_adjuster_null() {

        this.TEST_2007_07_15_12_30_40_987654321.minus(null);
    }

    @Test
    public void test_minus_Period_positiveMonths() {

        MockSimplePeriod period = MockSimplePeriod.of(7, ChronoUnit.MONTHS);
        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minus(period);
        assertEquals(t, LocalDateTime.of(2006, 12, 15, 12, 30, 40, 987654321));
    }

    @Test
    public void test_minus_Period_negativeDays() {

        MockSimplePeriod period = MockSimplePeriod.of(-25, ChronoUnit.DAYS);
        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minus(period);
        assertEquals(t, LocalDateTime.of(2007, 8, 9, 12, 30, 40, 987654321));
    }

    @Test(expected = NullPointerException.class)
    public void test_minus_Period_null() {

        this.TEST_2007_07_15_12_30_40_987654321.minus((MockSimplePeriod) null);
    }

    @Test(expected = DateTimeException.class)
    public void test_minus_Period_invalidTooLarge() {

        MockSimplePeriod period = MockSimplePeriod.of(-1, ChronoUnit.YEARS);
        LocalDateTime.of(Year.MAX_VALUE, 1, 1, 0, 0).minus(period);
    }

    @Test(expected = DateTimeException.class)
    public void test_minus_Period_invalidTooSmall() {

        MockSimplePeriod period = MockSimplePeriod.of(1, ChronoUnit.YEARS);
        LocalDateTime.of(Year.MIN_VALUE, 1, 1, 0, 0).minus(period);
    }

    @Test
    public void test_minus_longPeriodUnit_positiveMonths() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minus(7, ChronoUnit.MONTHS);
        assertEquals(t, LocalDateTime.of(2006, 12, 15, 12, 30, 40, 987654321));
    }

    @Test
    public void test_minus_longPeriodUnit_negativeDays() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minus(-25, ChronoUnit.DAYS);
        assertEquals(t, LocalDateTime.of(2007, 8, 9, 12, 30, 40, 987654321));
    }

    @Test(expected = NullPointerException.class)
    public void test_minus_longPeriodUnit_null() {

        this.TEST_2007_07_15_12_30_40_987654321.minus(1, (TemporalUnit) null);
    }

    @Test(expected = DateTimeException.class)
    public void test_minus_longPeriodUnit_invalidTooLarge() {

        LocalDateTime.of(Year.MAX_VALUE, 1, 1, 0, 0).minus(-1, ChronoUnit.YEARS);
    }

    @Test(expected = DateTimeException.class)
    public void test_minus_longPeriodUnit_invalidTooSmall() {

        LocalDateTime.of(Year.MIN_VALUE, 1, 1, 0, 0).minus(1, ChronoUnit.YEARS);
    }

    @Test
    public void test_minusYears_int_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusYears(1);
        check(t, 2006, 7, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusYears_int_negative() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusYears(-1);
        check(t, 2008, 7, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusYears_int_adjustDay() {

        LocalDateTime t = createDateMidnight(2008, 2, 29).minusYears(1);
        check(t, 2007, 2, 28, 0, 0, 0, 0);
    }

    @Test(expected = DateTimeException.class)
    public void test_minusYears_int_invalidTooLarge() {

        createDateMidnight(Year.MAX_VALUE, 1, 1).minusYears(-1);
    }

    @Test(expected = DateTimeException.class)
    public void test_minusYears_int_invalidTooSmall() {

        createDateMidnight(Year.MIN_VALUE, 1, 1).minusYears(1);
    }

    @Test
    public void test_minusMonths_int_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusMonths(1);
        check(t, 2007, 6, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusMonths_int_overYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusMonths(25);
        check(t, 2005, 6, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusMonths_int_negative() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusMonths(-1);
        check(t, 2007, 8, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusMonths_int_negativeAcrossYear() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusMonths(-7);
        check(t, 2008, 2, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusMonths_int_negativeOverYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusMonths(-31);
        check(t, 2010, 2, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusMonths_int_adjustDayFromLeapYear() {

        LocalDateTime t = createDateMidnight(2008, 2, 29).minusMonths(12);
        check(t, 2007, 2, 28, 0, 0, 0, 0);
    }

    @Test
    public void test_minusMonths_int_adjustDayFromMonthLength() {

        LocalDateTime t = createDateMidnight(2007, 3, 31).minusMonths(1);
        check(t, 2007, 2, 28, 0, 0, 0, 0);
    }

    @Test(expected = DateTimeException.class)
    public void test_minusMonths_int_invalidTooLarge() {

        createDateMidnight(Year.MAX_VALUE, 12, 1).minusMonths(-1);
    }

    @Test(expected = DateTimeException.class)
    public void test_minusMonths_int_invalidTooSmall() {

        createDateMidnight(Year.MIN_VALUE, 1, 1).minusMonths(1);
    }

    Object[][] provider_sampleMinusWeeksSymmetry() {

        return new Object[][] { { createDateMidnight(-1, 1, 1) }, { createDateMidnight(-1, 2, 28) },
        { createDateMidnight(-1, 3, 1) }, { createDateMidnight(-1, 12, 31) }, { createDateMidnight(0, 1, 1) },
        { createDateMidnight(0, 2, 28) }, { createDateMidnight(0, 2, 29) }, { createDateMidnight(0, 3, 1) },
        { createDateMidnight(0, 12, 31) }, { createDateMidnight(2007, 1, 1) }, { createDateMidnight(2007, 2, 28) },
        { createDateMidnight(2007, 3, 1) }, { createDateMidnight(2007, 12, 31) }, { createDateMidnight(2008, 1, 1) },
        { createDateMidnight(2008, 2, 28) }, { createDateMidnight(2008, 2, 29) }, { createDateMidnight(2008, 3, 1) },
        { createDateMidnight(2008, 12, 31) }, { createDateMidnight(2099, 1, 1) }, { createDateMidnight(2099, 2, 28) },
        { createDateMidnight(2099, 3, 1) }, { createDateMidnight(2099, 12, 31) }, { createDateMidnight(2100, 1, 1) },
        { createDateMidnight(2100, 2, 28) }, { createDateMidnight(2100, 3, 1) },
        { createDateMidnight(2100, 12, 31) }, };
    }

    @Test
    public void test_minusWeeks_symmetry() {

        for (Object[] data : provider_sampleMinusWeeksSymmetry()) {
            LocalDateTime reference = (LocalDateTime) data[0];

            for (int weeks = 0; weeks < 365 * 8; weeks++) {
                LocalDateTime t = reference.minusWeeks(weeks).minusWeeks(-weeks);
                assertEquals(t, reference);

                t = reference.minusWeeks(-weeks).minusWeeks(weeks);
                assertEquals(t, reference);
            }
        }
    }

    @Test
    public void test_minusWeeks_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusWeeks(1);
        check(t, 2007, 7, 8, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusWeeks_overMonths() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusWeeks(9);
        check(t, 2007, 5, 13, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusWeeks_overYears() {

        LocalDateTime t = LocalDateTime.of(2008, 7, 13, 12, 30, 40, 987654321).minusWeeks(52);
        assertEquals(t, this.TEST_2007_07_15_12_30_40_987654321);
    }

    @Test
    public void test_minusWeeks_overLeapYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusYears(-1).minusWeeks(104);
        check(t, 2006, 7, 18, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusWeeks_negative() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusWeeks(-1);
        check(t, 2007, 7, 22, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusWeeks_negativeAcrossYear() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusWeeks(-28);
        check(t, 2008, 1, 27, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusWeeks_negativeOverYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusWeeks(-104);
        check(t, 2009, 7, 12, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusWeeks_maximum() {

        LocalDateTime t = createDateMidnight(Year.MAX_VALUE, 12, 24).minusWeeks(-1);
        check(t, Year.MAX_VALUE, 12, 31, 0, 0, 0, 0);
    }

    @Test
    public void test_minusWeeks_minimum() {

        LocalDateTime t = createDateMidnight(Year.MIN_VALUE, 1, 8).minusWeeks(1);
        check(t, Year.MIN_VALUE, 1, 1, 0, 0, 0, 0);
    }

    @Test(expected = DateTimeException.class)
    public void test_minusWeeks_invalidTooLarge() {

        createDateMidnight(Year.MAX_VALUE, 12, 25).minusWeeks(-1);
    }

    @Test(expected = DateTimeException.class)
    public void test_minusWeeks_invalidTooSmall() {

        createDateMidnight(Year.MIN_VALUE, 1, 7).minusWeeks(1);
    }

    Object[][] provider_sampleMinusDaysSymmetry() {

        return new Object[][] { { createDateMidnight(-1, 1, 1) }, { createDateMidnight(-1, 2, 28) },
        { createDateMidnight(-1, 3, 1) }, { createDateMidnight(-1, 12, 31) }, { createDateMidnight(0, 1, 1) },
        { createDateMidnight(0, 2, 28) }, { createDateMidnight(0, 2, 29) }, { createDateMidnight(0, 3, 1) },
        { createDateMidnight(0, 12, 31) }, { createDateMidnight(2007, 1, 1) }, { createDateMidnight(2007, 2, 28) },
        { createDateMidnight(2007, 3, 1) }, { createDateMidnight(2007, 12, 31) }, { createDateMidnight(2008, 1, 1) },
        { createDateMidnight(2008, 2, 28) }, { createDateMidnight(2008, 2, 29) }, { createDateMidnight(2008, 3, 1) },
        { createDateMidnight(2008, 12, 31) }, { createDateMidnight(2099, 1, 1) }, { createDateMidnight(2099, 2, 28) },
        { createDateMidnight(2099, 3, 1) }, { createDateMidnight(2099, 12, 31) }, { createDateMidnight(2100, 1, 1) },
        { createDateMidnight(2100, 2, 28) }, { createDateMidnight(2100, 3, 1) },
        { createDateMidnight(2100, 12, 31) }, };
    }

    @Test
    public void test_minusDays_symmetry() {

        for (Object[] data : provider_sampleMinusDaysSymmetry()) {
            LocalDateTime reference = (LocalDateTime) data[0];

            for (int days = 0; days < 365 * 8; days++) {
                LocalDateTime t = reference.minusDays(days).minusDays(-days);
                assertEquals(t, reference);

                t = reference.minusDays(-days).minusDays(days);
                assertEquals(t, reference);
            }
        }
    }

    @Test
    public void test_minusDays_normal() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusDays(1);
        check(t, 2007, 7, 14, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusDays_overMonths() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusDays(62);
        check(t, 2007, 5, 14, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusDays_overYears() {

        LocalDateTime t = LocalDateTime.of(2008, 7, 16, 12, 30, 40, 987654321).minusDays(367);
        assertEquals(t, this.TEST_2007_07_15_12_30_40_987654321);
    }

    @Test
    public void test_minusDays_overLeapYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.plusYears(2).minusDays(365 + 366);
        assertEquals(t, this.TEST_2007_07_15_12_30_40_987654321);
    }

    @Test
    public void test_minusDays_negative() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusDays(-1);
        check(t, 2007, 7, 16, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusDays_negativeAcrossYear() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusDays(-169);
        check(t, 2007, 12, 31, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusDays_negativeOverYears() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusDays(-731);
        check(t, 2009, 7, 15, 12, 30, 40, 987654321);
    }

    @Test
    public void test_minusDays_maximum() {

        LocalDateTime t = createDateMidnight(Year.MAX_VALUE, 12, 30).minusDays(-1);
        check(t, Year.MAX_VALUE, 12, 31, 0, 0, 0, 0);
    }

    @Test
    public void test_minusDays_minimum() {

        LocalDateTime t = createDateMidnight(Year.MIN_VALUE, 1, 2).minusDays(1);
        check(t, Year.MIN_VALUE, 1, 1, 0, 0, 0, 0);
    }

    @Test(expected = DateTimeException.class)
    public void test_minusDays_invalidTooLarge() {

        createDateMidnight(Year.MAX_VALUE, 12, 31).minusDays(-1);
    }

    @Test(expected = DateTimeException.class)
    public void test_minusDays_invalidTooSmall() {

        createDateMidnight(Year.MIN_VALUE, 1, 1).minusDays(1);
    }

    @Test(expected = ArithmeticException.class)
    public void test_minusDays_overflowTooLarge() {

        createDateMidnight(Year.MAX_VALUE, 12, 31).minusDays(Long.MIN_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void test_minusDays_overflowTooSmall() {

        createDateMidnight(Year.MIN_VALUE, 1, 1).minusDays(Long.MAX_VALUE);
    }

    @Test
    public void test_minusHours_one() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = t.toLocalDate();

        for (int i = 0; i < 50; i++) {
            t = t.minusHours(1);

            if (i % 24 == 0) {
                d = d.minusDays(1);
            }

            assertEquals(t.toLocalDate(), d);
            assertEquals(t.getHour(), (((-i + 23) % 24) + 24) % 24);
        }
    }

    @Test
    public void test_minusHours_fromZero() {

        LocalDateTime base = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = base.toLocalDate().plusDays(2);
        LocalTime t = LocalTime.of(3, 0);

        for (int i = -50; i < 50; i++) {
            LocalDateTime dt = base.minusHours(i);
            t = t.minusHours(1);

            if (t.getHour() == 23) {
                d = d.minusDays(1);
            }

            assertEquals(String.valueOf(i), dt.toLocalDate(), d);
            assertEquals(dt.toLocalTime(), t);
        }
    }

    @Test
    public void test_minusHours_fromOne() {

        LocalDateTime base = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.of(1, 0));
        LocalDate d = base.toLocalDate().plusDays(2);
        LocalTime t = LocalTime.of(4, 0);

        for (int i = -50; i < 50; i++) {
            LocalDateTime dt = base.minusHours(i);

            t = t.minusHours(1);

            if (t.getHour() == 23) {
                d = d.minusDays(1);
            }

            assertEquals(String.valueOf(i), dt.toLocalDate(), d);
            assertEquals(dt.toLocalTime(), t);
        }
    }

    @Test
    public void test_minusMinutes_one() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = t.toLocalDate().minusDays(1);

        int hour = 0;
        int min = 0;

        for (int i = 0; i < 70; i++) {
            t = t.minusMinutes(1);
            min--;
            if (min == -1) {
                hour--;
                min = 59;

                if (hour == -1) {
                    hour = 23;
                }
            }
            assertEquals(t.toLocalDate(), d);
            assertEquals(t.getHour(), hour);
            assertEquals(t.getMinute(), min);
        }
    }

    @Test
    public void test_minusMinutes_fromZero() {

        LocalDateTime base = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = base.toLocalDate().minusDays(1);
        LocalTime t = LocalTime.of(22, 49);

        for (int i = 70; i > -70; i--) {
            LocalDateTime dt = base.minusMinutes(i);
            t = t.plusMinutes(1);

            if (t == LocalTime.MIDNIGHT) {
                d = d.plusDays(1);
            }

            assertEquals(dt.toLocalDate(), d);
            assertEquals(dt.toLocalTime(), t);
        }
    }

    @Test
    public void test_minusMinutes_noChange_oneDay() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.minusMinutes(24 * 60);
        assertEquals(t.toLocalDate(), this.TEST_2007_07_15_12_30_40_987654321.toLocalDate().minusDays(1));
    }

    @Test
    public void test_minusSeconds_one() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = t.toLocalDate().minusDays(1);

        int hour = 0;
        int min = 0;
        int sec = 0;

        for (int i = 0; i < 3700; i++) {
            t = t.minusSeconds(1);
            sec--;
            if (sec == -1) {
                min--;
                sec = 59;

                if (min == -1) {
                    hour--;
                    min = 59;

                    if (hour == -1) {
                        hour = 23;
                    }
                }
            }

            assertEquals(t.toLocalDate(), d);
            assertEquals(t.getHour(), hour);
            assertEquals(t.getMinute(), min);
            assertEquals(t.getSecond(), sec);
        }
    }

    Iterator<Object[]> minusSeconds_fromZero() {

        return new Iterator<Object[]>() {
            int delta = 30;

            int i = 3660;

            LocalDate date = TestLocalDateTime.this.TEST_2007_07_15_12_30_40_987654321.toLocalDate().minusDays(1);

            int hour = 22;

            int min = 59;

            int sec = 0;

            @Override
            public boolean hasNext() {

                return this.i >= -3660;
            }

            @Override
            public Object[] next() {

                final Object[] ret = new Object[] { this.i, this.date, this.hour, this.min, this.sec };
                this.i -= this.delta;
                this.sec += this.delta;

                if (this.sec >= 60) {
                    this.min++;
                    this.sec -= 60;

                    if (this.min == 60) {
                        this.hour++;
                        this.min = 0;

                        if (this.hour == 24) {
                            this.hour = 0;
                        }
                    }
                }

                if (this.i == 0) {
                    this.date = this.date.plusDays(1);
                }

                return ret;
            }

            @Override
            public void remove() {

                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void test_minusSeconds_fromZero() {

        Iterator<Object[]> minusSeconds_fromZero = minusSeconds_fromZero();
        while (minusSeconds_fromZero.hasNext()) {
            Object[] data = minusSeconds_fromZero.next();
            int seconds = (int) data[0];
            LocalDate date = (LocalDate) data[1];
            int hour = (int) data[2];
            int min = (int) data[3];
            int sec = (int) data[4];

            LocalDateTime base = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
            LocalDateTime t = base.minusSeconds(seconds);

            assertEquals(date, t.toLocalDate());
            assertEquals(hour, t.getHour());
            assertEquals(min, t.getMinute());
            assertEquals(sec, t.getSecond());
        }
    }

    @Test
    public void test_minusNanos_halfABillion() {

        LocalDateTime t = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
        LocalDate d = t.toLocalDate().minusDays(1);

        int hour = 0;
        int min = 0;
        int sec = 0;
        int nanos = 0;

        for (long i = 0; i < 3700 * 1000000000L; i += 500000000) {
            t = t.minusNanos(500000000);
            nanos -= 500000000;

            if (nanos < 0) {
                sec--;
                nanos += 1000000000;

                if (sec == -1) {
                    min--;
                    sec += 60;

                    if (min == -1) {
                        hour--;
                        min += 60;

                        if (hour == -1) {
                            hour += 24;
                        }
                    }
                }
            }

            assertEquals(t.toLocalDate(), d);
            assertEquals(t.getHour(), hour);
            assertEquals(t.getMinute(), min);
            assertEquals(t.getSecond(), sec);
            assertEquals(t.getNano(), nanos);
        }
    }

    Iterator<Object[]> minusNanos_fromZero() {

        return new Iterator<Object[]>() {
            long delta = 7500000000L;

            long i = 3660 * 1000000000L;

            LocalDate date = TestLocalDateTime.this.TEST_2007_07_15_12_30_40_987654321.toLocalDate().minusDays(1);

            int hour = 22;

            int min = 59;

            int sec = 0;

            long nanos = 0;

            @Override
            public boolean hasNext() {

                return this.i >= -3660 * 1000000000L;
            }

            @Override
            public Object[] next() {

                final Object[] ret = new Object[] { this.i, this.date, this.hour, this.min, this.sec,
                (int) this.nanos };
                this.i -= this.delta;
                this.nanos += this.delta;

                if (this.nanos >= 1000000000L) {
                    this.sec += this.nanos / 1000000000L;
                    this.nanos %= 1000000000L;

                    if (this.sec >= 60) {
                        this.min++;
                        this.sec %= 60;

                        if (this.min == 60) {
                            this.hour++;
                            this.min = 0;

                            if (this.hour == 24) {
                                this.hour = 0;
                                this.date = this.date.plusDays(1);
                            }
                        }
                    }
                }

                return ret;
            }

            @Override
            public void remove() {

                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void test_minusNanos_fromZero() {

        Iterator<Object[]> minusNanos_fromZero = minusNanos_fromZero();
        while (minusNanos_fromZero.hasNext()) {
            Object[] data = minusNanos_fromZero.next();
            long nanoseconds = ((Number) data[0]).longValue();
            LocalDate date = (LocalDate) data[1];
            int hour = (int) data[2];
            int min = (int) data[3];
            int sec = (int) data[4];
            int nanos = (int) data[5];

            LocalDateTime base = this.TEST_2007_07_15_12_30_40_987654321.with(LocalTime.MIDNIGHT);
            LocalDateTime t = base.minusNanos(nanoseconds);

            assertEquals(date, t.toLocalDate());
            assertEquals(hour, t.getHour());
            assertEquals(min, t.getMinute());
            assertEquals(sec, t.getSecond());
            assertEquals(nanos, t.getNano());
        }
    }

    Object[][] provider_until() {

        return new Object[][] { { "2012-06-15T00:00", "2012-06-15T00:00", NANOS, 0 },
        { "2012-06-15T00:00", "2012-06-15T00:00", MICROS, 0 }, { "2012-06-15T00:00", "2012-06-15T00:00", MILLIS, 0 },
        { "2012-06-15T00:00", "2012-06-15T00:00", SECONDS, 0 }, { "2012-06-15T00:00", "2012-06-15T00:00", MINUTES, 0 },
        { "2012-06-15T00:00", "2012-06-15T00:00", HOURS, 0 }, { "2012-06-15T00:00", "2012-06-15T00:00", HALF_DAYS, 0 },

        { "2012-06-15T00:00", "2012-06-15T00:00:01", NANOS, 1000000000 },
        { "2012-06-15T00:00", "2012-06-15T00:00:01", MICROS, 1000000 },
        { "2012-06-15T00:00", "2012-06-15T00:00:01", MILLIS, 1000 },
        { "2012-06-15T00:00", "2012-06-15T00:00:01", SECONDS, 1 },
        { "2012-06-15T00:00", "2012-06-15T00:00:01", MINUTES, 0 },
        { "2012-06-15T00:00", "2012-06-15T00:00:01", HOURS, 0 },
        { "2012-06-15T00:00", "2012-06-15T00:00:01", HALF_DAYS, 0 },

        { "2012-06-15T00:00", "2012-06-15T00:01", NANOS, 60000000000L },
        { "2012-06-15T00:00", "2012-06-15T00:01", MICROS, 60000000 },
        { "2012-06-15T00:00", "2012-06-15T00:01", MILLIS, 60000 },
        { "2012-06-15T00:00", "2012-06-15T00:01", SECONDS, 60 }, { "2012-06-15T00:00", "2012-06-15T00:01", MINUTES, 1 },
        { "2012-06-15T00:00", "2012-06-15T00:01", HOURS, 0 }, { "2012-06-15T00:00", "2012-06-15T00:01", HALF_DAYS, 0 },

        { "2012-06-15T12:30:40.500", "2012-06-15T12:30:39.499", SECONDS, -1 },
        { "2012-06-15T12:30:40.500", "2012-06-15T12:30:39.500", SECONDS, -1 },
        { "2012-06-15T12:30:40.500", "2012-06-15T12:30:39.501", SECONDS, 0 },
        { "2012-06-15T12:30:40.500", "2012-06-15T12:30:40.499", SECONDS, 0 },
        { "2012-06-15T12:30:40.500", "2012-06-15T12:30:40.500", SECONDS, 0 },
        { "2012-06-15T12:30:40.500", "2012-06-15T12:30:40.501", SECONDS, 0 },
        { "2012-06-15T12:30:40.500", "2012-06-15T12:30:41.499", SECONDS, 0 },
        { "2012-06-15T12:30:40.500", "2012-06-15T12:30:41.500", SECONDS, 1 },
        { "2012-06-15T12:30:40.500", "2012-06-15T12:30:41.501", SECONDS, 1 },

        { "2012-06-15T12:30:40.500", "2012-06-16T12:30:39.499", SECONDS, 86400 - 2 },
        { "2012-06-15T12:30:40.500", "2012-06-16T12:30:39.500", SECONDS, 86400 - 1 },
        { "2012-06-15T12:30:40.500", "2012-06-16T12:30:39.501", SECONDS, 86400 - 1 },
        { "2012-06-15T12:30:40.500", "2012-06-16T12:30:40.499", SECONDS, 86400 - 1 },
        { "2012-06-15T12:30:40.500", "2012-06-16T12:30:40.500", SECONDS, 86400 + 0 },
        { "2012-06-15T12:30:40.500", "2012-06-16T12:30:40.501", SECONDS, 86400 + 0 },
        { "2012-06-15T12:30:40.500", "2012-06-16T12:30:41.499", SECONDS, 86400 + 0 },
        { "2012-06-15T12:30:40.500", "2012-06-16T12:30:41.500", SECONDS, 86400 + 1 },
        { "2012-06-15T12:30:40.500", "2012-06-16T12:30:41.501", SECONDS, 86400 + 1 }, };
    }

    @Test
    public void test_until() {

        for (Object[] data : provider_until()) {
            String startStr = (String) data[0];
            String endStr = (String) data[1];
            TemporalUnit unit = (TemporalUnit) data[2];
            long expected = ((Number) data[3]).longValue();

            LocalDateTime start = LocalDateTime.parse(startStr);
            LocalDateTime end = LocalDateTime.parse(endStr);
            assertEquals(start.until(end, unit), expected);
        }
    }

    @Test
    public void test_until_reveresed() {

        for (Object[] data : provider_until()) {
            String startStr = (String) data[0];
            String endStr = (String) data[1];
            TemporalUnit unit = (TemporalUnit) data[2];
            long expected = ((Number) data[3]).longValue();

            LocalDateTime start = LocalDateTime.parse(startStr);
            LocalDateTime end = LocalDateTime.parse(endStr);
            assertEquals(end.until(start, unit), -expected);
        }
    }

    @Test
    public void test_atZone() {

        LocalDateTime t = LocalDateTime.of(2008, 6, 30, 11, 30);
        assertEquals(t.atZone(ZONE_PARIS), ZonedDateTime.of(LocalDateTime.of(2008, 6, 30, 11, 30), ZONE_PARIS));
    }

    @Test
    public void test_atZone_Offset() {

        LocalDateTime t = LocalDateTime.of(2008, 6, 30, 11, 30);
        assertEquals(t.atZone(OFFSET_PTWO), ZonedDateTime.of(LocalDateTime.of(2008, 6, 30, 11, 30), OFFSET_PTWO));
    }

    @Test
    public void test_atZone_dstGap() {

        LocalDateTime t = LocalDateTime.of(2007, 4, 1, 0, 0);
        assertEquals(t.atZone(ZONE_GAZA), ZonedDateTime.of(LocalDateTime.of(2007, 4, 1, 1, 0), ZONE_GAZA));
    }

    @Test
    public void test_atZone_dstOverlap() {

        LocalDateTime t = LocalDateTime.of(2007, 10, 28, 2, 30);
        assertEquals(t.atZone(ZONE_PARIS),
                ZonedDateTime.ofStrict(LocalDateTime.of(2007, 10, 28, 2, 30), OFFSET_PTWO, ZONE_PARIS));
    }

    @Test(expected = NullPointerException.class)
    public void test_atZone_nullTimeZone() {

        LocalDateTime t = LocalDateTime.of(2008, 6, 30, 11, 30);
        t.atZone((ZoneId) null);
    }

    @Test
    public void test_toEpochSecond_afterEpoch() {

        for (int i = -5; i < 5; i++) {
            ZoneOffset offset = ZoneOffset.ofHours(i);
            for (int j = 0; j < 100000; j++) {
                LocalDateTime a = LocalDateTime.of(1970, 1, 1, 0, 0).plusSeconds(j);
                assertEquals(a.toEpochSecond(offset), j - i * 3600);
            }
        }
    }

    @Test
    public void test_toEpochSecond_beforeEpoch() {

        for (int i = 0; i < 100000; i++) {
            LocalDateTime a = LocalDateTime.of(1970, 1, 1, 0, 0).minusSeconds(i);
            assertEquals(a.toEpochSecond(ZoneOffset.UTC), -i);
        }
    }

    @Test
    public void test_comparisons() {

        test_comparisons_LocalDateTime(LocalDate.of(Year.MIN_VALUE, 1, 1), LocalDate.of(Year.MIN_VALUE, 12, 31),
                LocalDate.of(-1, 1, 1), LocalDate.of(-1, 12, 31), LocalDate.of(0, 1, 1), LocalDate.of(0, 12, 31),
                LocalDate.of(1, 1, 1), LocalDate.of(1, 12, 31), LocalDate.of(2008, 1, 1), LocalDate.of(2008, 2, 29),
                LocalDate.of(2008, 12, 31), LocalDate.of(Year.MAX_VALUE, 1, 1), LocalDate.of(Year.MAX_VALUE, 12, 31));
    }

    void test_comparisons_LocalDateTime(LocalDate... localDates) {

        test_comparisons_LocalDateTime(localDates, LocalTime.MIDNIGHT, LocalTime.of(0, 0, 0, 999999999),
                LocalTime.of(0, 0, 59, 0), LocalTime.of(0, 0, 59, 999999999), LocalTime.of(0, 59, 0, 0),
                LocalTime.of(0, 59, 59, 999999999), LocalTime.NOON, LocalTime.of(12, 0, 0, 999999999),
                LocalTime.of(12, 0, 59, 0), LocalTime.of(12, 0, 59, 999999999), LocalTime.of(12, 59, 0, 0),
                LocalTime.of(12, 59, 59, 999999999), LocalTime.of(23, 0, 0, 0), LocalTime.of(23, 0, 0, 999999999),
                LocalTime.of(23, 0, 59, 0), LocalTime.of(23, 0, 59, 999999999), LocalTime.of(23, 59, 0, 0),
                LocalTime.of(23, 59, 59, 999999999));
    }

    void test_comparisons_LocalDateTime(LocalDate[] localDates, LocalTime... localTimes) {

        LocalDateTime[] localDateTimes = new LocalDateTime[localDates.length * localTimes.length];
        int i = 0;

        for (LocalDate localDate : localDates) {
            for (LocalTime localTime : localTimes) {
                localDateTimes[i++] = LocalDateTime.of(localDate, localTime);
            }
        }

        doTest_comparisons_LocalDateTime(localDateTimes);
    }

    void doTest_comparisons_LocalDateTime(LocalDateTime[] localDateTimes) {

        for (int i = 0; i < localDateTimes.length; i++) {
            LocalDateTime a = localDateTimes[i];
            for (int j = 0; j < localDateTimes.length; j++) {
                LocalDateTime b = localDateTimes[j];
                if (i < j) {
                    assertTrue(a + " <=> " + b, a.compareTo(b) < 0);
                    assertEquals(a + " <=> " + b, a.isBefore(b), true);
                    assertEquals(a + " <=> " + b, a.isAfter(b), false);
                    assertEquals(a + " <=> " + b, a.equals(b), false);
                } else if (i > j) {
                    assertTrue(a + " <=> " + b, a.compareTo(b) > 0);
                    assertEquals(a + " <=> " + b, a.isBefore(b), false);
                    assertEquals(a + " <=> " + b, a.isAfter(b), true);
                    assertEquals(a + " <=> " + b, a.equals(b), false);
                } else {
                    assertEquals(a + " <=> " + b, a.compareTo(b), 0);
                    assertEquals(a + " <=> " + b, a.isBefore(b), false);
                    assertEquals(a + " <=> " + b, a.isAfter(b), false);
                    assertEquals(a + " <=> " + b, a.equals(b), true);
                }
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void test_compareTo_ObjectNull() {

        this.TEST_2007_07_15_12_30_40_987654321.compareTo(null);
    }

    @Test(expected = NullPointerException.class)
    public void test_isBefore_ObjectNull() {

        this.TEST_2007_07_15_12_30_40_987654321.isBefore(null);
    }

    @Test(expected = NullPointerException.class)
    public void test_isAfter_ObjectNull() {

        this.TEST_2007_07_15_12_30_40_987654321.isAfter(null);
    }

    @Test(expected = ClassCastException.class)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void compareToNonLocalDateTime() {

        Comparable c = this.TEST_2007_07_15_12_30_40_987654321;
        c.compareTo(new Object());
    }

    Iterator<Object[]> provider_sampleDateTimes() {

        return new Iterator<Object[]>() {
            Object[][] sampleDates = provider_sampleDates();

            Object[][] sampleTimes = provider_sampleTimes();

            int datesIndex = 0;

            int timesIndex = 0;

            @Override
            public boolean hasNext() {

                return this.datesIndex < this.sampleDates.length;
            }

            @Override
            public Object[] next() {

                Object[] sampleDate = this.sampleDates[this.datesIndex];
                Object[] sampleTime = this.sampleTimes[this.timesIndex];

                Object[] ret = new Object[sampleDate.length + sampleTime.length];

                System.arraycopy(sampleDate, 0, ret, 0, sampleDate.length);
                System.arraycopy(sampleTime, 0, ret, sampleDate.length, sampleTime.length);

                if (++this.timesIndex == this.sampleTimes.length) {
                    this.datesIndex++;
                    this.timesIndex = 0;
                }

                return ret;
            }

            @Override
            public void remove() {

                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void test_equals_true() {

        Iterator<Object[]> provider_sampleDateTimes = provider_sampleDateTimes();
        while (provider_sampleDateTimes.hasNext()) {
            Object[] data = provider_sampleDateTimes.next();
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int mi = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];

            LocalDateTime a = LocalDateTime.of(y, m, d, h, mi, s, n);
            LocalDateTime b = LocalDateTime.of(y, m, d, h, mi, s, n);
            assertTrue(a.equals(b));
        }
    }

    @Test
    public void test_equals_false_year_differs() {

        Iterator<Object[]> provider_sampleDateTimes = provider_sampleDateTimes();
        while (provider_sampleDateTimes.hasNext()) {
            Object[] data = provider_sampleDateTimes.next();
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int mi = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];

            LocalDateTime a = LocalDateTime.of(y, m, d, h, mi, s, n);
            LocalDateTime b = LocalDateTime.of(y + 1, m, d, h, mi, s, n);
            assertFalse(a.equals(b));
        }
    }

    @Test
    public void test_equals_false_month_differs() {

        Iterator<Object[]> provider_sampleDateTimes = provider_sampleDateTimes();
        while (provider_sampleDateTimes.hasNext()) {
            Object[] data = provider_sampleDateTimes.next();
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int mi = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];

            LocalDateTime a = LocalDateTime.of(y, m, d, h, mi, s, n);
            LocalDateTime b = LocalDateTime.of(y, m + 1, d, h, mi, s, n);
            assertFalse(a.equals(b));
        }
    }

    @Test
    public void test_equals_false_day_differs() {

        Iterator<Object[]> provider_sampleDateTimes = provider_sampleDateTimes();
        while (provider_sampleDateTimes.hasNext()) {
            Object[] data = provider_sampleDateTimes.next();
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int mi = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];

            LocalDateTime a = LocalDateTime.of(y, m, d, h, mi, s, n);
            LocalDateTime b = LocalDateTime.of(y, m, d + 1, h, mi, s, n);
            assertFalse(a.equals(b));
        }
    }

    @Test
    public void test_equals_false_hour_differs() {

        Iterator<Object[]> provider_sampleDateTimes = provider_sampleDateTimes();
        while (provider_sampleDateTimes.hasNext()) {
            Object[] data = provider_sampleDateTimes.next();
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int mi = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];

            LocalDateTime a = LocalDateTime.of(y, m, d, h, mi, s, n);
            LocalDateTime b = LocalDateTime.of(y, m, d, h + 1, mi, s, n);
            assertFalse(a.equals(b));
        }
    }

    @Test
    public void test_equals_false_minute_differs() {

        Iterator<Object[]> provider_sampleDateTimes = provider_sampleDateTimes();
        while (provider_sampleDateTimes.hasNext()) {
            Object[] data = provider_sampleDateTimes.next();
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int mi = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];

            LocalDateTime a = LocalDateTime.of(y, m, d, h, mi, s, n);
            LocalDateTime b = LocalDateTime.of(y, m, d, h, mi + 1, s, n);
            assertFalse(a.equals(b));
        }
    }

    @Test
    public void test_equals_false_second_differs() {

        Iterator<Object[]> provider_sampleDateTimes = provider_sampleDateTimes();
        while (provider_sampleDateTimes.hasNext()) {
            Object[] data = provider_sampleDateTimes.next();
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int mi = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];

            LocalDateTime a = LocalDateTime.of(y, m, d, h, mi, s, n);
            LocalDateTime b = LocalDateTime.of(y, m, d, h, mi, s + 1, n);
            assertFalse(a.equals(b));
        }
    }

    @Test
    public void test_equals_false_nano_differs() {

        Iterator<Object[]> provider_sampleDateTimes = provider_sampleDateTimes();
        while (provider_sampleDateTimes.hasNext()) {
            Object[] data = provider_sampleDateTimes.next();
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int mi = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];

            LocalDateTime a = LocalDateTime.of(y, m, d, h, mi, s, n);
            LocalDateTime b = LocalDateTime.of(y, m, d, h, mi, s, n + 1);
            assertFalse(a.equals(b));
        }
    }

    @Test
    public void test_equals_itself_true() {

        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.equals(this.TEST_2007_07_15_12_30_40_987654321), true);
    }

    @Test
    public void test_equals_string_false() {

        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.equals("2007-07-15T12:30:40.987654321"), false);
    }

    @Test
    public void test_equals_null_false() {

        assertEquals(this.TEST_2007_07_15_12_30_40_987654321.equals(null), false);
    }

    @Test
    public void test_hashCode() {

        Iterator<Object[]> provider_sampleDateTimes = provider_sampleDateTimes();
        while (provider_sampleDateTimes.hasNext()) {
            Object[] data = provider_sampleDateTimes.next();
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int mi = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];

            LocalDateTime a = LocalDateTime.of(y, m, d, h, mi, s, n);
            assertEquals(a.hashCode(), a.hashCode());
            LocalDateTime b = LocalDateTime.of(y, m, d, h, mi, s, n);
            assertEquals(a.hashCode(), b.hashCode());
        }
    }

    Object[][] provider_sampleToString() {

        return new Object[][] { { 2008, 7, 5, 2, 1, 0, 0, "2008-07-05T02:01" },
        { 2007, 12, 31, 23, 59, 1, 0, "2007-12-31T23:59:01" },
        { 999, 12, 31, 23, 59, 59, 990000000, "0999-12-31T23:59:59.990" },
        { -1, 1, 2, 23, 59, 59, 999990000, "-0001-01-02T23:59:59.999990" },
        { -2008, 1, 2, 23, 59, 59, 999999990, "-2008-01-02T23:59:59.999999990" }, };
    }

    @Test
    public void test_toString() {

        for (Object[] data : provider_sampleToString()) {
            int y = (int) data[0];
            int m = (int) data[1];
            int d = (int) data[2];
            int h = (int) data[3];
            int mi = (int) data[4];
            int s = (int) data[5];
            int n = (int) data[6];
            String expected = (String) data[7];

            LocalDateTime t = LocalDateTime.of(y, m, d, h, mi, s, n);
            String str = t.toString();
            assertEquals(str, expected);
        }
    }

    @Test
    public void test_format_formatter() {

        DateTimeFormatter f = DateTimeFormatter.ofPattern("y M d H m s");
        String t = LocalDateTime.of(2010, 12, 3, 11, 30, 45).format(f);
        assertEquals(t, "2010 12 3 11 30 45");
    }

    @Test(expected = NullPointerException.class)
    public void test_format_formatter_null() {

        LocalDateTime.of(2010, 12, 3, 11, 30, 45).format(null);
    }

}
