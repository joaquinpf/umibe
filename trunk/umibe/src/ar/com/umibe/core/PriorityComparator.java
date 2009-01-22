package ar.com.umibe.core;

import java.util.Comparator;

/**
 * @author Joaqu�n Alejandro P�rez Fuentes
 */
public class PriorityComparator implements Comparator<VideoTask> {

	@Override
	public int compare(VideoTask o1, VideoTask o2) {
		if(o1 == null && o2 != null) {
			return 1;
		} else if (o1 != null && o2==null){
			return -1;
		} else if (o1.getPriority() == o2.getPriority()) {
			return 0;
		} else if (o1.getPriority() < o2.getPriority()) {
			return 1;
		} else {
			return -1;
		}
	}
}
