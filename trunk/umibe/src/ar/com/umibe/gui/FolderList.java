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

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import ar.com.umibe.core.DataModel;

public class FolderList extends DragAndDropList {

	private boolean profileable = false;
	
	private static final long serialVersionUID = 2415837270096685436L;

	public FolderList() {
		super();
		this.setModel(new DefaultListModel());
		ArrayList<String> a = DataModel.INSTANCE.getWatchedFolders();
		for (int i = 0; i < a.size(); i++) {
			this.addElement(a.get(i));
		}
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
					if (file.isDirectory()) {
						System.out.println("GOT DIR: "
								+ file.getCanonicalPath());
						if(profileable == true) {
							if (DataModel.INSTANCE.addWatchedFolder(file
									.getCanonicalPath(), profile) == true) {
								addElement(file.getCanonicalPath());
							}
						} else {
							if (DataModel.INSTANCE.addWatchedFolder(file
									.getCanonicalPath()) == true) {
								addElement(file.getCanonicalPath());
							}
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

	public void setProfileable(boolean profileable) {
		this.profileable = profileable;
	}

	public boolean isProfileable() {
		return profileable;
	}
}
