/*
 * GlyphDatabaseViewer.java
 *
 * Created on Oct 20, 2011 5:27:31 PM
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
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swayam.ocr.core.matcher.GlyphStore;
import com.swayam.ocr.core.matcher.HsqlGlyphStore;
import com.swayam.ocr.core.model.WordImage;
import com.swayam.ocr.gui.table.ImageTableCellRenderer;
import com.swayam.ocr.gui.table.IndicTableCellEditor;
import com.swayam.ocr.gui.table.IndicTableCellRenderer;

/**
 * 
 * @author paawak
 */
public class GlyphDatabaseViewer extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(GlyphDatabaseViewer.class);

	// FIXME: externalise the below
	// private static final String IMAGE_DIR =
	// "D:/personal/code/porua/porua-frontend/image-store/training/bangla/rajshekhar-basu-mahabharat/Bangla-mahabharat-1-page_1/05-03-2016_10-55-14/";
	private static final String IMAGE_DIR = "/home/paawak/kaaj/code/porua/porua-frontend/image-store/training/bangla/rajshekhar-basu-mahabharat/Bangla-mahabharat-1-page_1/05-03-2016_10-55-14/";

	private final GlyphStore glyphDB = HsqlGlyphStore.INSTANCE;

	public GlyphDatabaseViewer() {
		init();
	}

	private void init() {

		setLayout(new BorderLayout());

		JScrollPane scrpn = new JScrollPane();
		add(scrpn, BorderLayout.CENTER);
		JTable table = new JTable(getTableData());
		scrpn.setViewportView(table);

		table.setDefaultRenderer(String.class, new IndicTableCellRenderer());
		table.setDefaultRenderer(BufferedImage.class, new ImageTableCellRenderer());
		table.setDefaultEditor(String.class, new IndicTableCellEditor());
		table.setRowHeight(60);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener((ActionEvent ae) -> {
			save(table.getModel());
		});
		add(btnSave, BorderLayout.SOUTH);

	}

	private void save(TableModel tableModel) {
		int rows = tableModel.getRowCount();
		for (int row = 0; row < rows; row++) {
			String actualValue = (String) tableModel.getValueAt(row, 3);
			if (actualValue == null || actualValue.trim().length() == 0) {
				continue;
			}
			long id = (long) tableModel.getValueAt(row, 0);
			glyphDB.setActualValue(id, actualValue);
		}
	}

	private TableModel getTableData() {

		List<Object[]> tableData = new ArrayList<Object[]>();

		List<WordImage> wordImages = glyphDB.getWordImages();

		for (WordImage wordImage : wordImages) {

			Object[] row = new Object[4];
			row[0] = wordImage.getId();
			try {
				row[1] = ImageIO.read(new File(IMAGE_DIR, wordImage.getImageFileName()));
			} catch (IOException e) {
				LOG.error("could not read image file " + wordImage.getImageFileName(), e);
			}
			row[2] = wordImage.getTesseractValue();
			row[3] = wordImage.getActualVale();
			tableData.add(row);

		}

		return new DefaultTableModel(tableData.toArray(new Object[0][0]), new String[] { "Id", "Image", "Tesseract Value", "Actual Value" }) {

			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				Object value = getValueAt(0, columnIndex);
				if (value == null) {
					return String.class;
				}
				return value.getClass();
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if (columnIndex == 3) {
					return true;
				}
				return false;
			}

		};

	}

	// private static class ButtonRenderer implements TableCellRenderer {
	//
	// @Override
	// public Component getTableCellRendererComponent(JTable table,
	// Object value, boolean isSelected, boolean hasFocus, int row,
	// int column) {
	//
	// JButton button = new JButton("Delete");
	//
	// button.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// // table.getv
	// }
	//
	// });
	//
	// return button;
	//
	// }
	// }

}
