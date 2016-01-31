/*
 * Glyph.java
 *
 * Created on Oct 19, 2011 7:44:11 PM
 *
 * Copyright (c) 2002 - 2011 : Swayam Inc.
 *
 * P R O P R I E T A R Y & C O N F I D E N T I A L
 *
 * The copyright of this document is vested in Swayam Inc. without
 * whose prior written permission its contents must not be published,
 * adapted or reproduced in any form or disclosed or
 * issued to any third party.
 */

package com.swayam.ocr.core.util;

import java.io.Serializable;

/**
 * 
 * @author paawak
 */
public class Glyph implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final int id;

    private final Typeface typeface;

    private final String unicodeText;

    private final String description;

    private final BinaryImage image;

    public Glyph(int id, Typeface typeface, String unicodeText,
            BinaryImage image, String description) {
    	this.id = id;
        this.typeface = typeface;
        this.unicodeText = unicodeText;
        this.description = description;
        this.image = image;
    }

    public Glyph(int id, Typeface typeface, String unicodeText, BinaryImage image) {
        this(id, typeface, unicodeText, image, null);
    }

    public int getId() {
		return id;
	}

	public Typeface getTypeface() {
		return typeface;
	}

	public String getUnicodeText() {
        return unicodeText;
    }

    public String getDescription() {
        return description;
    }

    public BinaryImage getImage() {
        return image;
    }

}
