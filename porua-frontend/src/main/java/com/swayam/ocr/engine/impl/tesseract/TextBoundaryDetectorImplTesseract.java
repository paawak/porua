/*
 * TextBoundaryDetectorImplTesseract.java
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

package com.swayam.ocr.engine.impl.tesseract;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract;
import org.bytedeco.javacpp.tesseract.ResultIterator;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.engine.api.Rectangle;
import com.swayam.ocr.engine.api.TextBoundaryDetector;

/**
 * 
 * @author paawak
 */
public class TextBoundaryDetectorImplTesseract implements TextBoundaryDetector {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TextBoundaryDetectorImplTesseract.class);

    private final String tessdata;

    // TODO language should be an enum
    private final String language;

    private final File image;

    public TextBoundaryDetectorImplTesseract(String tessdata, String language,
            File image) {
        this.tessdata = tessdata;
        this.language = language;
        this.image = image;
    }

    @Override
    public List<Rectangle> getTextBoundaries() {

        List<Rectangle> bounds = new ArrayList<>();

        PIX tessImage = pixRead(image.getAbsolutePath());

        TessBaseAPI api = new TessBaseAPI();
        if (api.Init(tessdata, language) != 0) {
            try {
                api.close();
            } catch (Exception e) {
                LOGGER.warn("error closing the tesseract API", e);
            }
            throw new RuntimeException("Could not initialize tesseract.");
        }

        api.SetImage(tessImage);
        api.Recognize(null);

        ResultIterator ri = api.GetIterator();
        int level = tesseract.RIL_WORD;

        while (ri.Next(level)) {
            BytePointer word = ri.GetUTF8Text(level);
            String ocrText = word.getString().trim();
            float conf = ri.Confidence(level);
            IntBuffer x1buffer = IntBuffer.allocate(50);
            IntBuffer y1buffer = IntBuffer.allocate(50);
            IntBuffer x2buffer = IntBuffer.allocate(50);
            IntBuffer y2buffer = IntBuffer.allocate(50);

            ri.BoundingBox(level, x1buffer, y1buffer, x2buffer, y2buffer);

            int x1 = x1buffer.get();
            int y1 = y1buffer.get();
            int x2 = x2buffer.get();
            int y2 = y2buffer.get();

            LOGGER.debug(String.format(
                    "word: '%s';  \tconf: %.2f; BoundingBox: %d,%d,%d,%d;%n",
                    ocrText, conf, x1, y1, x2, y2));

            Rectangle rect = new Rectangle(x1, y1, x2 - x1, y2 - y1);
            bounds.add(rect);

        }

        // Destroy used object and release memory
        api.End();
        try {
            api.close();
        } catch (Exception e) {
            LOGGER.warn("error closing the tesseract API", e);
        }
        pixDestroy(tessImage);

        return bounds;
    }

}
