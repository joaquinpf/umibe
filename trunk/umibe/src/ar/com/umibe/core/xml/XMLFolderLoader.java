package ar.com.umibe.core.xml;

import java.util.ArrayList;
import java.util.Hashtable;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ar.com.regexmatching.core.MatchingRule;
import ar.com.regexmatching.core.MoveReaction;
import ar.com.umibe.core.monitor.MediaFolderWatcher;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class XMLFolderLoader extends BaseXMLParser {

	public XMLFolderLoader(String route) {
		super(route);
	}

	public ArrayList<MediaFolderWatcher> getFolders() {
		NodeList nlOptions;
		Element elementOption;
		ArrayList<MediaFolderWatcher> folders = new ArrayList<MediaFolderWatcher>();
		nlOptions = this.docEle.getElementsByTagName("folder");
		if (nlOptions != null && nlOptions.getLength() > 0) {
			for (int i = 0; i < nlOptions.getLength(); i++) {
				elementOption = (Element) nlOptions.item(i);				
				MediaFolderWatcher folder = new MediaFolderWatcher(elementOption.getAttribute("route"),
						Integer.parseInt(elementOption.getAttribute("pollinterval")));
				
				parseRules(elementOption, folder);
				
				folder.start();
				
				folders.add(folder);
			}
		}
		return folders;
	}

	private void parseRules(Element elementOption, MediaFolderWatcher folder) {
		NodeList nlOptions = elementOption.getElementsByTagName("rule");
		if (nlOptions != null && nlOptions.getLength() > 0) {
			for (int i = 0; i < nlOptions.getLength(); i++) {
				elementOption = (Element) nlOptions.item(i);				
				
				MoveReaction reaction = new MoveReaction(elementOption.getAttribute("reaction"));
				MatchingRule rule = new MatchingRule(elementOption.getAttribute("regexp"),reaction);

				folder.addRule(rule);
			}
		}
	}
}
