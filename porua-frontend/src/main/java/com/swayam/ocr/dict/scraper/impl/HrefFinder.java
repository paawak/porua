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
public class HrefFinder implements RawTextHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(HrefFinder.class);

    // <a href="http://hayyan.com.jo/images/index.php">
    private static final String HREF_FRAGMENT = "[\\s\\w=\"]*";
    private final Pattern linkPattern = Pattern.compile("<a" + HREF_FRAGMENT
            + "href=\"[\\w\\:\\/\\.\\-]*\"" + HREF_FRAGMENT + ">");

    @Override
    public void handleRawText(String text) {
        LOGGER.debug("{}", text);
    }

    // method added for testing only
    Pattern getLinkPattern() {
        return linkPattern;
    }

}
