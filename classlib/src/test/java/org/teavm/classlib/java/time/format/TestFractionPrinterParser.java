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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.teavm.classlib.java.time.temporal.TChronoField.NANO_OF_SECOND;
import static org.teavm.classlib.java.time.temporal.TChronoField.SECOND_OF_MINUTE;

import java.util.Locale;

import org.junit.Test;
import org.teavm.classlib.java.time.TDateTimeException;
import org.teavm.classlib.java.time.TLocalDateTime;
import org.teavm.classlib.java.time.TLocalTime;
import org.teavm.classlib.java.time.TZoneId;
import org.teavm.classlib.java.time.TZonedDateTime;
import org.teavm.classlib.java.time.chrono.TIsoChronology;
import org.teavm.classlib.java.time.format.TDateTimeFormatterBuilder.FractionPrinterParser;
import org.teavm.classlib.java.time.temporal.MockFieldValue;
import org.teavm.classlib.java.time.temporal.TTemporalField;

public class TestFractionPrinterParser extends AbstractTestPrinterParser {

    @Test(expected = TDateTimeException.class)
    public void test_print_emptyCalendrical() {

        FractionPrinterParser pp = new FractionPrinterParser(NANO_OF_SECOND, 0, 9, true);
        pp.print(this.printEmptyContext, this.buf);
    }

    public void test_print_append() {

        this.printContext.setDateTime(TLocalTime.of(12, 30, 40, 3));
        FractionPrinterParser pp = new FractionPrinterParser(NANO_OF_SECOND, 0, 9, true);
        this.buf.append("EXISTING");
        pp.print(this.printContext, this.buf);
        assertEquals(this.buf.toString(), "EXISTING.000000003");
    }

    Object[][] provider_nanos() {

        return new Object[][] { { 0, 9, 0, "" }, { 0, 9, 2, ".000000002" }, { 0, 9, 20, ".00000002" },
        { 0, 9, 200, ".0000002" }, { 0, 9, 2000, ".000002" }, { 0, 9, 20000, ".00002" }, { 0, 9, 200000, ".0002" },
        { 0, 9, 2000000, ".002" }, { 0, 9, 20000000, ".02" }, { 0, 9, 200000000, ".2" }, { 0, 9, 1, ".000000001" },
        { 0, 9, 12, ".000000012" }, { 0, 9, 123, ".000000123" }, { 0, 9, 1234, ".000001234" },
        { 0, 9, 12345, ".000012345" }, { 0, 9, 123456, ".000123456" }, { 0, 9, 1234567, ".001234567" },
        { 0, 9, 12345678, ".012345678" }, { 0, 9, 123456789, ".123456789" },

        { 1, 9, 0, ".0" }, { 1, 9, 2, ".000000002" }, { 1, 9, 20, ".00000002" }, { 1, 9, 200, ".0000002" },
        { 1, 9, 2000, ".000002" }, { 1, 9, 20000, ".00002" }, { 1, 9, 200000, ".0002" }, { 1, 9, 2000000, ".002" },
        { 1, 9, 20000000, ".02" }, { 1, 9, 200000000, ".2" },

        { 2, 3, 0, ".00" }, { 2, 3, 2, ".000" }, { 2, 3, 20, ".000" }, { 2, 3, 200, ".000" }, { 2, 3, 2000, ".000" },
        { 2, 3, 20000, ".000" }, { 2, 3, 200000, ".000" }, { 2, 3, 2000000, ".002" }, { 2, 3, 20000000, ".02" },
        { 2, 3, 200000000, ".20" }, { 2, 3, 1, ".000" }, { 2, 3, 12, ".000" }, { 2, 3, 123, ".000" },
        { 2, 3, 1234, ".000" }, { 2, 3, 12345, ".000" }, { 2, 3, 123456, ".000" }, { 2, 3, 1234567, ".001" },
        { 2, 3, 12345678, ".012" }, { 2, 3, 123456789, ".123" },

        { 6, 6, 0, ".000000" }, { 6, 6, 2, ".000000" }, { 6, 6, 20, ".000000" }, { 6, 6, 200, ".000000" },
        { 6, 6, 2000, ".000002" }, { 6, 6, 20000, ".000020" }, { 6, 6, 200000, ".000200" },
        { 6, 6, 2000000, ".002000" }, { 6, 6, 20000000, ".020000" }, { 6, 6, 200000000, ".200000" },
        { 6, 6, 1, ".000000" }, { 6, 6, 12, ".000000" }, { 6, 6, 123, ".000000" }, { 6, 6, 1234, ".000001" },
        { 6, 6, 12345, ".000012" }, { 6, 6, 123456, ".000123" }, { 6, 6, 1234567, ".001234" },
        { 6, 6, 12345678, ".012345" }, { 6, 6, 123456789, ".123456" }, };
    }

