package ar.com.umibe.commons.regex.reaction;

import ar.com.umibe.commons.execution.WindowsCLIEnvironment;
import ar.com.umibe.commons.util.UmibeFileUtils;

public class DownloadReaction implements Reaction {
	
	private String target;
	
	public DownloadReaction(String target){
		this.target = target;
	}
	
	@Override
	public void react(String file) {
		new WindowsCLIEnvironment().execute(target + UmibeFileUtils.addComillas(file));
	}
}
