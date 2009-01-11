package ar.com.KireNcoder.queue;

import java.util.ArrayList;
import java.util.Collections;

import ar.com.KireNcoder.core.PriorityComparator;
import ar.com.KireNcoder.core.Status;
import ar.com.KireNcoder.core.VideoFile;
import ar.com.KireNcoder.policies.Policy;

public class LocalQueue extends GenericQueue {

	public LocalQueue(){
		enqueued = new ArrayList<VideoFile>();
	}
	
	public synchronized VideoFile get(Policy p) {
		while (getFirstWaiting(p) == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.getCause().printStackTrace();
			}
		}
		VideoFile f = getFirstWaiting(p);
		
		changeItemStatus(f, Status.ENCODING);
	
		notifyAll();

		return f;
	}

	public synchronized void put(VideoFile newFile) {
		if(!exists(newFile)){
			this.enqueued.add(newFile);
			changeItemStatus(newFile, Status.WAITING);
			Collections.sort(this.enqueued, new PriorityComparator());
		}
		notifyAll();
	}

	public synchronized void delete(VideoFile newFile) {
		for (int i = 0; i < this.enqueued.size(); i++) {
			if (this.enqueued.get(i).compareTo(newFile) == 0) {
				this.enqueued.remove(i);
			}
		}
		setChanged();
		notifyObservers();
		notifyAll();
	}

	@Override
	public void changeItemStatus(VideoFile vf, Status status) {
		vf.setStatus(status);
		setChanged();
		notifyObservers();
	}
}
