package ar.com.umibe.core.xml;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BaseXMLParser {

	protected Document dom;
	protected Element docEle;

	public BaseXMLParser(String route) {
		try {
			File f = new File(route);
			this.dom = openDocument(f.toURI().toURL().toString());
			this.docEle = this.dom.getDocumentElement();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
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

	protected Document openDocument(final String route) {
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
