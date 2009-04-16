package ar.com.regexmatching.core;

import ar.com.umibe.util.UmibeFileUtils;


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
