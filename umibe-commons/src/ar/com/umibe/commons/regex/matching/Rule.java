package ar.com.umibe.commons.regex.matching;

import ar.com.umibe.commons.regex.reaction.Reaction;

public abstract class Rule {	
	protected Reaction reaction;

	public void setReaction(Reaction reaction) {
		this.reaction = reaction;
	}

	public Reaction getReaction() {
		return reaction;
	}
	
	public abstract boolean matchRule(String input);
	
}
