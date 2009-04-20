package ar.com.regexmatching.core;

import java.util.ArrayList;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class FolderWatcher {

	private ArrayList<RuledFolderWatcher> watchedFolders = null;

	private FolderWatcher() {
		loadWatchedFolders();
	}

	public ArrayList<String> getWatchedFolders() {
		ArrayList<String> a = new ArrayList<String>();
		for (int i = 0; i < this.watchedFolders.size(); i++) {
			a.add(this.watchedFolders.get(i).getDirectory());
		}
		return a;
	}

	//Perfil cannonical
	public boolean addWatchedFolder(String mfw) {
		String a;
		for (int i = 0; i < this.watchedFolders.size(); i++) {
			a = this.watchedFolders.get(i).getDirectory();
			if (a.equals(mfw)) {
				return false;
			}
		}
		RuledFolderWatcher mediafolder = new RuledFolderWatcher(mfw, 60);
		
		mediafolder.start();
		this.watchedFolders.add(mediafolder);
		saveWatchedFolders();
		return true;
	}

	public void removeWatchedFolder(String folder) {
		String a;
		for (int i = 0; i < this.watchedFolders.size(); i++) {
			a = this.watchedFolders.get(i).getDirectory();
			if (a.equals(folder)) {
				this.watchedFolders.remove(i);
				saveWatchedFolders();
				return;
			}
		}
	}

	public void loadWatchedFolders() {
		this.watchedFolders = new XMLFolderLoader("./config/folders.xml").getFolders();
	}
	
	public void saveWatchedFolders() {
		System.out.println("NOT YET IMPLEMENTED");
	}
	
	public static void main(String[] args) {
		new FolderWatcher();
	}
	
}
