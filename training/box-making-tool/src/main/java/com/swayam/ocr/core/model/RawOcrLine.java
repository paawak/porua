package com.swayam.ocr.core.model;

import java.io.Serializable;

public class RawOcrLine implements Serializable {

    private static final long serialVersionUID = 1L;

    // line number starts from 1
    public final int lineNumber;
    public final RawOcrWord rawOcrText;

    public RawOcrLine(int lineNumber, RawOcrWord rawOcrText) {
	this.lineNumber = lineNumber;
	this.rawOcrText = rawOcrText;
    }

    @Override
    public String toString() {
	return "RawOcrLine [lineNumber=" + lineNumber + ", rawOcrText=" + rawOcrText + "]";
    }

}
