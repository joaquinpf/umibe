package ar.com.feedactionizer.core;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ar.com.umibe.commons.regex.matching.MatchingRule;
import ar.com.umibe.commons.regex.matching.MatchingURLRule;
import ar.com.umibe.commons.regex.reaction.DownloadReaction;
import ar.com.umibe.commons.xml.BaseXMLParser;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class XMLFeedLoader extends BaseXMLParser {

	public XMLFeedLoader(String route) {
		super(route);
	}

	public ArrayList<GenericFeedActionizer> getFolders() {
		NodeList nlOptions;
		Element elementOption;
		ArrayList<GenericFeedActionizer> feeds = new ArrayList<GenericFeedActionizer>();
		nlOptions = this.docEle.getElementsByTagName("feed");
		if (nlOptions != null && nlOptions.getLength() > 0) {
			for (int i = 0; i < nlOptions.getLength(); i++) {
				elementOption = (Element) nlOptions.item(i);				
				GenericFeedActionizer feed = FeedActionizerFactory.INSTANCE.getFeedActionizer(
						elementOption.getAttribute("implementation"),
						elementOption.getAttribute("route"),
						Integer.parseInt(elementOption.getAttribute("pollinterval")));
				
				parseRules(elementOption, feed, elementOption.getAttribute("targetapp"));
								
				feeds.add(feed);
			}
		}
		return feeds;
	}

	private void parseRules(Element elementOption, GenericFeedActionizer feed, String targetapp) {
		NodeList nlOptions = elementOption.getElementsByTagName("rule");
		if (nlOptions != null && nlOptions.getLength() > 0) {
			for (int i = 0; i < nlOptions.getLength(); i++) {
				elementOption = (Element) nlOptions.item(i);				
				
				DownloadReaction reaction = new DownloadReaction(targetapp);
				MatchingRule rule = new MatchingURLRule(elementOption.getAttribute("regexp"),reaction);

				feed.addRule(rule);
			}
		}
	}
}
