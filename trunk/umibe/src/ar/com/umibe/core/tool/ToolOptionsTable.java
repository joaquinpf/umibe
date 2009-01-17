package ar.com.umibe.core.tool;

import javax.swing.JTable;

public class ToolOptionsTable extends JTable {
	
        public boolean isCellEditable(int rowIndex, int vColIndex) {
        	if(vColIndex == 0 || vColIndex == 1)
        		return false;
        	if(vColIndex == 3){
        		ToolOption t = (ToolOption)getModel().getValueAt(rowIndex, 0);
        		if(t.mandatory == true){
        			return false;
        		}
        	}
        	return true;
        }
}
