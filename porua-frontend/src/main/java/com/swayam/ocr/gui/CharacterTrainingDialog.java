/*
 * CharacterTrainingDialog.java
 *
 * Created on Sep 25, 2011 7:02:29 PM
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.swayam.ocr.core.matcher.GlyphStore;
import com.swayam.ocr.core.matcher.HsqlGlyphStore;
import com.swayam.ocr.core.util.BinaryImage;
import com.swayam.ocr.core.util.Glyph;
import com.swayam.ocr.core.util.Script;
import com.swayam.ocr.core.util.Typeface;

/**
 * 
 * @author paawak
 */
public class CharacterTrainingDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    
    private static final String ERASER_IMG = "/com/swayam/ocr/res/image/eraser.png";
//    private static final String ERASER_CURSOR = "/com/swayam/ocr/res/image/eraser_cursor.png";

    private static final int DIALOG_WIDTH = 500;
    private static final int DIALOG_HEIGHT = 500;
    
    private static final String CHAR_PATTERN_REGEX = "([\\da-f]{4}\\s?)+";

    private static final Pattern CHAR_PATTERN;

    static {

        CHAR_PATTERN = Pattern.compile(CHAR_PATTERN_REGEX);

    }

    private final BinaryImage binaryImage;
    
    private final Cursor eraserCursor;
    
    private JToggleButton eraserButton;

    private JTextField txtCharacters;

    private ImagePanel imagePanel;

    public CharacterTrainingDialog(BinaryImage binaryImage) {
    	
//    	Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
//		eraserCursor = defaultToolkit.createCustomCursor(
//				defaultToolkit.getImage(CharacterTrainingDialog.class.getResource(ERASER_CURSOR)), new Point(5, 5), "EraserCursor"); 
		
		eraserCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

        setModal(true);
        setTitle("Store Characters");

        this.binaryImage = binaryImage;

        init();

    }

    private void init() {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // FIXME: make it GridBagLayout
        getContentPane().setLayout(new BorderLayout());

        JLabel lbTitle = new JLabel(
                "<html>Enter the Unicode value of the character in the format xxxx, where 'x' is a digit (0-9). If there are more than one character, each must be separated by a space.</html>");
        
        getContentPane().add(lbTitle, BorderLayout.PAGE_START);
        
        getContentPane().add(getToolsPanel(), BorderLayout.LINE_START);

        BufferedImage charImage = binaryImage.getImage();

        imagePanel = new ImagePanel();
        imagePanel.setImage(charImage);
        ImageEraserMouseListener imageEraserMouseListener = new ImageEraserMouseListener();
        imagePanel.addMouseMotionListener(imageEraserMouseListener);
        imagePanel.addMouseListener(imageEraserMouseListener);

        getContentPane().add(imagePanel, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel();
        pnlBottom.setLayout(new BorderLayout());
        pnlBottom.setPreferredSize(new Dimension(DIALOG_WIDTH, 40));
        getContentPane().add(pnlBottom, BorderLayout.SOUTH);

        txtCharacters = new JTextField();
        pnlBottom.add(txtCharacters, BorderLayout.CENTER);

        JButton btStoreChar = new JButton("Store Char");
        pnlBottom.add(btStoreChar, BorderLayout.EAST);

        btStoreChar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {

                String chars = txtCharacters.getText().trim();

                if ("".equals(chars)) {

                    JOptionPane
                            .showMessageDialog(CharacterTrainingDialog.this,
                                    "Please enter some characters",
                                    "No characters entered!",
                                    JOptionPane.ERROR_MESSAGE);

                } else {
                	
                	Matcher match = CHAR_PATTERN.matcher(chars);

                    if (match.matches()) {
                    	
                		GlyphStore glyphDB = HsqlGlyphStore.INSTANCE;

                        //FIXME: do not hardcode
                        Typeface typeFace = new Typeface();
                        typeFace.setName("testing");
                        typeFace.setScript(Script.BANGLA);

                        Glyph glyph = new Glyph(-1, typeFace, chars.toUpperCase(),
                                new BinaryImage(imagePanel.getImage(), OcrWorkBench.COLOR_THRESHOLD, false)); 

                        glyphDB.addGlyph(glyph);

                        JOptionPane.showMessageDialog(
                                CharacterTrainingDialog.this, "Character(s) "
                                        + chars + " added successfully",
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                        
                        CharacterTrainingDialog.this.setVisible(false);
                        CharacterTrainingDialog.this.dispose();
                    	
                    } else {
                    	
                    	JOptionPane.showMessageDialog(
                                CharacterTrainingDialog.this,
                                "Please enter characters in the right format",
                                "Invalid format!", JOptionPane.ERROR_MESSAGE);
                    	
                    }

                }

            }

        });
        
        GuiUtils.centerWindow(this, DIALOG_WIDTH, DIALOG_HEIGHT);

    }
    
    private JPanel getToolsPanel() {
    	
    	JPanel toolsPanel = new JPanel();
    	toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));
    	eraserButton = new JToggleButton();
    	toolsPanel.add(eraserButton);
    	eraserButton.setText("Eraser");
    	eraserButton.setIcon(new ImageIcon(CharacterTrainingDialog.class.getResource(ERASER_IMG)));
    	
    	eraserButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				boolean state = eraserButton.isSelected();
				
				if (state) {
					setCursor(eraserCursor);
				} else {
					setCursor(Cursor.getDefaultCursor());
				}
				
			}
			
		});
    	
    	return toolsPanel;
    	
    }
    
    private class ImageEraserMouseListener implements MouseListener, MouseMotionListener {
    	
    	private BufferedImage tempImage;

		@Override
		public void mouseDragged(MouseEvent e) {
			
			if (!eraserButton.isSelected()) {
				return;
			}
			
			if (tempImage == null) {
				tempImage = imagePanel.getImage(); 
			}
			
			Point p = e.getPoint();
			
			if ((p.x >= 0 && p.y >= 0) 
					&& (p.x < tempImage.getWidth() && p.y < tempImage.getHeight())) {
				tempImage.setRGB(p.x, p.y, Color.BLACK.getRGB());
			}
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
			if (!eraserButton.isSelected()) {
				return;
			}
			
			if (tempImage != null) {
				
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						
						imagePanel.setImage(tempImage);
						tempImage = null;
						setCursor(Cursor.getDefaultCursor());
						eraserButton.setSelected(false);
						
					}
					
				});
				
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}
    	
    }
    
}
