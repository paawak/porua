/*
 * LetterAnalyzer.java
 *
 * Created on Aug 25, 2011 10:09:01 PM
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

/**
 * Given a word in an image, this analyses and breaks it down into its constituent letters.
 * 
 * @author paawak
 */
public interface LetterAnalyser {

    /**
     * 
     * Gets the binary image matrix of the individual letter
     * 
     * @param wordMatrix
     *            Binary pixel matrix of the given word
     * @return
     */
    List<boolean[][]> getLetterBoundaries(boolean[][] wordMatrix);

}
