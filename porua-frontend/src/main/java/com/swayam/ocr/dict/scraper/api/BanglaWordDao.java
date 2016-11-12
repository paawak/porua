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

/**
 * 
 * @author paawak
 */
public interface BanglaWordDao {

    int saveUrl(String url);

    void insertBanglaWord(int urlId, String token);

}
