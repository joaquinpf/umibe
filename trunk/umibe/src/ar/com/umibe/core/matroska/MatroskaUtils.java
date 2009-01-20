package ar.com.umibe.core.matroska;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;

import ar.com.umibe.core.MediaTrack;
import ar.com.umibe.core.execution.IExecutionEnvironment;
import ar.com.umibe.core.execution.WindowsCLIEnvironment;
import ar.com.umibe.util.UmibeFileUtils;

public class MatroskaUtils {

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

	public static ArrayList<MediaTrack> demux(String input, String outputFolder, ArrayList<InfoTrack> tracks, boolean verbosity) {
		if(tracks!= null && tracks.size()>0){
			String files = " ";
			ArrayList<MediaTrack> outputTracks = new ArrayList<MediaTrack>();
			for(int i=0; i<tracks.size(); i++) {
				String outFile = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(outputFolder 
						+ tracks.get(i).getTrackType() + "_" + i)) + " ";
				files += tracks.get(i).getTrackNumber() + ":" + outFile;
				MediaTrack m = new MediaTrack();
				m.setInfoTrack(tracks.get(i));
				m.setRouteToTrack(outFile);
			}
			String tool = UmibeFileUtils.addComillas(UmibeFileUtils
					.getFullPath(mkvextract))
					+ " tracks ";
			input = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(input));
			
			IExecutionEnvironment clienv = new WindowsCLIEnvironment();
			clienv.execute(tool + input + files, verbosity, false);
			
			return outputTracks;
		} else {
			return null;
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
	
	public static String extractChapters(String input, String outputFolder) {
		String tool = UmibeFileUtils.addComillas(UmibeFileUtils
				.getFullPath(mkvextract))
				+ " chapters ";
		String output = outputFolder + UmibeFileUtils.getFileName(input) + "_chapters.xml";
		output = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(output));
		
		input = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(input));
		
		IExecutionEnvironment clienv = new WindowsCLIEnvironment();
		clienv.execute(tool + input + " > " + output, false, false);
		
		return output;
	}
	
	public static void merge(ArrayList<MediaTrack> videoTracks, ArrayList<MediaTrack> audioTracks, String filename,
			String dirToMux, String doneDir, TracksInfoParser tracks, boolean verbosity) {

		String[] subs = UmibeFileUtils.filterFiles(dirToMux, "subtitles_", "sub");
		
		String[] chapters = UmibeFileUtils.filterFiles(dirToMux, "_chapters");
		
		String files = " ";
		String chaps = " ";

		if (chapters.length>0) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(UmibeFileUtils.getFullPath(
						dirToMux + chapters[0])));

				String str = in.readLine();
				if(str != null && !str.contains("(mkvextract) The file "))
					chaps += " --chapters " + UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(
							dirToMux + chapters[0]));
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for(int i=0; i<audioTracks.size(); i++) {
			String audioOptions = " ";
			MediaTrack m = audioTracks.get(i);
			if(m.getInfoTrack()!=null){
				audioOptions = " --language 1:" + m.getInfoTrack().getLanguage() +
							" --track-name 1:" + UmibeFileUtils.addComillas(m.getInfoTrack().getName()) + " ";
			}
			files += audioOptions + UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(m.getRouteToTrack()));
		}

		for(int i=0; i<videoTracks.size(); i++) {
			String videoOptions = " ";
			MediaTrack m = videoTracks.get(i);
			if(m.getInfoTrack()!=null){
				videoOptions = " --language 1:" + m.getInfoTrack().getLanguage() +
							" --track-name 1:" + UmibeFileUtils.addComillas(m.getInfoTrack().getName()) + " ";
			}
			files += videoOptions + UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(m.getRouteToTrack()));
		}
		
		ArrayList<InfoTrack> subtitleTracks = tracks.getTracks("subtitles");
		for (int i = 0; i < subs.length; i++) {
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
		}

		String title = " --title " + UmibeFileUtils.addComillas(filename + ": Encoded with KireNcoder");
		
		if (files.equals(" ")) {
			// No files to mux
		} else {
			String tool = UmibeFileUtils
					.addComillas(UmibeFileUtils
							.getFullPath(mkvmerge))
					+ " -o ";
			String output = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(doneDir
					+ filename + ".mkv"));

			IExecutionEnvironment clienv = new WindowsCLIEnvironment();
			clienv.execute(tool + output + title + files + chaps, verbosity, false);
		}
	}

	public static void mux(String fullPath, String output) {
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
