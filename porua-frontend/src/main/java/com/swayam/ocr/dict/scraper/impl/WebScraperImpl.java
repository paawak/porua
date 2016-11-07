/*
 * WebScraperImpl.java
 *
 * Created on 06-Nov-2016 10:36:05 PM
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.dict.scraper.api.RawTextHandler;
import com.swayam.ocr.dict.scraper.api.WebScraper;

/**
 * 
 * @author paawak
 */
public class WebScraperImpl implements WebScraper {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(WebScraperImpl.class);

    private final List<RawTextHandler> textHandlers;

    private final Executor executor;

    public WebScraperImpl(Executor executor) {
        this.executor = executor;
        textHandlers = new ArrayList<>();
    }

    @Override
    public void startScraping(String url) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        // Start the client
        httpclient.start();

        HttpGet request = new HttpGet(url);

        // Execute request
        httpclient.execute(request, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse response) {
                LOGGER.info("request completed");
                int statusCode = response.getStatusLine().getStatusCode();
                Header contentTypeHeader = response
                        .getFirstHeader("content-type");

                LOGGER.info("status-code: {}, content-type: {}", statusCode,
                        contentTypeHeader.getValue());

                if ((statusCode != 200)
                        || (!contentTypeHeader.getValue().contains("text"))) {

                    LOGGER.info("aborting request...");
                    request.abort();

                } else {

                    HttpEntity entity = response.getEntity();
                    if (entity != null) {

                        String rawText = null;

                        try (InputStream instream = entity.getContent();) {

                            rawText = read(instream);

                        } catch (UnsupportedOperationException
                                | IOException e) {
                            LOGGER.error("error reading content", e);
                        }

                        if (rawText != null) {
                            dispatchRawText(url, rawText);
                        }

                    }
                }

                close();
            }

            @Override
            public void cancelled() {
                LOGGER.info("request cancelled");
                close();
            }

            @Override
            public void failed(Exception e) {
                LOGGER.error("request failed", e);
                close();
            }

            private void close() {
                try {
                    httpclient.close();
                    request.completed();
                } catch (IOException e) {
                    LOGGER.error("error closing http-client", e);
                }
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("error", e);
        }
    }

    @Override
    public void addTextHandler(RawTextHandler textHandler) {
        textHandlers.add(textHandler);
    }

    private void dispatchRawText(String baseUrl, String rawText) {
        textHandlers.forEach((RawTextHandler textHandler) -> {
            executor.execute(() -> {
                textHandler.handleRawText(baseUrl, rawText);
            });
        });
    }

    private String read(InputStream instream) throws IOException {

        Reader reader = new InputStreamReader(instream, "utf8");

        StringBuilder sb = new StringBuilder(10_000);

        int c;
        while (true) {
            c = reader.read();

            if (c == -1) {
                return sb.toString();
            }

            sb.append((char) c);
        }

    }

}
