package ar.com.umibe.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import ar.com.umibe.commons.util.UmibeFileUtils;
import ar.com.umibe.core.Status;
import ar.com.umibe.core.VideoTask;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
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
			setHorizontalAlignment( LEFT );
		} else {
			setHorizontalAlignment( CENTER );
		}
		if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } 
		else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
		if(obj instanceof String || obj instanceof VideoTask || obj instanceof Boolean ||
				obj instanceof Integer || obj instanceof JComboBox){
			String evaluate = (String)table.getValueAt(row, 0);
			if (evaluate.equals(Status.ENCODING.toString())) {
				setBackground(Color.lightGray);
			} else if (evaluate.equals(Status.DONE.toString())) {
				setBackground(Color.green);
			} else if (evaluate.equals(Status.FAILED.toString())) {
				setBackground(Color.red);
			} else if (evaluate.equals(Status.MOVED.toString())) {
				setBackground(Color.yellow);
			}
		}
		return this;
	}
}
