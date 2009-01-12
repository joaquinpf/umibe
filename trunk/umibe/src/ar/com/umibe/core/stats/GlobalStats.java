package ar.com.umibe.core.stats;

public class GlobalStats {
	private double originalSize = 0;
	private double encodedSize = 0;
	private double gbSaved = 0;
	private double compRatio = 0;
	private double elapsedTime = 0;
	private int transcoded = 0;
	private int remoteJobsDone = 0;
	private int localJobsDone = 0;
	
	public double getOriginalSize() {
		return originalSize;
	}
	public void setOriginalSize(double originalSize) {
		this.originalSize = originalSize;
	}
	public double getEncodedSize() {
		return encodedSize;
	}
	public void setEncodedSize(double encodedSize) {
		this.encodedSize = encodedSize;
	}
	public double getGbSaved() {
		return gbSaved;
	}
	public void setGbSaved(double gbSaved) {
		this.gbSaved = gbSaved;
	}
	public double getCompRatio() {
		return compRatio;
	}
	public void setCompRatio(double compRatio) {
		this.compRatio = compRatio;
	}
	public double getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public int getTranscoded() {
		return transcoded;
	}
	public void setTranscoded(int transcoded) {
		this.transcoded = transcoded;
	}
	public int getRemoteJobsDone() {
		return remoteJobsDone;
	}
	public void setRemoteJobsDone(int remoteJobsDone) {
		this.remoteJobsDone = remoteJobsDone;
	}
	public int getLocalJobsDone() {
		return localJobsDone;
	}
	public void setLocalJobsDone(int localJobsDone) {
		this.localJobsDone = localJobsDone;
	}
	
	
}
