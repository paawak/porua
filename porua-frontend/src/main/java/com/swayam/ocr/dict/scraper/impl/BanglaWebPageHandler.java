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

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swayam.ocr.dict.scraper.api.BanglaWordDao;
import com.swayam.ocr.dict.scraper.api.TaskCompletionNotifier;
import com.swayam.ocr.dict.scraper.api.WebPageHandler;

/**
 * 
 * @author paawak
 */
@Component
public class BanglaWebPageHandler implements WebPageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BanglaWebPageHandler.class);

    private static final int MIN_DAYS_BEFORE_RE_SCRAPING = 10;

    private final BanglaWordDao banglaWordDao;
    private final BanglaWordFinder banglaWordFinder;
    private final HrefFinder hrefFinder;

    @Autowired
    public BanglaWebPageHandler(BanglaWordDao banglaWordDao, BanglaWordFinder banglaWordFinder, HrefFinder hrefFinder) {
        this.banglaWordDao = banglaWordDao;
        this.banglaWordFinder = banglaWordFinder;
        this.hrefFinder = hrefFinder;
    }

    @Override
    public void handleRawText(Optional<String> parentUrl, String baseUrl, String text,
            TaskCompletionNotifier taskCompletionNotifier) {

        int parentUrlId;

        if (parentUrl.isPresent()) {
            parentUrlId = banglaWordDao.getAuditWebsite(parentUrl.get()).getId();
        } else {
            parentUrlId = -1;
        }

        int baseUrlId;

        if (banglaWordDao.doesUrlExist(baseUrl)) {
            AuditWebsite auditWebsite = banglaWordDao.getAuditWebsite(baseUrl);
            Period period = Period.between(LocalDate.now(), auditWebsite.getScrapingStarted().toLocalDate());
            if (auditWebsite.isScrapingCompleted() && period.getDays() < MIN_DAYS_BEFORE_RE_SCRAPING) {
                taskCompletionNotifier.linksInsertedIntoDataStore();
                return;
            } else {
                baseUrlId = auditWebsite.getId();
            }
        } else {
            baseUrlId = banglaWordDao.saveUrl(parentUrlId, baseUrl);
        }

        Set<String> banglaWords = banglaWordFinder.tokenize(baseUrl, text);

        banglaWords.forEach((String banglaWord) -> {
            try {
                banglaWordDao.insertBanglaWord(banglaWord);
            } catch (Exception e) {
                LOGGER.warn("could not insert {}: {}", banglaWord, e.getMessage());
            }
        });

        if (!banglaWords.isEmpty()) {
            Set<String> banglaLinks = hrefFinder.tokenize(baseUrl, text);
            banglaLinks.stream().filter((String link) -> {
                return !banglaWordDao.doesUrlExist(link);
            }).forEach((String banglaLink) -> {
                banglaWordDao.saveUrl(baseUrlId, banglaLink);
            });

        }

        banglaWordDao.markScrapingCompleted(baseUrlId);

        taskCompletionNotifier.linksInsertedIntoDataStore();

    }

}
