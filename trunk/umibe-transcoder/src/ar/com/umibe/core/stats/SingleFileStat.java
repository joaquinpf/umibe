package ar.com.umibe.core.stats;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import ar.com.umibe.commons.util.UmibeFileUtils;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class SingleFileStat {
	private double originalSize = 0;
	private double encodedSize = 0;
	private double gbSaved = 0;
	private double compRatio = 0;
	private double elapsedTime = 0;
	private String routeToOwner = null;
	private Date finishedDate = null;
	
	public ArrayList<String> toStringArray(){
    	DecimalFormat formatter = new DecimalFormat("###.##");
		double mul = 1024;
		String filename = " - ";
		if(routeToOwner != null)
			filename = UmibeFileUtils.getFileName(routeToOwner);
		ArrayList<String> a = new ArrayList<String>();
		a.add("File               : " + filename);
		a.add("Original size : " + formatter.format(originalSize * mul) + "     |     " + "Encoded size: " + formatter.format(encodedSize * mul));
		a.add("MB saved     : " + formatter.format(gbSaved * mul) + "     |     " + "Compression ratio: " + formatter.format(compRatio));
		a.add("Elapsed time: " + formatter.format(elapsedTime) + "min");
		a.add("--------------------------------------------------------");
		return  a; 
	}
	
	/**
	 * @return the originalSize
	 */
	public double getOriginalSize() {
		return originalSize;
	}
	/**
	 * @param originalSize the originalSize to set
	 */
	public void setOriginalSize(double originalSize) {
		this.originalSize = originalSize;
	}
	/**
	 * @return the encodedSize
	 */
	public double getEncodedSize() {
		return encodedSize;
	}
	/**
	 * @param encodedSize the encodedSize to set
	 */
	public void setEncodedSize(double encodedSize) {
		this.encodedSize = encodedSize;
	}
	/**
	 * @return the gbSaved
	 */
	public double getGbSaved() {
		return gbSaved;
	}
	/**
	 * @param gbSaved the gbSaved to set
	 */
	public void setGbSaved(double gbSaved) {
		this.gbSaved = gbSaved;
	}
	/**
	 * @return the compRatio
	 */
	public double getCompRatio() {
		return compRatio;
	}
	/**
	 * @param compRatio the compRatio to set
	 */
	public void setCompRatio(double compRatio) {
		this.compRatio = compRatio;
	}
	/**
	 * @return the elapsedTime
	 */
	public double getElapsedTime() {
		return elapsedTime;
	}
	/**
	 * @param elapsedTime the elapsedTime to set
	 */
	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	/**
	 * @return the routeToOwner
	 */
	public String getRouteToOwner() {
		return routeToOwner;
	}
	/**
	 * @param routeToOwner the routeToOwner to set
	 */
	public void setRouteToOwner(String routeToOwner) {
		this.routeToOwner = routeToOwner;
	}
	/**
	 * @return the finishedDate
	 */
	public Date getFinishedDate() {
		return finishedDate;
	}
	/**
	 * @param finishedDate the finishedDate to set
	 */
	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}
}
