package com.swayam.ocr.core.model;

public class TextBox {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final float confidence;
    private final String text;

    public TextBox(int x, int y, int width, int height, float confidence, String text) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.confidence = confidence;
	this.text = text;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public float getConfidence() {
	return confidence;
    }

    public String getText() {
	return text;
    }

    @Override
    public String toString() {
	return "TextBox [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", confidence=" + confidence + ", text=" + text + "]";
    }

}
