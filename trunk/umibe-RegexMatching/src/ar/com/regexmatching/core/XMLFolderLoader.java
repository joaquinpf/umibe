package ar.com.regexmatching.core;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ar.com.umibe.commons.regex.matching.MatchingFileRule;
import ar.com.umibe.commons.regex.matching.MatchingRule;
import ar.com.umibe.commons.regex.reaction.MoveReaction;
import ar.com.umibe.commons.xml.BaseXMLParser;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class XMLFolderLoader extends BaseXMLParser {

	public XMLFolderLoader(String route) {
		super(route);
	}

	public ArrayList<RuledFolderWatcher> getFolders() {
		NodeList nlOptions;
		Element elementOption;
		ArrayList<RuledFolderWatcher> folders = new ArrayList<RuledFolderWatcher>();
		nlOptions = this.docEle.getElementsByTagName("folder");
		if (nlOptions != null && nlOptions.getLength() > 0) {
			for (int i = 0; i < nlOptions.getLength(); i++) {
				elementOption = (Element) nlOptions.item(i);				
				RuledFolderWatcher folder = new RuledFolderWatcher(elementOption.getAttribute("route"),
						Integer.parseInt(elementOption.getAttribute("pollinterval")));
				
				parseRules(elementOption, folder);
				
				folder.start();
				
				folders.add(folder);
			}
		}
		return folders;
	}

	private void parseRules(Element elementOption, RuledFolderWatcher folder) {
		NodeList nlOptions = elementOption.getElementsByTagName("rule");
		if (nlOptions != null && nlOptions.getLength() > 0) {
			for (int i = 0; i < nlOptions.getLength(); i++) {
				elementOption = (Element) nlOptions.item(i);				
				
				MoveReaction reaction = new MoveReaction(elementOption.getAttribute("reaction"));
				MatchingRule rule = new MatchingFileRule(elementOption.getAttribute("regexp"),reaction);

				folder.addRule(rule);
			}
		}
	}
}
