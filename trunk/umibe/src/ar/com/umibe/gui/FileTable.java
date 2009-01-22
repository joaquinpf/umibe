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
import ar.com.umibe.core.VideoTask;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class FileTable extends DragAndDropTable implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -293303729609358987L;
	
	private boolean filled = false;	
	private boolean profileable = false;
	
	public FileTable() {
		super();		
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		model.addColumn("Status");
		model.addColumn("Filename");
		model.addColumn("Filesize");
		model.addColumn("Owner Host");
		model.addColumn("Selected Profile");
		model.addColumn("Priority");
		model.addColumn("Enabled");
		model.addColumn("Video");
		model.addColumn("Audio");
		
		setCombobox();
 
		TableColumn column = getColumnModel().getColumn(0); 
		column.setPreferredWidth(20); 
		column = getColumnModel().getColumn(1); 
		column.setPreferredWidth(350); 
		column = getColumnModel().getColumn(2); 
		column.setPreferredWidth(4); 
		column = getColumnModel().getColumn(3); 
		column.setPreferredWidth(35); 
		column = getColumnModel().getColumn(4); 
		column.setPreferredWidth(100); 
		column = getColumnModel().getColumn(5); 
		column.setPreferredWidth(1); 
		column = getColumnModel().getColumn(6); 
		column.setPreferredWidth(1); 
		column = getColumnModel().getColumn(7); 
		column.setPreferredWidth(1); 
		column = getColumnModel().getColumn(8); 
		column.setPreferredWidth(1); 
		
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		getTableHeader().setResizingAllowed(false);
		setDefaultRenderer (Object.class, new FileTableRenderer());
		refreshList();
		DataModel.INSTANCE.addQueueObserver(this);
		
		model.addTableModelListener(new FileTableListener());
	}
	
	private void setCombobox(){
        String[] profiles = DataModel.INSTANCE.loadProfiles("profile_");
	    
	    // Set the combobox editor on the 1st visible column
	    int vColIndex = 4;
	    TableColumn col = this.getColumnModel().getColumn(vColIndex);
	    col.setCellEditor(new MyComboBoxEditor(profiles));
	    
	    // If the cell should appear like a combobox in its
	    // non-editing state, also set the combobox renderer
	    col.setCellRenderer(new MyComboBoxRenderer(profiles));
	}
	
	@SuppressWarnings("unchecked")
	public Class getColumnClass (int columna) { 
		try{ 
			if (columna == 5) 
				return Integer.class; 
			if (columna == 6 || columna == 7 || columna == 8) 
				return Boolean.class; 
			else {
				return Object.class;
			}
		} catch (Exception e) { 
			return Object.class; 
		} 
	}
	
    public boolean isCellEditable(int rowIndex, int vColIndex) {
    	if(vColIndex >= 3){
    		return true;
    	} else {
    		return false;
    	}
    }
    
	public VideoTask getSelectedItem(){
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		return (VideoTask)model.getValueAt(getSelectedRow(), 1);
	}
	
	@SuppressWarnings("unchecked")
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
							DataModel.INSTANCE.addToQueue(file.getCanonicalPath(),profile);
						} else {
							DataModel.INSTANCE.addToQueue(file
									.getCanonicalPath(), DataModel.INSTANCE.getProfilesDir() + "Profile_Default.xml");
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
		ArrayList<VideoTask> a = DataModel.INSTANCE.getAllJobs();
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		model.setRowCount(0);
		for (int i=0; i<model.getRowCount(); i++){
			model.removeRow(i);
		}
		for (int i = 0; i < a.size(); i++) {
			filled = true;
			if (a.get(i).getStatus() != Status.DELETED) {
				VideoTask vf = a.get(i);
				Object row [] = {vf.getStatus().toString(),vf,vf.getFilesize() + "MB",vf.getOwnerHost(),
						vf.getProfile().replaceAll(DataModel.INSTANCE.getProfilesDir(),""),vf.getPriority(),
						vf.getEnabled(),!vf.isKeepOriginalVideo(),!vf.isKeepOriginalAudio()};
				model.addRow(row);
			}
		}
		if(a.size() == 0){
			filled = false;
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

	public boolean isFilled() {
		return filled;
	}
}
