/*
 * OcrWorkBench.java
 *
 * Created on Jan 3, 2012
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
package com.swayam.ocr.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * 
 * @author paawak
 *
 */
public class GuiUtils {
	
	private GuiUtils() {
		
	}
	
	public static void centerWindow(Window window, int width, int height) {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
		
		window.setBounds(new java.awt.Rectangle(x, y, width, height));
		
	}

}
