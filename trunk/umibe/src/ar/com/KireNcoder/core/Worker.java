package ar.com.KireNcoder.core;

import java.io.File;
import java.util.ArrayList;

import ar.com.KireNcoder.matroska.MatroskaUtils;
import ar.com.KireNcoder.matroska.Track;
import ar.com.KireNcoder.matroska.TracksInfoParser;
import ar.com.KireNcoder.policies.AndPolicy;
import ar.com.KireNcoder.policies.DiskSpacePolicy;
import ar.com.KireNcoder.policies.OrPolicy;
import ar.com.KireNcoder.policies.Policy;
import ar.com.KireNcoder.policies.SimplePolicy;
import ar.com.KireNcoder.policies.UNCPolicy;
import ar.com.KireNcoder.util.AviSynthUtils;
import ar.com.KireNcoder.util.FileUtils;

public class Worker implements Runnable {

	private Thread workerThread;
	
	private Encoder vEncoder = null;
	private Encoder aEncoder = null;

	private static boolean verbosity = true;
	
	private VideoFile current;
	
	private String tempDir;
	
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
				
				String filename = FileUtils
						.getFileName(this.current.getRoute());
				String fullPath = FileUtils
				.getFullPath(this.current.getRoute());
				
				//Generacion de directorio temporal
				tempDir = DataModel.INSTANCE.getTempDir() + Integer.toString(this.hashCode()) + "/";
				File temp = new File(tempDir);
				if(!temp.exists()){
					temp.mkdir();
				}

				//Mkvizacion
				String MKVFile = tempDir + FileUtils.getFileName(fullPath) + ".mkv";
				MatroskaUtils.MKVize(fullPath, MKVFile, verbosity);
				
				//Extraccion de informacion de MKV
				String infoFile = tempDir + FileUtils.getFileName(fullPath) + "_info.txt";
				MatroskaUtils.generateInfo(MKVFile, infoFile);
				TracksInfoParser tip = new TracksInfoParser(infoFile, tempDir);
				
				//Generacion de script de video. Encoding de video
				encodeVideo(fullPath);
				
				//Audio, encodea cada track del idioma JPN o todas si no hay ninguna JPN
				ArrayList<Track> audioTracks = encodeAudio(tip,fullPath,MKVFile);
				
				//Extraccion de chapters
				MatroskaUtils.extractChapters(MKVFile, tempDir);
				
				//Demux de tracks de subtitulos
				MatroskaUtils.demux(MKVFile, tempDir, tip.getTracks("subtitles"), verbosity);
				
				//Merge
				MatroskaUtils.merge(tempDir+ "video.mkv", audioTracks, 
						filename, tempDir, this.current.getOutputFolder(),
						tip, verbosity);
				
				//Borrado de temporales
				FileUtils.cleanUpDirectory(tempDir);
				temp.delete();
				
				//Actualizacion de cola y estadisticas

				float elapsedTimeMin = (System.currentTimeMillis() - startTime)/(60*1000F);
				
				DataModel.INSTANCE.updateStats(current, this.current.getOutputFolder() + filename + ".mkv",
						elapsedTimeMin);
				DataModel.INSTANCE.saveQueue();
				
				//Move after done
				//FIXME No funciono :)
				if(!DataModel.INSTANCE.getMoveAfterDone().equals(" ")) {
					FileUtils.move(fullPath, DataModel.INSTANCE.getMoveAfterDone() + FileUtils.getFileName(MKVFile));
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

	private void encodeVideo(String fullPath){
		String script = AviSynthUtils.generateVideoScript(fullPath,
				tempDir, this.current.getAviSynthProfile());
		this.vEncoder = new Encoder(this.current.getVProfile(), false);
		vEncoder.encode(script,tempDir + "video.mkv");
		this.vEncoder = null;
	}
	
	private ArrayList<Track> encodeAudio(TracksInfoParser tip, String fullPath, String MKVFile){
		
		//Audio, encodea cada track del idioma JPN o todas si no hay ninguna JPN
		ArrayList<Track> audioTracks = tip.getTracks("audio","jpn");
		//Si no se pudieron extraer los tracks (no se pudo mkvizar)
		if(audioTracks == null) {
			//Generacion de script de audio. Encoding de audio
			String script = AviSynthUtils.generateAudioScript(fullPath,
					tempDir, this.current.getAviSynthProfile());
			this.aEncoder = new Encoder(this.current.getAProfile(), true);
			aEncoder.pipedEncode(script,tempDir + "encodedaudio_" + 0 + ".m4a");
			this.aEncoder = null;	
		} else {
			if(audioTracks.size() == 0) {
				audioTracks = tip.getTracks("audio");
			}
			MatroskaUtils.demux(MKVFile, tempDir, audioTracks , verbosity);
			String[] audios = FileUtils.filterFiles(tempDir, "audio");

			for(int i=0; i<audios.length; i++){
				//Generacion de script de audio. Encoding de audio
				MatroskaUtils.mux(FileUtils.getFullPath(tempDir + audios[i]),FileUtils.getFullPath(tempDir + audios[i]) + ".mka");
				String script = AviSynthUtils.generateAudioScript(FileUtils.getFullPath(tempDir + audios[i]) + ".mka",
						tempDir, this.current.getAviSynthProfile());
				this.aEncoder = new Encoder(this.current.getAProfile(), true);
				aEncoder.pipedEncode(script,tempDir + "encodedaudio_" + i + ".m4a");
				this.aEncoder = null;
			}
		}
		return audioTracks;
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
				File temp = new File(tempDir);
				FileUtils.cleanUpDirectory(tempDir);
				temp.delete();
			}
		}
	}
}
