package ar.com.umibe.gui;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import ar.com.umibe.core.DataModel;
import ar.com.umibe.core.VideoTask;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class FileTableListener implements TableModelListener {

	public void tableChanged(TableModelEvent e) {
		((MainWindow)DataModel.INSTANCE.getUi()).turnDndFileSign();
        int row = e.getFirstRow();
        int column = e.getColumn();
        DefaultTableModel model = (DefaultTableModel)e.getSource();
		if(column == 5) {
			VideoTask vt = (VideoTask)model.getValueAt(row, 1);
			vt.setEnabled(((Boolean)model.getValueAt(row, column)));			
		} else if(column == 4) {
			VideoTask vt = (VideoTask)model.getValueAt(row, 1);
			vt.setPriority((Integer)model.getValueAt(row, column));
		} else if(column == 3) {
			VideoTask vt = (VideoTask)model.getValueAt(row, 1);
			vt.loadProfiles(DataModel.INSTANCE.getProfilesDir() + ((String)model.getValueAt(row, column)));
		}
	}
}

