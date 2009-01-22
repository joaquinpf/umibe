package ar.com.umibe.gui;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class MyComboBoxEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -3055572583579131308L;

	public MyComboBoxEditor(String[] items) {
        super(new JComboBox(items));
    }
}