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

/**
 * 
 * @author paawak
 */
public class ImagePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private BufferedImage image;

    // private int offsetX;
    // private int offsetY;

    public ImagePanel() {

    }

    // int getOffsetX() {
    // return offsetX;
    // }
    //
    // int getOffsetY() {
    // return offsetY;
    // }

    @Override
    protected void paintComponent(Graphics g) {

        // offsetX = 0;
        // offsetY = 0;

        super.paintComponent(g);

        if (image != null) {

            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();

            // int pnlWidth = getWidth();
            // int pnlHeight = getHeight();

            int dx1 = 0;
            int dy1 = 0;
            int dx2 = imgWidth;
            int dy2 = imgHeight;

            // if (pnlWidth > imgWidth) {
            //
            // offsetX = (pnlWidth - imgWidth) / 2;
            //
            // dx1 = offsetX;
            // dx2 += offsetX;
            //
            // }
            //
            // if (pnlHeight > imgHeight) {
            //
            // offsetY = (pnlHeight - imgHeight) / 2;
            //
            // dy1 = offsetY;
            // dy2 += offsetY;
            //
            // }

            g.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, imgWidth, imgHeight,
                    this);

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

            if (imgWidth > getWidth() || imgHeight > getHeight()) {

                setPreferredSize(new Dimension(imgWidth, imgHeight));
                revalidate();

            }

        }

        repaint();

    }

}
