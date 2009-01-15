package ar.com.umibe.core;

import ar.com.umibe.core.matroska.InfoTrack;

public class MediaTrack {
	private String routeToTrack = null;
	private InfoTrack infoTrack = null;
	
	public String getRouteToTrack() {
		return routeToTrack;
	}
	public void setRouteToTrack(String routeToTrack) {
		this.routeToTrack = routeToTrack;
	}
	public InfoTrack getInfoTrack() {
		return infoTrack;
	}
	public void setInfoTrack(InfoTrack infoTrack) {
		this.infoTrack = infoTrack;
	}	
}
