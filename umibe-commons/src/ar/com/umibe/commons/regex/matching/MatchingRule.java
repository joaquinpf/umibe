package ar.com.umibe.commons.regex.matching;

import ar.com.umibe.commons.regex.reaction.Reaction;


public abstract class MatchingRule {
	protected String regex = ".*";
	protected Reaction reaction;

	public MatchingRule(String regex, Reaction reaction){
		this.reaction = reaction;
		this.regex = regex;
	}
	
	public abstract boolean matchRule(String input);

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
