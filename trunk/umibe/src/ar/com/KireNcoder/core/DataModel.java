package ar.com.KireNcoder.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import wox.serial.Easy;

import ar.com.KireNcoder.gui.UserIterface;
import ar.com.KireNcoder.monitor.MediaFolderWatcher;
import ar.com.KireNcoder.policies.Policy;
import ar.com.KireNcoder.queue.DistributedQueue;
import ar.com.KireNcoder.queue.GenericQueue;
import ar.com.KireNcoder.queue.LocalQueue;
import ar.com.KireNcoder.util.FileUtils;
import ar.com.umibe.stats.StatGenerator;
import ar.com.umibe.stats.SingleFileStat;

public class DataModel implements Observer {

	public final static DataModel INSTANCE = new DataModel();
	private GenericQueue queue;

	private ArrayList<MediaFolderWatcher> watchedFolders = null;
	private ArrayList<Worker> workers;
	private StatGenerator stats = new StatGenerator();
	private UserIterface ui = null;
	private Settings settings = new Settings();

	private DataModel() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			settings.hostname = addr.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.workers = new ArrayList<Worker>();
		
		this.watchedFolders = new ArrayList<MediaFolderWatcher>();
		loadConfig();
		
		saveConfig();
	}
	
	public SingleFileStat updateStats(VideoFile oldFile, String newFile, double elapsedTimeMin){
		return this.stats.updateStats(oldFile, newFile, elapsedTimeMin);
	}
	
	public void clearStats(){
		this.stats.clear();
	}

	public void stopWorkers() {
		for (int i = 0; i < this.workers.size(); i++) {
			this.workers.get(i).stop();
		}
	}

	public void addWorker() {
		Worker w = new Worker();
		this.workers.add(w);
	}

	public void startWorkers() {
		for (int i = 0; i < this.workers.size(); i++) {
			this.workers.get(i).start();
		}
	}

	public ArrayList<String> getWatchedFolders() {
		ArrayList<String> a = new ArrayList<String>();
		for (int i = 0; i < this.watchedFolders.size(); i++) {
			a.add(this.watchedFolders.get(i).getDirectory());
		}
		return a;
	}

	//Perfil cannonical
	public boolean addWatchedFolder(String mfw, String profile ) {
		String a;
		for (int i = 0; i < this.watchedFolders.size(); i++) {
			a = this.watchedFolders.get(i).getDirectory();
			if (a.equals(mfw)) {
				return false;
			}
		}
		this.watchedFolders.add(new MediaFolderWatcher(mfw, 60, profile));
		saveWatchedFolders();
		return true;
	}
	
	public boolean addWatchedFolder(String mfw) {
		return addWatchedFolder(mfw, mfw + "/config/config.xml");
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

	public boolean addToQueue(VideoFile vf) {
		if (FileUtils.isMediaFile(vf.getRoute()) && !this.queue.exists(vf)) {
			this.queue.put(vf);
			saveQueue();
			return true;
		}
		return false;
	}

	public void deleteFromQueue(VideoFile vf) {
		this.queue.delete(vf);
		saveQueue();
	}

	public void changeVideoStatus(VideoFile vf, Status status) {
		this.queue.changeItemStatus(vf, status);
	}
	
	public void addQueueObserver(Observer o) {
		this.queue.addObserver(o);
	}
	
	public void addStatsObserver(Observer o) {
		this.stats.addObserver(o);
	}

	public VideoFile getEnqueued(Policy p) {
		return this.queue.get(p);
	}

	public ArrayList<VideoFile> getAllJobs() {
		return this.queue.getAllElements();
	}
	
	public void saveConfig() {
		Easy.save(settings, "./config/config.xml");
	}

	public void loadConfig() {
		File f = new File("./config/config.xml");
		if (f.exists()) {
			settings = (Settings)Easy.load("./config/config.xml");
		}
	}

	public void loadQueue(boolean localMode) {
		if(!localMode) {
			this.queue = new DistributedQueue();
		} else {
			this.queue = new LocalQueue();
		}
		addQueueObserver(this);
		
		File f = new File("./config/jobs.xml");
		if (f.exists()) {
			this.queue.put((ArrayList<VideoFile>)Easy.load("./config/jobs.xml"));
		}
	}

	public synchronized void saveQueue() {
		ArrayList<VideoFile> a = this.queue.getEnqueuedElements();
		Easy.save(a,"./config/jobs.xml");
	}

	public void loadWatchedFolders() {
		
		this.watchedFolders = new ArrayList<MediaFolderWatcher>();
		File f = new File("./config/folders.xml");
		if (f.exists()) {
			Document dom = openDocument("config/folders.xml");
			Element docEle = dom.getRootElement();

			List<Element> folders = docEle.getChildren("Folder");

			Iterator<Element> i = folders.iterator();
			while (i.hasNext()) {
				Element e = i.next();
				this.watchedFolders.add(new MediaFolderWatcher(e
						.getAttributeValue("routeToFolder"), Integer.parseInt(e
						.getAttributeValue("pollInterval")), e
						.getAttributeValue("profile")));
			}
		}
	}

	public String[] loadProfiles(String type){
		return FileUtils.filterFiles(settings.profilesDir, type);
	}
	
	public void saveWatchedFolders() {
		Element root = new Element("Folders");
		for (int i = 0; i < this.watchedFolders.size(); i++) {
			Element el = new Element("Folder");
			el.setAttribute("id", String.valueOf(i));
			el.setAttribute("routeToFolder", this.watchedFolders.get(i)
					.getDirectory());
			String s = Integer.toString(this.watchedFolders.get(i)
					.getPollinterval());
			el.setAttribute("pollInterval", s);
			el.setAttribute("profile", this.watchedFolders.get(i).getProfile());
			root.addContent(el);			
		}
		Document doc = new Document(root);
		// serialize it onto System.out
		try {
			XMLOutputter serializer = new XMLOutputter();
			Format f = serializer.getFormat();
			f.setIndent("  ");
			serializer.setFormat(f);
			FileWriter fileWriter = new FileWriter("./config/folders.xml");
			serializer.output(doc, fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private Document openDocument(final String route) {
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();

		try {
			return builder.build(route);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public String getVProfile() {
		return settings.vProfile;
	}

	public void setVProfile(String s) {
		settings.vProfile = s;
		saveConfig();
	}

	public String getAProfile() {
		return settings.aProfile;
	}

	public void setAProfile(String s) {
		settings.aProfile = s;
		saveConfig();
	}

	public String getAviSynthProfile() {
		return settings.aviSynthProfile;
	}

	public void setAviSynthProfile(String s) {
		settings.aviSynthProfile = s;
		saveConfig();
	}

	public String getTempDir() {
		return settings.tempDir;
	}

	public void setTempDir(String s) {
		settings.tempDir = s;
		saveConfig();
	}

	public String getMoveAfterDone() {
		return settings.moveAfterDone;
	}

	public void setMoveAfterDone(String s) {
		settings.moveAfterDone = s;
		saveConfig();
	}

	
	public String getDoneDir() {
		return settings.doneDir;
	}

	public void setDoneDir(String s) {
		settings.doneDir = s;
		saveConfig();
	}

	public int getPriority() {
		return settings.priority;
	}

	public void setPriority(int priority) {
		settings.priority = priority;
		saveConfig();
	}
	
	public String getHostname(){
		return settings.hostname;
	}

	@Override
	public void update(Observable o, Object arg) {
		saveQueue();		
	}

	/**
	 * @return the stats
	 */
	public StatGenerator getStats() {
		return stats;
	}
	
	public UserIterface getUi() {
		return ui;
	}

	public void setUi(UserIterface ui) {
		this.ui = ui;
	}
}
