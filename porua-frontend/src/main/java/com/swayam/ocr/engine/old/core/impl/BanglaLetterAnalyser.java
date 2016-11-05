/*
 * IndicLetterAnalyser.java
 *
 * Created on Aug 25, 2011 10:15:10 PM
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.engine.api.Rectangle;
import com.swayam.ocr.engine.old.core.util.BinaryImage;
import com.swayam.ocr.engine.old.core.util.GeometryUtils;
import com.swayam.ocr.engine.old.core.util.PixelNeighbours;

/**
 * 
 * @author paawak
 */
public class BanglaLetterAnalyser {

    private static final Logger LOG = LoggerFactory.getLogger(BanglaLetterAnalyser.class);

    private static final int MIN_WORD_SIZE = 5;

    private static final int MIN_MATRA_WIDTH = 11;
    private static final int MIN_MATRA_HEIGHT = 3;

    /**
     * The max allowed height deviation from the widest matra
     */
    private static final int MAX_ALLOWED_MATRA_HEIGHT_DEVIATION = 0;

    private final BinaryImage binaryImage;
    private final List<Rectangle> matras;

    public BanglaLetterAnalyser(BinaryImage binaryImage) {

        this.binaryImage = binaryImage;

        LOG.info("wordWidth=" + binaryImage.getWidth() + ", wordHeight="
                + binaryImage.getHeight());

        matras = Collections.unmodifiableList(findMatras());

    }

    public List<Rectangle> getMatras() {
        return matras;
    }

    private List<Rectangle> findMatras() {

        int wordWidth = binaryImage.getWidth();
        int wordHeight = binaryImage.getHeight();
        // boolean[][] wordMatrix = binaryImage.getData();

        List<Rectangle> _matras = new ArrayList<Rectangle>();

        if (wordWidth > MIN_WORD_SIZE && wordHeight > MIN_WORD_SIZE) {

            for (int y = 1; y < wordHeight / 2; y++) {

                for (int x = 1; x < wordWidth - MIN_MATRA_WIDTH; x++) {

                    if (!binaryImage.getValueAt(x, y)
                            || GeometryUtils.isPointInside(_matras, x, y)) {
                        continue;
                    }

                    int steps = MIN_MATRA_WIDTH / 3;
                    int residue = MIN_MATRA_WIDTH % 3;

                    boolean isMatra = false;

                    for (int count = 0; count < steps; count++) {

                        PixelNeighbours neighbour = new PixelNeighbours(x + 3
                                * count, y, binaryImage);

                        int[] pixelsTrue;

                        switch (MIN_MATRA_HEIGHT) {
                        case 2:
                            pixelsTrue = new int[] { 1, 2, 3, 4, 8 };
                            break;
                        case 3:
                        default:
                            pixelsTrue = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
                            break;
                        }

                        isMatra = neighbour.checkAllTrue(pixelsTrue);

                    }

                    if (isMatra && (residue > 0)) {

                        int neighbourX = x + 3 * steps - 1;

                        PixelNeighbours neighbour = new PixelNeighbours(
                                neighbourX, y, binaryImage);

                        if (binaryImage.getValueAt(neighbourX, y)) {

                            int[] pixelsTrue;

                            switch (residue) {
                            case 1:
                                switch (MIN_MATRA_HEIGHT) {
                                case 2:
                                    pixelsTrue = new int[] { 1, 2, 8 };
                                    break;
                                case 3:
                                default:
                                    pixelsTrue = new int[] { 1, 2, 6, 7, 8 };
                                    break;
                                }
                                break;
                            case 2:
                            default:
                                switch (MIN_MATRA_HEIGHT) {
                                case 2:
                                    pixelsTrue = new int[] { 1, 2, 3, 8 };
                                    break;
                                case 3:
                                default:
                                    pixelsTrue = new int[] { 1, 2, 3, 4, 5, 6,
                                            7, 8 };
                                    break;
                                }
                                break;
                            }

                            isMatra = neighbour.checkAllTrue(pixelsTrue);

                        } else {

                            isMatra = false;

                        }

                    }

                    if (isMatra) {

                        Rectangle matra = new Rectangle(x, y, MIN_MATRA_WIDTH,
                                MIN_MATRA_HEIGHT);

                        int actualMatraWidth = findActualMatraWidth(matra);

                        matra = new Rectangle(matra.x, matra.y,
                                actualMatraWidth, MIN_MATRA_HEIGHT);

                        _matras.add(matra);

                        x += actualMatraWidth;

                    }

                }

            }

            if (_matras.size() > 1) {
                purgeFalseMatras(_matras);
            }

            // reduce all y-s by 1px
            // TODO: should be a smarter way to do this
            if (_matras.size() > 0) {

                for (int i = 0; i < _matras.size(); i++) {

                    Rectangle matra = _matras.get(i);

                    Rectangle newMatra = new Rectangle(matra.x, matra.y - 1,
                            matra.width, matra.height);

                    _matras.set(i, newMatra);

                }

            }

        } else {

            LOG.warn("The word is lesser than " + MIN_WORD_SIZE + " px");

        }

        return _matras;

    }

    /**
     * Finds the actual width of where the matra ends
     * 
     * @param matraDimension
     * @return
     */
    private int findActualMatraWidth(Rectangle matraDimension) {

        int endX = matraDimension.x + matraDimension.width;

        int wordWidth = binaryImage.getWidth();
        // boolean[][] wordMatrix = binaryImage.getData();

        for (int x = endX + 1; x < wordWidth - 1; x++) {

            if (binaryImage.getValueAt(x, matraDimension.y)) {

                PixelNeighbours neighbour = new PixelNeighbours(x,
                        matraDimension.y, binaryImage);

                int[] pixelsTrue;

                switch (MIN_MATRA_HEIGHT) {
                case 2:
                    pixelsTrue = new int[] { 1, 2, 8 };
                    break;
                case 3:
                default:
                    pixelsTrue = new int[] { 1, 2, 6, 7, 8 };
                    break;
                }

                if (neighbour.checkAllTrue(pixelsTrue)) {
                    endX = x;
                } else {
                    break;
                }

            }

        }

        return endX - matraDimension.x;

    }

    private void purgeFalseMatras(List<Rectangle> _matras) {

        Rectangle widestMatra = findWidestMatra(_matras);

        int minY = widestMatra.y - MAX_ALLOWED_MATRA_HEIGHT_DEVIATION;
        int maxY = widestMatra.y + widestMatra.height
                + MAX_ALLOWED_MATRA_HEIGHT_DEVIATION;

        Iterator<Rectangle> _matrasItr = _matras.iterator();

        while (_matrasItr.hasNext()) {

            Rectangle matra = _matrasItr.next();

            if (matra.y < minY || matra.y > maxY) {
                _matrasItr.remove();
            }

        }

    }

    private Rectangle findWidestMatra(List<Rectangle> _matras) {

        Rectangle widestMatra = _matras.get(0);

        for (int i = 1; i < _matras.size(); i++) {

            Rectangle matra = _matras.get(i);

            if (widestMatra.width < matra.width) {
                widestMatra = matra;
            }

        }

        return widestMatra;

    }

}
