package ar.com.feedactionizer.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import wox.serial.Easy;


/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class DownloadFeeder {

	public final static  DownloadFeeder INSTANCE = new DownloadFeeder();
	private ArrayList<GenericFeedActionizer> feeds = null;
	private Set<String> retrievedFiles = new HashSet<String>();

	private DownloadFeeder() {
		loadRetrievedFiles();
		loadFeeds();	
	}

	public void start(){
		for(GenericFeedActionizer feed: feeds){
			feed.start();
		}	
	}
	
	public void stop(){
		for(GenericFeedActionizer feed: feeds){
			feed.stop();
		}
		saveRetrievedFiles();
	}

	public void loadFeeds() {
		this.feeds = new XMLFeedLoader("./config/feeds.xml").getFolders();
	}
	
	public Set<String> getRetrievedFiles() {
		return retrievedFiles;
	}

	public void setRetrievedFiles(Set<String> retrievedFiles) {
		this.retrievedFiles = retrievedFiles;
	}
	
	public void addRetrievedFile(String retrievedFile) {
		this.retrievedFiles.add(retrievedFile);
	}
	
	public boolean alreadyRetrieved(String retrievedFile) {
		return this.retrievedFiles.contains(retrievedFile);
	}
	
	private void saveRetrievedFiles() {
		Easy.save(retrievedFiles, "./config/retrieved.xml");		
	}

	@SuppressWarnings("unchecked")
	public void loadRetrievedFiles() {
		File f = new File("./config/retrieved.xml");
		if (f.exists()) {
			retrievedFiles = (HashSet<String>) Easy.load("./config/retrieved.xml");
		}
	}
}
