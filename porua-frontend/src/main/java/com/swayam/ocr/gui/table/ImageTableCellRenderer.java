package com.swayam.ocr.gui.table;

import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ImageTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	if ((value != null) && !(value instanceof BufferedImage)) {
	    throw new IllegalArgumentException("Expecting type " + BufferedImage.class + ", but got " + value.getClass());
	}
	JLabel label = new JLabel();
	label.setOpaque(true);

	BufferedImage img = (BufferedImage) value;
	label.setIcon(new ImageIcon(img));

	return label;

    }

}