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
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swayam.ocr.dict.scraper.api.TaskCompletionNotifier;
import com.swayam.ocr.dict.scraper.api.WebPageHandler;
import com.swayam.ocr.dict.scraper.api.WebScraper;

/**
 * 
 * @author paawak
 */
@Component
public class WebScraperImpl implements WebScraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebScraperImpl.class);

    private static final int REQUEST_TIMEOUT_SECONDS = 4;

    private final WebPageHandler textHandler;

    private final Executor executor;

    @Autowired
    public WebScraperImpl(Executor executor, WebPageHandler textHandler) {
        this.executor = executor;
        this.textHandler = textHandler;
    }

    @Override
    public void startScraping(Optional<String> parentUrl, String url, TaskCompletionNotifier taskCompletionNotifier) {

        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        // Start the client
        httpclient.start();

        HttpGet request = new HttpGet(url);

        // Execute request
        Future<HttpResponse> futureResponse = httpclient.execute(request, null);

        try {
            HttpResponse response = futureResponse.get(REQUEST_TIMEOUT_SECONDS,
                    TimeUnit.SECONDS);
            int statusCode = response.getStatusLine().getStatusCode();
            Header contentTypeHeader = response.getFirstHeader("content-type");

            LOGGER.info(
                    "request completed: status-code: {}, content-type: {}, url: {}",
                    statusCode, contentTypeHeader.getValue(), url);

            if ((statusCode != 200)
                    || (!contentTypeHeader.getValue().contains("text"))) {

                LOGGER.info("aborting request...");
                taskCompletionNotifier.errorInRequest();
                request.abort();

            } else {

                HttpEntity entity = response.getEntity();
                if (entity != null) {

                    String rawText = null;

                    try (InputStream instream = entity.getContent();) {

                        rawText = read(instream);

                    } catch (UnsupportedOperationException | IOException e) {
                        LOGGER.error("error reading content", e);
                    }

                    if (rawText != null) {
                        dispatchRawText(parentUrl, url, rawText, taskCompletionNotifier);
                    }

                }
            }
        } catch (InterruptedException | ExecutionException
                | TimeoutException e) {
            LOGGER.error("request timed out", e);
            taskCompletionNotifier.errorInRequest();

        } finally {
            try {
                httpclient.close();
                request.completed();
            } catch (IOException e) {
                LOGGER.error("error closing http-client", e);
            }

            // FIXME: ideally should not be there
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                LOGGER.error("error waiting", e);
            }

            taskCompletionNotifier.taskCompleted();
        }

    }

    private void dispatchRawText(Optional<String> parentUrl, String baseUrl, String rawText,
            TaskCompletionNotifier taskCompletionNotifier) {
        executor.execute(() -> {
            textHandler.handleRawText(parentUrl, baseUrl, rawText, taskCompletionNotifier);
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
