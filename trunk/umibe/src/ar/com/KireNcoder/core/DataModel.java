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

public class DataModel implements Observer {

	public final static DataModel INSTANCE = new DataModel();
	private GenericQueue queue;

	private String tempDir = "./temp/";
	private String doneDir = "./done/";
	private String moveAfterDone = " ";
	private String vProfile = "profiles/Video_X264_AnimeHD_1PassCQ.xml";
	private String aProfile = "profiles/Audio_NeroAACEnc_CBR_64kbps.xml";
	private String aviSynthProfile = "profiles/AviSynth_Default.xml";
	private int priority = 3;
	private ArrayList<MediaFolderWatcher> watchedFolders;
	private ArrayList<Worker> workers;
	private String hostname;
	private StatGenerator stats = new StatGenerator();
	private UserIterface ui = null;
	private String profilesDir = "./profiles/";

	private DataModel() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.workers = new ArrayList<Worker>();
		
		this.watchedFolders = new ArrayList<MediaFolderWatcher>();
		loadConfig();
		
		saveConfig();
	}
	
	public Stats updateStats(VideoFile oldFile, String newFile, double elapsedTimeMin){
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
		Element root = new Element("Config");
		Element el = new Element("DoneDirectory");
		el.setText(this.doneDir);
		root.addContent(el);
		el = new Element("TempDirectory");
		el.setText(this.tempDir);
		root.addContent(el);
		el = new Element("MoveToAfterDone");
		el.setText(this.moveAfterDone);
		root.addContent(el);
		el = new Element("DefaultAudioProfile");
		el.setText(this.aProfile);
		root.addContent(el);
		el = new Element("DefaultVideoProfile");
		el.setText(this.vProfile);
		root.addContent(el);
		el = new Element("DefaultAviSynthProfile");
		el.setText(this.aviSynthProfile);
		root.addContent(el);
		el = new Element("DefaultPriority");
		el.setText(Integer.toString(this.priority));
		root.addContent(el);

		Document doc = new Document(root);
		// serialize it onto System.out
		try {
			XMLOutputter serializer = new XMLOutputter();
			Format f = serializer.getFormat();
			f.setIndent("  ");
			serializer.setFormat(f);
			FileWriter fileWriter = new FileWriter("./config/config.xml");
			serializer.output(doc, fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void loadConfig() {
		File f = new File("./config/config.xml");
		if (f.exists()) {
			XMLConfigLoader xml = new XMLConfigLoader("config/config.xml");
			String s = xml.getNodeText("DoneDirectory");
			if (s != null) {
				this.doneDir = s;
			}
			s = xml.getNodeText("DefaultAudioProfile");
			if (s != null) {
				this.aProfile = s;
			}
			s = xml.getNodeText("DefaultVideoProfile");
			if (s != null) {
				this.vProfile = s;
			}
			s = xml.getNodeText("DefaultAviSynthProfile");
			if (s != null) {
				this.aviSynthProfile = s;
			}
			s = xml.getNodeText("DefaultPriority");
			if (s != null) {
				this.priority = Integer.parseInt(s);
			}
			s = xml.getNodeText("MoveToAfterDone");
			if (s != null) {
				this.moveAfterDone = s;
			}
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
		
		/*File f = new File("./config/jobs.xml");
		if (f.exists()) {
			Document dom = openDocument("config/jobs.xml");
			Element docEle = dom.getRootElement();

			List<Element> jobs = docEle.getChildren("Job");

			Iterator<Element> i = jobs.iterator();
			while (i.hasNext()) {
				Element e = i.next();
				this.queue.put(new VideoFile(e.getAttributeValue("routeToFile"), 
						e.getAttributeValue("vProfile"), e.getAttributeValue("aProfile"), 
						e.getAttributeValue("aviSynthProfile"), e.getAttributeValue("doneFolder"),
						e.getAttributeValue("moveAfterDone"), Integer.parseInt(e.getAttributeValue("priority")),
						this.hostname));
			}
		}*/
	}

	public synchronized void saveQueue() {
		ArrayList<VideoFile> a = this.queue.getEnqueuedElements();
		Easy.save(a,"./config/jobs.xml");
		/*Element root = new Element("Jobs");
		for (int i = 0; i < a.size(); i++) {
			if(a.get(i).getOwnerHost().equals(this.hostname)){
				Element el = new Element("Job");
				el.setAttribute("id", String.valueOf(i));
				el.setAttribute("routeToFile", a.get(i).getRoute());
				el.setAttribute("aProfile", a.get(i).getAProfile());
				el.setAttribute("vProfile", a.get(i).getVProfile());
				el.setAttribute("aviSynthProfile", a.get(i).getAviSynthProfile());
				el.setAttribute("priority", Integer
						.toString(a.get(i).getPriority()));
				el.setAttribute("doneFolder", a.get(i).getOutputFolder());
				el.setAttribute("moveAfterDone", a.get(i).getMoveAfterDone());
				root.addContent(el);
			}
		}
		Document doc = new Document(root);
		// serialize it onto System.out
		try {
			XMLOutputter serializer = new XMLOutputter();
			Format f = serializer.getFormat();
			f.setIndent("  ");
			serializer.setFormat(f);
			FileWriter fileWriter = new FileWriter("./config/jobs.xml");
			serializer.output(doc, fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(e);
		}*/
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
		return FileUtils.filterFiles(profilesDir, type);
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
		return this.vProfile;
	}

	public void setVProfile(String s) {
		this.vProfile = s;
		saveConfig();
	}

	public String getAProfile() {
		return this.aProfile;
	}

	public void setAProfile(String s) {
		this.aProfile = s;
		saveConfig();
	}

	public String getAviSynthProfile() {
		return this.aviSynthProfile;
	}

	public void setAviSynthProfile(String s) {
		this.aviSynthProfile = s;
		saveConfig();
	}

	public String getTempDir() {
		return this.tempDir;
	}

	public void setTempDir(String s) {
		this.tempDir = s;
		saveConfig();
	}

	public String getMoveAfterDone() {
		return this.moveAfterDone;
	}

	public void setMoveAfterDone(String s) {
		this.moveAfterDone = s;
		saveConfig();
	}

	
	public String getDoneDir() {
		return this.doneDir;
	}

	public void setDoneDir(String s) {
		this.doneDir = s;
		saveConfig();
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
		saveConfig();
	}
	
	public String getHostname(){
		return this.hostname;
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
