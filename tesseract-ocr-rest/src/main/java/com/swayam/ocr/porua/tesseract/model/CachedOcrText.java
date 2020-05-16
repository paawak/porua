package com.swayam.ocr.porua.tesseract.model;

import java.io.Serializable;

import lombok.Value;

@Value
public class CachedOcrText implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final RawOcrWord rawOcrText;
    private final String correctText;
    private final int lineNumber;

}
