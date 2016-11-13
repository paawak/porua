/*
 * BanglaWordDao.java
 *
 * Created on 12-Nov-2016 11:20:59 PM
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

package com.swayam.ocr.dict.scraper.api;

import java.util.Optional;

import com.swayam.ocr.dict.scraper.impl.AuditWebsite;

/**
 * 
 * @author paawak
 */
public interface BanglaWordDao {

    int saveUrl(int parentId, String url);

    void insertBanglaWord(String token);

    AuditWebsite getAuditWebsite(String url);

    boolean doesUrlExist(String url);

    void markScrapingCompleted(int baseUrlId);

    Optional<String> getNextUrlForScrapping();

    void markErrorInScrapping(String url);

}
