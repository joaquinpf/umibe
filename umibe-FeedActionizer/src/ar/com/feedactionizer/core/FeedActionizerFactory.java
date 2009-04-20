package ar.com.feedactionizer.core;

import java.util.HashMap;
import java.util.Map;

public class FeedActionizerFactory {
	
	public static FeedActionizerFactory INSTANCE = new FeedActionizerFactory();
	
	private Map<String, Class> typeMap;
	
	/**
	 * CommandFactory constructor comment.
	 */
	protected FeedActionizerFactory() {
		super();
		
		typeMap = new HashMap<String, Class>();
		typeMap.put(FeedActionizerConstants.IMP_JAKARTA, JakartaFeedActionizer.class);
		typeMap.put(FeedActionizerConstants.IMP_ROME, RomeFeedActionizer.class);
	}
	
	public GenericFeedActionizer getFeedActionizer(String impname, String resource, int pollinterval) {
		if(typeMap.containsKey(impname)){
			try {
				GenericFeedActionizer feedActionizer = (GenericFeedActionizer) typeMap.get(impname).newInstance();
				feedActionizer.setResource(resource);
				feedActionizer.setSleepInterval(pollinterval);
				return feedActionizer;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
		
}