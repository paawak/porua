/*
 * BanglaWebScraperMain.java
 *
 * Created on 06-Nov-2016 11:07:30 PM
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

package com.swayam.ocr.dict.scraper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.dict.scraper.api.TokenHandler;
import com.swayam.ocr.dict.scraper.api.WebScraper;
import com.swayam.ocr.dict.scraper.impl.BanglaWordFinder;
import com.swayam.ocr.dict.scraper.impl.HrefFinder;
import com.swayam.ocr.dict.scraper.impl.WebScraperImpl;

/**
 * 
 * @author paawak
 */
public class BanglaWebScraperMain {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BanglaWebScraperMain.class);

    public static void main(String[] args) {

        Executor executor = Executors.newCachedThreadPool();

        WebScraper webScraper = new WebScraperImpl(executor);

        TokenHandler tokenHandler = (String token) -> {
            LOGGER.debug(token);
        };

        webScraper.addTextHandler(new HrefFinder(executor, tokenHandler));
        webScraper.addTextHandler(new BanglaWordFinder(executor, tokenHandler));

        webScraper.startScraping(
                "http://www.rabindra-rachanabali.nltr.org/node/1");

    }

}
