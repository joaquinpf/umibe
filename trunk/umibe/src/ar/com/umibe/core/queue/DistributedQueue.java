package ar.com.umibe.core.queue;

import java.util.concurrent.locks.Lock;

import ar.com.umibe.core.Status;
import ar.com.umibe.core.VideoTask;
import ar.com.umibe.core.policies.Policy;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IList;
import com.hazelcast.core.ItemListener;

/**
 * @author Joaqu�n Alejandro P�rez Fuentes
 */
public class DistributedQueue extends GenericQueue implements ItemListener<VideoTask> {

	private Lock lock;
	
	@SuppressWarnings("unchecked")
	public DistributedQueue() {
		enqueued = Hazelcast.getList("Umibe");
		lock = Hazelcast.getLock("Umibe");
		((IList)enqueued).addItemListener(this, true);
	}
	
	public synchronized VideoTask get(Policy p) {
		while (getFirstWaiting(p) == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		VideoTask f = getFirstWaitingWithPriority(p);
		
		changeItemStatus(f, Status.ENCODING);

		notifyAll();
		
		return f;
	}

	public void changeItemStatus(VideoTask vf, Status status) {
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
	
	public synchronized void put(VideoTask newFile) {
		if(!exists(newFile)){
			lock.lock();
			enqueued.add(newFile);
			lock.unlock();
			//sort(); <---- evil
			notifyAll();
		}
	}

	public synchronized void delete(VideoTask newFile) {
		if(exists(newFile)) {
			lock.lock();
			enqueued.remove(newFile);	
			lock.unlock();
		}
		notifyAll();
	}
	
	@Override
	public void itemAdded(VideoTask arg0) {
		setChanged();
		notifyObservers();
	}

	@Override
	public void itemRemoved(VideoTask arg0) {
		setChanged();
		notifyObservers();
	}
}
