package ar.com.umibe.stats;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

import org.grlea.log.SimpleLogger;

import wox.serial.Easy;
import ar.com.KireNcoder.core.DataModel;
import ar.com.KireNcoder.core.VideoFile;
import ar.com.KireNcoder.util.FileUtils;



public class StatGenerator extends Observable {
	
	private ArrayList<SingleFileStat> sessionStats;
	private GlobalStats globalStats;
	private String globalRoute = "./config/globalstats.xml";
	private String fileRoute = "./config/filestats.xml";
	private static final SimpleLogger log = new SimpleLogger(StatGenerator.class);
	
	public StatGenerator(){
		loadStats();
	}
	
	public synchronized SingleFileStat updateStats(VideoFile oldFile, String newFile, double elapsedTimeMin){	
		File fOld = new File(oldFile.getRoute());
		File fNew = new File(newFile);
		if(fOld.exists() && fNew.exists()){
			this.globalStats.setElapsedTime(this.globalStats.getElapsedTime() + elapsedTimeMin);
			this.globalStats.setTranscoded(this.globalStats.getTranscoded() + 1);
			if(oldFile.getOwnerHost().equals(DataModel.INSTANCE.getHostname())){
				this.globalStats.setLocalJobsDone(this.globalStats.getLocalJobsDone() + 1);
			} else {
				this.globalStats.setRemoteJobsDone(this.globalStats.getRemoteJobsDone() + 1);
			}
			//Tama�os en gigas
			double oldsize = fOld.length();
			double newsize = fNew.length();
			double divisor = 1024;
			oldsize = ((oldsize / divisor) / divisor) / divisor;
			newsize = ((newsize / divisor) / divisor) / divisor;

			this.globalStats.setOriginalSize(this.globalStats.getOriginalSize() + oldsize);
			this.globalStats.setEncodedSize(this.globalStats.getEncodedSize() + newsize);
			this.globalStats.setGbSaved(this.globalStats.getOriginalSize() - this.globalStats.getEncodedSize());
			this.globalStats.setCompRatio(this.globalStats.getOriginalSize() / this.globalStats.getEncodedSize());

			SingleFileStat s = new SingleFileStat();
			s.setElapsedTime(elapsedTimeMin);
			s.setEncodedSize(newsize);
			s.setOriginalSize(oldsize);
			s.setGbSaved(oldsize - newsize);
			s.setCompRatio(oldsize/newsize);
			s.setRouteToOwner(oldFile.getRoute());
			
			sessionStats.add(s);
			
			log.info("FINISHED ENCODING: " + FileUtils.getFileName(oldFile.getRoute()));
			log.info(s.toString());
			
			saveStats();
			
			setChanged();
			notifyObservers(s);
			
			return s;
		}
		return null;
	}
	
	private void saveStats(){
		Easy.save(globalStats, "./config/globalstats.xml");
		Easy.save(sessionStats, "./config/filestats.xml");
	}

	private void loadStats(){
		File f = new File(globalRoute);
		if (f.exists()) {
			globalStats = (GlobalStats) Easy.load(globalRoute);
			if(globalStats == null) {
				globalStats = new GlobalStats();
			}
		}
		f = new File(fileRoute);
		if (f.exists()) {
			sessionStats = (ArrayList<SingleFileStat>) Easy.load(fileRoute);
			if(sessionStats == null) {
				sessionStats = new ArrayList<SingleFileStat>();
			}
		}
	}
	
	public synchronized void clear(){
		sessionStats = new ArrayList<SingleFileStat>();
		globalStats = new GlobalStats();
		saveStats();
		setChanged();
		notifyObservers();
	}

	public GlobalStats getGlobalStats() {
		return globalStats;
	}

	public void setGlobalStats(GlobalStats globalStats) {
		this.globalStats = globalStats;
	}
}
