package com.swayam.ocr.porua.tesseract.model;

import java.io.Serializable;

import lombok.Value;

@Value
public class RawOcrLine implements Serializable {

    private static final long serialVersionUID = 1L;

    // line number starts from 1
    private final int lineNumber;
    private final RawOcrWord rawOcrText;

}
