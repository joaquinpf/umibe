package ar.com.umibe.commons.regex.reaction;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.umibe.commons.util.StringUtils;
import ar.com.umibe.commons.util.UmibeFileUtils;

public class AnimeAutomaticReaction implements Reaction {
	
	private String regexp;
	private String baseFolder;
	private int group;
	private int levenshteinThreshold = 2;
	
	public AnimeAutomaticReaction(String baseFolder,String regexp, int group){
		this.baseFolder = baseFolder;
		this.group = group;
		this.regexp = regexp;
	}
	
	@Override
	public void react(String file) {
		Pattern p = Pattern.compile(regexp,Pattern.CASE_INSENSITIVE);
		String cuteFilename = UmibeFileUtils.getFileName(file);
		Matcher m = p.matcher(cuteFilename);
		if(m.find()){
			CharSequence folder = cuteFilename.subSequence(m.start(group),m.end(group));
			String finalFolder;
			try {
				finalFolder = checkifSimilarExists(baseFolder, folder.toString().replaceAll("_", " "));
			} catch (IOException e) {
				finalFolder = folder.toString().replaceAll("_", " ");
			}
			UmibeFileUtils.move(file, baseFolder + finalFolder);
		}
	}

	private String checkifSimilarExists(String baseFolder, String likelyFolder) throws IOException {
		File[] directories = UmibeFileUtils.getSubDirectories(baseFolder);
		int minLevenshtein = 35000;
		String levenshteinDir = null;
		
		for(File f: directories){
			String localLevenshteinDir = UmibeFileUtils.getFileName(f.getCanonicalPath());
			int localLevenshtein = StringUtils.getLevenshteinDistance(likelyFolder, localLevenshteinDir);
			if(localLevenshtein <= minLevenshtein){
				minLevenshtein = localLevenshtein;
				levenshteinDir = localLevenshteinDir;
			}
		}
		
		if(minLevenshtein <= levenshteinThreshold && levenshteinDir != null){
			return levenshteinDir;
		} else {
			return likelyFolder;
		}
	}
}
