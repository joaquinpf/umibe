package ar.com.umibe.core.matroska;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;



public class TracksInfoParser {
	
	public ArrayList<InfoTrack> tracks = null;

	public TracksInfoParser(String input, String workDir) {
		File f = new File(input);
		if(f.exists()){
			String str = XMLIzeTracks(input, workDir);
			if(str != null) {
				loadXMLTracks(str);//XMLPath
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadXMLTracks(String xmlinput) {
		tracks = new ArrayList<InfoTrack>();
		File f = new File(xmlinput);
		if (f.exists()) {
			try {
				Document dom = openDocument(f.toURI().toURL().toString());			

				Element docEle = dom.getRootElement();

				List<Element> jobs = docEle.getChildren("Track");

				Iterator<Element> i = jobs.iterator();
				while (i.hasNext()) {
					Element e = i.next();
					this.tracks.add(new InfoTrack(Integer.parseInt(e.getAttributeValue("Track_number")), 
							e.getAttributeValue("Track_type"), e.getAttributeValue("Language"), 
							e.getAttributeValue("Name")));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	}
		
	private Document openDocument(final String route) {
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();

		try {
			return builder.build(route);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	private String XMLIzeTracks(String input, String workDir) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(input));
			String str = in.readLine();
			while(!str.equals("| + A track")){
				str = in.readLine(); 
			}
			//crear nodo padre Tracks
			
			Element tracks = new Element("Tracks");
			
			str = in.readLine();
			while(!str.contains("EbmlVoid")) {
				//Crear elemento Track
				Element track = new Element("Track");
				while(!str.equals("| + A track") && !str.contains("EbmlVoid")) {
					String[] split = str.split(": ");
					String[] splitName = split[0].split("\\+ ");
					//Agregar subelemento splitName[0], split[1] a track
					if(split.length >= 2 && !str.contains("CodecPrivate")) {
						track.setAttribute(splitName[1].replaceAll(" ","_"),split[1]);
					}
					str = in.readLine(); 
				}
				//agregar track a tracks
				tracks.addContent(track);
				if(!str.contains("EbmlVoid"))
					str = in.readLine(); 
			}
			in.close();

			Document doc = new Document(tracks);
			// serialize it onto System.out
			XMLOutputter serializer = new XMLOutputter();
			Format f = serializer.getFormat();
			f.setIndent("  ");
			serializer.setFormat(f);
			FileWriter fileWriter = new FileWriter(workDir + "mkvinfo.xml");
			serializer.output(doc, fileWriter);
			fileWriter.flush();
			fileWriter.close();
		    
			return workDir + "mkvinfo.xml";
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<InfoTrack> getTracks(String type) {
		if(tracks != null){
			ArrayList<InfoTrack> a = new ArrayList<InfoTrack>();
			for(int i=0; i<tracks.size(); i++){
				if(tracks.get(i).getTrackType().equals(type))
					a.add(tracks.get(i));
			}
			return a;
		}
		else {
			return null;
		}
	}
	
	public ArrayList<InfoTrack> getTracks(String type, String language) {
		if(tracks != null){
			ArrayList<InfoTrack> a = new ArrayList<InfoTrack>();
			for(int i=0; i<tracks.size(); i++){
				if(tracks.get(i).getTrackType().equals(type) &&
						tracks.get(i).getLanguage().equals(language))
					a.add(tracks.get(i));
			}
			return a;
		}
		else {
			return null;
		}
	}
}
