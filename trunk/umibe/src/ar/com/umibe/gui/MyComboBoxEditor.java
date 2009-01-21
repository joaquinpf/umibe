package ar.com.umibe.gui;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class MyComboBoxEditor extends DefaultCellEditor {
    public MyComboBoxEditor(String[] items) {
        super(new JComboBox(items));
    }
}