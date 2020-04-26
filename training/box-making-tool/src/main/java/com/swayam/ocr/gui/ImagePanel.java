/*
 * ImagePanel.java
 *
 * Created on Aug 25, 2011 8:58:56 PM
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

package com.swayam.ocr.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author paawak
 */
public class ImagePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ImagePanel.class);

    private BufferedImage image;

    public ImagePanel() {

    }

    @Override
    protected void paintComponent(Graphics g) {

	super.paintComponent(g);

	if (image != null) {

	    int imgWidth = image.getWidth();
	    int imgHeight = image.getHeight();

	    int dx1 = 0;
	    int dy1 = 0;
	    int dx2 = imgWidth;
	    int dy2 = imgHeight;

	    g.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, imgWidth, imgHeight, this);

	}

    }

    public BufferedImage getImage() {
	return image;
    }

    public void setImage(BufferedImage image) {

	this.image = image;

	if (image != null) {

	    int imgWidth = image.getWidth();
	    int imgHeight = image.getHeight();

	    LOG.info("An image with width {} and height {} was loaded", imgWidth, imgHeight);

	    if (imgWidth > getWidth() || imgHeight > getHeight()) {

		setPreferredSize(new Dimension(imgWidth, imgHeight));
		revalidate();

	    }

	}

	repaint();

    }

}
