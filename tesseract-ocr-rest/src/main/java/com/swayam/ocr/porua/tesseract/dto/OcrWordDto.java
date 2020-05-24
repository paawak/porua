package com.swayam.ocr.porua.tesseract.dto;

import lombok.Value;

@Value
public class OcrWordDto {

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final float confidence;
    private final String text;
    private final int wordSequenceNumber;

}
