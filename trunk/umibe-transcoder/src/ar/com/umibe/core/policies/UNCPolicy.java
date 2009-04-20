package ar.com.umibe.core.policies;

import ar.com.umibe.core.VideoTask;

/**
 * @author Joaqu�n Alejandro P�rez Fuentes
 */
public class UNCPolicy extends Policy {
	
	@Override
	public boolean evaluate(VideoTask vf) {
		if(vf.getRoute().startsWith("\\\\") && vf.getOutputFolder().startsWith("\\\\")) {
			return true;
		} else {
			return false;
		}
	}
}
