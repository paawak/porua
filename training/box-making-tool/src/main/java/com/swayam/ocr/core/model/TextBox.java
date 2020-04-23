package com.swayam.ocr.core.model;

public class TextBox {

    public final int x1;
    public final int y1;
    public final int x2;
    public final int y2;
    public final float confidence;
    public final String text;

    public TextBox(int x1, int y1, int x2, int y2, float confidence, String text) {
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;
	this.confidence = confidence;
	this.text = text;
    }

    @Override
    public String toString() {
	return "TextBox [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", confidence=" + confidence + ", text=" + text + "]";
    }

}
