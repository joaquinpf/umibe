package ar.com.umibe.core;

import java.io.File;

import ar.com.umibe.core.matroska.IContainer;
import ar.com.umibe.core.matroska.MatroskaUtils;
import ar.com.umibe.core.matroska.TracksInfoParser;
import ar.com.umibe.core.policies.AndPolicy;
import ar.com.umibe.core.policies.DiskSpacePolicy;
import ar.com.umibe.core.policies.OrPolicy;
import ar.com.umibe.core.policies.Policy;
import ar.com.umibe.core.policies.SimplePolicy;
import ar.com.umibe.core.policies.UNCPolicy;
import ar.com.umibe.util.UmibeFileUtils;

public class Worker implements Runnable {

	private Thread workerThread;
	
	private Encoder vEncoder = null;
	private Encoder aEncoder = null;

	private static boolean verbosity = true;
	
	private VideoTask current = null;
	
	private File tempDir = null;
	
	private Policy policy;
	
	public Worker() {
		DiskSpacePolicy d = new DiskSpacePolicy();
		UNCPolicy u = new UNCPolicy();
		SimplePolicy s = new SimplePolicy("OwnerHost",DataModel.INSTANCE.getHostname());
		SimplePolicy s2 = new SimplePolicy("Enabled","true");
		OrPolicy o = new OrPolicy(s,u);
		AndPolicy a = new AndPolicy(o,d);
		AndPolicy a2 = new AndPolicy(a,s2);
		this.policy = a2;
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
				
				EncodingVideo currentev = new EncodingVideo(filename);
				
				//Generacion de directorio temporal
				String tempDirPath = generateTempDir();

				IContainer containerTools = new MatroskaUtils();
				
				//Mkvizacion
				String MKVFile = tempDirPath + UmibeFileUtils.getFileName(fullPath) + ".mkv";
				int mkvResult = MatroskaUtils.MKVize(fullPath, MKVFile, verbosity);
				if(mkvResult == 0) {
					fullPath = MKVFile;
				}
								
				//Extraccion de informacion de MKV
				String infoFile = tempDirPath + UmibeFileUtils.getFileName(fullPath) + "_info.txt";
				MatroskaUtils.generateInfo(MKVFile, infoFile);
				TracksInfoParser tip = new TracksInfoParser(infoFile, tempDirPath);
				
				//Encoding de video
				vEncoder = new VideoEncoder(this.current.getVProfile(), 
						this.current.getAviSynthProfile(), tempDirPath, true); 
				currentev.setVideoTracks(vEncoder.encode(fullPath, tip));
				vEncoder = null;
				
				//Audio, encodea cada track del idioma JPN o todas si no hay ninguna JPN
				aEncoder = new AudioEncoder(this.current.getAProfile(), 
						this.current.getAviSynthProfile(), tempDirPath, 
						this.current.isKeepOriginalAudio(), true); 
				currentev.setAudioTracks(aEncoder.encode(fullPath, tip));
				aEncoder = null;
				
				//Extraccion de chapters
				currentev.setChapters(containerTools.extractChapters(MKVFile, tempDirPath));
				
				//Demux de tracks de subtitulos
				currentev.setSubtitleTracks(containerTools.demux(MKVFile, tempDirPath, tip.getTracks("subtitles"), verbosity));
				
				//Merge
				containerTools.merge(currentev, tempDirPath, this.current.getOutputFolder(), verbosity);
				
				//Borrado de temporales
				UmibeFileUtils.cleanUpDirectory(tempDir);
				tempDir.delete();
				
				//Actualizacion de cola y estadisticas

				float elapsedTimeMin = (System.currentTimeMillis() - startTime)/(60*1000F);
				
				DataModel.INSTANCE.updateStats(current, this.current.getOutputFolder() + filename + ".mkv",
						elapsedTimeMin);
				DataModel.INSTANCE.saveQueue();
				
				//Move after done
				
				if(current.getMoveAfterDone().equals(" ") == false) {
					UmibeFileUtils.move(current.getRoute(), current.getMoveAfterDone());
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
