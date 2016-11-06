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

import com.swayam.ocr.dict.scraper.api.RawTextHandler;

/**
 * 
 * @author paawak
 */
public class LinkHandler implements RawTextHandler {

    // <a href="http://hayyan.com.jo/images/index.php">
    private final Pattern linkPattern = Pattern
            .compile("<a[\\s\\w]*href=\"[\\w\\:\\/\\.]*\">");

    @Override
    public void handleRawText(String text) {
        // TODO Auto-generated method stub

    }

    // method added for testing only
    Pattern getLinkPattern() {
        return linkPattern;
    }

}
