package ar.com.umibe.core.matroska;

import java.util.ArrayList;

import ar.com.umibe.core.EncodingVideo;
import ar.com.umibe.core.MediaTrack;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public interface IContainer {

	public ArrayList<MediaTrack> demux(String input, String outputFolder, ArrayList<InfoTrack> tracks, boolean verbosity);
	
	public String extractChapters(String input, String outputFolder);
	
	public void merge(EncodingVideo outputvideo, String dirToMux, String doneDir, boolean verbosity);

	public void mux(String fullPath, String output);
}
