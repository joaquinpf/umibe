package ar.com.umibe.core.filter;
import javax.swing.JTable;


public class FilterDefaultTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5477151842555084808L;

	public boolean isCellEditable(int rowIndex, int vColIndex) {
		if(vColIndex == 0 || vColIndex == 1){
			return false;
		}
		return true;
	}
}
