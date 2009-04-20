package ar.com.feedactionizer.core;

import java.util.ArrayList;

import org.grlea.log.SimpleLogger;

import ar.com.umibe.commons.regex.matching.MatchingRule;

public abstract class GenericFeedActionizer implements Runnable{

	protected ArrayList<MatchingRule> rules = new ArrayList<MatchingRule>();
	protected String resource;
	protected int sleepInterval;
	protected volatile Thread blinker;
	private static final SimpleLogger log = new SimpleLogger(GenericFeedActionizer.class);
	
	public GenericFeedActionizer(){
		
	}
	
	public GenericFeedActionizer(String resource, int sleepInterval){
		this.resource = resource;
		this.sleepInterval = sleepInterval;
	}

	@Override
	public void run() {    
		Thread thisThread = Thread.currentThread();
		while (blinker == thisThread) {
			try {
				parseFeed();
				//Sleep
				Thread.sleep(sleepInterval * 1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	protected abstract void parseFeed();
	
	protected void matchEntryWithRules(String title, String link){
		if(DownloadFeeder.INSTANCE.alreadyRetrieved(title) == false){
			boolean foundMatch = false;
			int i = 0;
			while(foundMatch == false && i < rules.size()) {
				foundMatch = rules.get(i).matchRule(link);
				i++;
			}

			if(foundMatch == true){
				DownloadFeeder.INSTANCE.addRetrievedFile(title);
				log.info("Matched: " + title);
			}
		}
	}
	
	public void start() {
		blinker = new Thread(this);
		blinker.start();
	}

	public void stop() {
		Thread tmpBlinker = blinker;
		blinker = null;
		if (tmpBlinker != null) {
			tmpBlinker.interrupt();
		}
	}

	public void setRules(ArrayList<MatchingRule> rules) {
		this.rules = rules;
	}

	public ArrayList<MatchingRule> getRules() {
		return rules;
	}

	public void addRule(MatchingRule rule) {
		this.rules.add(rule);
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public int getSleepInterval() {
		return sleepInterval;
	}

	public void setSleepInterval(int sleepInterval) {
		this.sleepInterval = sleepInterval;
	}
}
