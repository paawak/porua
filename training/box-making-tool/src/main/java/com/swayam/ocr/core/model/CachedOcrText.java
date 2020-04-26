package com.swayam.ocr.core.model;

import java.io.Serializable;

public class CachedOcrText implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int id;
    public final RawOcrWord rawOcrText;
    public final String correctText;

    public CachedOcrText(int id, RawOcrWord rawOcrText, String correctText) {
	this.id = id;
	this.rawOcrText = rawOcrText;
	this.correctText = correctText;
    }

    @Override
    public String toString() {
	return "CachedOcrText [id=" + id + ", rawOcrText=" + rawOcrText + ", correctText=" + correctText + "]";
    }

}
