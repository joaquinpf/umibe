package ar.com.umibe.commons.regex.matching;

import ar.com.umibe.commons.regex.reaction.Reaction;


public abstract class MatchingRule extends Rule {
	protected String regex = ".*";

	public MatchingRule(String regex, Reaction reaction){
		this.reaction = reaction;
		this.regex = regex;
	}
	
	@Override
	public abstract boolean matchRule(String input);

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}
}
