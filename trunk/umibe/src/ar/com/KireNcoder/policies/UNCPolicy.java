package ar.com.KireNcoder.policies;

import ar.com.KireNcoder.core.VideoFile;

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
