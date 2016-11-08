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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
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
        new BanglaWebScraperMain().startScraping(
                Executors.newCachedThreadPool(),
                "http://www.rabindra-rachanabali.nltr.org/node/1");
    }

    private void startScraping(ExecutorService executor, String url) {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        WebScraper webScraper = new WebScraperImpl(executor);

        TokenHandler banglaTokenHandler = (String token) -> {
            LOGGER.debug(token);
        };

        List<String> hrefLinks = new ArrayList<>();

        TokenHandler linkTokenHandler = (String href) -> {
            hrefLinks.add(href);
        };

        webScraper.addTextHandler(new HrefFinder(executor, linkTokenHandler));
        webScraper.addTextHandler(
                new BanglaWordFinder(executor, banglaTokenHandler));

        webScraper.startScraping(url, () -> {
            executor.shutdown();
            countDownLatch.countDown();
            hrefLinks.forEach((String href) -> {
                new BanglaWebScraperMain()
                        .startScraping(Executors.newCachedThreadPool(), href);
            });
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("error waiting", e);
        }
    }

}
