package ar.com.umibe.commons.regex.reaction;

import ar.com.umibe.commons.util.UmibeFileUtils;


public class MoveReaction implements Reaction {

	private String targetFolder;

	public MoveReaction(String targetFolder){
		this.targetFolder = targetFolder;
	}
	
	@Override
	public void react(String file) {
		UmibeFileUtils.move(file, targetFolder);
	}

	public void setTargetFolder(String targetFolder) {
		this.targetFolder = targetFolder;
	}

	public String getTargetFolder() {
		return targetFolder;
	}
	
}
