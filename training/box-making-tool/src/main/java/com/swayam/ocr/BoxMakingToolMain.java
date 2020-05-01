package com.swayam.ocr;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import com.swayam.ocr.core.impl.HsqlBackedWordCache;
import com.swayam.ocr.gui.OcrWorkBench;

public class BoxMakingToolMain {

    private static final String HSQL_DATA_STORE_JDBC_URL = "jdbc:hsqldb:file:./db/ocrdb;shutdown=true";

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

	EventQueue.invokeAndWait(new Runnable() {

	    @Override
	    public void run() {

		JFrame frame = new OcrWorkBench(new HsqlBackedWordCache(HSQL_DATA_STORE_JDBC_URL));
		frame.setVisible(true);

	    }

	});

    }

}
