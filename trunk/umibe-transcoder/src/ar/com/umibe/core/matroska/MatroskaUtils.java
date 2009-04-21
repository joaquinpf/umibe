package ar.com.umibe.core.matroska;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;

import ar.com.umibe.commons.util.StringUtils;
import ar.com.umibe.commons.util.UmibeFileUtils;
import ar.com.umibe.core.DataModel;
import ar.com.umibe.core.EncodingVideo;
import ar.com.umibe.core.MediaTrack;
import ar.com.umibe.core.execution.IExecutionEnvironment;
import ar.com.umibe.core.execution.UmibeWindowsCLIEnvironment;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class MatroskaUtils implements IContainer{

	private static String mkvmerge = "./resources/mkvtools/mkvmerge.exe";
	private static String mkvinfo = "./resources/mkvtools/mkvinfo.exe";
	private static String mkvextract = "./resources/mkvtools/mkvextract.exe";
	
	public static int MKVize(String input, String output, boolean verbosity) {
		String tool = StringUtils.addComillas(UmibeFileUtils
				.getFullPath(mkvmerge))
				+ " -o ";
		output = StringUtils.addComillas(UmibeFileUtils.getFullPath(output));
		input = StringUtils.addComillas(UmibeFileUtils.getFullPath(input));

		IExecutionEnvironment clienv = new UmibeWindowsCLIEnvironment();
		return clienv.execute(tool + output + input, verbosity, false);		
	}

	public ArrayList<MediaTrack> demux(String input, String outputFolder, ArrayList<InfoTrack> tracks, boolean verbosity) {
		if(tracks!= null && tracks.size()>0){
			String files = " ";
			ArrayList<MediaTrack> outputTracks = new ArrayList<MediaTrack>();
			for(int i=0; i<tracks.size(); i++) {
				String outFile = StringUtils.addComillas(UmibeFileUtils.getFullPath(outputFolder 
						+ tracks.get(i).getTrackType() + "_" + i)) + " ";
				files += tracks.get(i).getTrackNumber() + ":" + outFile;
				MediaTrack m = new MediaTrack();
				m.setInfoTrack(tracks.get(i));
				m.setRouteToTrack(UmibeFileUtils.getFullPath(outputFolder + tracks.get(i).getTrackType() + "_" + i));
				outputTracks.add(m);
			}
			String tool = StringUtils.addComillas(UmibeFileUtils
					.getFullPath(mkvextract))
					+ " tracks ";
			input = StringUtils.addComillas(UmibeFileUtils.getFullPath(input));
			
			IExecutionEnvironment clienv = new UmibeWindowsCLIEnvironment();
			clienv.execute(tool + input + files, verbosity, false);
			
			return outputTracks;
		} else {
			return new ArrayList<MediaTrack>();
		}
	}

	public static void generateInfo(String input, String infoFile) {
		File f = new File(input);
		if(f.exists()){
			String tool = StringUtils.addComillas(UmibeFileUtils
					.getFullPath(mkvinfo))
					+ " ";

			infoFile = StringUtils.addComillas(UmibeFileUtils.getFullPath(infoFile));

			input = StringUtils.addComillas(UmibeFileUtils.getFullPath(input));

			IExecutionEnvironment clienv = new UmibeWindowsCLIEnvironment();
			clienv.execute(tool + input + " > " + infoFile, true, false);
		}
	}
	
	public String extractChapters(String input, String outputFolder) {
		String tool = StringUtils.addComillas(UmibeFileUtils
				.getFullPath(mkvextract))
				+ " chapters ";
		String output = UmibeFileUtils.getFullPath(outputFolder + UmibeFileUtils.getFileName(input) + "_chapters.xml");
		
		input = StringUtils.addComillas(UmibeFileUtils.getFullPath(input));
		
		IExecutionEnvironment clienv = new UmibeWindowsCLIEnvironment();
		clienv.execute(tool + input + " > " + StringUtils.addComillas(output), false, false);
		
		return output;
	}

	@Override
	public void merge(EncodingVideo outputvideo, String dirToMux,
			String doneDir, boolean verbosity) {
		
		String chapters = outputvideo.getChapters();
		
		String files = " ";
		String chaps = " ";

		if(outputvideo.getRoute().endsWith("mkv")){
			files = "--noaudio --novideo --nosubs --nobuttons " + 
			StringUtils.addComillas(outputvideo.getRoute());
		} else {
			if (chapters != null) {
				try {
					BufferedReader in = new BufferedReader(new FileReader(chapters));

					String str = in.readLine();
					if(str != null && !str.contains("(mkvextract) The file "))
						chaps += " --chapters " + StringUtils.addComillas(chapters);
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		ArrayList<MediaTrack> audioTracks = outputvideo.getAudioTracks();
		if(audioTracks != null){
			for(int i=0; i<audioTracks.size(); i++) {
				String audioOptions = " ";
				MediaTrack m = audioTracks.get(i);
				if(m.getInfoTrack()!=null){
					audioOptions = " --language 1:" + m.getInfoTrack().getLanguage() +
					" --track-name 1:" + StringUtils.addComillas(m.getInfoTrack().getName()) + " ";
				}
				files += audioOptions + StringUtils.addComillas(UmibeFileUtils.getFullPath(m.getRouteToTrack()));
			}
		}
		
		ArrayList<MediaTrack> videoTracks = outputvideo.getVideoTracks();
		if(videoTracks != null){
			for(int i=0; i<videoTracks.size(); i++) {
				String videoOptions = " ";
				MediaTrack m = videoTracks.get(i);
				if(m.getInfoTrack()!=null){
					videoOptions = " --language 1:" + m.getInfoTrack().getLanguage() +
					" --track-name 1:" + StringUtils.addComillas(m.getInfoTrack().getName()) + " ";
					if(m.getInfoTrack().getDisplayH() != 0 && m.getInfoTrack().getDisplayW() != 0){
						videoOptions += "--aspect-ratio 1:" + Integer.toString(m.getInfoTrack().getDisplayW()) + 
						"/" + Integer.toString(m.getInfoTrack().getDisplayH()) + " ";
					}					
				}
				files += videoOptions + StringUtils.addComillas(UmibeFileUtils.getFullPath(m.getRouteToTrack()));
			}
		}
		
		ArrayList<MediaTrack> subtitleTracks = outputvideo.getSubtitleTracks();
		for(int i=0; i<subtitleTracks.size(); i++) {
			String subOptions = " ";
			MediaTrack m = subtitleTracks.get(i);
			if(m.getInfoTrack()!=null){
				String name;
				if(m.getInfoTrack().getName() == null){
					//FIXME locale no funca como quisiera
					Locale l = new Locale(m.getInfoTrack().getLanguage());
					name = l.getDisplayName(new Locale("eng"));
				} else {
					name = StringUtils.addComillas(m.getInfoTrack().getName());
				}
				subOptions = " --language 0:" + m.getInfoTrack().getLanguage() +
							" --track-name 0:" + name + " ";
			}
			files += subOptions + StringUtils.addComillas(UmibeFileUtils.getFullPath(m.getRouteToTrack()));
		}

		String title = " --title " + StringUtils.addComillas(outputvideo.getFilename() + ": Encoded with " + DataModel.INSTANCE.getBuildID());
		
		if (files.replaceAll(" ","").equals("")) {
			// No files to mux
		} else {
			String tool = StringUtils
					.addComillas(UmibeFileUtils
							.getFullPath(mkvmerge))
					+ " -o ";
			String output = StringUtils.addComillas(UmibeFileUtils.getFullPath(doneDir
					+ outputvideo.getFilename() + ".mkv"));

			IExecutionEnvironment clienv = new UmibeWindowsCLIEnvironment();
			clienv.execute(tool + output + title + files + chaps, verbosity, false);
		}
	}
	
	public void mux(String fullPath, String output) {
		String tool = StringUtils
				.addComillas(UmibeFileUtils
						.getFullPath(mkvmerge))
				+ " -o ";

		IExecutionEnvironment clienv = new UmibeWindowsCLIEnvironment();
		clienv.execute(tool + StringUtils.addComillas(output) + StringUtils.addComillas(fullPath), true, false);
	}

	public static void setMkvmerge(String mkvmerge) {
		MatroskaUtils.mkvmerge = mkvmerge;
	}

	public static void setMkvinfo(String mkvinfo) {
		MatroskaUtils.mkvinfo = mkvinfo;
	}

	public static void setMkvextract(String mkvextract) {
		MatroskaUtils.mkvextract = mkvextract;
	}
}
