package ar.com.KireNcoder.matroska;

public class Track {
	private int trackNumber;
	private String trackType;
	private String language;
	private String name;
	
	public Track (int trackNumber, String trackType, String language, String name) {
		this.language = language;
		this.name = name;
		this.trackType = trackType;
		this.trackNumber = trackNumber;
	}
	
	public int getTrackNumber() {
		return this.trackNumber;
	}
	
	public String getName() {
		return this.name;
	}

	public String getLanguage() {
		return this.language;
	}

	public String getTrackType() {
		return this.trackType;
	}
}
