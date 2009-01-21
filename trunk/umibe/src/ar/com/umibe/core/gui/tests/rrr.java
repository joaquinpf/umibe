package ar.com.umibe.core.gui.tests;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class rrr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    JTable table = new JTable();
	    DefaultTableModel model = (DefaultTableModel)table.getModel();
	    
	    // Add some columns
	    model.addColumn("A", new Object[]{"item1"});
	    model.addColumn("B", new Object[]{"item2"});
	    
	    // These are the combobox values
	    String[] values = new String[]{"item1", "item2", "item3"};
	    
	    // Set the combobox editor on the 1st visible column
	    int vColIndex = 0;
	    TableColumn col = table.getColumnModel().getColumn(vColIndex);
	    col.setCellEditor(new MyComboBoxEditor(values));
	    
	    // If the cell should appear like a combobox in its
	    // non-editing state, also set the combobox renderer
	    col.setCellRenderer(new MyComboBoxRenderer(values));
	    
	    JFrame j = new JFrame();
	    j.add(table);
	    j.setVisible(true);
	}

}
