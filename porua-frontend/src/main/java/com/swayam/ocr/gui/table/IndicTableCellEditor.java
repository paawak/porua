package com.swayam.ocr.gui.table;

import java.awt.Component;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.TableCellEditor;

import com.swayam.bhasha.min.IndicPaneMin;
import com.swayam.bhasha.utils.PropertyFileUtils;

@SuppressWarnings("serial")
public class IndicTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final String BANGLA_PROPERTY_FILE = "/keymappings/KeyboardSettings_bn_IN.def";

    private static final Map<String, String> INDIC_MAP;

    static {
	try {
	    List<String> lines = Files.readAllLines(Paths.get(IndicTableCellEditor.class.getResource(BANGLA_PROPERTY_FILE).toURI()));
	    StringBuilder contents = new StringBuilder(1000);
	    lines.forEach((String line) -> {
		contents.append(line).append("\n");
	    });
	    INDIC_MAP = new PropertyFileUtils().getKeyMap(contents.toString(), false);
	} catch (IOException | URISyntaxException e) {
	    throw new RuntimeException("could not parse indic property file", e);
	}
    }

    private final JTextPane textComponent;

    public IndicTableCellEditor() {
	textComponent = new IndicPaneMin(INDIC_MAP, IndicPaneMin.BANGLA_LOCALE);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	if ((value != null) && !(value instanceof String)) {
	    throw new IllegalArgumentException("Expecting type " + String.class + ", but got " + value.getClass());
	}
	textComponent.setText((String) value);
	return textComponent;
    }

    @Override
    public Object getCellEditorValue() {
	return textComponent.getText();
    }

}
