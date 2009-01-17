package ar.com.umibe.core.tool;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ToolXMLParser {


	private Document dom;
	private Element docEle;


	public ToolXMLParser(String route) {
		try {
			File f = new File(route);
			this.dom = openDocument(f.toURI().toURL().toString());
			this.docEle = this.dom.getDocumentElement();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
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


	public String getNodeText(String name) {
		NodeList nlOption;
		Element elementOption;
		String option = null;


		nlOption = this.docEle.getElementsByTagName(name);
		if (nlOption != null && nlOption.getLength() > 0) {
			option = new String();
			elementOption = (Element) nlOption.item(0);
			option += elementOption.getTextContent();
		}
		return option;
	}


	private Document openDocument(final String route) {
		Document doc = null;
		DocumentBuilderFactory dbf;


		// Obteher el objeto DocumentBuilderFactory
		dbf = DocumentBuilderFactory.newInstance();
		try {
			// Usar DocumentBuilderFactory para crear un DocumentBuilder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// Parsear a partir del archivo
			doc = db.parse(route);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return doc;
	}
}