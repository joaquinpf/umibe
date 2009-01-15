package ar.com.umibe.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import ar.com.umibe.core.DataModel;
import ar.com.umibe.core.Status;
import ar.com.umibe.core.VideoFile;

public class FileTable extends DragAndDropTable implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -293303729609358987L;

	private boolean profileable = false;
	
	public FileTable() {
		super();		
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		model.addColumn("Status");
		model.addColumn("Filename");
		model.addColumn("Owner Host");
		TableColumn column = getColumnModel().getColumn(0); 
		column.setPreferredWidth(5); 
		column = getColumnModel().getColumn(1); 
		column.setPreferredWidth(300); 
		column = getColumnModel().getColumn(2); 
		column.setPreferredWidth(5); 
		
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		getTableHeader().setResizingAllowed(false);
		setDefaultRenderer (Object.class, new FileTableRenderer());
		refreshList();
		DataModel.INSTANCE.addQueueObserver(this);
		
		model.addTableModelListener(new FileTableListener());
	}
	
	public VideoFile getSelectedItem(){
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		return (VideoFile)model.getValueAt(getSelectedRow(), 1);
	}
	
	@Override
	public void drop(DropTargetDropEvent event) {

		try {
			Transferable transferable = event.getTransferable();

			// we accept only Strings
			if (transferable
					.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {

				event.acceptDrop(DnDConstants.ACTION_MOVE);
				List s = (List) transferable
						.getTransferData(DataFlavor.javaFileListFlavor);
				Iterator iter = s.iterator();
				String profile = null;
				if(profileable == true){
					ProfileSelectorDialog p = new ProfileSelectorDialog(new JFrame(),true);
					p.setVisible(true);
					profile = p.getSelectedProfile();
				}
	
				while (iter.hasNext()) {
					File file = (File) iter.next();
					if (file.isFile()) {
						System.out.println("GOT FILE: "
								+ file.getCanonicalPath());
						if(profileable == true) {
							DataModel.INSTANCE.addToQueue(new VideoFile(file
									.getCanonicalPath(),profile));
						} else {
							DataModel.INSTANCE.addToQueue(new VideoFile(file
									.getCanonicalPath()));
						}
					}
				}
				event.getDropTargetContext().dropComplete(true);
			} else {
				event.rejectDrop();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
			System.err.println("Exception" + exception.getMessage());
			event.rejectDrop();
		} catch (UnsupportedFlavorException ufException) {
			ufException.printStackTrace();
			System.err.println("Exception" + ufException.getMessage());
			event.rejectDrop();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		refreshList();
	}
	public void refreshList() {
		ArrayList<VideoFile> a = DataModel.INSTANCE.getAllJobs();
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		model.setRowCount(0);
		for (int i=0; i<model.getRowCount(); i++){
			model.removeRow(i);
		}
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).getStatus() != Status.DELETED) {
				VideoFile vf = a.get(i);
				Object row [] = {vf.getStatus().toString(),vf,vf.getOwnerHost()};
				model.addRow(row);
			}
		}
		padding(model);
		this.setModel(model);
		repaint();
	}
	
	private void padding(DefaultTableModel d){
		int rows = d.getRowCount();
		if(rows<1){
			Object row [] = {};
			d.addRow(row);
		}
	}

	public void setProfileable(boolean profileable) {
		this.profileable = profileable;
	}

	public boolean isProfileable() {
		return profileable;
	}
}
