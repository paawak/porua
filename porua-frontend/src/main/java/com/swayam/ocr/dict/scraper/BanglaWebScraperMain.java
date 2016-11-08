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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private static final List<String> LINKS_ALREADY_VISITED = new ArrayList<>();

    public static void main(String[] args) {
        new BanglaWebScraperMain().startScraping(
                "http://www.rabindra-rachanabali.nltr.org/node/1");
    }

    private void startScraping(String url) {

        LOGGER.info("started scraping {} ...", url);

        ExecutorService executor = Executors.newCachedThreadPool();

        WebScraper webScraper = new WebScraperImpl(executor);

        AtomicBoolean banglaWordFound = new AtomicBoolean(false);

        TokenHandler banglaTokenHandler = (String token) -> {
            banglaWordFound.compareAndSet(false, true);
            LOGGER.debug(token);
        };

        List<String> hrefLinks = new ArrayList<>();

        TokenHandler linkTokenHandler = (String href) -> {
            LOGGER.debug("href, {}", href);

            if (!LINKS_ALREADY_VISITED.contains(href)) {
                hrefLinks.add(href);
                LINKS_ALREADY_VISITED.add(href);
            }

        };

        webScraper.addTextHandler(new HrefFinder(executor, linkTokenHandler));
        webScraper.addTextHandler(
                new BanglaWordFinder(executor, banglaTokenHandler));

        webScraper.startScraping(url, () -> {

            LOGGER.info("scraping completed for {}", url);

            if (banglaWordFound.get()) {
                hrefLinks.forEach((String href) -> {

                    new BanglaWebScraperMain().startScraping(href);

                });
            } else {
                LOGGER.info("ignoring all hrefs for {} as no Bangla word found",
                        url);
            }

        });

    }

}
