package ar.com.umibe.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import ar.com.umibe.core.Status;
import ar.com.umibe.core.VideoTask;
import ar.com.umibe.util.UmibeFileUtils;

public class FileTableRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -7957223427596792537L;

	/** Creates a new instance of LocaleRenderer */
	public FileTableRenderer() {
	}

	public Component getTableCellRendererComponent (JTable table, 
			Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
		Component cell = super.getTableCellRendererComponent(
				table, obj, isSelected, hasFocus, row, column);
		if(column == 1 && obj instanceof VideoTask){
			setText(UmibeFileUtils.getFileName(((VideoTask)obj).getRoute()));
		}
		if(obj instanceof String || obj instanceof VideoTask){
			String evaluate = (String)table.getValueAt(row, 0);
			if (evaluate.equals(Status.WAITING.toString())) {
				setBackground(Color.white);
			} else if (evaluate.equals(Status.ENCODING.toString())) {
				setBackground(Color.lightGray);
			} else if (evaluate.equals(Status.DONE.toString())) {
				setBackground(Color.green);
			} else if (evaluate.equals(Status.FAILED.toString())) {
				setBackground(Color.red);
			} else if (evaluate.equals(Status.MOVED.toString())) {
				setBackground(Color.yellow);
			}
		}
		else {
			setBackground(Color.white);
		}
		if (isSelected)
			setBackground(Color.black);
		return this;
	}
}
