package ar.com.umibe.core;

import java.util.Comparator;

public class PriorityComparator implements Comparator<VideoFile> {

	@Override
	public int compare(VideoFile o1, VideoFile o2) {
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
