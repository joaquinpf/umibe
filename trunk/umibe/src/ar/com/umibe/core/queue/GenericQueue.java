package ar.com.umibe.core.queue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import ar.com.umibe.core.PriorityComparator;
import ar.com.umibe.core.Status;
import ar.com.umibe.core.VideoTask;
import ar.com.umibe.core.policies.Policy;

public abstract class GenericQueue extends Observable {	
	
	protected List<VideoTask> enqueued;
	protected boolean availableItems = false;
		
	public abstract VideoTask get(Policy p);
	public void put(ArrayList<VideoTask> newFiles){
		if(newFiles != null){
			for(int i=0; i<newFiles.size();i++){
				this.put(newFiles.get(i));
			}
		}
	}
	public abstract void put(VideoTask newFile);
	public abstract void delete(VideoTask newFile);
	public abstract void changeItemStatus(VideoTask vf, Status status);
		
	public ArrayList<VideoTask> getEnqueuedElements() {
		ArrayList<VideoTask> a = new ArrayList<VideoTask>();
		Iterator<VideoTask> it = enqueued.iterator();
		while (it.hasNext()) { 
			VideoTask vf = it.next(); 
			if ((vf.getStatus() == Status.WAITING)
					|| (vf.getStatus() == Status.FAILED)) {
				a.add(vf);
			}
		}
		return a;
	}

	protected VideoTask getFirstWaiting(Policy p) {
		Iterator<VideoTask> it = enqueued.iterator();
		while (it.hasNext()) { 
			VideoTask vf = it.next(); 
			if (vf.getStatus() == Status.WAITING && p.evaluate(vf) == true) {
				return vf;
			}
		}
		return null;
	}
	
	protected synchronized VideoTask getFirstWaitingWithPriority(Policy policy) {
		Iterator<VideoTask> it = enqueued.iterator();
		PriorityComparator p = new PriorityComparator();
		VideoTask last = null;
		while (it.hasNext()) { 
			VideoTask vf = it.next(); 
			if (vf.getStatus() == Status.WAITING && policy.evaluate(vf) == true) {
				if(p.compare(last, vf)==1) {
					last = vf;					
				}
			}
		}
		return last;
	}

	public ArrayList<VideoTask> getAllElements() {
		ArrayList<VideoTask> a = new ArrayList<VideoTask>();
		Iterator<VideoTask> it = enqueued.iterator();
		while (it.hasNext()) { 
			VideoTask vf = it.next(); 
			a.add(vf);
		}
		return a;
	}

	public boolean exists(VideoTask file) {
		Iterator<VideoTask> it = enqueued.iterator();
		while (it.hasNext()) { 
			VideoTask vf = it.next(); 
			if (vf.compareTo(file) == 0) {
				return true;
			}
		}
		return false;
	}
}
