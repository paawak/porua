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
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.concurrent.CountDownLatch;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;

/**
 * 
 * @author paawak
 */
public class HttpClientTest {

    private static final String BANGLA_URL = "http://www.rabindra-rachanabali.nltr.org/node/1";

    @Test
    public void test_1() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        // Start the client
        httpclient.start();

        // Execute request
        httpclient.execute(new HttpGet(BANGLA_URL),
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

    @Test
    public void test_2() throws InterruptedException {

        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        // Start the client
        httpclient.start();

        final CountDownLatch latch2 = new CountDownLatch(1);
        final HttpGet request3 = new HttpGet(BANGLA_URL);
        HttpAsyncRequestProducer producer3 = HttpAsyncMethods.create(request3);
        AsyncCharConsumer<HttpResponse> consumer3 = new AsyncCharConsumer<HttpResponse>() {

            @Override
            protected void onCharReceived(CharBuffer buf, IOControl ioctrl)
                    throws IOException {

                StringBuilder sb = new StringBuilder();

                while (buf.hasRemaining()) {
                    sb.append(buf.get());
                }

                System.out.println("**** " + sb.toString());

            }

            @Override
            protected void onResponseReceived(HttpResponse response)
                    throws HttpException, IOException {

                System.out.println("111111111111111111");

                if (response.getStatusLine().getStatusCode() == 200) {
                    Header header = response.getFirstHeader("content-type");
                    System.out.println("content-type: " + header.getValue());
                }
            }

            @Override
            protected HttpResponse buildResult(HttpContext context)
                    throws Exception {
                // TODO Auto-generated method stub
                return null;
            }

        };
        httpclient.execute(producer3, consumer3,
                new FutureCallback<HttpResponse>() {

                    @Override
                    public void completed(final HttpResponse response3) {

                        System.out.println("***********************");

                        latch2.countDown();
                    }

                    @Override
                    public void failed(final Exception ex) {
                        System.out
                                .println(request3.getRequestLine() + "->" + ex);

                        latch2.countDown();
                    }

                    @Override
                    public void cancelled() {
                        System.out.println(
                                request3.getRequestLine() + " cancelled");
                        latch2.countDown();
                    }

                });
        latch2.await();
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
