/*
 * RectangleMarkerGlassPane.java
 *
 * Created on Sep 25, 2011 2:22:50 PM
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

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.swayam.ocr.core.util.Rectangle;

/**
 * 
 * @author paawak
 */
public class TextMarkerGlassPane extends JComponent {

    private static final long serialVersionUID = 1L;

    private Rectangle characterArea;

    public TextMarkerGlassPane() {

    }

    @Override
    protected void paintComponent(Graphics g) {

        if (characterArea != null) {

            g.setColor(Color.RED);
            g.drawRect(characterArea.x, characterArea.y, characterArea.width,
                    characterArea.height);

        }

    }

    public void setCharacterArea(Rectangle characterArea) {
        this.characterArea = characterArea;
    }

}
