package ar.com.umibe.commons.regex.matching;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.umibe.commons.regex.reaction.Reaction;
import ar.com.umibe.commons.util.UmibeFileUtils;

public class MatchingURLRule extends MatchingRule {

	public MatchingURLRule(String regex, Reaction reaction) {
		super(regex, reaction);
	}
	
	@Override
	public boolean matchRule(String input) {
		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(UmibeFileUtils.getFileNameFromURL(input));
		if(m.find()){
			reaction.react(input);
			System.out.println("Reacted to: " + input);
			return true;
		} else {
			return false;
		}
	}

}
