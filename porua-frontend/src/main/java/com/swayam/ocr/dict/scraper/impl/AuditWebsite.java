/*
 * AuditWebsite.java
 *
 * Created on 13-Nov-2016 11:05:14 AM
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

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 
 * @author paawak
 */
public class AuditWebsite {

    private final int id;

    private final Optional<Integer> parentId;

    private final String name;

    private final LocalDateTime scrapingStarted;

    private final boolean scrapingCompleted;

    public AuditWebsite(int id, Optional<Integer> parentId, String name, LocalDateTime scrapingStarted,
            boolean scrapingCompleted) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.scrapingStarted = scrapingStarted;
        this.scrapingCompleted = scrapingCompleted;
    }

    public int getId() {
        return id;
    }

    public Optional<Integer> getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getScrapingStarted() {
        return scrapingStarted;
    }

    public boolean isScrapingCompleted() {
        return scrapingCompleted;
    }

    @Override
    public String toString() {
        return "AuditWebsite [id=" + id + ", parentId=" + parentId + ", name=" + name + ", scrapingStarted="
                + scrapingStarted + ", scrapingCompleted=" + scrapingCompleted + "]";
    }

}
