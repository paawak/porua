/*
 * OcrWorkBench.java
 *
 * Created on Aug 16, 2011 11:46:03 PM
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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.core.impl.TesseractOcrWordAnalyser;
import com.swayam.ocr.core.model.RawOcrWord;

/**
 * 
 * @author paawak
 */
public class OcrWorkBench extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(OcrWorkBench.class);

    private static final String LAST_USED_IMAGE_DIRECTORY = "LAST_USED_IMAGE_DIRECTORY";

    private static final float WORD_BOUNDARY_STROKE_WIDTH = 4;

    private GlassPanedImagePanel imagePanel;

    private final JLabel statusLabel;

    private BufferedImage currentImage;

    private File currentSelectedImageFile;

    private Collection<RawOcrWord> detectedWords = Collections.emptyList();

    private Optional<RawOcrWord> matchingTextBox = Optional.empty();

    public OcrWorkBench() {

	statusLabel = new JLabel();

	init();

    }

    private void init() {

	setTitle("Ocr Workbench");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	TextMarkerGlassPane glassPane = new TextMarkerGlassPane();

	setGlassPane(glassPane);

	imagePanel = new GlassPanedImagePanel(glassPane);

	imagePanel.addMouseMotionListener(new MouseMotionAdapter() {

	    @Override
	    public void mouseMoved(MouseEvent e) {

		Point currentMousePosition = e.getPoint();

		setStatusString(currentMousePosition.x + ", " + currentMousePosition.y);

		setDetectedTextInToolTip(currentMousePosition);

	    }

	});

	JMenuBar menuBar = new JMenuBar();

	JMenu imageMenu = new JMenu("Image");
	menuBar.add(imageMenu);

	JMenuItem loadImageMenuItem = new JMenuItem("Load");
	imageMenu.add(loadImageMenuItem);

	loadImageMenuItem.addActionListener(evt -> {

	    Preferences imageDirectoryPrefs = Preferences.userNodeForPackage(OcrWorkBench.class);

	    String initialDirectory = imageDirectoryPrefs.get(LAST_USED_IMAGE_DIRECTORY, System.getProperty("user.home"));

	    LOG.info("initialDirectory: {}", initialDirectory);

	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setCurrentDirectory(new File(initialDirectory));

	    int option = fileChooser.showOpenDialog(OcrWorkBench.this);

	    if (option == JFileChooser.APPROVE_OPTION) {

		OcrWorkBench.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		try {

		    getGlassPane().setVisible(false);

		    currentSelectedImageFile = fileChooser.getSelectedFile();

		    imageDirectoryPrefs.put(LAST_USED_IMAGE_DIRECTORY, currentSelectedImageFile.getParentFile().getAbsolutePath());

		    currentImage = ImageIO.read(currentSelectedImageFile);
		    setImageInFrame(currentImage);

		} catch (IOException e) {
		    LOG.error("could not load image", e);
		}

		OcrWorkBench.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	    }

	});

	JMenu tesseractMenu = new JMenu("Tesseract");
	menuBar.add(tesseractMenu);
	JMenuItem detectWordsMenuItem = new JMenuItem("Detect Words");
	tesseractMenu.add(detectWordsMenuItem);

	detectWordsMenuItem.addActionListener(actionEvt -> {

	    if (currentImage == null) {

		JOptionPane.showMessageDialog(OcrWorkBench.this, "Please load an image", "No image loaded!", JOptionPane.WARNING_MESSAGE);

	    } else {

		setStatusString("Detecting text, please wait...");

		OcrWorkBench.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		EventQueue.invokeLater(() -> {
		    long startTime = System.currentTimeMillis();

		    setImageInFrame(detectWordsWithTesseract());

		    long endTime = System.currentTimeMillis();

		    OcrWorkBench.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		    LOG.info("The Tesseract OCR word detection took {} ms", (endTime - startTime));
		}

		);
	    }

	});

	setJMenuBar(menuBar);

	getContentPane().setLayout(new BorderLayout());

	JScrollPane centreScrPane = new JScrollPane();
	getContentPane().add(centreScrPane, BorderLayout.CENTER);

	centreScrPane.setViewportView(imagePanel);

	JPanel statusPanel = new JPanel();
	statusPanel.setLayout(new BorderLayout());
	getContentPane().add(statusPanel, BorderLayout.SOUTH);

	statusLabel.setText("   ");

	statusPanel.add(statusLabel, BorderLayout.CENTER);

	statusLabel.setForeground(Color.BLUE);

	final JPopupMenu ocrCorrectionPopup = new JPopupMenu();

	JMenuItem textCorrectionPopupMenuItem = new JMenuItem("Correct OCR Text");
	ocrCorrectionPopup.add(textCorrectionPopupMenuItem);
	textCorrectionPopupMenuItem.addActionListener(actionEvent -> {
	    EventQueue.invokeLater(() -> {

		if (!matchingTextBox.isPresent()) {
		    JOptionPane.showMessageDialog(OcrWorkBench.this, "Select a proper word box", "No word found in this region!", JOptionPane.WARNING_MESSAGE);
		    return;
		}

		RawOcrWord textBox = matchingTextBox.get();
		LOG.info("Text for correction: {}", textBox.text);
		Rectangle area = textBox.getRectangle();
		BufferedImage wordImage = currentImage.getSubimage(area.x, area.y, area.width, area.height);
		JDialog dialog = new OcrTextCorrectionDialog(this, wordImage, textBox);
		dialog.setVisible(true);
	    });
	});

	imagePanel.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent mouseEvent) {
		if (mouseEvent.isMetaDown()) {
		    ocrCorrectionPopup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
		}
	    }
	});

	pack();

	int width = 800;
	int height = 600;

	GuiUtils.centerWindow(this, width, height);

    }

    private void setImageInFrame(BufferedImage image) {

	imagePanel.setImage(image);

    }

    private void setStatusString(String message) {

	statusLabel.setText(message);

    }

    private BufferedImage detectWordsWithTesseract() {

	TesseractOcrWordAnalyser wordAnalyser = new TesseractOcrWordAnalyser(currentSelectedImageFile.toPath());

	detectedWords = wordAnalyser.getDetectedWords();

	Graphics2D g = (Graphics2D) currentImage.getGraphics();

	detectedWords.forEach(textBox -> paintWordBoundary(g, textBox));

	return currentImage;
    }

    private void paintWordBoundary(Graphics2D g, RawOcrWord textBox) {
	Color confidenceColor = textBox.getColorCodedConfidence();
	g.setColor(confidenceColor);
	g.setStroke(new BasicStroke(WORD_BOUNDARY_STROKE_WIDTH));
	Rectangle wordArea = textBox.getRectangle();
	g.drawRect(wordArea.x, wordArea.y, wordArea.width, wordArea.height);
    }

    private void setDetectedTextInToolTip(Point point) {
	matchingTextBox = getDetectedOcrText(point);

	if (!matchingTextBox.isPresent()) {
	    return;
	}

	RawOcrWord textBox = matchingTextBox.get();
	imagePanel.setToolTipText(String.format("<html><h1 bgcolor=\"%s\">%s</h1></html>", toHtmlColor(textBox.getColorCodedConfidence()), textBox.text));
    }

    private Optional<RawOcrWord> getDetectedOcrText(Point point) {
	if (detectedWords == null || detectedWords.isEmpty()) {
	    return Optional.empty();
	}

	return detectedWords.parallelStream().filter(textBox -> textBox.getRectangle().contains(point)).findFirst();
    }

    private String toHtmlColor(Color color) {
	Function<Integer, String> toHex = colorValue -> {
	    String hex = Integer.toHexString(colorValue);
	    if (hex.length() == 1) {
		hex = "0" + hex;
	    }
	    return hex;
	};

	return "#" + toHex.apply(color.getRed()) + toHex.apply(color.getGreen()) + toHex.apply(color.getBlue());
    }

}
