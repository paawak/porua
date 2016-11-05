/*
 * ImageSearcher.java
 *
 * Created on Nov 19, 2011 12:14:27 PM
 *
 * Copyright (c) 2002 - 2011 : Swayam Inc.
 *
 * P R O P R I E T A R Y & C O N F I D E N T I A L
 *
 * The copyright of this document is vested in Swayam Inc. without
 * whose prior written permission its contents must not be published,
 * adapted or reproduced in any form or disclosed or
 * issued to any third party.
 */

package com.swayam.ocr.engine.old.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.engine.old.core.util.BinaryImage;
import com.swayam.ocr.engine.old.core.util.Rectangle;

/**
 * 
 * @author paawak
 */
public class ImageSearcher {

    private static final Logger LOG = LoggerFactory.getLogger(ImageSearcher.class);

    private static final int DEVIATION_TOLERANCE_PERCENT = 10;

    /**
     * Searches a `needle` in a `haystack`
     * 
     * @param needle
     *            The image to be searched
     * @param haystack
     *            The image in which to be searched
     */
    public void search(BinaryImage needle, BinaryImage haystack) {

        if (needle.getWidth() < haystack.getWidth()
                && needle.getHeight() < haystack.getHeight()) {

            // move horizontally
            for (int x = 0; (x < haystack.getWidth() && (x + needle.getWidth() < haystack
                    .getWidth())); x++) {

                for (int y = 0; (y < haystack.getHeight() && (y
                        + needle.getHeight() < haystack.getHeight())); y++) {

                    BinaryImage haystackSnapShot = haystack
                            .getSubImage(new Rectangle(x, y, needle.getWidth(),
                                    needle.getHeight()));

                    if (compareSameSizeImages(needle, haystackSnapShot)) {
                        System.err.println("Match found at: " + x + ", " + y);
                        x += needle.getWidth();
                        y += needle.getHeight();
                    }

                }

            }
        } else {

            LOG.warn("The dimensions of the `needle` image should be lesser than the `haystack`");

        }

    }

    private boolean compareSameSizeImages(BinaryImage needle,
            BinaryImage haystackSnapShot) {

        if (needle.getWidth() != haystackSnapShot.getWidth()
                || needle.getHeight() != haystackSnapShot.getHeight()) {
            throw new IllegalArgumentException(
                    "The two images should have the same dimensions");
        }

        int deviationCount = 0;

        for (int x = 0; x < needle.getWidth(); x++) {
            for (int y = 0; y < needle.getHeight(); y++) {

                if (needle.getValueAt(x, y)
                        && (needle.getValueAt(x, y) ^ haystackSnapShot
                                .getValueAt(x, y))) {
                    deviationCount++;
                }

            }
        }

        int deviationPercent = getDeviationPercent(needle, deviationCount);

        // System.err.println("deviationPercent = " + deviationPercent);

        return deviationPercent <= DEVIATION_TOLERANCE_PERCENT;

    }

    private int getDeviationPercent(BinaryImage image, int deviationCount) {

        int totalInterestPoints = 0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                if (image.getValueAt(x, y)) {
                    totalInterestPoints++;
                }

            }
        }

        int deviationPercent = 0;

        if (totalInterestPoints != 0) {

            deviationPercent = (deviationCount * 100) / totalInterestPoints;
        }

        return deviationPercent;

    }

    // private boolean matchByJOpenSurf(BinaryImage image1, BinaryImage image2)
    // {
    //
    // return new Surf(image1.getImage()).isEquivalentTo(new Surf(image2
    // .getImage()));
    //
    // }

}
