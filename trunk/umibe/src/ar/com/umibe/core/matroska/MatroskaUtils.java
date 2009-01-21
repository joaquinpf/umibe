package ar.com.umibe.core.matroska;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;

import ar.com.umibe.core.EncodingVideo;
import ar.com.umibe.core.MediaTrack;
import ar.com.umibe.core.execution.IExecutionEnvironment;
import ar.com.umibe.core.execution.WindowsCLIEnvironment;
import ar.com.umibe.util.UmibeFileUtils;

public class MatroskaUtils implements IContainer{

	private static String mkvmerge = "./resources/mkvtools/mkvmerge.exe";
	private static String mkvinfo = "./resources/mkvtools/mkvinfo.exe";
	private static String mkvextract = "./resources/mkvtools/mkvextract.exe";
	
	public static int MKVize(String input, String output, boolean verbosity) {
		String tool = UmibeFileUtils.addComillas(UmibeFileUtils
				.getFullPath(mkvmerge))
				+ " -o ";
		output = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(output));
		input = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(input));

		IExecutionEnvironment clienv = new WindowsCLIEnvironment();
		return clienv.execute(tool + output + input, verbosity, false);		
	}

	public ArrayList<MediaTrack> demux(String input, String outputFolder, ArrayList<InfoTrack> tracks, boolean verbosity) {
		if(tracks!= null && tracks.size()>0){
			String files = " ";
			ArrayList<MediaTrack> outputTracks = new ArrayList<MediaTrack>();
			for(int i=0; i<tracks.size(); i++) {
				String outFile = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(outputFolder 
						+ tracks.get(i).getTrackType() + "_" + i)) + " ";
				files += tracks.get(i).getTrackNumber() + ":" + outFile;
				MediaTrack m = new MediaTrack();
				m.setInfoTrack(tracks.get(i));
				m.setRouteToTrack(UmibeFileUtils.getFullPath(outputFolder + tracks.get(i).getTrackType() + "_" + i));
				outputTracks.add(m);
			}
			String tool = UmibeFileUtils.addComillas(UmibeFileUtils
					.getFullPath(mkvextract))
					+ " tracks ";
			input = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(input));
			
			IExecutionEnvironment clienv = new WindowsCLIEnvironment();
			clienv.execute(tool + input + files, verbosity, false);
			
			return outputTracks;
		} else {
			return new ArrayList<MediaTrack>();
		}
	}

	public static void generateInfo(String input, String infoFile) {
		File f = new File(input);
		if(f.exists()){
			String tool = UmibeFileUtils.addComillas(UmibeFileUtils
					.getFullPath(mkvinfo))
					+ " ";

			infoFile = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(infoFile));

			input = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(input));

			IExecutionEnvironment clienv = new WindowsCLIEnvironment();
			clienv.execute(tool + input + " > " + infoFile, true, false);
		}
	}
	
	public String extractChapters(String input, String outputFolder) {
		String tool = UmibeFileUtils.addComillas(UmibeFileUtils
				.getFullPath(mkvextract))
				+ " chapters ";
		String output = UmibeFileUtils.getFullPath(outputFolder + UmibeFileUtils.getFileName(input) + "_chapters.xml");
		
		input = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(input));
		
		IExecutionEnvironment clienv = new WindowsCLIEnvironment();
		clienv.execute(tool + input + " > " + UmibeFileUtils.addComillas(output), false, false);
		
		return output;
	}

	@Override
	public void merge(EncodingVideo outputvideo, String dirToMux,
			String doneDir, boolean verbosity) {
		
		String chapters = outputvideo.getChapters();
		
		String files = " ";
		String chaps = " ";

		if (chapters != null) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(chapters));

				String str = in.readLine();
				if(str != null && !str.contains("(mkvextract) The file "))
					chaps += " --chapters " + UmibeFileUtils.addComillas(chapters);
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ArrayList<MediaTrack> audioTracks = outputvideo.getAudioTracks();
		if(audioTracks != null){
			for(int i=0; i<audioTracks.size(); i++) {
				String audioOptions = " ";
				MediaTrack m = audioTracks.get(i);
				if(m.getInfoTrack()!=null){
					audioOptions = " --language 1:" + m.getInfoTrack().getLanguage() +
					" --track-name 1:" + UmibeFileUtils.addComillas(m.getInfoTrack().getName()) + " ";
				}
				files += audioOptions + UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(m.getRouteToTrack()));
			}
		}

		ArrayList<MediaTrack> videoTracks = outputvideo.getVideoTracks();
		if(videoTracks != null){
			for(int i=0; i<videoTracks.size(); i++) {
				String videoOptions = " ";
				MediaTrack m = videoTracks.get(i);
				if(m.getInfoTrack()!=null){
					videoOptions = " --language 1:" + m.getInfoTrack().getLanguage() +
					" --track-name 1:" + UmibeFileUtils.addComillas(m.getInfoTrack().getName()) + " ";
				}
				files += videoOptions + UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(m.getRouteToTrack()));
			}
		}
		
		ArrayList<MediaTrack> subtitleTracks = outputvideo.getSubtitleTracks();
		for(int i=0; i<subtitleTracks.size(); i++) {
			String subOptions = " ";
			MediaTrack m = subtitleTracks.get(i);
			if(m.getInfoTrack()!=null){
				String name;
				if(m.getInfoTrack().getName() == null){
					Locale l = new Locale(m.getInfoTrack().getLanguage());
					name = l.getDisplayName(new Locale("eng"));
				} else {
					name = UmibeFileUtils.addComillas(m.getInfoTrack().getName());
				}
				subOptions = " --language 0:" + m.getInfoTrack().getLanguage() +
							" --track-name 0:" + name + " ";
			}
			files += subOptions + UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(m.getRouteToTrack()));
		}
		
		/*for (int i = 0; i < subs.length; i++) {
				String subOptions = " ";
				if(subtitleTracks!=null && subtitleTracks.size()>0){
					//FIXME locale no funca como quisiera
					String name;
					if(subtitleTracks.get(i).getName() == null){
						Locale l = new Locale(subtitleTracks.get(i).getLanguage());
						name = l.getDisplayName(new Locale("eng"));
					} else {
						name = UmibeFileUtils.addComillas(subtitleTracks.get(i).getName());
					}
					subOptions = " --language 0:" + subtitleTracks.get(i).getLanguage() +
					" --track-name 0:" + name + " ";
				}
				files += subOptions + UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(dirToMux
						+ subs[i]));
		}*/

		String title = " --title " + UmibeFileUtils.addComillas(outputvideo.getFilename() + ": Encoded with KireNcoder");
		
		if (files.equals(" ")) {
			// No files to mux
		} else {
			String tool = UmibeFileUtils
					.addComillas(UmibeFileUtils
							.getFullPath(mkvmerge))
					+ " -o ";
			String output = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(doneDir
					+ outputvideo.getFilename() + ".mkv"));

			IExecutionEnvironment clienv = new WindowsCLIEnvironment();
			clienv.execute(tool + output + title + files + chaps, verbosity, false);
		}
	}
	
	public void mux(String fullPath, String output) {
		String tool = UmibeFileUtils
				.addComillas(UmibeFileUtils
						.getFullPath(mkvmerge))
				+ " -o ";

		IExecutionEnvironment clienv = new WindowsCLIEnvironment();
		clienv.execute(tool + UmibeFileUtils.addComillas(output) + UmibeFileUtils.addComillas(fullPath), true, false);
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
