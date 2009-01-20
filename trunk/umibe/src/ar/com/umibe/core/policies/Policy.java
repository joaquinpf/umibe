package ar.com.umibe.core.policies;

import ar.com.umibe.core.VideoTask;

public abstract class Policy {
	public abstract boolean evaluate(VideoTask vf);
}
