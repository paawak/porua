/*
 * LinkHandler.java
 *
 * Created on 06-Nov-2016 5:42:25 PM
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

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.dict.scraper.api.RawTextHandler;

/**
 * 
 * @author paawak
 */
public class BanglaWordFinder implements RawTextHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BanglaWordFinder.class);

    private final Pattern banglaWordPattern = Pattern
            .compile("[\u0980-\u09ff]+");

    @Override
    public void handleRawText(String text) {
        LOGGER.debug("{}", text);
    }

    // method added for testing only
    Pattern getBanglaWordPattern() {
        return banglaWordPattern;
    }

}
