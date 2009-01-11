package ar.com.KireNcoder.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import ar.com.KireNcoder.core.Status;
import ar.com.KireNcoder.core.VideoFile;
import ar.com.KireNcoder.util.FileUtils;

public class FileTableRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -7957223427596792537L;

	/** Creates a new instance of LocaleRenderer */
	public FileTableRenderer() {
	}

	public Component getTableCellRendererComponent (JTable table, 
			Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
		Component cell = super.getTableCellRendererComponent(
				table, obj, isSelected, hasFocus, row, column);
		if(column == 1 && obj instanceof VideoFile){
			setText(FileUtils.getFileName(((VideoFile)obj).getRoute()));
		}
		if(obj instanceof String || obj instanceof VideoFile){
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
