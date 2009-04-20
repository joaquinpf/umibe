package ar.com.feedactionizer.core;

import java.io.InputStream;
import java.util.Date;

import org.apache.commons.feedparser.DefaultFeedParserListener;
import org.apache.commons.feedparser.FeedParser;
import org.apache.commons.feedparser.FeedParserException;
import org.apache.commons.feedparser.FeedParserFactory;
import org.apache.commons.feedparser.FeedParserListener;
import org.apache.commons.feedparser.FeedParserState;
import org.apache.commons.feedparser.network.ResourceRequest;
import org.apache.commons.feedparser.network.ResourceRequestFactory;

public class JakartaFeedActionizer extends GenericFeedActionizer{

	public JakartaFeedActionizer() {
		super();
	}
	
	public JakartaFeedActionizer(String resource, int sleepInterval) {
		super(resource, sleepInterval);
	}

	@Override
	protected void parseFeed() {				//create a new FeedParser...
		FeedParser parser;
		try {
			parser = FeedParserFactory.newFeedParser();

			//create a listener for handling our callbacks
			FeedParserListener listener = new DefaultFeedParserListener() {

				public void onChannel( FeedParserState state,
						String title,
						String link,
						String description ) throws FeedParserException {

					System.out.println( "Found a new channel: " + title );

				}

				public void onItem( FeedParserState state,
						String title,
						String link,
						String description,
						String permalink ) throws FeedParserException {

					matchEntryWithRules(title, link);
					System.out.println( "Found a new published article: " + title );
				}

				public void onCreated( FeedParserState state, Date date ) throws FeedParserException {
					System.out.println( "Which was created on: " + date );
				}
			};

			System.out.println( "Fetching resource:" + resource );

			//use the FeedParser network IO package to fetch our resource URL
			ResourceRequest request = ResourceRequestFactory.getResourceRequest( resource );

			//grab our input stream
			InputStream is = request.getInputStream();

			//start parsing our feed and have the above onItem methods called
			parser.parse( listener, is, resource );

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
