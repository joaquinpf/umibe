package ar.com.umibe.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import wox.serial.Easy;
import ar.com.umibe.commons.util.UmibeFileUtils;
import ar.com.umibe.core.matroska.MatroskaUtils;
import ar.com.umibe.core.monitor.MediaFolderWatcher;
import ar.com.umibe.core.policies.Policy;
import ar.com.umibe.core.queue.DistributedQueue;
import ar.com.umibe.core.queue.GenericQueue;
import ar.com.umibe.core.queue.LocalQueue;
import ar.com.umibe.core.stats.SingleFileStat;
import ar.com.umibe.core.stats.StatGenerator;
import ar.com.umibe.core.tool.Tool;
import ar.com.umibe.gui.UserIterface;
import ar.com.umibe.util.VideoUtils;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class DataModel implements Observer {

	public final static DataModel INSTANCE = new DataModel();
	private GenericQueue queue;
	public Hashtable<String, Tool> tools = new Hashtable<String, Tool>();

	private ArrayList<MediaFolderWatcher> watchedFolders = null;
	private ArrayList<Worker> workers;
	private StatGenerator stats = new StatGenerator();
	private UserIterface ui = null;
	private Settings settings = new Settings();

	private DataModel() {

		this.workers = new ArrayList<Worker>();

		this.watchedFolders = new ArrayList<MediaFolderWatcher>();
		loadConfig();
		loadTools();
		try{
			VideoUtils.setMediainfo(getTool("MediaInfo.exe").getPath());
			VideoUtils.setMplayer(getTool("mplayer.exe").getPath());
			MatroskaUtils.setMkvextract(getTool("mkvextract.exe").getPath());
			MatroskaUtils.setMkvinfo(getTool("mkvinfo.exe").getPath());
			MatroskaUtils.setMkvmerge(getTool("mkvmerge.exe").getPath());
		}catch (Exception e) {
			System.out.println("Faltan tools");
		}
	}

    public void resetSettings() {
        this.settings = new Settings();
        saveConfig();
    }
	
	public SingleFileStat updateStats(VideoTask oldFile, String newFile, double elapsedTimeMin){
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
		MediaFolderWatcher mediafolder = new MediaFolderWatcher(mfw, 60, profile);
		mediafolder.start();
		this.watchedFolders.add(mediafolder);
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

	public boolean addToQueue(String file, String profile) {
		VideoTask vf = new VideoTask(file, profile);
		if (UmibeFileUtils.isMediaFile(file) && !this.queue.exists(vf)) {
			this.queue.put(vf);
			saveQueue();
			return true;
		}
		return false;
	}

	public void deleteFromQueue(VideoTask vt) {
		this.queue.delete(vt);
		saveQueue();
	}

	public void changeVideoStatus(VideoTask vf, Status status) {
		this.queue.changeItemStatus(vf, status);
	}
	
	public void addQueueObserver(Observer o) {
		this.queue.addObserver(o);
	}
	
	public void addStatsObserver(Observer o) {
		this.stats.addObserver(o);
	}

	public VideoTask getEnqueued(Policy p) {
		return this.queue.get(p);
	}

	public ArrayList<VideoTask> getAllJobs() {
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

	@SuppressWarnings("unchecked")
	public void loadQueue(boolean localMode) {
		if(!localMode) {
			this.queue = new DistributedQueue();
		} else {
			this.queue = new LocalQueue();
		}
		addQueueObserver(this);
		
		File f = new File("./config/jobs.xml");
		if (f.exists()) {
			this.queue.put((ArrayList<VideoTask>)Easy.load("./config/jobs.xml"));
		}
	}

	public synchronized void saveQueue() {
		ArrayList<VideoTask> a = this.queue.getEnqueuedElements();
		Easy.save(a,"./config/jobs.xml");
	}

	@SuppressWarnings("unchecked")
	public void loadWatchedFolders() {
		
		this.watchedFolders = new ArrayList<MediaFolderWatcher>();
		File f = new File("./config/folders.xml");
		if (f.exists()) {
			this.watchedFolders = (ArrayList<MediaFolderWatcher>) Easy.load("./config/folders.xml");
			if (watchedFolders == null)
				watchedFolders = new ArrayList<MediaFolderWatcher>();
			for(int i=0;i<watchedFolders.size();i++){
				watchedFolders.get(i).start();
			}
		}
	}

	public String[] loadProfiles(String type){
		return UmibeFileUtils.filterFiles(settings.profilesDir, type);
	}

	public String[] loadFilters(){
		return UmibeFileUtils.filterFiles(settings.filtersDir, "");
	}
	
	public void saveWatchedFolders() {
		Easy.save(watchedFolders, "./config/folders.xml");
	}

	public void loadTools(){
		String[] xmls = UmibeFileUtils.filterFiles("./tools/", "xml");
		if(xmls!=null){
			for(int i=0; i<xmls.length; i++){
				Tool t = new Tool("./tools/" + xmls[i]);
				tools.put(t.getName(),t);
			}
		}
	}
	
	public Tool getTool(String tool) {
		return tools.get(tool);
	}
	
	public ArrayList<Tool> getToolsByType(String type){
		ArrayList<Tool> a = new ArrayList<Tool>();
		Enumeration<String> enu = tools.keys();
		while(enu.hasMoreElements()){
			Tool t = tools.get(enu.nextElement());
			if(t.getType().equals(type)){
				a.add(t);
			}
		}
		return a;
	}
	
	public String getVProfile() {
		return settings.vProfile;
	}

	public String getAProfile() {
		return settings.aProfile;
	}

	public String getAviSynthProfile() {
		return settings.aviSynthProfile;
	}

	public String getTempDir() {
		return settings.tempDir;
	}
	
	public String getProfilesDir() {
		return settings.profilesDir;
	}

	public void setTempDir(String s) {
		settings.tempDir = s;
		saveConfig();
	}

	public String getMoveAfterDone() {
		return settings.moveAfterDone;
	}
	
	public String getDoneDir() {
		return settings.doneDir;
	}

	public int getPriority() {
		return settings.priority;
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

	public boolean getKeepOriginalAudio() {
		return settings.keepOriginalAudio;
	}

	public boolean getKeepOriginalVideo() {
		return settings.keepOriginalVideo;
	}

	public String getBuildID() {
		return settings.buildID;
	}
}
