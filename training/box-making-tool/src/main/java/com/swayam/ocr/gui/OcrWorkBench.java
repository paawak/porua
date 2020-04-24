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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
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
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.core.impl.TesseractOcrWordAnalyser;
import com.swayam.ocr.core.model.TextBox;
import com.swayam.ocr.core.util.BinaryImage;
import com.swayam.ocr.core.util.ImageUtils;

/**
 * 
 * @author paawak
 */
public class OcrWorkBench extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(OcrWorkBench.class);

    private static final String LAST_USED_IMAGE_DIRECTORY = "LAST_USED_IMAGE_DIRECTORY";

    private GlassPanedImagePanel imagePanel;

    private final JLabel statusLabel;

    private BufferedImage currentImage;

    private BinaryImage binaryImage;

    private File currentSelectedImageFile;

    private Collection<TextBox> detectedWords;

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

	imagePanel.addMouseMotionListener(new MouseMotionListener() {

	    @Override
	    public void mouseMoved(MouseEvent e) {

		Point p = e.getPoint();

		setStatusString(p.x + ", " + p.y);

		setDetectedTextInToolTip(p);

	    }

	    @Override
	    public void mouseDragged(MouseEvent e) {

	    }

	});

	JMenuBar menuBar = new JMenuBar();

	JMenu imageMenu = new JMenu("Image");
	menuBar.add(imageMenu);

	JMenuItem loadImageMenuItem = new JMenuItem("Load");
	imageMenu.add(loadImageMenuItem);

	loadImageMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent evt) {

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

			binaryImage = null;

		    } catch (IOException e) {
			LOG.error("could not load image", e);
		    }

		    OcrWorkBench.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		}

	    }

	});

	JMenu tesseractMenu = new JMenu("Tesseract");
	menuBar.add(tesseractMenu);
	JMenuItem detectWordsMenuItem = new JMenuItem("Detect Words");
	tesseractMenu.add(detectWordsMenuItem);

	detectWordsMenuItem.addActionListener(new ProcessImageActionListener(ProcessImageOption.DETECT_WORDS_TESSERACT_OCR));

	JMenu trainingMenu = new JMenu("Training");
	menuBar.add(trainingMenu);

	JMenuItem showBinaryImageMenuItem = new JMenuItem("Show Binary Image");
	trainingMenu.add(showBinaryImageMenuItem);
	showBinaryImageMenuItem.addActionListener(new ProcessImageActionListener(ProcessImageOption.BINARY_IMAGE_ONLY));

	JMenuItem edgeDetectionMenuItem = new JMenuItem("Edge Detection");
	trainingMenu.add(edgeDetectionMenuItem);
	edgeDetectionMenuItem.addActionListener(new ProcessImageActionListener(ProcessImageOption.EDGE_DETECTION));

	JMenuItem markCharacterMenuItem = new JMenuItem("Mark Character");
	trainingMenu.add(markCharacterMenuItem);
	markCharacterMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

		if (currentImage == null || binaryImage == null) {

		    getGlassPane().setVisible(false);

		    JOptionPane.showMessageDialog(OcrWorkBench.this, "Please load an image and select OCR->Mark Texts from the menu", "Binary image not found!", JOptionPane.WARNING_MESSAGE);

		} else {

		    getGlassPane().setVisible(true);

		}

	    }

	});

	JMenuItem showGlyphDbMenuItem = new JMenuItem("Show Glyph DB");
	trainingMenu.add(showGlyphDbMenuItem);

	showGlyphDbMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

		EventQueue.invokeLater(new Runnable() {

		    @Override
		    public void run() {

			// JDialog dialog = new JDialog(OcrWorkBench.this,
			// true);
			JFrame frame = new JFrame();
			GuiUtils.centerWindow(frame, 500, 800);
			frame.getContentPane().add(new GlyphDatabaseViewer());
			frame.setVisible(true);

		    }

		});

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
	textCorrectionPopupMenuItem.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent mouseEvent) {
		EventQueue.invokeLater(() -> {

		    Point mouseLocation = SwingUtilities.convertPoint(textCorrectionPopupMenuItem, mouseEvent.getLocationOnScreen(), imagePanel);

		    Optional<TextBox> optionalText = getDetectedOcrText(mouseLocation);

		    if (!optionalText.isPresent()) {
			JOptionPane.showMessageDialog(OcrWorkBench.this, "Select a proper word box", "No word found in this region!", JOptionPane.WARNING_MESSAGE);
			return;
		    }

		    TextBox textBox = optionalText.get();
		    LOG.info("Text for correction: {}", textBox.text);
		    Rectangle area = textBox.getRectangle();
		    JDialog dialog = new CharacterTrainingDialog(currentImage.getSubimage(area.x, area.y, area.width, area.height));
		    dialog.setVisible(true);
		});
	    }
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

    private BufferedImage textImageManipulation(ProcessImageOption option) {

	BufferedImage filteredImage;

	switch (option) {
	case EDGE_DETECTION:

	    binaryImage = new BinaryImage(currentImage, BinaryImage.DEFAULT_COLOR_THRESHOLD, true);
	    binaryImage = ImageUtils.applyRobertsonEdgeDetection(binaryImage);
	    filteredImage = binaryImage.getImage();

	    break;
	default:
	case BINARY_IMAGE_ONLY:

	    binaryImage = new BinaryImage(currentImage, BinaryImage.DEFAULT_COLOR_THRESHOLD, true);
	    filteredImage = binaryImage.getImage();

	    break;

	case DETECT_WORDS_TESSERACT_OCR:
	    filteredImage = detectWordsWithTesseract();
	    break;

	}

	return filteredImage;

    }

    private BufferedImage detectWordsWithTesseract() {

	TesseractOcrWordAnalyser wordAnalyser = new TesseractOcrWordAnalyser(currentSelectedImageFile.toPath());

	detectedWords = wordAnalyser.getDetectedWords();

	Graphics2D g = (Graphics2D) currentImage.getGraphics();

	detectedWords.forEach(textBox -> paintWordBoundary(g, textBox));

	return currentImage;
    }

    private void paintWordBoundary(Graphics2D g, TextBox textBox) {
	Color confidenceColor = textBox.getColorCodedConfidence();
	g.setColor(confidenceColor);
	g.setStroke(new BasicStroke(2));
	Rectangle wordArea = textBox.getRectangle();
	g.drawRect(wordArea.x, wordArea.y, wordArea.width, wordArea.height);
    }

    private void setDetectedTextInToolTip(Point point) {
	Optional<TextBox> matchingTextBoxOpt = getDetectedOcrText(point);

	if (!matchingTextBoxOpt.isPresent()) {
	    return;
	}

	TextBox matchingTextBox = matchingTextBoxOpt.get();
	imagePanel.setToolTipText(String.format("<html><h1 bgcolor=\"%s\">%s</h1></html>", toHtmlColor(matchingTextBox.getColorCodedConfidence()), matchingTextBox.text));
    }

    private Optional<TextBox> getDetectedOcrText(Point point) {
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

    private enum ProcessImageOption {

	BINARY_IMAGE_ONLY, EDGE_DETECTION, DETECT_WORDS_TESSERACT_OCR;

    }

    private class ProcessImageActionListener implements ActionListener {

	private final ProcessImageOption option;

	ProcessImageActionListener(ProcessImageOption option) {
	    this.option = option;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	    if (currentImage == null) {

		JOptionPane.showMessageDialog(OcrWorkBench.this, "Please load an image", "No image loaded!", JOptionPane.WARNING_MESSAGE);

	    } else {

		setStatusString("Detecting text, please wait...");

		OcrWorkBench.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		EventQueue.invokeLater(new Runnable() {

		    @Override
		    public void run() {

			long startTime = System.currentTimeMillis();

			setImageInFrame(textImageManipulation(option));

			long endTime = System.currentTimeMillis();

			OcrWorkBench.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			LOG.info("The {} operation took {} ms", option, (endTime - startTime));

		    }

		});

	    }

	}

    }

}
