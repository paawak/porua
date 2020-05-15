package com.swayam.ocr.porua.tesseract.model;

import java.io.Serializable;

public class CachedOcrText implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int id;
    public final RawOcrWord rawOcrText;
    public final String correctText;
    public final int lineNumber;

    public CachedOcrText(int id, RawOcrWord rawOcrText, String correctText, int lineNumber) {
	this.id = id;
	this.rawOcrText = rawOcrText;
	this.correctText = correctText;
	this.lineNumber = lineNumber;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((correctText == null) ? 0 : correctText.hashCode());
	result = prime * result + id;
	result = prime * result + lineNumber;
	result = prime * result + ((rawOcrText == null) ? 0 : rawOcrText.hashCode());
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
	CachedOcrText other = (CachedOcrText) obj;
	if (correctText == null) {
	    if (other.correctText != null)
		return false;
	} else if (!correctText.equals(other.correctText))
	    return false;
	if (id != other.id)
	    return false;
	if (lineNumber != other.lineNumber)
	    return false;
	if (rawOcrText == null) {
	    if (other.rawOcrText != null)
		return false;
	} else if (!rawOcrText.equals(other.rawOcrText))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "CachedOcrText [id=" + id + ", rawOcrText=" + rawOcrText + ", correctText=" + correctText + ", lineNumber=" + lineNumber + "]";
    }

}
