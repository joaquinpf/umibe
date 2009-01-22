package ar.com.umibe.core.tool;

import javax.swing.JTable;

public class ToolOptionsTable extends JTable {

	private static final long serialVersionUID = -8849771725724293423L;

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
