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

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.swayam.ocr.config.SpringConfig;
import com.swayam.ocr.dict.scraper.api.BanglaWordDao;
import com.swayam.ocr.dict.scraper.api.TaskCompletionNotifier;
import com.swayam.ocr.dict.scraper.api.WebScraper;

/**
 * 
 * @author paawak
 */
public class BanglaWebScraperMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(BanglaWebScraperMain.class);

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        WebScraper webScraper = ctx.getBean(WebScraper.class);

        Executor executor = ctx.getBean(Executor.class);

        BanglaWordDao banglaWordDao = ctx.getBean(BanglaWordDao.class);

        new BanglaWebScraperMain().startScraping(executor, banglaWordDao, webScraper, Optional.<String> empty(),
                "http://www.rabindra-rachanabali.nltr.org/node/1");
    }

    private void startScraping(Executor executor, BanglaWordDao banglaWordDao, WebScraper webScraper,
            Optional<String> parentUrl,
            String url) {

        LOGGER.info("started scraping {} ...", url);

        webScraper.startScraping(parentUrl, url, new TaskCompletionNotifier() {

            private final CountDownLatch countDownLatch = new CountDownLatch(1);

            @Override
            public void taskCompleted() {

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    LOGGER.error("", e);
                }

                executor.execute(() -> {
                    LOGGER.info("getting the next url for scrapping");

                    Optional<String> nextUrl = banglaWordDao.getNextUrlForScrapping();

                    if (nextUrl.isPresent()) {
                        startScraping(executor, banglaWordDao, webScraper, Optional.of(url), nextUrl.get());
                    }
                });

            }

            @Override
            public void linksInsertedIntoDataStore() {
                countDownLatch.countDown();
            }

            @Override
            public void errorInRequest(String url) {
                banglaWordDao.markErrorInScrapping(url);
                countDownLatch.countDown();
            }
        });

    }

}
