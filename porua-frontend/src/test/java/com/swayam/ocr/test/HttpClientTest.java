/*
 * HttpClientTest.java
 *
 * Created on 06-Nov-2016 5:57:10 PM
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

package com.swayam.ocr.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.Test;

/**
 * 
 * @author paawak
 */
public class HttpClientTest {

    @Test
    public void test() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        // Start the client
        httpclient.start();

        // Execute request
        httpclient.execute(
                new HttpGet("http://www.rabindra-rachanabali.nltr.org/node/1"),
                new FutureCallback<HttpResponse>() {

                    @Override
                    public void cancelled() {
                        countDownLatch.countDown();
                    }

                    @Override
                    public void completed(HttpResponse response) {

                        if (response.getStatusLine().getStatusCode() == 200) {
                            Header header = response
                                    .getFirstHeader("content-type");
                            System.out.println(
                                    "content-type: " + header.getValue());

                            if (header.getValue().contains("text")) {

                                HttpEntity entity = response.getEntity();
                                if (entity != null) {

                                    try (InputStream instream = entity
                                            .getContent();) {

                                        System.out.println(
                                                "content:\n" + read(instream));

                                    } catch (UnsupportedOperationException
                                            | IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }

                        countDownLatch.countDown();

                    }

                    @Override
                    public void failed(Exception e) {
                        countDownLatch.countDown();
                    }
                });

        countDownLatch.await();

    }

    private String read(InputStream instream) throws IOException {

        InputStreamReader reader = new InputStreamReader(instream, "utf8");

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
