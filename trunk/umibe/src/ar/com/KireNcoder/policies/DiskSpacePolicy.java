package ar.com.KireNcoder.policies;

import java.io.File;

import ar.com.KireNcoder.core.DataModel;
import ar.com.KireNcoder.core.VideoFile;

public class DiskSpacePolicy extends Policy {

	@Override
	//TODO TEsting
	public boolean evaluate(VideoFile vf) {
		File dir = new File(DataModel.INSTANCE.getTempDir());
		File file = new File(vf.getRoute());
		if(dir.getFreeSpace() < (file.length() * 3)){
			return false;
		}
		return true;
	}
}
