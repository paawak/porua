/*
 * ImageTransforms.java
 *
 * Created on Aug 4, 2011 9:05:22 PM
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.engine.old.core.WordAnalyser;
import com.swayam.ocr.engine.old.core.util.BinaryImage;
import com.swayam.ocr.engine.old.core.util.GeometryUtils;
import com.swayam.ocr.engine.old.core.util.Rectangle;

/**
 * 
 * Breaks down a given image having lines of text (left to right) into words
 * 
 * @author paawak
 */
public class LeftToRightWordAnalyser implements WordAnalyser {

    private static final Logger LOG = LoggerFactory.getLogger(LeftToRightWordAnalyser.class);

    /**
     * Length of the a side of the next square to compare
     */
    private static final int NEXT_SQUARE_LENGTH = 2;

    private static final int INITIAL_LENGTH = 10;

    /**
     * Minimum no. of white cells that should be present for it to qualify as a
     * glyph
     */
    private static final int MINIMUM_WHITE_COUNT = 20;

    private final BinaryImage binaryImage;

    private final List<Rectangle> wordBoundaries;

    public LeftToRightWordAnalyser(BinaryImage binaryImage) {

        this.binaryImage = binaryImage;

        LOG.info("width=" + binaryImage.getWidth() + ", height="
                + binaryImage.getHeight());

        wordBoundaries = new ArrayList<Rectangle>();

        computeWordBoundaries();

    }

    @Override
    public List<Rectangle> getWordBoundaries() {
        return wordBoundaries;
    }

    @Override
    public BinaryImage getWordMatrix(Rectangle wordBoundary) {

        return binaryImage.getSubImage(wordBoundary);

    }

    private void computeWordBoundaries() {

        int imageWidth = binaryImage.getWidth();
        int imageHeight = binaryImage.getHeight();

        for (int y = 0; y < imageHeight; y++) {

            for (int x = 0; x < imageWidth; x++) {

                if (GeometryUtils.isPointInside(wordBoundaries, x, y)) {
                    continue;
                }

                if (binaryImage.getValueAt(x, y)) {

                    Rectangle area = analyseWhiteNeighbour(x, y,
                            INITIAL_LENGTH, INITIAL_LENGTH, true);

                    if (area != null) {

                        x += area.width;

                    }

                }

            }

        }

        LOG.trace("WordBoundaries: " + wordBoundaries);

    }

    private Rectangle analyseWhiteNeighbour(final int startX, final int startY,
            int width, int height, boolean countWhites) {

        int imageWidth = binaryImage.getWidth();
        int imageHeight = binaryImage.getHeight();

        Rectangle area = null;

        if (!countWhites
                || countWhitesInSquare(startX, startY, width, height) >= MINIMUM_WHITE_COUNT) {

            if (intersectsX(startX, startY, width)) {

                int nextY = Math.max(startY - NEXT_SQUARE_LENGTH, 0);

                if (startY == nextY) {

                    area = null;

                } else {

                    area = analyseWhiteNeighbour(startX, nextY, width, height,
                            false);

                }

            } else if (intersectsY(startX, startY, height)) {

                int nextX = Math.max(startX - NEXT_SQUARE_LENGTH, 0);

                if (startX == nextX) {

                    area = null;

                } else {

                    area = analyseWhiteNeighbour(nextX, startY, width, height,
                            false);

                }

            } else if (intersectsX(startX,
                    Math.min(startY + height, imageHeight - 1), width)) {

                // the bottom horizontal line of the area intersects as white...
                // increase the height

                int nextHeight = Math.min(height + NEXT_SQUARE_LENGTH,
                        imageHeight);

                if (height == nextHeight) {

                    area = null;

                } else {

                    area = analyseWhiteNeighbour(startX, startY, width,
                            nextHeight, false);

                }

            } else if (intersectsY(Math.min(startX + width, imageWidth - 1),
                    startY, height)) {

                // the left verticaal line of the area intersects as white...
                // increase the width

                int nextWidth = Math
                        .min(width + NEXT_SQUARE_LENGTH, imageWidth);

                if (width == nextWidth) {

                    area = null;

                } else {

                    area = analyseWhiteNeighbour(startX, startY, nextWidth,
                            height, false);

                }

            } else {

                area = new Rectangle(startX, startY, width, height);

                mergeNearestRectangleAndAdd(area);

                if (LOG.isTraceEnabled()) {
                    StringBuilder sb = new StringBuilder("Found Area: ");
                    sb.append(startX).append(",").append(startY).append(",")
                            .append(width).append(",").append(height);
                    LOG.trace(sb.toString());
                }

            }

        }

        return area;

    }

    private int countWhitesInSquare(final int startX, final int startY,
            int width, int height) {

        int imageWidth = binaryImage.getWidth();
        int imageHeight = binaryImage.getHeight();

        int endX = Math.min(startX + width, imageWidth);

        int endY = Math.min(startY + height, imageHeight);

        int whiteCount = 0;

        for (int x = startX; x < endX; x++) {

            for (int y = startY; y < endY; y++) {

                if (binaryImage.getValueAt(x, y)) {
                    whiteCount++;
                }

            }

        }

        return whiteCount;

    }

    private boolean intersectsX(final int startX, final int startY, int width) {

        int imageWidth = binaryImage.getWidth();

        boolean intersects = false;

        int endX = Math.min(startX + width, imageWidth - 1);

        for (int x = startX + 1; x < endX; x++) {

            if (binaryImage.getValueAt(x, startY)) {
                intersects = true;
                break;
            }

        }

        return intersects;

    }

    private boolean intersectsY(final int startX, final int startY, int height) {

        int imageHeight = binaryImage.getHeight();

        boolean intersects = false;

        int endY = Math.min(startY + height, imageHeight - 1);

        for (int y = startY + 1; y < endY; y++) {

            if (binaryImage.getValueAt(startX, y)) {
                intersects = true;
                break;
            }

        }

        return intersects;

    }

    private void mergeNearestRectangleAndAdd(final Rectangle newArea) {

        Rectangle mergedArea = null;

        Iterator<Rectangle> itr = wordBoundaries.iterator();

        while (itr.hasNext()) {

            Rectangle area = itr.next();

            boolean nearMatch = (Math.abs(area.x - newArea.x) <= NEXT_SQUARE_LENGTH)
                    && (Math.abs(area.y - newArea.y) <= NEXT_SQUARE_LENGTH);

            if (nearMatch) {

                mergedArea = new Rectangle(Math.min(area.x, newArea.x),
                        Math.min(area.y, newArea.y), Math.max(area.width,
                                newArea.width),
                        Math.max(area.height,
                                newArea.height));

                itr.remove();

                break;

            }

        }

        if (mergedArea == null) {
            mergedArea = newArea;
        }

        wordBoundaries.add(mergedArea);

    }

}
