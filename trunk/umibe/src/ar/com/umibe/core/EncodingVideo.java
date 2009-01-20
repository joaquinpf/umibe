package ar.com.umibe.core;

import java.util.ArrayList;

public class EncodingVideo {
	private ArrayList<MediaTrack> videoTracks;
	private ArrayList<MediaTrack> audioTracks;
	private ArrayList<MediaTrack> subtitleTracks;
	private String chapters;
	private String info;
	private String filename;
	
	public EncodingVideo(String filename){
		this.filename = filename;
	}

	public ArrayList<MediaTrack> getVideoTracks() {
		return videoTracks;
	}

	public void setVideoTracks(ArrayList<MediaTrack> videoTracks) {
		this.videoTracks = videoTracks;
	}

	public ArrayList<MediaTrack> getAudioTracks() {
		return audioTracks;
	}

	public void setAudioTracks(ArrayList<MediaTrack> audioTracks) {
		this.audioTracks = audioTracks;
	}

	public ArrayList<MediaTrack> getSubtitleTracks() {
		return subtitleTracks;
	}

	public void setSubtitleTracks(ArrayList<MediaTrack> subtitleTracks) {
		this.subtitleTracks = subtitleTracks;
	}

	public String getChapters() {
		return chapters;
	}

	public void setChapters(String chapters) {
		this.chapters = chapters;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
