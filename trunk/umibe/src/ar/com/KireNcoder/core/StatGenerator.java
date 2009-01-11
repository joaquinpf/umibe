package ar.com.KireNcoder.core;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import org.grlea.log.SimpleLogger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import ar.com.KireNcoder.util.FileUtils;



public class StatGenerator extends Observable {
	
	private ArrayList<Stats> sessionStats = new ArrayList<Stats>();
	private double originalSize = 0;
	private double encodedSize = 0;
	private double gbSaved = 0;
	private double compRatio = 0;
	private double elapsedTime = 0;
	private int transcoded = 0;
	private int remoteJobsDone = 0;
	private int localJobsDone = 0;
	private String route = "./config/stats.xml";
	private static final SimpleLogger log = new SimpleLogger(StatGenerator.class);
	
	public StatGenerator(){
		loadStats();
	}
	
	public synchronized Stats updateStats(VideoFile oldFile, String newFile, double elapsedTimeMin){	
		File fOld = new File(oldFile.getRoute());
		File fNew = new File(newFile);
		if(fOld.exists() && fNew.exists()){
			this.elapsedTime += elapsedTimeMin;
			this.transcoded++;
			if(oldFile.getOwnerHost().equals(DataModel.INSTANCE.getHostname())){
				this.localJobsDone++;
			} else {
				this.remoteJobsDone++;
			}
			//Tama�os en gigas
			double oldsize = fOld.length();
			double newsize = fNew.length();
			double divisor = 1024;
			oldsize = ((oldsize / divisor) / divisor) / divisor;
			newsize = ((newsize / divisor) / divisor) / divisor;
			
			this.originalSize += oldsize;
			this.encodedSize += newsize;
			this.gbSaved = originalSize - encodedSize;
			this.compRatio = originalSize / encodedSize;

			Stats s = new Stats();
			s.setElapsedTime(elapsedTimeMin);
			s.setEncodedSize(newsize);
			s.setOriginalSize(oldsize);
			s.setGbSaved(oldsize - newsize);
			s.setCompRatio(oldsize/newsize);
			s.setRouteToOwner(oldFile.getRoute());
			
			sessionStats.add(s);
			
			log.info("FINISHED ENCODING: " + FileUtils.getFileName(oldFile.getRoute()));
			log.info(s.toString());
			
			saveStats();
			
			setChanged();
			notifyObservers(s);
			
			return s;
		}
		return null;
	}
	
	private void saveStats(){
		Element root = new Element("Stats");
		Element el = new Element("transcoded");
		el.setText(Integer.toString(this.transcoded));
		root.addContent(el);
		el = new Element("originalSize");
		el.setText(Double.toString(this.originalSize));
		root.addContent(el);
		el = new Element("encodedSize");
		el.setText(Double.toString(this.encodedSize));
		root.addContent(el);
		el = new Element("gbSaved");
		el.setText(Double.toString(this.gbSaved));
		root.addContent(el);
		el = new Element("compRatio");
		el.setText(Double.toString(this.compRatio));
		root.addContent(el);
		el = new Element("remoteJobsDone");
		el.setText(Integer.toString(this.remoteJobsDone));
		root.addContent(el);
		el = new Element("localJobsDone");
		el.setText(Integer.toString(this.localJobsDone));
		root.addContent(el);
		el = new Element("elapsedTime");
		el.setText(Double.toString(this.elapsedTime));
		root.addContent(el);

		Document doc = new Document(root);
		// serialize it onto System.out
		try {
			XMLOutputter serializer = new XMLOutputter();
			Format f = serializer.getFormat();
			f.setIndent("  ");
			serializer.setFormat(f);
			FileWriter fileWriter = new FileWriter(route);
			serializer.output(doc, fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private void loadStats(){
		File f = new File(route);
		if (f.exists()) {
			XMLConfigLoader xml = new XMLConfigLoader(route);
			String s = xml.getNodeText("transcoded");
			if (s != null) {
				this.transcoded = Integer.parseInt(s);
			}
			s = xml.getNodeText("originalSize");
			if (s != null) {
				this.originalSize = Double.parseDouble(s);
			}
			s = xml.getNodeText("encodedSize");
			if (s != null) {
				this.encodedSize = Double.parseDouble(s);
			}
			s = xml.getNodeText("gbSaved");
			if (s != null) {
				this.gbSaved = Double.parseDouble(s);
			}
			s = xml.getNodeText("compRatio");
			if (s != null) {
				this.compRatio = Double.parseDouble(s);
			}			
			s = xml.getNodeText("remoteJobsDone");
			if (s != null) {
				this.remoteJobsDone = Integer.parseInt(s);
			}			
			s = xml.getNodeText("localJobsDone");
			if (s != null) {
				this.localJobsDone = Integer.parseInt(s);
			}			
			s = xml.getNodeText("elapsedTime");
			if (s != null) {
				this.elapsedTime = Double.parseDouble(s);
			}
		}
	}
	
	public synchronized void clear(){
		sessionStats = new ArrayList<Stats>();
		this.originalSize = 0;
		this.encodedSize = 0;
		this.gbSaved = 0;
		this.compRatio = 0;
		this.elapsedTime = 0;
		this.transcoded = 0;
		this.remoteJobsDone = 0;
		this.localJobsDone = 0;
		saveStats();
		setChanged();
		notifyObservers();
	}
	
	/**
	 * @return the transcoded
	 */
	public int getTranscoded() {
		return transcoded;
	}

	/**
	 * @return the originalSize
	 */
	public double getOriginalSize() {
		return originalSize;
	}

	/**
	 * @return the encodedSize
	 */
	public double getEncodedSize() {
		return encodedSize;
	}

	/**
	 * @return the gbSaved
	 */
	public double getGbSaved() {
		return gbSaved;
	}

	/**
	 * @return the compRatio
	 */
	public double getCompRatio() {
		return compRatio;
	}

	/**
	 * @return the remoteJobsDone
	 */
	public int getRemoteJobsDone() {
		return remoteJobsDone;
	}

	/**
	 * @return the localJobsDone
	 */
	public int getLocalJobsDone() {
		return localJobsDone;
	}

	/**
	 * @return the elapsedTime
	 */
	public double getElapsedTime() {
		return elapsedTime;
	}
}
