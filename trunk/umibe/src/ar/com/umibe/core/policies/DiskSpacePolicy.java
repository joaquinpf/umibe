package ar.com.umibe.core.policies;

import java.io.File;

import org.apache.commons.io.FileUtils;

import ar.com.umibe.core.DataModel;
import ar.com.umibe.core.VideoTask;

public class DiskSpacePolicy extends Policy {

	@Override
	//TODO TEsting
	public boolean evaluate(VideoTask vf) {
		File dir = new File(DataModel.INSTANCE.getTempDir());
		File file = new File(vf.getRoute());
		if(dir.getFreeSpace() < (file.length() * 3)){
			return false;
		}
		return true;
	}
}
