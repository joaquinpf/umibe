package ar.com.umibe.core.gui.tests;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JSliderRenderer extends JSlider implements TableCellRenderer {
    public JSliderRenderer(String[] items) {
        super();
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        // Select the current value
        setValue((Integer)value);
        return this;
    }
}