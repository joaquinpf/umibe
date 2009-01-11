package ar.com.KireNcoder.policies;

import ar.com.KireNcoder.core.VideoFile;

public class OrPolicy extends Policy {

	Policy a;
	Policy b;
	
	public OrPolicy(Policy a, Policy b){
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean evaluate(VideoFile vf) {
		return a.evaluate(vf) || a.evaluate(vf);
	}

}
