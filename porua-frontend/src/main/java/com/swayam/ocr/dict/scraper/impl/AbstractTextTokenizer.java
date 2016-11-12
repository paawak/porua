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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.dict.scraper.api.TextTokenizer;

/**
 * 
 * @author paawak
 */
public abstract class AbstractTextTokenizer implements TextTokenizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTextTokenizer.class);

    @Override
    public Set<String> tokenize(String baseUrl, String text) {
        LOGGER.trace("text: {}", text);

        Set<String> tokens = new HashSet<>();

        Matcher matcher = getRegex().matcher(text);

        while (matcher.find()) {
            String rawToken = matcher.group();
            String token = processRawToken(baseUrl, rawToken);
            LOGGER.debug("token: {}", token);
            tokens.add(token);
        }

        return tokens;

    }

    protected abstract Pattern getRegex();

    protected abstract String processRawToken(String baseUrl, String rawToken);

}
