package com.swayam.ocr;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import com.swayam.ocr.gui.OcrWorkBench;

public class BoxMakingToolMain {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

	EventQueue.invokeAndWait(new Runnable() {

	    @Override
	    public void run() {

		JFrame frame = new OcrWorkBench();
		frame.setVisible(true);

	    }

	});

    }

}
