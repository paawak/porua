/*
 * HuMomentsCalculatorTest.java
 *
 * Created on Sep 22, 2012
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
package com.swayam.ocr.test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Ignore;
import org.junit.Test;

import com.swayam.ocr.core.moments.HuMomentsCalculator;
import com.swayam.ocr.core.moments.HuMomentsCalculator.MomentInvariantOrder;
import com.swayam.ocr.core.util.BinaryImage;

/**
 * 
 * @author paawak
 * 
 */
public class HuMomentsCalculatorTest {

	@Test
	public void testKa() throws IOException {

		printMoments(getBinaryImage("/com/swayam/ocr/test/res/bangla-single-ka-1.png"));

	}

	@Test
	public void testKa2() throws IOException {

		printMoments(getBinaryImage("/com/swayam/ocr/test/res/bangla-single-ka-2.png"));

	}

	@Test
	public void testKa3() throws IOException {

		printMoments(getBinaryImage("/com/swayam/ocr/test/res/bangla-single-ka-3.png"));

	}

	@Ignore
	public void testRa() throws IOException {

		printMoments(getBinaryImage("/com/swayam/ocr/test/res/bangla-single-ra-1.png"));

	}

	private void printMoments(BinaryImage testImage) {

		HuMomentsCalculator momCalc = new HuMomentsCalculator(testImage);

		for (MomentInvariantOrder order : MomentInvariantOrder.values()) {

			System.out.print(order + ":" + momCalc.getInvariantMoment(order)
					* 1e6 + " ");

		}

		System.out.println();

	}

	private BinaryImage getBinaryImage(String imagePath) throws IOException {

		BufferedImage image = ImageIO.read(HuMomentsCalculatorTest.class
				.getResourceAsStream(imagePath));

		return new BinaryImage(image, BinaryImage.DEFAULT_COLOR_THRESHOLD, true);

	}

}
