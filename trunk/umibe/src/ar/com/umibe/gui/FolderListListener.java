package ar.com.umibe.gui;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import ar.com.umibe.core.DataModel;

	public class FolderListListener implements ListDataListener {

		@Override
		public void contentsChanged(ListDataEvent e) {
			((MainWindow)DataModel.INSTANCE.getUi()).turnDndFolderSign();
		}

		@Override
		public void intervalAdded(ListDataEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			// TODO Auto-generated method stub
			
		}}