    @Test
    public void test_print_nanos() {

        for (Object[] data : provider_nanos()) {
            int minWidth = (int) data[0];
            int maxWidth = (int) data[1];
            int value = (int) data[2];
            String result = (String) data[3];

            StringBuilder sb = new StringBuilder();
            this.printContext.setDateTime(new MockFieldValue(NANO_OF_SECOND, value));
            FractionPrinterParser pp = new FractionPrinterParser(NANO_OF_SECOND, minWidth, maxWidth, true);
            pp.print(this.printContext, sb);
            if (result == null) {
                fail("Expected exception");
            }
            assertEquals(sb.toString(), result);
        }
    }

    @Test
    public void test_print_nanos_noDecimalPoint() {

        for (Object[] data : provider_nanos()) {
            int minWidth = (int) data[0];
            int maxWidth = (int) data[1];
            int value = (int) data[2];
            String result = (String) data[3];

            StringBuilder sb = new StringBuilder();
            this.printContext.setDateTime(new MockFieldValue(NANO_OF_SECOND, value));
            FractionPrinterParser pp = new FractionPrinterParser(NANO_OF_SECOND, minWidth, maxWidth, false);
            pp.print(this.printContext, sb);
            if (result == null) {
                fail("Expected exception");
            }
            assertEquals(sb.toString(), (result.startsWith(".") ? result.substring(1) : result));
        }
    }

    Object[][] provider_seconds() {

        return new Object[][] { { 0, 9, 0, "" }, { 0, 9, 3, ".05" }, { 0, 9, 6, ".1" }, { 0, 9, 9, ".15" },
        { 0, 9, 12, ".2" }, { 0, 9, 15, ".25" }, { 0, 9, 30, ".5" }, { 0, 9, 45, ".75" },

        { 2, 2, 0, ".00" }, { 2, 2, 3, ".05" }, { 2, 2, 6, ".10" }, { 2, 2, 9, ".15" }, { 2, 2, 12, ".20" },
        { 2, 2, 15, ".25" }, { 2, 2, 30, ".50" }, { 2, 2, 45, ".75" }, };
    }

    @Test
    public void test_print_seconds() {

        TZonedDateTime zdt = TLocalDateTime.of(2011, 6, 30, 12, 30, 40, 0).atZone(TZoneId.of("Europe/Paris"));
        for (Object[] data : provider_seconds()) {
            int minWidth = (int) data[0];
            int maxWidth = (int) data[1];
            int value = (int) data[2];
            String result = (String) data[3];

            StringBuilder sb = new StringBuilder();
            this.printContext = new TDateTimePrintContext(zdt, Locale.ENGLISH, TDecimalStyle.STANDARD);
            this.printContext.setDateTime(new MockFieldValue(SECOND_OF_MINUTE, value));
            FractionPrinterParser pp = new FractionPrinterParser(SECOND_OF_MINUTE, minWidth, maxWidth, true);
            pp.print(this.printContext, sb);
            if (result == null) {
                fail("Expected exception");
            }
            assertEquals(sb.toString(), result);
        }
    }

