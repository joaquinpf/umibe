package ar.com.umibe.core.xml;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class XMLConfigLoader extends BaseXMLParser {

	public XMLConfigLoader(String route) {
		super(route);
	}

	public ArrayList<String> getOptions(String nodename) {
		NodeList nlOptions;
		Element elementOption;
		ArrayList<String> options = new ArrayList<String>();
		nlOptions = this.docEle.getElementsByTagName(nodename);
		if (nlOptions != null && nlOptions.getLength() > 0) {
			for (int i = 0; i < nlOptions.getLength(); i++) {
				elementOption = (Element) nlOptions.item(i);
				options.add(elementOption.getTextContent());
			}
		}
		return options;
	}
}
