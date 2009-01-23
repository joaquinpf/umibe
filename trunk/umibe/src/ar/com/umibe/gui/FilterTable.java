package ar.com.umibe.gui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class FilterTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -293303729609358987L;
		
	public FilterTable() {
		super();		
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		model.addColumn("Filter");
		model.addColumn("Type");
		
		setCombobox();
 
		TableColumn column = getColumnModel().getColumn(0); 
		column.setPreferredWidth(350); 
		column = getColumnModel().getColumn(1); 
		column.setPreferredWidth(100); 
		
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		getTableHeader().setResizingAllowed(false);
		
		model.addTableModelListener(new FileTableListener());
	}
	
	private void setCombobox(){
        String[] profiles = {"Video", "Audio", "Audio/Video"};
	    
	    // Set the combobox editor on the 1st visible column
	    int vColIndex = 1;
	    TableColumn col = this.getColumnModel().getColumn(vColIndex);
	    col.setCellEditor(new MyComboBoxEditor(profiles));
	    
	    // If the cell should appear like a combobox in its
	    // non-editing state, also set the combobox renderer
	    col.setCellRenderer(new MyComboBoxRenderer(profiles));
	}
	
    public boolean isCellEditable(int rowIndex, int vColIndex) {
    	if(vColIndex == 1){
    		return true;
    	} else {
    		return false;
    	}
    }
}
