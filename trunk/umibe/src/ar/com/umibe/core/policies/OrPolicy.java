package ar.com.umibe.core.policies;

import ar.com.umibe.core.VideoTask;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class OrPolicy extends Policy {

	Policy a;
	Policy b;
	
	public OrPolicy(Policy a, Policy b){
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean evaluate(VideoTask vf) {
		return a.evaluate(vf) || b.evaluate(vf);
	}

}
