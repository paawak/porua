package com.swayam.ocr.core.model;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;

public class RawOcrWord implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int x1;
    public final int y1;
    public final int x2;
    public final int y2;
    public final float confidence;
    public final String text;
    public final int wordSequenceNumber;

    public RawOcrWord(int x1, int y1, int x2, int y2, float confidence, String text, int wordSequenceNumber) {
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;
	this.confidence = confidence;
	this.text = text;
	this.wordSequenceNumber = wordSequenceNumber;
    }

    public Rectangle getRectangle() {
	return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    public Color getColorCodedConfidence() {
	float red = (100 - confidence) / 100;
	float green = confidence / 100;
	return new Color(red, green, 0);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Float.floatToIntBits(confidence);
	result = prime * result + ((text == null) ? 0 : text.hashCode());
	result = prime * result + wordSequenceNumber;
	result = prime * result + x1;
	result = prime * result + x2;
	result = prime * result + y1;
	result = prime * result + y2;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	RawOcrWord other = (RawOcrWord) obj;
	if (Float.floatToIntBits(confidence) != Float.floatToIntBits(other.confidence))
	    return false;
	if (text == null) {
	    if (other.text != null)
		return false;
	} else if (!text.equals(other.text))
	    return false;
	if (wordSequenceNumber != other.wordSequenceNumber)
	    return false;
	if (x1 != other.x1)
	    return false;
	if (x2 != other.x2)
	    return false;
	if (y1 != other.y1)
	    return false;
	if (y2 != other.y2)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "RawOcrWord [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", confidence=" + confidence + ", text=" + text + ", wordSequenceNumber=" + wordSequenceNumber + "]";
    }

}
