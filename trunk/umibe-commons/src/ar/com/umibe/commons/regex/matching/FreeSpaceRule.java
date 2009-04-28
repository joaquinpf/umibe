package ar.com.umibe.commons.regex.matching;

import ar.com.umibe.commons.regex.reaction.Reaction;
import ar.com.umibe.commons.util.UmibeFileUtils;


public abstract class FreeSpaceRule extends Rule {
	protected long threshold = 0;

	public FreeSpaceRule(long threshold, Reaction reaction){
		this.reaction = reaction;
		this.threshold = threshold;
	}
	
	@Override
	public boolean matchRule(String input){
		long freeSpace = UmibeFileUtils.getFreeSpace(input);
		if(freeSpace >= 0 && freeSpace <= threshold){
			reaction.react(input);
			return true;
		}
		return false;
	}
}
