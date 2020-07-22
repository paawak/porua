package com.swayam.ocr.porua.tesseract.rest.dto;

import com.swayam.ocr.porua.tesseract.OcrWordId;

import lombok.Data;

@Data
public class OcrCorrectionDto implements OcrCorrection {

    private OcrWordId ocrWordId;

    private String correctedText;

}
