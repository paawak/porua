package com.swayam.ocr.gui.table;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class IndicTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	if ((value != null) && !(value instanceof String)) {
	    throw new IllegalArgumentException("Expecting type " + String.class + ", but got " + value.getClass());
	}
	JLabel label = new JLabel();
	label.setOpaque(true);

	label.setFont(new Font("SolaimanLipi", Font.PLAIN, 18));

	String text = (String) value;
	label.setText(text);

	return label;

    }

}