/*
 * ImageArrayUtil.java
 *
 * Created on Aug 4, 2011 12:40:53 PM
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

package com.swayam.ocr.engine.old.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;

import javax.imageio.ImageIO;

import com.swayam.ocr.engine.old.core.ImageFormat;

/**
 * 
 * @author paawak
 */
public class ImageUtils {

	private ImageUtils() {

	}

	public static BufferedImage getGrayScaleImage(BufferedImage bimg) {

		return getGrayScaleImage(bimg, 0.333f, 0.333f, 0.333f);

	}

	public static BufferedImage getGrayScaleImage(BufferedImage bimg, float wr, float wg, float wb) {

		BufferedImage grey = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_INT_RGB);

		int w = bimg.getWidth();
		int h = bimg.getHeight();

		for (int y = 0; y < h; y++) {

			for (int x = 0; x < w; x++) {

				int red = getChannelValue(bimg, x, y, Channel.RED);
				int green = getChannelValue(bimg, x, y, Channel.GREEN);
				int blue = getChannelValue(bimg, x, y, Channel.BLUE);

				float color = ((blue * wb + green * wg + red * wr) / 255f);

				int rgb = pack((int) (color * 255), (int) (color * 255), (int) (color * 255));

				grey.setRGB(x, y, rgb);

			}

		}

		return grey;

	}

	public static int pack(int r, int g, int b) {

		int color = 255 << 8;
		color |= r;
		color <<= 8;
		color |= g;
		color <<= 8;
		color |= b;

		return color;

	}

	public static int getChannelValue(BufferedImage bimg, int x, int y, Channel channel) {

		int rgbVal = bimg.getRGB(x, y);

		switch (channel) {

		case RED:
			rgbVal = (rgbVal >> 16) & 255;
			break;
		case GREEN:
			rgbVal = (rgbVal >> 8) & 255;
			break;
		case BLUE:
			rgbVal = rgbVal & 255;
			break;

		}

		return rgbVal;

	}

	public static BinaryImage applyRobertsonEdgeDetection(BinaryImage binaryImage) {

		boolean[][] data = new boolean[binaryImage.getWidth()][binaryImage.getHeight()];

		for (int y = 1; y < binaryImage.getHeight() - 1; y++) {

			for (int x = 1; x < binaryImage.getWidth() - 1; x++) {

				PixelNeighbours neighbour = new PixelNeighbours(x, y, binaryImage);

				data[x][y] = (binaryImage.getValueAt(x, y) ^ neighbour.cell5) || (neighbour.cell4 ^ neighbour.cell6);

			}

		}

		return new BinaryImage(data, binaryImage.getWidth(), binaryImage.getHeight());

	}

	public static void splitImageIntoSubImages(BufferedImage bimg, List<Rectangle> areas, File imageSplitDirectory, ImageFormat imageFormat,
			BiFunction<Rectangle, ImageFormat, String> subImageNameGenerator) {
		if (!imageSplitDirectory.exists()) {
			imageSplitDirectory.mkdirs();
		}

		areas.parallelStream().forEach((Rectangle area) -> {
			File wordImageFile = new File(imageSplitDirectory, subImageNameGenerator.apply(area, imageFormat));

			try {
				wordImageFile.createNewFile();
				ImageIO.write(bimg.getSubimage(area.x, area.y, area.width, area.height), imageFormat.name(), wordImageFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		});

	}

}
