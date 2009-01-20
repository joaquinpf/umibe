package ar.com.umibe.core.policies;

import ar.com.umibe.core.VideoTask;

public class OrPolicy extends Policy {

	Policy a;
	Policy b;
	
	public OrPolicy(Policy a, Policy b){
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean evaluate(VideoTask vf) {
		return a.evaluate(vf) || a.evaluate(vf);
	}

}
