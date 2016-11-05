/*
 * BinaryImage.java
 *
 * Created on Sep 25, 2011 7:57:09 PM
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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.swayam.ocr.engine.api.Rectangle;

/**
 * 
 * @author paawak
 */
public class BinaryImage implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean[][] data;
    private final int width;
    private final int height;

    public BinaryImage(BufferedImage image, int colorThreshold, boolean negativeThreshold) {

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        boolean[][] imageMatrix = new boolean[imageWidth][imageHeight];

        for (int y = 0; y < imageHeight; y++) {

            for (int x = 0; x < imageWidth; x++) {

                int rgbVal = (image.getRGB(x, y) >> 16) & 255;

                if (rgbVal < colorThreshold) {
                	
                	if (negativeThreshold) {
                		imageMatrix[x][y] = true;
                	}


                } else if (!negativeThreshold) {
                	
                	imageMatrix[x][y] = true;
                	
                }

            }

        }

        data = imageMatrix;
        width = imageWidth;
        height = imageHeight;

    }

    public BinaryImage(boolean[][] data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }

    public boolean getValueAt(int x, int y) {
        return data[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BinaryImage getSubImage(Rectangle subImageBounds) {

        boolean[][] subImageData = new boolean[subImageBounds.width][subImageBounds.height];

        for (int x = 0; x < subImageBounds.width; x++) {

            for (int y = 0; y < subImageBounds.height; y++) {

                subImageData[x][y] = data[x + subImageBounds.x][y
                        + subImageBounds.y];

            }

        }

        return new BinaryImage(subImageData, subImageBounds.width,
                subImageBounds.height);

    }
    
    public BinaryImage getClone() {

        return getSubImage(new Rectangle(0, 0, width, height));

    }

    public BufferedImage getImage() {

        int imageWidth = getWidth();
        int imageHeight = getHeight();

        BufferedImage image = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < imageWidth; x++) {

            for (int y = 0; y < imageHeight; y++) {

                if (getValueAt(x, y)) {

                    image.setRGB(x, y, Color.WHITE.getRGB());

                }

            }

        }

        return image;

    }

}
