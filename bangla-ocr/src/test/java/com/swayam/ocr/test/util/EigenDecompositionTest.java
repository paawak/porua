/*
 * EigenDecompositionTest.java
 *
 * Created on Sep 16, 2012
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import com.swayam.ocr.core.util.BinaryImage;

/**
 * 
 * @author paawak
 * 
 */
public class EigenDecompositionTest {

	public static void main(String[] a) throws IOException {

		BufferedImage image = ImageIO
				.read(FastFourierTransformTest.class
						.getResourceAsStream("/com/swayam/ocr/test/res/bangla-single-ka-1.png"));

		BinaryImage binaryImage = new BinaryImage(image,
				BinaryImage.DEFAULT_COLOR_THRESHOLD, true);

		RealMatrix data = getData(binaryImage);

		// RealMatrix transpose = data.transpose();
		//
		// printMatrix(transpose);

		// EigenDecomposition eigen = new EigenDecomposition(data, 0);
		//
		// eigen.getSolver();

	}

	private static void printMatrix(RealMatrix matrix)
			throws FileNotFoundException, IOException {

		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();

		BufferedImage transformedImage = new BufferedImage(rows, cols,
				BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (matrix.getEntry(i, j) > 0) {
					transformedImage.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		}

		ImageIO.write(transformedImage, "png",
				new FileOutputStream(System.getProperty("user.home")
						+ "/Desktop/fft.png"));

	}

	private static RealMatrix getData(BinaryImage binaryImage) {

		int matrixSize = Math.max(binaryImage.getWidth(),
				binaryImage.getHeight());

		RealMatrix matrix = new OpenMapRealMatrix(matrixSize, matrixSize);

		for (int i = 0; i < binaryImage.getWidth(); i++) {
			for (int j = 0; j < binaryImage.getHeight(); j++) {
				if (binaryImage.getValueAt(i, j)) {
					matrix.setEntry(i, j, 1);
				}
			}
		}

		return matrix;

	}

}
