package ar.com.umibe.core.policies;

import ar.com.umibe.core.VideoTask;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public abstract class Policy {
	public abstract boolean evaluate(VideoTask vf);
}
