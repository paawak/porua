/*
 * OcrWorkBench.java
 *
 * Created on Jan 12, 2012
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
package com.swayam.ocr.core;

import com.swayam.ocr.core.util.BinaryImage;
import com.swayam.ocr.core.util.ImageUtils;

/**
 * 
 * Converts a given image to a single pixel.
 * 
 * @author paawak
 *
 */
public class ImageDigester {
	
	private final BinaryImage image;

	public ImageDigester(BinaryImage image) {
		this.image = image;
	}
	
	public BinaryImage digest() {
		
		BinaryImage digestedImage = ImageUtils.applyRobertsonEdgeDetection(image);
		
		return digestedImage;
		
	}

}
