package com.swayam.ocr.porua.tesseract.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.swayam.ocr.porua.tesseract.OcrWordId;
import com.swayam.ocr.porua.tesseract.rest.dto.OcrCorrection;

import lombok.Data;

@Entity
@Table(name = "ocr_word")
@Data
public class OcrWord implements OcrCorrection {

    @EmbeddedId
    private OcrWordId ocrWordId;

    @Column(name = "raw_text")
    private String rawText;

    @Column(name = "corrected_text")
    private String correctedText;

    @Column
    private int x1;

    @Column
    private int y1;

    @Column
    private int x2;

    @Column
    private int y2;

    @Column
    private float confidence;

    @Column(name = "line_number")
    private Integer lineNumber;

    @Column
    private boolean ignored;

}
