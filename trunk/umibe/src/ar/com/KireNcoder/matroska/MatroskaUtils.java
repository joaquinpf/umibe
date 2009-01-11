package ar.com.KireNcoder.matroska;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;

import ar.com.KireNcoder.util.CLIUtils;
import ar.com.KireNcoder.util.FileUtils;

public class MatroskaUtils {

	private static String mkvPath = "./resources/mkvtools/";
	
	public static void MKVize(String input, String output, boolean verbosity) {
		String tool = FileUtils.addComillas(FileUtils
				.getFullPath(mkvPath + "mkvmerge.exe"))
				+ " -o ";
		output = FileUtils.addComillas(FileUtils.getFullPath(output));
		input = FileUtils.addComillas(FileUtils.getFullPath(input));

		CLIUtils clienv = new CLIUtils();
		clienv.executeCommand(tool + output + input, verbosity, false);
	}

	public static void demux(String input, String outputFolder, ArrayList<Track> tracks, boolean verbosity) {
		if(tracks!= null && tracks.size()>0){
			String files = " ";
			for(int i=0; i<tracks.size(); i++) {
				String outFile = FileUtils.addComillas(FileUtils.getFullPath(outputFolder 
						+ tracks.get(i).getTrackType() + "_" + i)) + " ";
				files += tracks.get(i).getTrackNumber() + ":" + outFile;
			}
			String tool = FileUtils.addComillas(FileUtils
					.getFullPath(mkvPath + "mkvextract.exe"))
					+ " tracks ";
			input = FileUtils.addComillas(FileUtils.getFullPath(input));
			
			CLIUtils clienv = new CLIUtils();
			clienv.executeCommand(tool + input + files, verbosity, false);
		}
	}

	public static void generateInfo(String input, String infoFile) {
		File f = new File(input);
		if(f.exists()){
			String tool = FileUtils.addComillas(FileUtils
					.getFullPath(mkvPath + "mkvinfo.exe"))
					+ " ";

			infoFile = FileUtils.addComillas(FileUtils.getFullPath(infoFile));

			input = FileUtils.addComillas(FileUtils.getFullPath(input));

			CLIUtils clienv = new CLIUtils();
			clienv.executeCommand(tool + input + " > " + infoFile, true, false);
		}
	}
	
	public static void extractChapters(String input, String outputFolder) {
		String tool = FileUtils.addComillas(FileUtils
				.getFullPath(mkvPath + "mkvextract.exe"))
				+ " chapters ";
		String output = outputFolder + FileUtils.getFileName(input) + "_chapters.xml";
		output = FileUtils.addComillas(FileUtils.getFullPath(output));
		
		input = FileUtils.addComillas(FileUtils.getFullPath(input));
		
		CLIUtils clienv = new CLIUtils();
		clienv.executeCommand(tool + input + " > " + output, false, false);
	}
	
	public static void merge(String video, ArrayList<Track> audioTracks, String filename,
			String dirToMux, String doneDir, TracksInfoParser tracks, boolean verbosity) {

		String[] subs = FileUtils.filterFiles(dirToMux, "subtitles_", "sub");
		
		String[] chapters = FileUtils.filterFiles(dirToMux, "_chapters");

		String[] audios = FileUtils.filterFiles(dirToMux, "encodedaudio_");
		
		String files = " ";
		String chaps = " ";

		if (chapters.length>0) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(FileUtils.getFullPath(
						dirToMux + chapters[0])));

				String str = in.readLine();
				if(str != null && !str.contains("(mkvextract) The file "))
					chaps += " --chapters " + FileUtils.addComillas(FileUtils.getFullPath(
							dirToMux + chapters[0]));
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for(int i=0; i<audios.length; i++) {
			String audioOptions = " ";
			if(audioTracks!=null && audioTracks.size()>0){
				audioOptions = " --language 1:" + audioTracks.get(i).getLanguage() +
							" --track-name 1:" + FileUtils.addComillas(audioTracks.get(i).getName()) + " ";
			}
			files += audioOptions + FileUtils.addComillas(FileUtils.getFullPath(dirToMux + audios[i]));
		}

		File v = new File(FileUtils.getFullPath(video));
		if (v.exists()) {
			ArrayList<Track> videoTracks = tracks.getTracks("video");
			String videoOptions = " ";
			if(videoTracks!=null && videoTracks.size()>0){
				videoOptions = " --language 1:" + videoTracks.get(0).getLanguage() +
							" --track-name 1:" + FileUtils.addComillas(videoTracks.get(0).getName()) + " ";
			}
			files += videoOptions + FileUtils.addComillas(FileUtils.getFullPath(video));
		}
		
		ArrayList<Track> subtitleTracks = tracks.getTracks("subtitles");
		for (int i = 0; i < subs.length; i++) {
				String subOptions = " ";
				if(subtitleTracks!=null && subtitleTracks.size()>0){
					//FIXME locale no funca como quisiera
					String name;
					if(subtitleTracks.get(i).getName() == null){
						Locale l = new Locale(subtitleTracks.get(i).getLanguage());
						name = l.getDisplayName(new Locale("eng"));
					} else {
						name = FileUtils.addComillas(subtitleTracks.get(i).getName());
					}
					subOptions = " --language 0:" + subtitleTracks.get(i).getLanguage() +
					" --track-name 0:" + name + " ";
				}
				files += subOptions + FileUtils.addComillas(FileUtils.getFullPath(dirToMux
						+ subs[i]));
		}

		String title = " --title " + FileUtils.addComillas(filename + ": Encoded with KireNcoder");
		
		if (files.equals(" ")) {
			// No files to mux
		} else {
			String tool = FileUtils
					.addComillas(FileUtils
							.getFullPath(mkvPath + "mkvmerge.exe"))
					+ " -o ";
			String output = FileUtils.addComillas(FileUtils.getFullPath(doneDir
					+ filename + ".mkv"));

			CLIUtils clienv = new CLIUtils();
			clienv.executeCommand(tool + output + title + files + chaps, verbosity, false);
		}
	}

	public static void mux(String fullPath, String output) {
		String tool = FileUtils
				.addComillas(FileUtils
						.getFullPath(mkvPath + "mkvmerge.exe"))
				+ " -o ";

		CLIUtils clienv = new CLIUtils();
		clienv.executeCommand(tool + FileUtils.addComillas(output) + FileUtils.addComillas(fullPath), true, false);
	}
}
