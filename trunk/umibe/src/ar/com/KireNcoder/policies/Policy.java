package ar.com.KireNcoder.policies;

import ar.com.KireNcoder.core.VideoFile;

public abstract class Policy {
	public abstract boolean evaluate(VideoFile vf);
}