    @Test
    public void test_print_seconds_noDecimalPoint() {

        TZonedDateTime zdt = TLocalDateTime.of(2011, 6, 30, 12, 30, 40, 0).atZone(TZoneId.of("Europe/Paris"));
        for (Object[] data : provider_seconds()) {
            int minWidth = (int) data[0];
            int maxWidth = (int) data[1];
            int value = (int) data[2];
            String result = (String) data[3];

            StringBuilder sb = new StringBuilder();
            this.printContext = new TDateTimePrintContext(zdt, Locale.ENGLISH, TDecimalStyle.STANDARD);
            this.printContext.setDateTime(new MockFieldValue(SECOND_OF_MINUTE, value));
            FractionPrinterParser pp = new FractionPrinterParser(SECOND_OF_MINUTE, minWidth, maxWidth, false);
            pp.print(this.printContext, sb);
            if (result == null) {
                fail("Expected exception");
            }
            assertEquals(sb.toString(), (result.startsWith(".") ? result.substring(1) : result));
        }
    }

    @Test
    public void test_reverseParse() {

        for (Object[] data : provider_nanos()) {
            int minWidth = (int) data[0];
            int maxWidth = (int) data[1];
            int value = (int) data[2];
            String result = (String) data[3];

            this.parseContext = new TDateTimeParseContext(Locale.ENGLISH, TDecimalStyle.STANDARD,
                    TIsoChronology.INSTANCE);
            FractionPrinterParser pp = new FractionPrinterParser(NANO_OF_SECOND, minWidth, maxWidth, true);
            int newPos = pp.parse(this.parseContext, result, 0);
            assertEquals(newPos, result.length());
            int expectedValue = fixParsedValue(maxWidth, value);
            assertParsed(this.parseContext, NANO_OF_SECOND, value == 0 && minWidth == 0 ? null : (long) expectedValue);
        }
    }

    @Test
    public void test_reverseParse_noDecimalPoint() {

        for (Object[] data : provider_nanos()) {
            int minWidth = (int) data[0];
            int maxWidth = (int) data[1];
            int value = (int) data[2];
            String result = (String) data[3];

            this.parseContext = new TDateTimeParseContext(Locale.ENGLISH, TDecimalStyle.STANDARD,
                    TIsoChronology.INSTANCE);
            FractionPrinterParser pp = new FractionPrinterParser(NANO_OF_SECOND, minWidth, maxWidth, false);
            int newPos = pp.parse(this.parseContext, result, (result.startsWith(".") ? 1 : 0));
            assertEquals(newPos, result.length());
            int expectedValue = fixParsedValue(maxWidth, value);
            assertParsed(this.parseContext, NANO_OF_SECOND, value == 0 && minWidth == 0 ? null : (long) expectedValue);
        }
    }

    @Test
    public void test_reverseParse_followedByNonDigit() {

        for (Object[] data : provider_nanos()) {
            int minWidth = (int) data[0];
            int maxWidth = (int) data[1];
            int value = (int) data[2];
            String result = (String) data[3];

            this.parseContext = new TDateTimeParseContext(Locale.ENGLISH, TDecimalStyle.STANDARD,
                    TIsoChronology.INSTANCE);
            FractionPrinterParser pp = new FractionPrinterParser(NANO_OF_SECOND, minWidth, maxWidth, true);
            int newPos = pp.parse(this.parseContext, result + " ", 0);
            assertEquals(newPos, result.length());
            int expectedValue = fixParsedValue(maxWidth, value);
            assertParsed(this.parseContext, NANO_OF_SECOND, value == 0 && minWidth == 0 ? null : (long) expectedValue);
        }
    }

