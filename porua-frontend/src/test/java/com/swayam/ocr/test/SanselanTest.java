/*
 * ImageReadTest.java
 *
 * Created on Jul 27, 2011 9:21:34 PM
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

package com.swayam.ocr.test;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.junit.Test;

/**
 * 
 * @author paawak
 */
public class SanselanTest {

    private static final String SAMPLE_IMAGE = "/com/swayam/ocr/test/res/Bangla-300.png";

    @Test
    public void test() throws ImageReadException, IOException {

        BufferedImage image = Sanselan.getBufferedImage(SanselanTest.class
                .getResourceAsStream(SAMPLE_IMAGE));

        assertEquals(1525, image.getWidth());
        assertEquals(2201, image.getHeight());

    }

}
