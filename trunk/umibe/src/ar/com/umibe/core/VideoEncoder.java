package ar.com.umibe.core;

import java.util.ArrayList;

import ar.com.umibe.core.matroska.InfoTrack;
import ar.com.umibe.core.matroska.TracksInfoParser;
import ar.com.umibe.util.AviSynthUtils;

public class VideoEncoder extends Encoder {

	public VideoEncoder(String config, String avsProfile, String tempdir, boolean verbosity) {
		super(config, avsProfile, tempdir, verbosity);
	}

	@Override
	public ArrayList<MediaTrack> encode(String file, TracksInfoParser tip) {
		ArrayList<MediaTrack> ret = new ArrayList<MediaTrack>();
		ArrayList<InfoTrack> videoTracks = tip.getTracks("video");
		
		String output = tempDir + "video.mkv";
		
		String script = AviSynthUtils.generateVideoScript(file,
				tempDir, avsProfile);
		this.encodeTrack(script,output);
		
		MediaTrack m = new MediaTrack();
		m.setRouteToTrack(output);
		if(videoTracks.size()>0){
			m.setInfoTrack(videoTracks.get(0));
		}
		ret.add(m);
		return ret;
	}
}
