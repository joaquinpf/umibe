package ar.com.umibe.core.queue;

import java.util.ArrayList;
import java.util.Collections;

import ar.com.umibe.core.PriorityComparator;
import ar.com.umibe.core.Status;
import ar.com.umibe.core.VideoTask;
import ar.com.umibe.core.policies.Policy;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class LocalQueue extends GenericQueue {

	public LocalQueue(){
		enqueued = new ArrayList<VideoTask>();
	}
	
	public synchronized VideoTask get(Policy p) {
		while (getFirstWaiting(p) == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.getCause().printStackTrace();
			}
		}
		VideoTask f = getFirstWaiting(p);
		
		changeItemStatus(f, Status.ENCODING);
	
		notifyAll();

		return f;
	}

	public synchronized void put(VideoTask newFile) {
		if(!exists(newFile)){
			this.enqueued.add(newFile);
			changeItemStatus(newFile, Status.WAITING);
			Collections.sort(this.enqueued, new PriorityComparator());
		}
		notifyAll();
	}

	public synchronized void delete(VideoTask newFile) {
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
	public void changeItemStatus(VideoTask vf, Status status) {
		vf.setStatus(status);
		setChanged();
		notifyObservers();
	}
}
