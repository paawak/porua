/*
 * GlassPanedImagePanel.java
 *
 * Created on Sep 27, 2011 12:02:43 AM
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

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import com.swayam.ocr.engine.old.core.util.Rectangle;

/**
 * 
 * @author paawak
 */
public class GlassPanedImagePanel extends ImagePanel {

    private static final long serialVersionUID = 1L;

    private final TextMarkerGlassPane glassPane;

    private Rectangle trainingCharacter;

    public GlassPanedImagePanel(TextMarkerGlassPane glassPane) {
        this.glassPane = glassPane;
        ImagePanelMouseListener listener = new ImagePanelMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public Rectangle getTrainingCharacter() {
        return trainingCharacter;
    }

    private class ImagePanelMouseListener implements MouseListener,
            MouseMotionListener {

        private Point startPoint;

        @Override
        public void mouseClicked(MouseEvent e) {

            Point point = e.getPoint();

            if (shouldMarkCharacter(point)) {
                startPoint = point;
            } else {
                startPoint = null;
            }

            trainingCharacter = null;
            glassPane.setCharacterArea(null);
            glassPane.repaint();

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            paintRectangleOnGlassPane(e.getPoint());
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            paintRectangleOnGlassPane(e.getPoint());
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

        private void paintRectangleOnGlassPane(Point point) {

            if (startPoint != null && shouldMarkCharacter(point)) {

                Point rectangleStart = startPoint;

                if (startPoint.y > point.y) {

                    rectangleStart = point;

                }

                int rectangleWidth = Math.abs(startPoint.x
                /*- getOffsetX()*/
                - Math.min(getImage().getWidth(), point.x
                /*- getOffsetX()*/));
                int rectangleHeight = Math.abs(startPoint.y
                /*- getOffsetY()*/
                - Math.min(getImage().getHeight(), point.y
                /*- getOffsetY()*/));

                trainingCharacter = new Rectangle(rectangleStart.x
                /*- getOffsetX()*/, rectangleStart.y /*- getOffsetY()*/,
                        rectangleWidth, rectangleHeight);

                rectangleStart = getPointRelativeToGlassPane(rectangleStart);
                glassPane.setCharacterArea(new Rectangle(rectangleStart.x,
                        rectangleStart.y, rectangleWidth, rectangleHeight));

            } else {

                trainingCharacter = null;
                glassPane.setCharacterArea(null);

            }

            glassPane.repaint();

        }

        private Point getPointRelativeToGlassPane(Point imagePoint) {

            Point glassPanePoint = SwingUtilities.convertPoint(
                    GlassPanedImagePanel.this, imagePoint, glassPane);

            return glassPanePoint;

        }

        private boolean shouldMarkCharacter(Point p) {

            boolean mark = false;

            if (glassPane.isVisible() && getImage() != null) {
                mark = (p.x >= 0) && (p.y >= 0);
            }

            return mark;

        }

    }

}
