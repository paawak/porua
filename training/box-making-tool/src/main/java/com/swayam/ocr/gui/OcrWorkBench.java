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
import java.awt.Dimension;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
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
import com.swayam.ocr.core.impl.WordCache;
import com.swayam.ocr.core.model.CachedOcrText;
import com.swayam.ocr.core.model.Language;
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

    private static final Dimension INITIAL_SIZE = new Dimension(800, 600);

    private final Language LANGUAGE = Language.BENGALI;

    private final WordCache wordCache;

    private GlassPanedImagePanel imagePanel;

    private JLabel statusLabel;

    private File currentSelectedImageFile;

    private Optional<CachedOcrText> matchingTextBox = Optional.empty();

    public OcrWorkBench(WordCache wordCache) {

	this.wordCache = wordCache;
	init();

    }

    private void init() {

	setTitle("Ocr Workbench");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setPreferredSize(INITIAL_SIZE);

	statusLabel = new JLabel();

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

		getGlassPane().setVisible(false);

		currentSelectedImageFile = fileChooser.getSelectedFile();

		imageDirectoryPrefs.put(LAST_USED_IMAGE_DIRECTORY, currentSelectedImageFile.getParentFile().getAbsolutePath());

		setImageInFrame(getCurrentImageFromFile());

		OcrWorkBench.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	    }

	});

	JMenu tesseractMenu = new JMenu("Tesseract");
	menuBar.add(tesseractMenu);
	JMenuItem detectWordsMenuItem = new JMenuItem("Detect Words");
	tesseractMenu.add(detectWordsMenuItem);

	detectWordsMenuItem.addActionListener(actionEvt -> {

	    if (currentSelectedImageFile == null) {

		JOptionPane.showMessageDialog(OcrWorkBench.this, "Please load an image", "No image loaded!", JOptionPane.WARNING_MESSAGE);

	    } else {

		setStatusString("Detecting text, please wait...");

		OcrWorkBench.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		EventQueue.invokeLater(() -> {
		    long startTime = System.currentTimeMillis();

		    detectWordsWithTesseract();

		    long endTime = System.currentTimeMillis();

		    OcrWorkBench.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		    LOG.info("The Tesseract OCR word detection took {} ms", (endTime - startTime));
		}

		);
	    }

	});

	JMenuItem generateBoxFileMenuItem = new JMenuItem("Generate Box File");
	generateBoxFileMenuItem.addActionListener(actionEvt -> {
	    generateBoxFile();
	});
	tesseractMenu.add(generateBoxFileMenuItem);

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

		CachedOcrText textBox = matchingTextBox.get();
		LOG.info("Text for correction: {}", textBox.rawOcrText.text);
		Rectangle area = textBox.rawOcrText.getRectangle();
		BufferedImage wordImage = getCurrentImageFromFile().getSubimage(area.x, area.y, area.width, area.height);
		JDialog dialog = new OcrTextCorrectionDialog(this, wordCache, wordImage, textBox);
		dialog.setVisible(true);
		setImageInFrame(getImageWithPaintedWordBoundaries());
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

	setLocationRelativeTo(null);

    }

    private void setImageInFrame(BufferedImage image) {

	imagePanel.setImage(image);

    }

    private void setStatusString(String message) {

	statusLabel.setText(message);

    }

    private void detectWordsWithTesseract() {

	TesseractOcrWordAnalyser wordAnalyser = new TesseractOcrWordAnalyser(currentSelectedImageFile.toPath(), LANGUAGE);

	List<RawOcrWord> detectedWords = wordAnalyser.getDetectedText();

	if (wordCache.getWordCount(currentSelectedImageFile.getName()) != 0) {
	    LOG.warn("Entries already present for {}: using existing entries", currentSelectedImageFile);
	} else {
	    wordCache.storeRawOcrWords(currentSelectedImageFile.getName(), LANGUAGE, detectedWords);
	}

	setImageInFrame(getImageWithPaintedWordBoundaries());

    }

    private void generateBoxFile() {
	if (currentSelectedImageFile == null) {
	    throw new IllegalArgumentException("No image file is currently selected!");
	}

	List<String> boxes = new TesseractOcrWordAnalyser(currentSelectedImageFile.toPath(), LANGUAGE).getBoxStrings(wordCache.getWords(currentSelectedImageFile.getName()));

	try {
	    Path boxFilePath = Paths.get(currentSelectedImageFile.getParent(), currentSelectedImageFile.getName() + ".box");
	    Files.write(boxFilePath, boxes);
	    LOG.info("box file {} generated successfully", boxFilePath);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    private BufferedImage getCurrentImageFromFile() {
	if (currentSelectedImageFile == null) {
	    throw new IllegalArgumentException("No image file is currently selected!");
	}
	BufferedImage image;
	try {
	    image = ImageIO.read(currentSelectedImageFile);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	return image;
    }

    private BufferedImage getImageWithPaintedWordBoundaries() {
	BufferedImage image = getCurrentImageFromFile();
	Graphics2D g = (Graphics2D) image.getGraphics();
	wordCache.getWords(currentSelectedImageFile.getName()).forEach(word -> paintSingleWordBoundary(g, word.rawOcrText));
	return image;
    }

    private void paintSingleWordBoundary(Graphics2D g, RawOcrWord textBox) {
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

	CachedOcrText ocrText = matchingTextBox.get();
	String toolTipText;
	if (ocrText.correctText != null && ocrText.correctText.trim().length() > 0) {
	    toolTipText = ocrText.correctText.trim();
	} else {
	    toolTipText = ocrText.rawOcrText.text;
	}
	imagePanel.setToolTipText(String.format("<html><h1 bgcolor=\"%s\">%s</h1></html>", toHtmlColor(ocrText.rawOcrText.getColorCodedConfidence()), toolTipText));
    }

    private Optional<CachedOcrText> getDetectedOcrText(Point point) {
	if (currentSelectedImageFile == null) {
	    return Optional.empty();
	}
	Collection<CachedOcrText> detectedWords = wordCache.getWords(currentSelectedImageFile.getName());
	if (detectedWords == null || detectedWords.isEmpty()) {
	    return Optional.empty();
	}

	return detectedWords.parallelStream().filter(text -> {
	    Rectangle originaWordArea = text.rawOcrText.getRectangle();
	    int expandBy = 10;
	    Rectangle expandedWordArea = new Rectangle(originaWordArea.x - expandBy, originaWordArea.y - expandBy, originaWordArea.width + expandBy * 2, originaWordArea.height + expandBy * 2);
	    return expandedWordArea.contains(point);
	}).findFirst();
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
