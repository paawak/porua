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

import java.util.concurrent.Executor;
import java.util.regex.Pattern;

import com.swayam.ocr.dict.scraper.api.TokenHandler;

/**
 * 
 * @author paawak
 */
public class HrefFinder extends AbstractTokenHandler {

    // <a href="http://hayyan.com.jo/images/index.php">
    private static final String HREF_FRAGMENT = "[\\s\\w=\"]*";
    private final Pattern linkPattern = Pattern.compile("<a" + HREF_FRAGMENT
            + "href=\"[\\w\\:\\/\\.\\-]*\"" + HREF_FRAGMENT + ">");

    public HrefFinder(Executor executor, TokenHandler tokenHandler) {
        super(executor, tokenHandler);
    }

    @Override
    public Pattern getRegex() {
        return linkPattern;
    }

    @Override
    protected String processRawToken(String baseUrl, String rawToken) {
        String hrefWithClosingQuotes = rawToken.split("href=\"")[1];
        String href = hrefWithClosingQuotes.split("\"")[0];

        if (href.startsWith("http://")) {
            return href;
        }

        return baseUrl + href;

    }

}
