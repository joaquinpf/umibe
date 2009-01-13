package ar.com.umibe.gui;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import ar.com.umibe.core.DataModel;

public class FileTableListener implements TableModelListener {

	public void tableChanged(TableModelEvent e) {
		((MainWindow)DataModel.INSTANCE.getUi()).turnDndFileSign();
	}
}

