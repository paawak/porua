/*
 * BinaryImageHeper.java
 *
 * Created on Sep 10, 2012
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
package com.swayam.ocr.test.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import com.swayam.ocr.core.util.BinaryImage;

/**
 * 
 * @author paawak
 * 
 */
public class FastFourierTransformTest {

	public static void main(String[] a) throws IOException {

		BufferedImage image = ImageIO
				.read(FastFourierTransformTest.class
						.getResourceAsStream("/com/swayam/ocr/test/res/bangla-single-ka-1.png"));

		BinaryImage binaryImage = new BinaryImage(image,
				BinaryImage.DEFAULT_COLOR_THRESHOLD, true);

		Complex[] data = getData(binaryImage);

		FastFourierTransformer fft = new FastFourierTransformer(
				DftNormalization.STANDARD);

		Complex[] transformed = fft.transform(data, TransformType.FORWARD);

		Complex[] reverseTransform = fft.transform(transformed,
				TransformType.INVERSE);

		writeDataToImage(reverseTransform);

	}

	private static void writeDataToImage(Complex[] data) throws IOException {

		BufferedImage transformedImage = new BufferedImage(data.length,
				data.length, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < data.length; i++) {
			Complex c = data[i];
			int x = (int) c.getReal();
			int y = (int) c.getImaginary();
			System.out.println("x=" + x + ", y=" + y);
			transformedImage.setRGB(x, y, Color.WHITE.getRGB());
		}

		ImageIO.write(transformedImage, "png",
				new FileOutputStream(System.getProperty("user.home")
						+ "/Desktop/fft.png"));

	}

	private static int getNearestPowerOf2(int x) {

		return (int) Math.pow(2, Math.ceil(Math.log(x) / Math.log(2)));

	}

	private static Complex[] getData(BinaryImage binaryImage) {

		List<Complex> points = new ArrayList<Complex>(binaryImage.getWidth());

		for (int row = 0; row < binaryImage.getWidth(); row++) {

			for (int col = 0; col < binaryImage.getHeight(); col++) {

				if (binaryImage.getValueAt(row, col)) {
					points.add(new Complex(row, col));
				}

			}

		}

		int actualDataSize = points.size();

		int nearestPowerOf2 = getNearestPowerOf2(actualDataSize);

		System.err.println("actualDataSize: " + actualDataSize);
		System.err.println("nearestPowerOf2: " + nearestPowerOf2);

		Complex[] data = new Complex[nearestPowerOf2];

		for (int i = 0; i < nearestPowerOf2; i++) {

			if (i < actualDataSize) {
				data[i] = points.get(i);
			} else {
				data[i] = new Complex(0, 0);
			}

		}

		return data;

	}

}
