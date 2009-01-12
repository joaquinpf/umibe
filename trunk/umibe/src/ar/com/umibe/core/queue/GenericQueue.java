package ar.com.umibe.core.queue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import ar.com.umibe.core.PriorityComparator;
import ar.com.umibe.core.Status;
import ar.com.umibe.core.VideoFile;
import ar.com.umibe.core.policies.Policy;

public abstract class GenericQueue extends Observable {	
	
	protected List<VideoFile> enqueued;
	protected boolean availableItems = false;
		
	public abstract VideoFile get(Policy p);
	public void put(ArrayList<VideoFile> newFiles){
		if(newFiles != null){
			for(int i=0; i<newFiles.size();i++){
				this.put(newFiles.get(i));
			}
		}
	}
	public abstract void put(VideoFile newFile);
	public abstract void delete(VideoFile newFile);
	public abstract void changeItemStatus(VideoFile vf, Status status);
		
	public ArrayList<VideoFile> getEnqueuedElements() {
		ArrayList<VideoFile> a = new ArrayList<VideoFile>();
		Iterator<VideoFile> it = enqueued.iterator();
		while (it.hasNext()) { 
			VideoFile vf = it.next(); 
			if ((vf.getStatus() == Status.WAITING)
					|| (vf.getStatus() == Status.FAILED)) {
				a.add(vf);
			}
		}
		return a;
	}

	protected VideoFile getFirstWaiting(Policy p) {
		Iterator<VideoFile> it = enqueued.iterator();
		while (it.hasNext()) { 
			VideoFile vf = it.next(); 
			if (vf.getStatus() == Status.WAITING && p.evaluate(vf) == true) {
				return vf;
			}
		}
		return null;
	}
	
	protected synchronized VideoFile getFirstWaitingWithPriority(Policy policy) {
		Iterator<VideoFile> it = enqueued.iterator();
		PriorityComparator p = new PriorityComparator();
		VideoFile last = null;
		while (it.hasNext()) { 
			VideoFile vf = it.next(); 
			if (vf.getStatus() == Status.WAITING && policy.evaluate(vf) == true) {
				if(p.compare(last, vf)==1) {
					last = vf;					
				}
			}
		}
		return last;
	}

	public ArrayList<VideoFile> getAllElements() {
		ArrayList<VideoFile> a = new ArrayList<VideoFile>();
		Iterator<VideoFile> it = enqueued.iterator();
		while (it.hasNext()) { 
			VideoFile vf = it.next(); 
			a.add(vf);
		}
		return a;
	}

	public boolean exists(VideoFile file) {
		Iterator<VideoFile> it = enqueued.iterator();
		while (it.hasNext()) { 
			VideoFile vf = it.next(); 
			if (vf.compareTo(file) == 0) {
				return true;
			}
		}
		return false;
	}
}
