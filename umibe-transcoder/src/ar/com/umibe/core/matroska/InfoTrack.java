package ar.com.umibe.core.matroska;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class InfoTrack {
	private int trackNumber;
	private String trackType;
	private String language;
	private String name;
	private int displayW = 0;
	private int displayH = 0;
	
	public InfoTrack (int trackNumber, String trackType, String language, String name) {
		this.language = language;
		this.name = name;
		this.trackType = trackType;
		this.trackNumber = trackNumber;
	}
	
	public InfoTrack (int trackNumber, String trackType, String language, String name,
			int displayW, int displayH) {
		this.language = language;
		this.name = name;
		this.trackType = trackType;
		this.trackNumber = trackNumber;
		this.displayH = displayH;
		this.displayW = displayW;
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

	public int getDisplayW() {
		return displayW;
	}

	public int getDisplayH() {
		return displayH;
	}
}
