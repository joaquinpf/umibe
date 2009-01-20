package ar.com.umibe.core.policies;

import java.lang.reflect.Method;

import ar.com.umibe.core.VideoTask;

public class SimplePolicy extends Policy {

	private String key;
	private String expected;
	
	public SimplePolicy(String key, String expected){
		this.key = key;
		this.expected = expected;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean evaluate(VideoTask vf) {
		try {
			Class params[] = {};
			Object paramsObj[] = {};
			Class thisClass = Class.forName("ar.com.umibe.core.VideoFile");
			Method thisMethod = thisClass.getDeclaredMethod("get" + key, params);

			if(thisMethod.invoke(vf, paramsObj).toString().equals(expected)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String args[]) {
		VideoTask v = new VideoTask("lala");
		SimplePolicy s = new SimplePolicy("Route","o");
		s.evaluate(v);
	}
}
