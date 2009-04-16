package ar.com.regexmatching.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wox.serial.Easy;


import ar.com.umibe.core.monitor.MediaFolderWatcher;
import ar.com.umibe.core.xml.XMLFolderLoader;
import ar.com.umibe.util.UmibeFileUtils;


/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class FolderWatcher {

	private ArrayList<MediaFolderWatcher> watchedFolders = null;

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
		MediaFolderWatcher mediafolder = new MediaFolderWatcher(mfw, 60);
		
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

	@SuppressWarnings("unchecked")
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
