package ar.com.umibe.core.policies;

import ar.com.umibe.core.VideoTask;

/**
 * @author Joaqu�n Alejandro P�rez Fuentes
 */
public abstract class Policy {
	public abstract boolean evaluate(VideoTask vf);
}
