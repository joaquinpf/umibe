package ar.com.umibe.commons.regex.reaction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.umibe.commons.util.UmibeFileUtils;

public class AnimeAutomaticReaction implements Reaction {
	
	private String regexp;
	private String baseFolder;
	private int group;
	
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
			String pepe = folder.toString().replaceAll("_", " ");
			UmibeFileUtils.move(file, baseFolder + pepe);
		}
	}
}
