/*
 * OcrWorkBench.java
 *
 * Created on Jan 3, 2012
 *
 * Copyright (c) 2002 - 2012 : Swayam Inc.
 *
 * P R O P R I E T A R Y & C O N F I D E N T I A L
 *
 * The copyright of this document is vested in Swayam Inc. without
 * whose prior written permission its contents must not be published,
 * adapted or reproduced in any form or disclosed or
 * issued to any third party.
 */
package com.swayam.ocr.core.matcher;

import java.io.File;
import java.util.List;

import com.swayam.ocr.core.util.Glyph;
import com.swayam.ocr.core.util.Script;

/**
 * 
 * @author paawak
 *
 */
public interface GlyphStore {

	List<Glyph> getGlyphs(Script script);

	void addGlyph(Glyph glyph);

	void addWordImage(File imageFile, String tesseractValue);

}
