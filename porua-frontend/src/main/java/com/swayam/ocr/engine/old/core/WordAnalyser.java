/*
 * WordAnalyser.java
 *
 * Created on Aug 25, 2011 10:10:31 PM
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

package com.swayam.ocr.engine.old.core;

import java.util.List;

import com.swayam.ocr.engine.old.core.util.BinaryImage;
import com.swayam.ocr.engine.old.core.util.Rectangle;

/**
 * Given an image, this analyses and breaks it down into words.
 * 
 * @author paawak
 */
public interface WordAnalyser {

    /**
     * Gets the bounds of all words it encounters in the image
     * 
     */
    List<Rectangle> getWordBoundaries();

    /**
     * Gets the partial binary pixel matrix of the given word.
     * 
     * @param wordBoundary
     *            The bounds of the word
     */
    BinaryImage getWordMatrix(Rectangle wordBoundary);

}
