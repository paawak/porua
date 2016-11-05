/*
 * GeometryUtils.java
 *
 * Created on Sep 9, 2011 12:17:20 PM
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

import java.util.List;

/**
 * 
 * @author paawak
 */
public class GeometryUtils {

    // private static final Logger LOG = Logger.getLogger(GeometryUtils.class);

    private GeometryUtils() {

    }

    public static boolean isPointInside(List<Rectangle> rectangles, int x, int y) {

        boolean found = false;

        for (Rectangle area : rectangles) {

            int endX = area.x + area.width;
            int endY = area.y + area.height;

            found = (x >= area.x && x <= endX) && (y >= area.y && y <= endY);

            if (found) {
                break;
            }

        }

        return found;

    }

}
