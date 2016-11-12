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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.swayam.ocr.config.SpringConfig;
import com.swayam.ocr.dict.scraper.api.WebScraper;

/**
 * 
 * @author paawak
 */
public class BanglaWebScraperMain {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BanglaWebScraperMain.class);

    public static void main(String[] args) {
        new BanglaWebScraperMain().startScraping(
                "http://www.rabindra-rachanabali.nltr.org/node/1");
    }

    private void startScraping(String url) {

        LOGGER.info("started scraping {} ...", url);

        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        WebScraper webScraper = ctx.getBean(WebScraper.class);

        webScraper.startScraping(url, () -> {

            LOGGER.info("scraping completed for {}", url);

        });

    }

}
