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
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.DataProvider;
import org.junit.Test;
import org.teavm.classlib.java.time.TZoneId;
import org.teavm.classlib.java.time.TZoneOffset;
import org.teavm.classlib.java.time.format.TDateTimeFormatterBuilder.ZoneIdPrinterParser;
import org.teavm.classlib.java.time.temporal.TTemporalQueries;
import org.teavm.classlib.java.time.zone.TZoneRulesProvider;

@Test
public class TestZoneIdParser extends AbstractTestPrinterParser {

    private static final String AMERICA_DENVER = "America/Denver";
    private static final TZoneId TIME_ZONE_DENVER = TZoneId.of(AMERICA_DENVER);

    //-----------------------------------------------------------------------
    @DataProvider(name="error")
    Object[][] data_error() {
        return new Object[][] {
            {new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null), "hello", -1, IndexOutOfBoundsException.class},
            {new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null), "hello", 6, IndexOutOfBoundsException.class},
        };
    }

    @Test(dataProvider="error")
    public void test_parse_error(ZoneIdPrinterParser pp, String text, int pos, Class<?> expected) {
        try {
            pp.parse(parseContext, text, pos);
        } catch (RuntimeException ex) {
            assertTrue(expected.isInstance(ex));
            assertEquals(parseContext.toParsed().fieldValues.size(), 0);
        }
    }

    //-----------------------------------------------------------------------
    public void test_parse_exactMatch_Denver() throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        int result = pp.parse(parseContext, AMERICA_DENVER, 0);
        assertEquals(result, AMERICA_DENVER.length());
        assertParsed(TIME_ZONE_DENVER);
    }

    public void test_parse_startStringMatch_Denver() throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        int result = pp.parse(parseContext, AMERICA_DENVER + "OTHER", 0);
        assertEquals(result, AMERICA_DENVER.length());
        assertParsed(TIME_ZONE_DENVER);
    }

    public void test_parse_midStringMatch_Denver() throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        int result = pp.parse(parseContext, "OTHER" + AMERICA_DENVER + "OTHER", 5);
        assertEquals(result, 5 + AMERICA_DENVER.length());
        assertParsed(TIME_ZONE_DENVER);
    }

    public void test_parse_endStringMatch_Denver() throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        int result = pp.parse(parseContext, "OTHER" + AMERICA_DENVER, 5);
        assertEquals(result, 5+ AMERICA_DENVER.length());
        assertParsed(TIME_ZONE_DENVER);
    }

    public void test_parse_partialMatch() throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        int result = pp.parse(parseContext, "OTHERAmerica/Bogusville", 5);
        assertEquals(result, -6);
        assertParsed(null);
    }

    //-----------------------------------------------------------------------
    @DataProvider(name="zones")
    Object[][] populateTestData() {
        Set<String> ids = TZoneRulesProvider.getAvailableZoneIds();
        Object[][] rtnval = new Object[ids.size()][];
        int i = 0;
        for (String id : ids) {
            rtnval[i++] = new Object[] { id, TZoneId.of(id) };
        }
        return rtnval;
    }

    @Test(dataProvider="zones")
    public void test_parse_exactMatch(String parse, TZoneId expected) throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        int result = pp.parse(parseContext, parse, 0);
        assertEquals(result, parse.length());
        assertParsed(expected);
    }

    @Test
    public void test_parse_lowerCase() throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        parseContext.setCaseSensitive(false);
        int result = pp.parse(parseContext, "europe/london", 0);
        assertEquals(result, 13);
        assertParsed(TZoneId.of("Europe/London"));
    }

    //-----------------------------------------------------------------------
    public void test_parse_endStringMatch_utc() throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        int result = pp.parse(parseContext, "OTHERZ", 5);
        assertEquals(result, 6);
        assertParsed(TZoneOffset.UTC);
    }

    public void test_parse_endStringMatch_utc_plus1() throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        int result = pp.parse(parseContext, "OTHER+01:00", 5);
        assertEquals(result, 11);
        assertParsed(TZoneId.of("+01:00"));
    }

    //-----------------------------------------------------------------------
    public void test_parse_midStringMatch_utc() throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        int result = pp.parse(parseContext, "OTHERZOTHER", 5);
        assertEquals(result, 6);
        assertParsed(TZoneOffset.UTC);
    }

    public void test_parse_midStringMatch_utc_plus1() throws Exception {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), null);
        int result = pp.parse(parseContext, "OTHER+01:00OTHER", 5);
        assertEquals(result, 11);
        assertParsed(TZoneId.of("+01:00"));
    }

    //-----------------------------------------------------------------------
    public void test_toString_id() {
        ZoneIdPrinterParser pp = new ZoneIdPrinterParser(TTemporalQueries.zoneId(), "TZoneId()");
        assertEquals(pp.toString(), "TZoneId()");
    }

    private void assertParsed(TZoneId expectedZone) {
        assertEquals(parseContext.toParsed().zone, expectedZone);
    }

}