    @Test
    public void test_reverseParse_preceededByNonDigit() {

        for (Object[] data : provider_nanos()) {
            int minWidth = (int) data[0];
            int maxWidth = (int) data[1];
            int value = (int) data[2];
            String result = (String) data[3];

            this.parseContext = new TDateTimeParseContext(Locale.ENGLISH, TDecimalStyle.STANDARD,
                    TIsoChronology.INSTANCE);
            FractionPrinterParser pp = new FractionPrinterParser(NANO_OF_SECOND, minWidth, maxWidth, true);
            int newPos = pp.parse(this.parseContext, " " + result, 1);
            assertEquals(newPos, result.length() + 1);
            int expectedValue = fixParsedValue(maxWidth, value);
            assertParsed(this.parseContext, NANO_OF_SECOND, value == 0 && minWidth == 0 ? null : (long) expectedValue);
        }
    }

    private int fixParsedValue(int maxWidth, int value) {

        if (maxWidth < 9) {
            int power = (int) Math.pow(10, (9 - maxWidth));
            value = (value / power) * power;
        }
        return value;
    }

    @Test
    public void test_reverseParse_seconds() {

        for (Object[] data : provider_seconds()) {
            int minWidth = (int) data[0];
            int maxWidth = (int) data[1];
            int value = (int) data[2];
            String result = (String) data[3];

            this.parseContext = new TDateTimeParseContext(Locale.ENGLISH, TDecimalStyle.STANDARD,
                    TIsoChronology.INSTANCE);
            FractionPrinterParser pp = new FractionPrinterParser(SECOND_OF_MINUTE, minWidth, maxWidth, true);
            int newPos = pp.parse(this.parseContext, result, 0);
            assertEquals(newPos, result.length());
            assertParsed(this.parseContext, SECOND_OF_MINUTE, value == 0 && minWidth == 0 ? null : (long) value);
        }
    }

    private void assertParsed(TDateTimeParseContext context, TTemporalField field, Long value) {

        if (value == null) {
            assertEquals(context.getParsed(field), null);
        } else {
            assertEquals(context.getParsed(field), value);
        }
    }

    Object[][] provider_parseNothing() {

        return new Object[][] { { new FractionPrinterParser(NANO_OF_SECOND, 3, 6, true), "", 0, ~0 },
        { new FractionPrinterParser(NANO_OF_SECOND, 3, 6, true), "A", 0, ~0 },
        { new FractionPrinterParser(NANO_OF_SECOND, 3, 6, true), ".", 0, ~1 },
        { new FractionPrinterParser(NANO_OF_SECOND, 3, 6, true), ".5", 0, ~1 },
        { new FractionPrinterParser(NANO_OF_SECOND, 3, 6, true), ".51", 0, ~1 },
        { new FractionPrinterParser(NANO_OF_SECOND, 3, 6, true), ".A23456", 0, ~1 },
        { new FractionPrinterParser(NANO_OF_SECOND, 3, 6, true), ".1A3456", 0, ~1 }, };
    }

    @Test
    public void test_parse_nothing() {

        for (Object[] data : provider_parseNothing()) {
            FractionPrinterParser pp = (FractionPrinterParser) data[0];
            String text = (String) data[1];
            int pos = (int) data[2];
            int expected = (int) data[3];

            int newPos = pp.parse(this.parseContext, text, pos);
            assertEquals(newPos, expected);
            assertEquals(this.parseContext.getParsed(NANO_OF_SECOND), null);
        }
    }

    public void test_toString() {

        FractionPrinterParser pp = new FractionPrinterParser(NANO_OF_SECOND, 3, 6, true);
        assertEquals(pp.toString(), "Fraction(NanoOfSecond,3,6,DecimalPoint)");
    }

    public void test_toString_noDecimalPoint() {

        FractionPrinterParser pp = new FractionPrinterParser(NANO_OF_SECOND, 3, 6, false);
        assertEquals(pp.toString(), "Fraction(NanoOfSecond,3,6)");
    }

}
