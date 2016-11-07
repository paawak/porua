/*
 * TokenHandler.java
 *
 * Created on 07-Nov-2016 11:18:04 PM
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.dict.scraper.api.RawTextHandler;
import com.swayam.ocr.dict.scraper.api.TokenHandler;

/**
 * 
 * @author paawak
 */
public abstract class AbstractTokenHandler implements RawTextHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractTokenHandler.class);

    private final Executor executor;
    private final TokenHandler tokenHandler;

    public AbstractTokenHandler(Executor executor, TokenHandler tokenHandler) {
        this.executor = executor;
        this.tokenHandler = tokenHandler;
    }

    @Override
    public void handleRawText(String baseUrl, String text) {
        LOGGER.trace("{}", text);

        Matcher matcher = getRegex().matcher(text);

        while (matcher.find()) {
            String rawToken = matcher.group();
            String token = processRawToken(baseUrl, rawToken);
            notifyTokenFound(token);
        }

    }

    protected abstract Pattern getRegex();

    protected abstract String processRawToken(String baseUrl, String rawToken);

    private void notifyTokenFound(String token) {
        executor.execute(() -> {
            tokenHandler.handleToken(token);
        });
    }

}
