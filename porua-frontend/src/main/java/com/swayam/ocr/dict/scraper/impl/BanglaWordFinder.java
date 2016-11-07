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
public class BanglaWordFinder extends AbstractTokenHandler {

    // exclude numbers
    private final Pattern banglaWordPattern = Pattern
            .compile("[\u0980-\u09e5\u09f0-\u09ff]{2,}");

    public BanglaWordFinder(Executor executor, TokenHandler tokenHandler) {
        super(executor, tokenHandler);
    }

    @Override
    public Pattern getRegex() {
        return banglaWordPattern;
    }

    @Override
    protected String processRawToken(String baseUrl, String rawToken) {
        return rawToken;
    }

}
