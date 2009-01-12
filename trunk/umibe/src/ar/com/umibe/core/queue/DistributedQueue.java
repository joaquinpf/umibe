package ar.com.umibe.core.queue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;

import ar.com.umibe.core.PriorityComparator;
import ar.com.umibe.core.Status;
import ar.com.umibe.core.VideoFile;
import ar.com.umibe.core.policies.Policy;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IList;
import com.hazelcast.core.ItemListener;

public class DistributedQueue extends GenericQueue implements ItemListener<VideoFile> {

	private Lock lock;
	
	@SuppressWarnings("unchecked")
	public DistributedQueue() {
		enqueued = Hazelcast.getList("KireNcoder");
		lock = Hazelcast.getLock("KireNcoder");
		((IList)enqueued).addItemListener(this, true);
	}
	
	public synchronized VideoFile get(Policy p) {
		while (getFirstWaiting(p) == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		VideoFile f = getFirstWaitingWithPriority(p);
		
		changeItemStatus(f, Status.ENCODING);

		notifyAll();
		
		return f;
	}

	public void changeItemStatus(VideoFile vf, Status status) {
		//TODO Sorting evilness
		if(exists(vf)){
			//No se utiliza put por razones de performance
			lock.lock();
			enqueued.remove(vf);
			vf.setStatus(status);
			enqueued.add(vf);
			lock.unlock();
			//sort(); <---- Evil
		}
	}
	
/*	//Baratez
	private synchronized void sort() {
		lock.lock();
		List<VideoFile> a = getAllElements();
		Collections.sort(a, new PriorityComparator());
		Iterator<VideoFile> it = enqueued.iterator();
		while (it.hasNext()) { 
			VideoFile vf = it.next(); 
			enqueued.remove(vf);
		}
		for(int i=0; i<a.size(); i++)
			enqueued.add(a.get(i));
		lock.unlock();
	}*/
	
	public synchronized void put(VideoFile newFile) {
		if(!exists(newFile)){
			lock.lock();
			enqueued.add(newFile);
			lock.unlock();
			//sort(); <---- evil
			notifyAll();
		}
	}

	public synchronized void delete(VideoFile newFile) {
		if(exists(newFile)) {
			lock.lock();
			enqueued.remove(newFile);	
			lock.unlock();
		}
		notifyAll();
	}
	
	@Override
	public void itemAdded(VideoFile arg0) {
		setChanged();
		notifyObservers();
	}

	@Override
	public void itemRemoved(VideoFile arg0) {
		setChanged();
		notifyObservers();
	}
}
