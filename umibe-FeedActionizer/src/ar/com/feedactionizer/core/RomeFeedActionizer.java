package ar.com.feedactionizer.core;

import java.net.URL;

import org.grlea.log.SimpleLogger;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherEvent;
import com.sun.syndication.fetcher.FetcherListener;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;

public class RomeFeedActionizer extends GenericFeedActionizer{
	
	private static final SimpleLogger log = new SimpleLogger(RomeFeedActionizer.class);
	
	public RomeFeedActionizer() {
		super();
	}
	
	public RomeFeedActionizer(String resource, int sleepInterval) {
		super(resource, sleepInterval);
	}

	public static void main(String[] args) {

	}

	@Override
	protected void parseFeed() {
		try {
			URL feedUrl = new URL(resource);
			FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
			FeedFetcher fetcher = new HttpURLFeedFetcher(feedInfoCache);

			FetcherEventListenerImpl listener = new FetcherEventListenerImpl();

			fetcher.addFetcherEventListener(listener);

			System.err.println("Retrieving feed " + feedUrl);
			// Retrieve the feed.
			// We will get a Feed Polled Event and then a
			// Feed Retrieved event (assuming the feed is valid)
			SyndFeed feed = fetcher.retrieveFeed(feedUrl);

			System.err.println(feedUrl + " retrieved");
			System.err.println(feedUrl + " has a title: " + feed.getTitle() + " and contains " + feed.getEntries().size() + " entries.");
			log.info("Parsed: " + feed.getTitle() + " and contains " + feed.getEntries().size() + " entries.");
			
			for(Object e: feed.getEntries()){
				SyndEntry entry = (SyndEntry) e;
				matchEntryWithRules(entry.getTitle(), entry.getLink());
			}
		}
		catch (Exception ex) {
			System.out.println("ERROR: "+ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	static class FetcherEventListenerImpl implements FetcherListener {
		/**
		 * @see com.sun.syndication.fetcher.FetcherListener#fetcherEvent(com.sun.syndication.fetcher.FetcherEvent)
		 */
		public void fetcherEvent(FetcherEvent event) {
			String eventType = event.getEventType();
			if (FetcherEvent.EVENT_TYPE_FEED_POLLED.equals(eventType)) {
				System.err.println("\tEVENT: Feed Polled. URL = " + event.getUrlString());
			} else if (FetcherEvent.EVENT_TYPE_FEED_RETRIEVED.equals(eventType)) {
				System.err.println("\tEVENT: Feed Retrieved. URL = " + event.getUrlString());
			} else if (FetcherEvent.EVENT_TYPE_FEED_UNCHANGED.equals(eventType)) {
				System.err.println("\tEVENT: Feed Unchanged. URL = " + event.getUrlString());
			}
		}
	}
}
