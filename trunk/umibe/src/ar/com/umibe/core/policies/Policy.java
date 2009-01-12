package ar.com.umibe.core.policies;

import ar.com.umibe.core.VideoFile;

public abstract class Policy {
	public abstract boolean evaluate(VideoFile vf);
}
