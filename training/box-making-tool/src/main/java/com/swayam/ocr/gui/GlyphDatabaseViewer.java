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
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.swayam.ocr.core.matcher.GlyphStore;
import com.swayam.ocr.core.matcher.HsqlGlyphStore;
import com.swayam.ocr.core.util.BinaryImage;
import com.swayam.ocr.core.util.Glyph;
import com.swayam.ocr.core.util.Script;

/**
 * 
 * @author paawak
 */
public class GlyphDatabaseViewer extends JPanel {

    private static final long serialVersionUID = 1L;

    public GlyphDatabaseViewer() {
	init();
    }

    private void init() {

	setLayout(new BorderLayout());

	JScrollPane scrpn = new JScrollPane();
	add(scrpn, BorderLayout.CENTER);
	JTable table = new JTable(getTableData());
	scrpn.setViewportView(table);

	table.setDefaultRenderer(BufferedImage.class, new GlyphRenderer());
	table.setRowHeight(60);

    }

    private TableModel getTableData() {

	GlyphStore glyphDB = HsqlGlyphStore.INSTANCE;

	List<Object[]> tableData = new ArrayList<Object[]>();

	List<Glyph> glyphs = glyphDB.getGlyphs(Script.BANGLA);

	for (Glyph glyph : glyphs) {

	    Object[] row = new Object[4];
	    row[0] = glyph.getId();
	    row[1] = glyph.getUnicodeText();
	    BinaryImage glyphImage = glyph.getImage();
	    row[2] = glyphImage.getImage();
	    row[3] = null;// new ImageDigester(glyphImage).digest().getImage();
	    tableData.add(row);

	}

	return new DefaultTableModel(tableData.toArray(new Object[0][0]), new String[] { "Id", "Character", "Glyph", "Skeleton" }) {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	    }

	    @Override
	    public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	    }

	};

    }

    private static class GlyphRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

	    JLabel label = new JLabel();
	    label.setOpaque(true);

	    BufferedImage img = (BufferedImage) value;
	    label.setIcon(new ImageIcon(img));

	    return label;

	}

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
