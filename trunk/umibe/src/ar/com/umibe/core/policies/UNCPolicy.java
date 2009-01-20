package ar.com.umibe.core.policies;

import ar.com.umibe.core.VideoTask;

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
