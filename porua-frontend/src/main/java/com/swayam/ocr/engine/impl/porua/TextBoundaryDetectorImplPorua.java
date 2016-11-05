/*
 * TextBoundaryDetectorImplPorua.java
 *
 * Created on 05-Nov-2016 10:09:31 PM
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

package com.swayam.ocr.engine.impl.porua;

import java.util.List;

import com.swayam.ocr.engine.api.Rectangle;
import com.swayam.ocr.engine.api.TextBoundaryDetector;
import com.swayam.ocr.engine.old.core.WordAnalyser;
import com.swayam.ocr.engine.old.core.impl.LeftToRightWordAnalyser;
import com.swayam.ocr.engine.old.core.util.BinaryImage;

/**
 * 
 * @author paawak
 */
public class TextBoundaryDetectorImplPorua implements TextBoundaryDetector {

    private final BinaryImage binaryImage;

    public TextBoundaryDetectorImplPorua(BinaryImage binaryImage) {
        this.binaryImage = binaryImage;
    }

    @Override
    public List<Rectangle> getTextBoundaries() {
        WordAnalyser wordAnalyser = new LeftToRightWordAnalyser(binaryImage);
        return wordAnalyser.getWordBoundaries();
    }

}
