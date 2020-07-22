package com.swayam.ocr.porua.tesseract.rest.dto;

import com.swayam.ocr.porua.tesseract.OcrWordId;

public interface OcrCorrection {

    OcrWordId getOcrWordId();

    void setOcrWordId(OcrWordId ocrWordId);

    String getCorrectedText();

    void setCorrectedText(String correctedText);

}