package ar.com.umibe.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import ar.com.umibe.core.matroska.MatroskaUtils;
import ar.com.umibe.core.matroska.InfoTrack;
import ar.com.umibe.core.matroska.TracksInfoParser;
import ar.com.umibe.core.policies.AndPolicy;
import ar.com.umibe.core.policies.DiskSpacePolicy;
import ar.com.umibe.core.policies.OrPolicy;
import ar.com.umibe.core.policies.Policy;
import ar.com.umibe.core.policies.SimplePolicy;
import ar.com.umibe.core.policies.UNCPolicy;
import ar.com.umibe.util.AviSynthUtils;
import ar.com.umibe.util.UmibeFileUtils;

public class Worker implements Runnable {

	private Thread workerThread;
	
	private Encoder vEncoder = null;
	private Encoder aEncoder = null;

	private static boolean verbosity = true;
	
	private VideoFile current = null;
	
	private File tempDir = null;
	
	private Policy policy;
	
	public Worker() {
		DiskSpacePolicy d = new DiskSpacePolicy();
		UNCPolicy u = new UNCPolicy();
		SimplePolicy s = new SimplePolicy("OwnerHost",DataModel.INSTANCE.getHostname());
		OrPolicy o = new OrPolicy(s,u);
		AndPolicy a = new AndPolicy(o,d);
		this.policy = a;
	}
	
	@Override
	public void run() {
		while (true) {
			this.current = DataModel.INSTANCE.getEnqueued(this.policy);

			File f = new File(this.current.getRoute());
			if (f.exists()) {

				long startTime = System.currentTimeMillis();
				
				String filename = UmibeFileUtils
						.getFileName(this.current.getRoute());
				String fullPath = UmibeFileUtils
						.getFullPath(this.current.getRoute());
				
				//Generacion de directorio temporal
				String tempDirPath = generateTempDir();

				//Mkvizacion
				String MKVFile = tempDir + UmibeFileUtils.getFileName(fullPath) + ".mkv";
				int mkvResult = MatroskaUtils.MKVize(fullPath, MKVFile, verbosity);
				if(mkvResult == 0) {
					fullPath = MKVFile;
				}
								
				//Extraccion de informacion de MKV
				String infoFile = tempDir + UmibeFileUtils.getFileName(fullPath) + "_info.txt";
				MatroskaUtils.generateInfo(MKVFile, infoFile);
				TracksInfoParser tip = new TracksInfoParser(infoFile, tempDirPath);
				
				//Encoding de video
				vEncoder = new VideoEncoder(this.current.getVProfile(), 
						this.current.getAviSynthProfile(), tempDirPath, true); 
				ArrayList<MediaTrack> videoTracks = vEncoder.encode(fullPath, tip);
				vEncoder = null;
				
				//Audio, encodea cada track del idioma JPN o todas si no hay ninguna JPN
				aEncoder = new AudioEncoder(this.current.getAProfile(), 
						this.current.getAviSynthProfile(), tempDirPath, true); 
				ArrayList<MediaTrack> audioTracks = aEncoder.encode(fullPath, tip);
				aEncoder = null;
				
				//Extraccion de chapters
				MatroskaUtils.extractChapters(MKVFile, tempDirPath);
				
				//Demux de tracks de subtitulos
				MatroskaUtils.demux(MKVFile, tempDirPath, tip.getTracks("subtitles"), verbosity);
				
				//Merge
				MatroskaUtils.merge(videoTracks, audioTracks, 
						filename, tempDirPath, this.current.getOutputFolder(),
						tip, verbosity);
				
				//Borrado de temporales
				UmibeFileUtils.cleanUpDirectory(tempDir);
				tempDir.delete();
				
				//Actualizacion de cola y estadisticas

				float elapsedTimeMin = (System.currentTimeMillis() - startTime)/(60*1000F);
				
				DataModel.INSTANCE.updateStats(current, this.current.getOutputFolder() + filename + ".mkv",
						elapsedTimeMin);
				DataModel.INSTANCE.saveQueue();
				
				//Move after done
				if(!DataModel.INSTANCE.getMoveAfterDone().equals(" ")) {
					UmibeFileUtils.move(fullPath, DataModel.INSTANCE.getMoveAfterDone());
				}
				
				DataModel.INSTANCE.changeVideoStatus(current, Status.DONE);
				
				this.current = null;
				this.tempDir = null;

			} else {
				DataModel.INSTANCE.changeVideoStatus(current, Status.MOVED);
				this.current = null;
				DataModel.INSTANCE.saveQueue();
			}
		}
	}
	
	private String generateTempDir() {
		String temp = DataModel.INSTANCE.getTempDir() + Integer.toString(this.hashCode()) 
			+ UmibeFileUtils.getFileName(this.current.getRoute()) + "/";
		tempDir = new File(temp);
		if(!tempDir.exists()){
			tempDir.mkdirs();
		}
		return temp;		
	}

	public void start() {
		if (this.workerThread == null) {
			this.workerThread = new Thread(this);
			this.workerThread.start();
		}
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		if (this.workerThread != null) {
			this.workerThread.stop();
			this.workerThread = null;
			if (this.aEncoder != null) {
				this.aEncoder.stop();
			}
			if (this.vEncoder != null) {
				this.vEncoder.stop();
			}
			if (this.current != null) {
				DataModel.INSTANCE.changeVideoStatus(current, Status.WAITING);
			}
			if (this.tempDir != null) {
				UmibeFileUtils.cleanUpDirectory(tempDir);
				tempDir.delete();
			}
		}
	}
}
