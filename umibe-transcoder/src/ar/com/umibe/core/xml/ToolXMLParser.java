package ar.com.umibe.core.xml;


import java.util.ArrayList;
import java.util.Hashtable;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ar.com.umibe.commons.xml.BaseXMLParser;
import ar.com.umibe.core.tool.ToolMode;
import ar.com.umibe.core.tool.ToolOption;

/**
 * @author Joaqu�n Alejandro P�rez Fuentes
 */
public class ToolXMLParser extends BaseXMLParser {


	public ToolXMLParser(String route) {
		super(route);
	}

	public ArrayList<ToolMode> getToolModes() {
		NodeList nlOptions;
		Element elementOption;
		ArrayList<ToolMode> options = new ArrayList<ToolMode>();
		nlOptions = this.docEle.getElementsByTagName("Mode");
		if (nlOptions != null && nlOptions.getLength() > 0) {
			for (int i = 0; i < nlOptions.getLength(); i++) {
				elementOption = (Element) nlOptions.item(i);
				int limit = Integer.parseInt(elementOption.getAttribute("steps"));
				ToolMode tm = new ToolMode();
				tm.name = elementOption.getAttribute("name");
				ArrayList<String> steps = new ArrayList<String>();
				for(int j=1;j<=limit; j++){
					NodeList step = elementOption.getElementsByTagName("ExecutionStep" + Integer.toString(j));
					Element stepElem = (Element)step.item(0);
					steps.add(stepElem.getTextContent());
				}
				tm.steps = steps;
				options.add(tm);
			}
		}
		return options;
	}

	public Hashtable<String,ToolOption> getToolOptions() {
		NodeList nlOptions;
		Element elementOption;
		Hashtable<String,ToolOption> options = new Hashtable<String,ToolOption>();
		nlOptions = this.docEle.getElementsByTagName("Option");
		if (nlOptions != null && nlOptions.getLength() > 0) {
			for (int i = 0; i < nlOptions.getLength(); i++) {
				elementOption = (Element) nlOptions.item(i);
				ToolOption to = new ToolOption();                       
				to.key = elementOption.getAttribute("key");
				to.name = elementOption.getAttribute("name");
				to.value = elementOption.getAttribute("value");
				to.modes = elementOption.getAttribute("modes");
				to.mandatory = Boolean.parseBoolean(elementOption.getAttribute("mandatory"));
				to.advanced = Boolean.parseBoolean(elementOption.getAttribute("advanced"));
				options.put(to.key,to);
			}
		}
		return options;
	}
}