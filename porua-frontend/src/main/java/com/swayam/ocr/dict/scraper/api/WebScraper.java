/*
 * BanglaWebScraper.java
 *
 * Created on 06-Nov-2016 5:41:14 PM
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
public interface WebScraper {

    void startScraping(String url,
            TaskCompletionNotifier taskCompletionNotifier);

}
