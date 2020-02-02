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
package org.teavm.classlib.java.time.zone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.teavm.classlib.java.time.TDayOfWeek.FRIDAY;
import static org.teavm.classlib.java.time.TDayOfWeek.MONDAY;
import static org.teavm.classlib.java.time.TDayOfWeek.SATURDAY;
import static org.teavm.classlib.java.time.TDayOfWeek.SUNDAY;
import static org.teavm.classlib.java.time.TDayOfWeek.THURSDAY;
import static org.teavm.classlib.java.time.TDayOfWeek.TUESDAY;
import static org.teavm.classlib.java.time.TMonth.APRIL;
import static org.teavm.classlib.java.time.TMonth.AUGUST;
import static org.teavm.classlib.java.time.TMonth.FEBRUARY;
import static org.teavm.classlib.java.time.TMonth.MARCH;
import static org.teavm.classlib.java.time.TMonth.MAY;
import static org.teavm.classlib.java.time.TMonth.NOVEMBER;
import static org.teavm.classlib.java.time.TMonth.OCTOBER;
import static org.teavm.classlib.java.time.TMonth.SEPTEMBER;
import static org.teavm.classlib.java.time.zone.TZoneOffsetTransitionRule.TimeDefinition.STANDARD;
import static org.teavm.classlib.java.time.zone.TZoneOffsetTransitionRule.TimeDefinition.UTC;
import static org.teavm.classlib.java.time.zone.TZoneOffsetTransitionRule.TimeDefinition.WALL;

import java.util.List;

import org.junit.Test;
import org.teavm.classlib.java.time.TDateTimeException;
import org.teavm.classlib.java.time.TLocalDateTime;
import org.teavm.classlib.java.time.TLocalTime;
import org.teavm.classlib.java.time.TMonth;
import org.teavm.classlib.java.time.TYear;
import org.teavm.classlib.java.time.TZoneOffset;
import org.teavm.classlib.java.time.zone.TZoneOffsetTransitionRule.TimeDefinition;

public class TestZoneRulesBuilder {

    private static final TZoneOffset OFFSET_1 = TZoneOffset.ofHours(1);

    private static final TZoneOffset OFFSET_2 = TZoneOffset.ofHours(2);

    private static final TZoneOffset OFFSET_1_15 = TZoneOffset.ofHoursMinutes(1, 15);

    private static final TZoneOffset OFFSET_2_30 = TZoneOffset.ofHoursMinutes(2, 30);

    private static final int PERIOD_0 = 0;

    private static final int PERIOD_1HOUR = 60 * 60;

    private static final int PERIOD_1HOUR30MIN = ((1 * 60) + 30) * 60;

    private static final TLocalDateTime DATE_TIME_FIRST = dateTime(TYear.MIN_VALUE, 1, 1, 0, 0);

    private static final TLocalDateTime DATE_TIME_LAST = dateTime(TYear.MAX_VALUE, 12, 31, 23, 59);

    private static final TLocalDateTime DATE_TIME_2008_01_01 = dateTime(2008, 1, 1, 0, 0);

    private static final TLocalDateTime DATE_TIME_2008_07_01 = dateTime(2008, 7, 1, 0, 0);

