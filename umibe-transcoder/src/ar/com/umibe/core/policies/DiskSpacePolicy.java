package ar.com.umibe.core.policies;

import java.io.File;

import ar.com.umibe.core.DataModel;
import ar.com.umibe.core.VideoTask;

/**
 * @author Joaqu�n Alejandro P�rez Fuentes
 */
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
