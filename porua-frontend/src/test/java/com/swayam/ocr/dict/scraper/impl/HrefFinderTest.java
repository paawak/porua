/*
 * LinkHandlerTest.java
 *
 * Created on 06-Nov-2016 11:20:59 PM
 *
 * Copyright (c) 2002 - 2008 : Swayam Inc.
 *
 * P R O P R I E T A R Y & C O N F I D E N T I A L
 *
 * The copyright of this document is vested in Swayam Inc. without
 * whose prior written permission its contents must not be published,
 * adapted or reproduced in any form or disclosed or
 * issued to any third party.
 */

package com.swayam.ocr.dict.scraper.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author paawak
 */
public class HrefFinderTest {

    private String sampleHtmlContents;

    @Before
    public void readSampleHtml() throws IOException {

        try (Reader reader = new InputStreamReader(
                HrefFinderTest.class.getResourceAsStream(
                        "/com/swayam/ocr/test/res/sample-html.txt"),
                "utf8");) {

            StringBuilder sb = new StringBuilder();

            int c;
            while (true) {
                c = reader.read();

                if (c == -1) {
                    break;
                }

                sb.append((char) c);
            }

            sampleHtmlContents = sb.toString();
        }

    }

    @Test
    public void testLinkPattern_1() {
        // given
        HrefFinder testClass = new HrefFinder(null, null);
        Pattern linkPattern = testClass.getRegex();

        // when
        boolean result = linkPattern
                .matcher("<a href=\"http://hayyan.com.jo/images/index.php\">")
                .matches();

        // then
        assertTrue(result);

    }

    @Test
    public void testLinkPattern_2() {
        // given
        HrefFinder testClass = new HrefFinder(null, null);
        Pattern linkPattern = testClass.getRegex();

        // when
        boolean result = linkPattern
                .matcher(
                        "<a href=\"http://hayyan.com.jo/images/index.php\">bbbbbbbbbbbbbbbbbb<a href=\"http://hayyan.com.jo/images/index.php\">")
                .matches();

        // then
        assertFalse(result);

    }

    @Test
    public void testLinkPattern_3() throws IOException {
        // given
        List<String> expectedLinks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                HrefFinderTest.class.getResourceAsStream(
                        "/com/swayam/ocr/test/res/urls-in-sample-html.txt"),
                "utf8"));) {
            while (true) {
                String link = reader.readLine();
                if (link == null) {
                    break;
                }

                expectedLinks.add(link);
            }

        }

        List<String> result = new ArrayList<>();

        HrefFinder testClass = new HrefFinder(null, null);
        Pattern linkPattern = testClass.getRegex();

        // when
        Matcher matcher = linkPattern.matcher(sampleHtmlContents);
        while (matcher.find()) {
            String link = matcher.group();
            System.out.println("**** " + link);
            result.add(link);
        }

        // then
        assertEquals(expectedLinks, result);
    }

    @Test
    public void testProcessRawToken_1() {
        // given
        HrefFinder testClass = new HrefFinder(null, null);

        // when
        String result = testClass.processRawToken(null,
                "<a href=\"http://www.nltr.org/download/windows/Baishakhi_2.0.0.42_x86_x64.exe\" target=\"_self\">");

        // then
        assertEquals(
                "http://www.nltr.org/download/windows/Baishakhi_2.0.0.42_x86_x64.exe",
                result);

    }

    @Test
    public void testProcessRawToken_2() {
        // given
        HrefFinder testClass = new HrefFinder(null, null);

        // when
        String result = testClass.processRawToken(null,
                "<a href=\"http://hayyan.com.jo/images/index.php\">");

        // then
        assertEquals("http://hayyan.com.jo/images/index.php", result);

    }

    @Test
    public void testProcessRawToken_3() {
        // given
        HrefFinder testClass = new HrefFinder(null, null);

        // when
        String result = testClass.processRawToken("http://hayyan.com.jo",
                "<a href=\"/node/16141\">");

        // then
        assertEquals("http://hayyan.com.jo/node/16141", result);

    }

}
