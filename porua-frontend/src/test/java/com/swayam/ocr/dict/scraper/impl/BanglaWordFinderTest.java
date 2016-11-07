/*
 * BanglaWordFinderTest.java
 *
 * Created on 07-Nov-2016 10:27:57 PM
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * 
 * @author paawak
 */
public class BanglaWordFinderTest {

    @Test
    public void testBanglaWordPattern() {
        // given
        String input = "rabindra,rabindranath,gitanjali,rabindra sangeet,রবীন্দ্র,রচনাবলী,রবীন্দ্র-রচনাবলী,অবতরণিকা,উপন্যাস,গল্প,নাটক,গান,কবিতা,প্রবন্ধ,অচলিত সংগ্রহ,দুই বোন,বউ-ঠাকুরানীর হাট,চোখের বালি ঘরে বাইরে,প্রজাপতির নির্বন্ধ,চতুরঙ্গ চার অধ্যায়,যোগাযোগ,মালঞ্চ রাজর্ষি,গোরা,শেষের কবিতা,নৌকাডুবি,রবীন্দ্রনাথ ,রবিরচনা, Braille, Bangla Braille, Bengali Braille, Indian Language Braille, Bharati Braille, Braille Transliteration, Technology for Blinds, Technology for Cerebral Palsy and Speech Impaired, Sanyog, Aakash Bani, Sparsha, Speech Enabled Baishakhi Keyboard, Web Browser for Blinds, Sweepsticks, Assistive Technology";

        List<String> expected = Arrays.asList("রবীন্দ্র", "রচনাবলী", "রবীন্দ্র",
                "রচনাবলী", "অবতরণিকা", "উপন্যাস", "গল্প", "নাটক", "গান",
                "কবিতা", "প্রবন্ধ", "অচলিত", "সংগ্রহ", "দুই", "বোন", "বউ",
                "ঠাকুরানীর", "হাট", "চোখের", "বালি", "ঘরে", "বাইরে",
                "প্রজাপতির", "নির্বন্ধ", "চতুরঙ্গ", "চার", "অধ্যায়", "যোগাযোগ",
                "মালঞ্চ", "রাজর্ষি", "গোরা", "শেষের", "কবিতা", "নৌকাডুবি",
                "রবীন্দ্রনাথ", "রবিরচনা");

        BanglaWordFinder testClass = new BanglaWordFinder(null, null);
        Pattern testPattern = testClass.getRegex();

        // when
        Matcher matcher = testPattern.matcher(input);

        // then
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            String word = matcher.group();
            result.add(word);
        }

        assertEquals(expected, result);
    }

}
