package ar.com.regexmatching.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.umibe.util.UmibeFileUtils;


public class MatchingRule {
	private String regex = ".*";
	private Reaction reaction;

	public MatchingRule(String regex, Reaction reaction){
		this.reaction = reaction;
		this.regex = regex;
	}
	
	public boolean matchRule(String input){
		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(UmibeFileUtils.getFileName(input));
		if(m.find()){
			reaction.react(input);
			System.out.println("Reacted to: " + input);
			return true;
		} else {
			return false;
		}
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}

	public void setReaction(Reaction reaction) {
		this.reaction = reaction;
	}

	public Reaction getReaction() {
		return reaction;
	}
}