    @Test(expected = IllegalStateException.class)
    public void test_toRules_noWindows() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.toRules("Europe/London");
    }

    @Test(expected = NullPointerException.class)
    public void test_toRules_null() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_2_30);
        b.toRules(null);
    }

    @Test
    public void test_combined_singleCutover() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(1950, 1, 1, 1, 0), STANDARD);
        b.addWindowForever(OFFSET_2);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertGap(test, 1950, 1, 1, 1, 30, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_2);
    }

    @Test
    public void test_combined_localFixedRules() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1_15, dateTime(1920, 1, 1, 1, 0), WALL);
        b.addWindow(OFFSET_1, dateTime(1950, 1, 1, 1, 0), WALL);
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1_15);
        assertOverlap(test, 1920, 1, 1, 0, 55, OFFSET_1_15, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);
        assertOffsetInfo(test, dateTime(1800, 7, 1, 1, 0), OFFSET_1_15);
        assertOffsetInfo(test, dateTime(1920, 1, 1, 1, 0), OFFSET_1);
        assertOffsetInfo(test, dateTime(1960, 1, 1, 1, 0), OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 1, 1, 1, 0), OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_2008_01_01, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_2008_07_01, OFFSET_2_30);
        assertGap(test, 2008, 3, 30, 1, 20, OFFSET_1, OFFSET_2_30);
        assertOverlap(test, 2008, 10, 26, 0, 20, OFFSET_2_30, OFFSET_1);
    }

    @Test
    public void test_combined_windowChangeDuringDST() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(2000, 7, 1, 1, 0), WALL);
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(2, 0), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("Europe/Dublin");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);

        assertOffsetInfo(test, dateTime(2000, 1, 1, 0, 0), OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 7, 1, 0, 0), OFFSET_1);
        assertGap(test, 2000, 7, 1, 1, 20, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, dateTime(2000, 7, 1, 3, 0), OFFSET_2);
        assertOverlap(test, 2000, 10, 29, 1, 20, OFFSET_2, OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 12, 1, 0, 0), OFFSET_1);
    }

    @Test
    public void test_combined_windowChangeWithinDST() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(2000, 7, 1, 1, 0), WALL);
        b.addWindow(OFFSET_1, dateTime(2000, 8, 1, 2, 0), WALL);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(2, 0), false, WALL, PERIOD_0);
        b.addWindowForever(OFFSET_1);
        TZoneRules test = b.toRules("Europe/Dublin");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);

        assertOffsetInfo(test, dateTime(2000, 1, 1, 0, 0), OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 7, 1, 0, 0), OFFSET_1);
        assertGap(test, 2000, 7, 1, 1, 20, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, dateTime(2000, 7, 1, 3, 0), OFFSET_2);
        assertOverlap(test, 2000, 8, 1, 1, 20, OFFSET_2, OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 12, 1, 0, 0), OFFSET_1);
    }

    @Test
    public void test_combined_endsInSavings() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1_15, dateTime(1920, 1, 1, 1, 0), WALL);
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_0);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR);
        TZoneRules test = b.toRules("Pacific/Auckland");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1_15);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_2);
        assertOverlap(test, 1920, 1, 1, 0, 55, OFFSET_1_15, OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 3, 26, 0, 59), OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 3, 26, 1, 00), OFFSET_1);
        assertGap(test, 2000, 10, 29, 1, 20, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2001, 3, 25, 0, 20, OFFSET_2, OFFSET_1);
        assertGap(test, 2001, 10, 28, 1, 20, OFFSET_1, OFFSET_2);
    }

    @Test
    public void test_combined_closeTransitions() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(1920, 1, 1, 1, 0), WALL);
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, MARCH, 20, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2000, MARCH, 20, time(4, 2), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 3, 20, 1, 59), OFFSET_1);
        assertGap(test, 2000, 3, 20, 2, 0, OFFSET_1, OFFSET_2);
        assertGap(test, 2000, 3, 20, 2, 59, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, dateTime(2000, 3, 20, 3, 0), OFFSET_2);
        assertOffsetInfo(test, dateTime(2000, 3, 20, 3, 1), OFFSET_2);
        assertOverlap(test, 2000, 3, 20, 3, 2, OFFSET_2, OFFSET_1);
        assertOverlap(test, 2000, 3, 20, 4, 1, OFFSET_2, OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 3, 20, 4, 2), OFFSET_1);
    }

    @Test
    public void test_combined_closeTransitionsMeet() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(1920, 1, 1, 1, 0), WALL);
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, MARCH, 20, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2000, MARCH, 20, time(4, 0), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 3, 20, 1, 59), OFFSET_1);
        assertGap(test, 2000, 3, 20, 2, 0, OFFSET_1, OFFSET_2);
        assertGap(test, 2000, 3, 20, 2, 59, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2000, 3, 20, 3, 0, OFFSET_2, OFFSET_1);
        assertOverlap(test, 2000, 3, 20, 3, 59, OFFSET_2, OFFSET_1);
        assertOffsetInfo(test, dateTime(2000, 3, 20, 4, 0), OFFSET_1);
    }

    @Test
    public void test_combined_weirdSavingsBeforeLast() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(1920, 1, 1, 1, 0), WALL);
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(1998, MARCH, 20, time(2, 0), false, WALL, PERIOD_1HOUR30MIN);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, 20, null, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, OCTOBER, 20, null, time(2, 0), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");

        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);

        assertOffsetInfo(test, dateTime(1999, 1, 1, 0, 0), OFFSET_2_30);
        assertOverlap(test, 2000, 3, 20, 1, 30, OFFSET_2_30, OFFSET_2);
        assertOverlap(test, 2000, 10, 20, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2001, 3, 20, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2001, 10, 20, 1, 30, OFFSET_2, OFFSET_1);
    }

    @Test
    public void test_combined_differentLengthLastRules1() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(1920, 1, 1, 1, 0), WALL);
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(1998, MARCH, 20, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(1998, TYear.MAX_VALUE, OCTOBER, 30, null, time(2, 0), false, WALL, PERIOD_0);
        b.addRuleToWindow(1999, MARCH, 21, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2000, MARCH, 22, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2001, MARCH, 23, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2002, TYear.MAX_VALUE, MARCH, 24, null, time(2, 0), false, WALL, PERIOD_1HOUR);
        TZoneRules test = b.toRules("Europe/London");

        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);

        assertGap(test, 1998, 3, 20, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 1998, 10, 30, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 1999, 3, 21, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 1999, 10, 30, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2000, 3, 22, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2000, 10, 30, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2001, 3, 23, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2001, 10, 30, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2002, 3, 24, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2002, 10, 30, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2003, 3, 24, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2003, 10, 30, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2004, 3, 24, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2004, 10, 30, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2005, 3, 24, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2005, 10, 30, 1, 30, OFFSET_2, OFFSET_1);
    }

    @Test
    public void test_combined_differentLengthLastRules2() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(1920, 1, 1, 1, 0), WALL);
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(1998, TYear.MAX_VALUE, MARCH, 30, null, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(1998, OCTOBER, 20, time(2, 0), false, WALL, PERIOD_0);
        b.addRuleToWindow(1999, OCTOBER, 21, time(2, 0), false, WALL, PERIOD_0);
        b.addRuleToWindow(2000, OCTOBER, 22, time(2, 0), false, WALL, PERIOD_0);
        b.addRuleToWindow(2001, OCTOBER, 23, time(2, 0), false, WALL, PERIOD_0);
        b.addRuleToWindow(2002, TYear.MAX_VALUE, OCTOBER, 24, null, time(2, 0), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");

        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);

        assertGap(test, 1998, 3, 30, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 1998, 10, 20, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 1999, 3, 30, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 1999, 10, 21, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2000, 3, 30, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2000, 10, 22, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2001, 3, 30, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2001, 10, 23, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2002, 3, 30, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2002, 10, 24, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2003, 3, 30, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2003, 10, 24, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2004, 3, 30, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2004, 10, 24, 1, 30, OFFSET_2, OFFSET_1);
        assertGap(test, 2005, 3, 30, 2, 30, OFFSET_1, OFFSET_2);
        assertOverlap(test, 2005, 10, 24, 1, 30, OFFSET_2, OFFSET_1);
    }

    @Test
    public void test_twoChangesSameDay() {

        // ensures that TZRule.compare works
        TZoneOffset plus2 = TZoneOffset.ofHours(2);
        TZoneOffset plus3 = TZoneOffset.ofHours(3);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(plus2);
        b.addRuleToWindow(2010, 2010, SEPTEMBER, 10, null, time(12, 0), false, STANDARD, PERIOD_1HOUR);
        b.addRuleToWindow(2010, 2010, SEPTEMBER, 10, null, time(23, 0), false, STANDARD, PERIOD_0);
        TZoneRules test = b.toRules("Africa/Cairo");

        assertOffsetInfo(test, DATE_TIME_FIRST, plus2);
        assertOffsetInfo(test, DATE_TIME_LAST, plus2);

        assertGap(test, 2010, 9, 10, 12, 0, plus2, plus3); // jump forward from 12:00 to 13:00 on Tue 10th Sep
        assertOverlap(test, 2010, 9, 10, 23, 0, plus3, plus2); // overlaps from Wed 11th Sep 00:00 back to Tue 10th Sep
                                                               // 23:00
    }

    @Test
    public void test_twoChangesDifferentDefinition() {

        // ensures that TZRule.compare works
        TZoneOffset plus2 = TZoneOffset.ofHours(2);
        TZoneOffset plus3 = TZoneOffset.ofHours(3);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(plus2);
        b.addRuleToWindow(2010, 2010, SEPTEMBER, -1, TUESDAY, time(0, 0), false, STANDARD, PERIOD_1HOUR);
        b.addRuleToWindow(2010, 2010, SEPTEMBER, 29, null, time(23, 0), false, STANDARD, PERIOD_0);
        TZoneRules test = b.toRules("Africa/Cairo");

        assertOffsetInfo(test, DATE_TIME_FIRST, plus2);
        assertOffsetInfo(test, DATE_TIME_LAST, plus2);

        assertGap(test, 2010, 9, 28, 0, 0, plus2, plus3); // jump forward from 00:00 to 01:00 on Tue 28th Sep
        assertOverlap(test, 2010, 9, 29, 23, 0, plus3, plus2); // overlaps from Thu 30th Sep 00:00 back to Wed 29th Sep
                                                               // 23:00
    }

    @Test
    public void test_argentina() {
        // # On October 3, 1999, 0:00 local, Argentina implemented daylight savings time,
        // # which did not result in the switch of a time zone, as they stayed 9 hours
        // # from the International TDate Line.
        // Rule Arg 1989 1993 - Mar Sun>=1 0:00 0 -
        // Rule Arg 1989 1992 - Oct Sun>=15 0:00 1:00 S
        // Rule Arg 1999 only - Oct Sun>=1 0:00 1:00 S
        // Rule Arg 2000 only - Mar 3 0:00 0 -
        // Zone America/Argentina/Tucuman -4:20:52 - LMT 1894 Oct 31
        // -3:00 Arg AR%sT 1999 Oct 3
        // -4:00 Arg AR%sT 2000 Mar 3

        TZoneOffset minus3 = TZoneOffset.ofHours(-3);
        TZoneOffset minus4 = TZoneOffset.ofHours(-4);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(minus3, dateTime(1900, 1, 1, 0, 0), WALL);
        b.addWindow(minus3, dateTime(1999, 10, 3, 0, 0), WALL);
        b.addRuleToWindow(1993, MARCH, 3, time(0, 0), false, WALL, PERIOD_0);
        b.addRuleToWindow(1999, OCTOBER, 3, time(0, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2000, MARCH, 3, time(0, 0), false, WALL, PERIOD_0);
        b.addWindow(minus4, dateTime(2000, 3, 3, 0, 0), WALL);
        b.addRuleToWindow(1993, MARCH, 3, time(0, 0), false, WALL, PERIOD_0);
        b.addRuleToWindow(1999, OCTOBER, 3, time(0, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2000, MARCH, 3, time(0, 0), false, WALL, PERIOD_0);
        b.addWindowForever(minus3);
        TZoneRules test = b.toRules("America/Argentina/Tucuman");

        assertOffsetInfo(test, DATE_TIME_FIRST, minus3);
        assertOffsetInfo(test, DATE_TIME_LAST, minus3);

        assertOffsetInfo(test, dateTime(1999, 10, 2, 22, 59), minus3);
        assertOffsetInfo(test, dateTime(1999, 10, 2, 23, 59), minus3);
        assertOffsetInfo(test, dateTime(1999, 10, 3, 0, 0), minus3);
        assertOffsetInfo(test, dateTime(1999, 10, 3, 1, 0), minus3);

        assertOffsetInfo(test, dateTime(2000, 3, 2, 22, 59), minus3);
        assertOffsetInfo(test, dateTime(2000, 3, 2, 23, 59), minus3);
        assertOffsetInfo(test, dateTime(2000, 3, 3, 0, 0), minus3);
        assertOffsetInfo(test, dateTime(2000, 3, 3, 1, 0), minus3);
    }

    @Test
    public void test_cairo_dateChange() {

        // Rule Egypt 2008 max - Apr lastFri 0:00s 1:00 S
        // Rule Egypt 2008 max - Aug lastThu 23:00s 0 -
        // Zone Africa/Cairo 2:05:00 - LMT 1900 Oct
        // 2:00 Egypt EE%sT
        TZoneOffset plus2 = TZoneOffset.ofHours(2);
        TZoneOffset plus3 = TZoneOffset.ofHours(3);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(plus2);
        b.addRuleToWindow(2008, TYear.MAX_VALUE, APRIL, -1, FRIDAY, time(0, 0), false, STANDARD, PERIOD_1HOUR);
        b.addRuleToWindow(2008, TYear.MAX_VALUE, AUGUST, -1, THURSDAY, time(23, 0), false, STANDARD, PERIOD_0);
        TZoneRules test = b.toRules("Africa/Cairo");

        assertOffsetInfo(test, DATE_TIME_FIRST, plus2);
        assertOffsetInfo(test, DATE_TIME_LAST, plus2);

        assertGap(test, 2009, 4, 24, 0, 0, plus2, plus3);
        assertOverlap(test, 2009, 8, 27, 23, 0, plus3, plus2); // overlaps from Fri 00:00 back to Thu 23:00
    }

    @Test
    public void test_cairo_twoChangesSameMonth() {

        // 2011i
        // Rule Egypt 2010 only - Aug 11 0:00 0 -
        // Rule Egypt 2010 only - Sep 10 0:00 1:00 S
        // Rule Egypt 2010 only - Sep lastThu 23:00s 0 -
        // Zone Africa/Cairo 2:05:00 - LMT 1900 Oct
        // 2:00 Egypt EE%sT
        TZoneOffset plus2 = TZoneOffset.ofHours(2);
        TZoneOffset plus3 = TZoneOffset.ofHours(3);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(plus2);
        b.addRuleToWindow(2010, 2010, AUGUST, 11, null, time(0, 0), false, STANDARD, PERIOD_0);
        b.addRuleToWindow(2010, 2010, SEPTEMBER, 10, null, time(0, 0), false, STANDARD, PERIOD_1HOUR);
        b.addRuleToWindow(2010, 2010, SEPTEMBER, -1, THURSDAY, time(23, 0), false, STANDARD, PERIOD_0);
        TZoneRules test = b.toRules("Africa/Cairo");

        assertOffsetInfo(test, DATE_TIME_FIRST, plus2);
        assertOffsetInfo(test, DATE_TIME_LAST, plus2);

        assertGap(test, 2010, 9, 10, 0, 0, plus2, plus3); // jump forward from 00:00 to 01:00 on Fri 10th Sep
        assertOverlap(test, 2010, 9, 30, 23, 0, plus3, plus2); // overlaps from Fri 1st Oct 00:00 back to Thu 30th Sep
                                                               // 23:00 (!!!)
    }

    @Test
    public void test_sofia_lastRuleClash() {

        // UTC rule change in 1996 occurs after Wall change
        // need to ensure that last rule is only applied to last window
        // Rule E-Eur 1981 max - Mar lastSun 0:00 1:00 S
        // Rule E-Eur 1996 max - Oct lastSun 0:00 0 -
        // Rule EU 1981 max - Mar lastSun 1:00u 1:00 S
        // Rule EU 1996 max - Oct lastSun 1:00u 0 -
        // Zone Europe/Sofia
        // 2:00 E-Eur EE%sT 1997
        // 2:00 EU EE%sT
        TZoneOffset plus2 = TZoneOffset.ofHours(2);
        TZoneOffset plus3 = TZoneOffset.ofHours(3);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(plus2, dateTime(1997, 1, 1, 0, 0), WALL);
        b.addRuleToWindow(1996, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(1996, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_0);
        b.addWindowForever(plus2);
        b.addRuleToWindow(1996, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, UTC, PERIOD_1HOUR);
        b.addRuleToWindow(1996, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(1, 0), false, UTC, PERIOD_0);
        TZoneRules test = b.toRules("Europe/Sofia");

        assertOffsetInfo(test, DATE_TIME_FIRST, plus2);
        assertOffsetInfo(test, DATE_TIME_LAST, plus2);

        assertGap(test, 1996, 3, 31, 1, 0, plus2, plus3);
        assertOverlap(test, 1996, 10, 27, 0, 0, plus3, plus2);
        assertOffsetInfo(test, dateTime(1996, 10, 27, 1, 0), plus2);
        assertOffsetInfo(test, dateTime(1996, 10, 27, 2, 0), plus2);
        assertOffsetInfo(test, dateTime(1996, 10, 27, 3, 0), plus2);
        assertOffsetInfo(test, dateTime(1996, 10, 27, 4, 0), plus2);
    }

    @Test
    public void test_prague() {

        // need to calculate savings applicable at window start based on
        // first rule being transition from no savings to DST
        // Rule C-Eur 1944 1945 - Apr Mon>=1 2:00s 1:00 S
        // Rule C-Eur 1944 only - Oct 2 2:00s 0 -
        // Rule C-Eur 1945 only - Sep 16 2:00s 0 -
        // Rule Czech 1945 only - Apr 8 2:00s 1:00 S
        // Rule Czech 1945 only - Nov 18 2:00s 0 -
        // Zone Europe/Prague 0:57:44 - LMT 1850
        // 0:57:44 - PMT 1891 Oct
        // 1:00 C-Eur CE%sT 1944 Sep 17 2:00s
        // 1:00 Czech CE%sT 1979
        // 1:00 EU CE%sT
        TZoneOffset plus1 = TZoneOffset.ofHours(1);
        TZoneOffset plus2 = TZoneOffset.ofHours(2);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(plus1, dateTime(1944, 9, 17, 2, 0), STANDARD);
        b.addRuleToWindow(1944, 1945, APRIL, 1, MONDAY, time(2, 0), false, STANDARD, PERIOD_1HOUR);
        b.addRuleToWindow(1944, OCTOBER, 2, time(2, 0), false, STANDARD, PERIOD_0);
        b.addRuleToWindow(1945, SEPTEMBER, 16, time(2, 0), false, STANDARD, PERIOD_0);
        b.addWindow(plus1, dateTime(1979, 1, 1, 0, 0), WALL);
        b.addRuleToWindow(1945, APRIL, 8, time(2, 0), false, STANDARD, PERIOD_1HOUR);
        b.addRuleToWindow(1945, NOVEMBER, 18, time(2, 0), false, STANDARD, PERIOD_0);
        b.addWindowForever(plus1);
        TZoneRules test = b.toRules("Europe/Sofia");

        assertOffsetInfo(test, DATE_TIME_FIRST, plus1);
        assertOffsetInfo(test, DATE_TIME_LAST, plus1);

        assertGap(test, 1944, 4, 3, 2, 30, plus1, plus2);
        assertOverlap(test, 1944, 9, 17, 2, 30, plus2, plus1);
        assertOffsetInfo(test, dateTime(1944, 9, 17, 3, 30), plus1);
        assertOffsetInfo(test, dateTime(1944, 9, 17, 4, 30), plus1);
        assertGap(test, 1945, 4, 8, 2, 30, plus1, plus2);
        assertOverlap(test, 1945, 11, 18, 2, 30, plus2, plus1);
    }

    @Test
    public void test_tbilisi() {

        // has transition into and out of 1 year of permanent DST (Mar96-Oct97)
        // where the date in the window and rule are the same
        // this is weird because the wall time in the rule is amended by the actual
        // wall time from the zone lines
        // Rule E-EurAsia 1981 max - Mar lastSun 0:00 1:00 S
        // Rule E-EurAsia 1979 1995 - Sep lastSun 0:00 0 -
        // Rule E-EurAsia 1996 max - Oct lastSun 0:00 0 -
        // Zone Asia/Tbilisi 2:59:16 - LMT 1880
        // 4:00 E-EurAsia GE%sT 1996 Oct lastSun
        // 4:00 1:00 GEST 1997 Mar lastSun
        // 4:00 E-EurAsia GE%sT 2004 Jun 27
        // 3:00 RussiaAsia GE%sT 2005 Mar lastSun 2:00
        // 4:00 - GET
        TZoneOffset plus4 = TZoneOffset.ofHours(4);
        TZoneOffset plus5 = TZoneOffset.ofHours(5);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(plus4, dateTime(1996, 10, 27, 0, 0), WALL);
        b.addRuleToWindow(1996, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(0, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(1996, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(0, 0), false, WALL, PERIOD_0);
        b.addWindow(plus4, dateTime(1997, 3, 30, 0, 0), WALL);
        b.setFixedSavingsToWindow(PERIOD_1HOUR);
        b.addWindowForever(plus4);
        b.addRuleToWindow(1996, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(0, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(1996, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(0, 0), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("Europe/Sofia");

        assertOffsetInfo(test, DATE_TIME_FIRST, plus4);
        assertOffsetInfo(test, DATE_TIME_LAST, plus4);

        assertGap(test, 1996, 3, 31, 0, 30, plus4, plus5);
        // assertOverlap(test, 1996, 10, 26, 23, 30, plus5, plus4); // fixed DST blocks overlap
        assertOffsetInfo(test, dateTime(1996, 10, 26, 22, 30), plus5);
        assertOffsetInfo(test, dateTime(1996, 10, 26, 23, 30), plus5);
        assertOffsetInfo(test, dateTime(1996, 10, 27, 0, 30), plus5);
        // assertOverlap(test, 1997, 3, 30, 0, 30, plus5, plus4); // end of fixed blocks overlap
        assertOffsetInfo(test, dateTime(1997, 3, 29, 22, 30), plus5);
        assertOffsetInfo(test, dateTime(1997, 3, 29, 23, 30), plus5);
        assertOffsetInfo(test, dateTime(1997, 3, 30, 0, 30), plus5);
        assertOffsetInfo(test, dateTime(1997, 3, 30, 1, 30), plus5);
        assertOffsetInfo(test, dateTime(1997, 3, 30, 2, 30), plus5);
        assertOverlap(test, 1997, 10, 25, 23, 30, plus5, plus4);
    }

    @Test
    public void test_vincennes() {

        // need to ensure that at least one real rule is added to expand on the last rule
        // Rule US 2007 max - Mar Sun>=8 2:00 1:00 D
        // Rule US 2007 max - Nov Sun>=1 2:00 0 S
        // -5:00 - EST 2006 Apr 2 2:00
        // -6:00 US C%sT 2007 Nov 4 2:00
        // -5:00 US E%sT
        TZoneOffset minus5 = TZoneOffset.ofHours(-5);
        TZoneOffset minus6 = TZoneOffset.ofHours(-6);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(minus6, dateTime(2007, 11, 4, 2, 0), WALL);
        b.addRuleToWindow(2007, TYear.MAX_VALUE, MARCH, 8, SUNDAY, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2007, TYear.MAX_VALUE, NOVEMBER, 1, SUNDAY, time(2, 0), false, WALL, PERIOD_0);
        b.addWindowForever(minus5);
        b.addRuleToWindow(2007, TYear.MAX_VALUE, MARCH, 8, SUNDAY, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2007, TYear.MAX_VALUE, NOVEMBER, 1, SUNDAY, time(2, 0), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("America/Indiana/Vincennes");

        assertOffsetInfo(test, DATE_TIME_FIRST, minus6);
        assertOffsetInfo(test, DATE_TIME_LAST, minus5);

        assertOffsetInfo(test, dateTime(2007, 3, 11, 0, 0), minus6);
        assertOffsetInfo(test, dateTime(2007, 3, 11, 1, 0), minus6);
        assertGap(test, 2007, 3, 11, 2, 0, minus6, minus5);
        assertOffsetInfo(test, dateTime(2007, 3, 11, 3, 0), minus5);
        assertOffsetInfo(test, dateTime(2007, 3, 11, 4, 0), minus5);
        assertOffsetInfo(test, dateTime(2007, 3, 11, 5, 0), minus5);
    }

    @Test
    public void test_iqaluit() {

        // two hour overlap due to end of daylight and change of standard offset
        // Rule Canada 1987 2006 - Apr Sun>=1 2:00 1:00 D
        // Rule Canada 1974 2006 - Oct lastSun 2:00 0 S
        // Rule NT_YK 1987 2006 - Apr Sun>=1 2:00 1:00 D
        // Rule NT_YK 1980 2006 - Oct lastSun 2:00 0 S
        // -5:00 NT_YK E%sT 1999 Oct 31 2:00
        // -6:00 Canada C%sT
        TZoneOffset minus4 = TZoneOffset.ofHours(-4);
        TZoneOffset minus5 = TZoneOffset.ofHours(-5);
        TZoneOffset minus6 = TZoneOffset.ofHours(-6);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(minus5, dateTime(1999, 10, 31, 2, 0), WALL);
        b.addRuleToWindow(1987, TYear.MAX_VALUE, APRIL, 1, SUNDAY, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(1987, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(2, 0), false, WALL, PERIOD_0);
        b.addWindowForever(minus6);
        b.addRuleToWindow(1987, TYear.MAX_VALUE, APRIL, 1, SUNDAY, time(2, 0), false, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(1987, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(2, 0), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("America/Iqaluit");

        assertOffsetInfo(test, DATE_TIME_FIRST, minus5);
        assertOffsetInfo(test, DATE_TIME_LAST, minus6);

        assertOffsetInfo(test, dateTime(1999, 10, 30, 23, 0), minus4);
        assertOverlap(test, 1999, 10, 31, 0, 0, minus4, minus6);
        assertOverlap(test, 1999, 10, 31, 1, 0, minus4, minus6);
        assertOverlap(test, 1999, 10, 31, 1, 59, minus4, minus6);
        assertOffsetInfo(test, dateTime(1999, 10, 31, 2, 0), minus6);
        assertOffsetInfo(test, dateTime(1999, 10, 31, 3, 0), minus6);
    }

    @Test
    public void test_jordan2400() {

        // rule is 24:00 - this is simplified from the TZDB
        // Rule Jordan 2002 max - Mar lastThu 24:00 1:00 S
        // Rule Jordan 2002 max - Sep lastFri 0:00s 0 -
        // # Zone NAME GMTOFF RULES FORMAT [UNTIL]
        // 2:00 Jordan EE%sT
        TZoneOffset plus2 = TZoneOffset.ofHours(2);
        TZoneOffset plus3 = TZoneOffset.ofHours(3);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(plus2);
        b.addRuleToWindow(2002, TYear.MAX_VALUE, MARCH, -1, THURSDAY, time(0, 0), true, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(2002, TYear.MAX_VALUE, SEPTEMBER, -1, FRIDAY, time(0, 0), false, STANDARD, PERIOD_0);
        TZoneRules test = b.toRules("Asia/Amman");

        assertOffsetInfo(test, DATE_TIME_FIRST, plus2);
        assertOffsetInfo(test, DATE_TIME_LAST, plus2);

        assertGap(test, 2002, 3, 29, 0, 0, plus2, plus3);
        assertOffsetInfo(test, dateTime(2002, 3, 28, 23, 0), plus2);
        assertOffsetInfo(test, dateTime(2002, 3, 29, 1, 0), plus3);

        assertOverlap(test, 2002, 9, 27, 0, 0, plus3, plus2);
        assertOffsetInfo(test, dateTime(2002, 9, 26, 23, 0), plus3);
        assertOffsetInfo(test, dateTime(2002, 9, 27, 1, 0), plus2);
    }

    @Test
    public void test_japan2500() {

        // cutover time overflows into next day
        // Rule Japan 1948 only - May Sat>=1 24:00 1:00 D
        // Rule Japan 1948 1951 - Sep Sat>=8 25:00 0 S
        // Rule Japan 1949 only - Apr Sat>=1 24:00 1:00 D
        // Rule Japan 1950 1951 - May Sat>=1 24:00 1:00 D
        TZoneOffset plus9 = TZoneOffset.ofHours(9);
        TZoneOffset plus10 = TZoneOffset.ofHours(10);
        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(plus9);
        b.addRuleToWindow(1948, 1948, MAY, 1, SATURDAY, time(0, 0), true, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(1948, 1951, SEPTEMBER, 8, SATURDAY, time(1, 0), 1, WALL, PERIOD_0);
        b.addRuleToWindow(1949, 1949, APRIL, 1, SATURDAY, time(0, 0), true, WALL, PERIOD_1HOUR);
        b.addRuleToWindow(1950, 1951, MAY, 1, SATURDAY, time(0, 0), true, WALL, PERIOD_1HOUR);
        TZoneRules test = b.toRules("Japan");
        assertOffsetInfo(test, DATE_TIME_FIRST, plus9);
        assertOffsetInfo(test, DATE_TIME_LAST, plus9);
        // Sat>=1 => May 1st
        assertGap(test, 1948, 5, 2, 0, 0, plus9, plus10);
        assertOffsetInfo(test, dateTime(1948, 5, 1, 23, 0), plus9);
        assertOffsetInfo(test, dateTime(1948, 5, 2, 1, 0), plus10);
        // Sat>=8 => September 11th
        assertOverlap(test, 1948, 9, 12, 0, 0, plus10, plus9);
        assertOffsetInfo(test, dateTime(1948, 9, 11, 23, 0), plus10);
        assertOffsetInfo(test, dateTime(1948, 9, 12, 1, 0), plus9);
        // Sat>=1 => May 2nd
        assertGap(test, 1949, 4, 3, 0, 0, plus9, plus10);
        assertOffsetInfo(test, dateTime(1949, 4, 2, 23, 0), plus9);
        assertOffsetInfo(test, dateTime(1949, 4, 3, 1, 0), plus10);
    }

    @Test
    public void test_addWindow_constrainedRules() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(1800, 7, 1, 0, 0), WALL);
        b.addWindow(OFFSET_1, dateTime(2008, 6, 30, 0, 0), STANDARD);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_2_30);
        assertOffsetInfo(test, DATE_TIME_2008_01_01, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_2008_07_01, OFFSET_2_30);
        assertGap(test, 2000, 3, 26, 1, 30, OFFSET_1, OFFSET_2_30);
        assertOverlap(test, 2000, 10, 29, 0, 30, OFFSET_2_30, OFFSET_1);
        assertGap(test, 2008, 3, 30, 1, 30, OFFSET_1, OFFSET_2_30);
        assertOffsetInfo(test, dateTime(2008, 10, 26, 0, 30), OFFSET_2_30);
    }

    @Test
    public void test_addWindow_noRules() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(1800, 7, 1, 0, 0), WALL);
        b.addWindow(OFFSET_1, dateTime(2008, 6, 30, 0, 0), STANDARD);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_2008_01_01, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_2008_07_01, OFFSET_1);
    }

    @Test(expected = NullPointerException.class)
    public void test_addWindow_nullOffset() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow((TZoneOffset) null, dateTime(2008, 6, 30, 0, 0), STANDARD);
    }

    @Test(expected = NullPointerException.class)
    public void test_addWindow_nullTime() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, (TLocalDateTime) null, STANDARD);
    }

    @Test(expected = NullPointerException.class)
    public void test_addWindow_nullTimeDefinition() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(2008, 6, 30, 0, 0), (TimeDefinition) null);
    }

    @Test
    public void test_addWindowForever_noRules() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_2008_01_01, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_2008_07_01, OFFSET_1);
    }

    @Test
    public void test_addWindowForever_rules() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, OCTOBER, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_2008_01_01, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_2008_07_01, OFFSET_2_30);
        assertGap(test, 2008, 3, 30, 1, 20, OFFSET_1, OFFSET_2_30);
        assertOverlap(test, 2008, 10, 26, 0, 20, OFFSET_2_30, OFFSET_1);
    }

    @Test(expected = NullPointerException.class)
    public void test_addWindowForever_nullOffset() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever((TZoneOffset) null);
    }

    @Test
    public void test_setFixedSavingsToWindow() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindow(OFFSET_1, dateTime(1800, 7, 1, 0, 0), WALL);
        b.addWindowForever(OFFSET_1);
        b.setFixedSavingsToWindow(PERIOD_1HOUR30MIN);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_1);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_2_30);
        assertOffsetInfo(test, DATE_TIME_2008_01_01, OFFSET_2_30);
        assertOffsetInfo(test, DATE_TIME_2008_07_01, OFFSET_2_30);
        assertGap(test, 1800, 7, 1, 0, 0, OFFSET_1, OFFSET_2_30);
    }

    @Test
    public void test_setFixedSavingsToWindow_first() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.setFixedSavingsToWindow(PERIOD_1HOUR30MIN);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, DATE_TIME_FIRST, OFFSET_2_30);
        assertOffsetInfo(test, DATE_TIME_LAST, OFFSET_2_30);
    }

    @Test(expected = IllegalStateException.class)
    public void test_setFixedSavingsToWindow_noWindow() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.setFixedSavingsToWindow(PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalStateException.class)
    public void test_setFixedSavingsToWindow_cannotMixSavingsWithRule() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, 2020, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
        b.setFixedSavingsToWindow(PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalStateException.class)
    public void test_setFixedSavingsToWindow_cannotMixSavingsWithLastRule() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
        b.setFixedSavingsToWindow(PERIOD_1HOUR30MIN);
    }

    @Test
    public void test_addRuleToWindow_endOfMonth() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, 2001, MARCH, -1, SUNDAY, time(1, 0), false, UTC, PERIOD_1HOUR);
        b.addRuleToWindow(2000, 2001, OCTOBER, -1, SUNDAY, time(1, 0), false, UTC, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, dateTime(1999, 7, 1, 0, 0), OFFSET_1);

        assertOffsetInfo(test, dateTime(2000, 1, 1, 0, 0), OFFSET_1);
        assertGap(test, 2000, 3, 26, 2, 30, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, dateTime(2000, 7, 1, 0, 0), OFFSET_2);
        assertOverlap(test, 2000, 10, 29, 2, 30, OFFSET_2, OFFSET_1);

        assertOffsetInfo(test, dateTime(2001, 1, 1, 0, 0), OFFSET_1);
        assertGap(test, 2001, 3, 25, 2, 30, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, dateTime(2001, 7, 1, 0, 0), OFFSET_2);
        assertOverlap(test, 2001, 10, 28, 2, 30, OFFSET_2, OFFSET_1);

        assertOffsetInfo(test, dateTime(2002, 7, 1, 0, 0), OFFSET_1);
    }

    @Test
    public void test_addRuleToWindow_endOfMonthFeb() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2004, 2005, FEBRUARY, -1, SUNDAY, time(1, 0), false, UTC, PERIOD_1HOUR);
        b.addRuleToWindow(2004, 2005, OCTOBER, -1, SUNDAY, time(1, 0), false, UTC, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, dateTime(2003, 7, 1, 0, 0), OFFSET_1);

        assertOffsetInfo(test, dateTime(2004, 1, 1, 0, 0), OFFSET_1);
        assertGap(test, 2004, 2, 29, 2, 30, OFFSET_1, OFFSET_2); // leap
        assertOffsetInfo(test, dateTime(2004, 7, 1, 0, 0), OFFSET_2);
        assertOverlap(test, 2004, 10, 31, 2, 30, OFFSET_2, OFFSET_1);

        assertOffsetInfo(test, dateTime(2005, 1, 1, 0, 0), OFFSET_1);
        assertGap(test, 2005, 2, 27, 2, 30, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, dateTime(2005, 7, 1, 0, 0), OFFSET_2);
        assertOverlap(test, 2005, 10, 30, 2, 30, OFFSET_2, OFFSET_1);

        assertOffsetInfo(test, dateTime(2006, 7, 1, 0, 0), OFFSET_1);
    }

    @Test
    public void test_addRuleToWindow_fromDayOfMonth() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, 2001, MARCH, 10, SUNDAY, time(1, 0), false, UTC, PERIOD_1HOUR);
        b.addRuleToWindow(2000, 2001, OCTOBER, 10, SUNDAY, time(1, 0), false, UTC, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, dateTime(1999, 7, 1, 0, 0), OFFSET_1);

        assertOffsetInfo(test, dateTime(2000, 1, 1, 0, 0), OFFSET_1);
        assertGap(test, 2000, 3, 12, 2, 30, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, dateTime(2000, 7, 1, 0, 0), OFFSET_2);
        assertOverlap(test, 2000, 10, 15, 2, 30, OFFSET_2, OFFSET_1);

        assertOffsetInfo(test, dateTime(2001, 1, 1, 0, 0), OFFSET_1);
        assertGap(test, 2001, 3, 11, 2, 30, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, dateTime(2001, 7, 1, 0, 0), OFFSET_2);
        assertOverlap(test, 2001, 10, 14, 2, 30, OFFSET_2, OFFSET_1);

        assertOffsetInfo(test, dateTime(2002, 7, 1, 0, 0), OFFSET_1);
    }

    @Test(expected = IllegalStateException.class)
    public void test_addRuleToWindow_noWindow() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalStateException.class)
    public void test_addRuleToWindow_cannotMixRuleWithSavings() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.setFixedSavingsToWindow(PERIOD_1HOUR30MIN);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = TDateTimeException.class)
    public void test_addRuleToWindow_illegalYear1() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(TYear.MIN_VALUE - 1, 2008, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = TDateTimeException.class)
    public void test_addRuleToWindow_illegalYear2() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, TYear.MIN_VALUE - 1, MARCH, -1, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addRuleToWindow_illegalDayOfMonth_tooSmall() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, 2008, MARCH, -29, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addRuleToWindow_illegalDayOfMonth_zero() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, 2008, MARCH, 0, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addRuleToWindow_illegalDayOfMonth_tooLarge() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, 2008, MARCH, 32, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = NullPointerException.class)
    public void test_addRuleToWindow_nullMonth() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, (TMonth) null, 31, SUNDAY, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = NullPointerException.class)
    public void test_addRuleToWindow_nullTime() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, (TLocalTime) null, false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addRuleToWindow_illegalEndOfDayTime() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, 2008, MARCH, 1, SUNDAY, time(1, 0), true, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = NullPointerException.class)
    public void test_addRuleToWindow_nullTimeDefinition() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, TYear.MAX_VALUE, MARCH, -1, SUNDAY, time(1, 0), false, (TimeDefinition) null,
                PERIOD_1HOUR30MIN);
    }

    @Test
    public void test_addRuleToWindow_singleYearObject() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(dateTime(2000, MARCH, 26, 1, 0), UTC, PERIOD_1HOUR);
        b.addRuleToWindow(dateTime(2000, OCTOBER, 29, 1, 0), UTC, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, dateTime(1999, 7, 1, 0, 0), OFFSET_1);

        assertOffsetInfo(test, dateTime(2000, 1, 1, 0, 0), OFFSET_1);
        assertGap(test, 2000, 3, 26, 2, 30, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, dateTime(2000, 7, 1, 0, 0), OFFSET_2);
        assertOverlap(test, 2000, 10, 29, 2, 30, OFFSET_2, OFFSET_1);

        assertOffsetInfo(test, dateTime(2001, 7, 1, 0, 0), OFFSET_1);
    }

    @Test(expected = NullPointerException.class)
    public void test_addRuleToWindow_singleYearObject_nullTime() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow((TLocalDateTime) null, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = NullPointerException.class)
    public void test_addRuleToWindow_singleYearObject_nullTimeDefinition() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(dateTime(2000, MARCH, 31, 1, 0), (TimeDefinition) null, PERIOD_1HOUR30MIN);
    }

    @Test
    public void test_addRuleToWindow_singleYear() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, MARCH, 26, time(1, 0), false, UTC, PERIOD_1HOUR);
        b.addRuleToWindow(2000, OCTOBER, 29, time(1, 0), false, UTC, PERIOD_0);
        TZoneRules test = b.toRules("Europe/London");
        assertOffsetInfo(test, dateTime(1999, 7, 1, 0, 0), OFFSET_1);

        assertOffsetInfo(test, dateTime(2000, 1, 1, 0, 0), OFFSET_1);
        assertGap(test, 2000, 3, 26, 2, 30, OFFSET_1, OFFSET_2);
        assertOffsetInfo(test, dateTime(2000, 7, 1, 0, 0), OFFSET_2);
        assertOverlap(test, 2000, 10, 29, 2, 30, OFFSET_2, OFFSET_1);

        assertOffsetInfo(test, dateTime(2001, 7, 1, 0, 0), OFFSET_1);
    }

    @Test(expected = IllegalStateException.class)
    public void test_addRuleToWindow_singleYear_noWindow() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addRuleToWindow(2000, MARCH, 31, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalStateException.class)
    public void test_addRuleToWindow_singleYear_cannotMixRuleWithSavings() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.setFixedSavingsToWindow(PERIOD_1HOUR30MIN);
        b.addRuleToWindow(2000, MARCH, 31, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = TDateTimeException.class)
    public void test_addRuleToWindow_singleYear_illegalYear() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(TYear.MIN_VALUE - 1, MARCH, 31, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addRuleToWindow_singleYear_illegalDayOfMonth_tooSmall() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, MARCH, -29, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addRuleToWindow_singleYear_illegalDayOfMonth_zero() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, MARCH, 0, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addRuleToWindow_singleYear_illegalDayOfMonth_tooLarge() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, MARCH, 32, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = NullPointerException.class)
    public void test_addRuleToWindow_singleYear_nullMonth() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, (TMonth) null, 31, time(1, 0), false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = NullPointerException.class)
    public void test_addRuleToWindow_singleYear_nullTime() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, MARCH, 31, (TLocalTime) null, false, WALL, PERIOD_1HOUR30MIN);
    }

    @Test(expected = NullPointerException.class)
    public void test_addRuleToWindow_singleYear_nullTimeDefinition() {

        TZoneRulesBuilder b = new TZoneRulesBuilder();
        b.addWindowForever(OFFSET_1);
        b.addRuleToWindow(2000, MARCH, 31, time(1, 0), false, (TimeDefinition) null, PERIOD_1HOUR30MIN);
    }

    private static void assertGap(TZoneRules test, int y, int m, int d, int hr, int min, TZoneOffset before,
            TZoneOffset after) {

        TLocalDateTime dt = dateTime(y, m, d, hr, min);
        TZoneOffsetTransition zot = test.getTransition(dt);
        assertNotNull(zot);
        assertEquals(zot.isGap(), true);
        assertEquals(zot.getOffsetBefore(), before);
        assertEquals(zot.getOffsetAfter(), after);
    }

    private static void assertOverlap(TZoneRules test, int y, int m, int d, int hr, int min, TZoneOffset before,
            TZoneOffset after) {

        TLocalDateTime dt = dateTime(y, m, d, hr, min);
        TZoneOffsetTransition zot = test.getTransition(dt);
        assertNotNull(zot);
        assertEquals(zot.isOverlap(), true);
        assertEquals(zot.getOffsetBefore(), before);
        assertEquals(zot.getOffsetAfter(), after);
    }

    private void assertOffsetInfo(TZoneRules test, TLocalDateTime dateTime, TZoneOffset offset) {

        List<TZoneOffset> offsets = test.getValidOffsets(dateTime);
        assertEquals(offsets.size(), 1);
        assertEquals(offsets.get(0), offset);
    }

    private static TLocalTime time(int h, int m) {

        return TLocalTime.of(h, m);
    }

    private static TLocalDateTime dateTime(int year, int month, int day, int h, int m) {

        return TLocalDateTime.of(year, month, day, h, m);
    }

    private static TLocalDateTime dateTime(int year, TMonth month, int day, int h, int m) {

        return TLocalDateTime.of(year, month, day, h, m);
    }

}
