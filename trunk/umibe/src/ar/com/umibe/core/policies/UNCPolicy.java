package ar.com.umibe.core.policies;

import ar.com.umibe.core.VideoFile;

public class UNCPolicy extends Policy {
	
	@Override
	public boolean evaluate(VideoFile vf) {
		if(vf.getRoute().startsWith("\\\\") && vf.getOutputFolder().startsWith("\\\\")) {
			return true;
		} else {
			return false;
		}
	}
}
