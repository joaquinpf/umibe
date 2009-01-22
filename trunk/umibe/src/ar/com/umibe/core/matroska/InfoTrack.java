package ar.com.umibe.core.matroska;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class InfoTrack {
	private int trackNumber;
	private String trackType;
	private String language;
	private String name;
	
	public InfoTrack (int trackNumber, String trackType, String language, String name) {
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
