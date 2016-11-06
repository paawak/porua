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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

/**
 * 
 * @author paawak
 */
public class LinkHandlerTest {

    @Test
    public void testLinkPattern_1() {
        // given
        LinkHandler testClass = new LinkHandler();
        Pattern linkPattern = testClass.getLinkPattern();

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
        LinkHandler testClass = new LinkHandler();
        Pattern linkPattern = testClass.getLinkPattern();

        // when
        boolean result = linkPattern
                .matcher(
                        "<a href=\"http://hayyan.com.jo/images/index.php\">bbbbbbbbbbbbbbbbbb<a href=\"http://hayyan.com.jo/images/index.php\">")
                .matches();

        // then
        assertFalse(result);

    }

}
