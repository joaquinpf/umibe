package ar.com.umibe.core.gui.tests;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.EventObject;

public class JSliderEditor extends AbstractCellEditor implements TableCellEditor {

    protected JSlider slider = null;

    public JSliderEditor() {
        slider = new JSlider();
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(25);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Integer val = (Integer)value;
        slider.setValue(val.intValue());
        return slider;
    }

    public Object getCellEditorValue() {
        return new Integer(slider.getValue());
    }
}