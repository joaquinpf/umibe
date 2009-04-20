package ar.com.umibe.core.encoder;

import java.util.ArrayList;

import ar.com.umibe.commons.util.UmibeFileUtils;
import ar.com.umibe.core.MediaTrack;
import ar.com.umibe.core.matroska.InfoTrack;
import ar.com.umibe.core.matroska.MatroskaUtils;
import ar.com.umibe.core.matroska.TracksInfoParser;
import ar.com.umibe.util.AviSynthUtils;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class VideoEncoder extends Encoder {

	protected boolean keepOriginalTracks = false;

	public VideoEncoder(String config, String avsProfile, String tempdir, boolean keepOriginalTracks, 
			boolean verbosity) {
		super(config, avsProfile, tempdir, verbosity);
		this.keepOriginalTracks = keepOriginalTracks;
	}

	@Override
	public ArrayList<MediaTrack> encode(String file, TracksInfoParser tip) {
		ArrayList<MediaTrack> ret = new ArrayList<MediaTrack>();
		ArrayList<InfoTrack> videoTracks = tip.getTracks("video");
		
		MatroskaUtils mkv = new MatroskaUtils();
		mkv.demux(file, tempDir, tip.getTracks("video"), true);
		
		String[] videos = UmibeFileUtils.filterFiles(tempDir, "video");

		if(keepOriginalTracks == true){				
			for(int i=0; i<videos.length; i++){
				MediaTrack m = new MediaTrack();
				m.setRouteToTrack(tempDir + videos[i]);
				if(videoTracks!=null && videoTracks.size() >= i){
					m.setInfoTrack(videoTracks.get(i));
				}
				ret.add(m);
			}
		} else {
			String output = tempDir + "video.mkv";
			String script;
			if(file.endsWith("avs") == false){
				script = AviSynthUtils.generateVideoScript(file,
						tempDir, avsProfile);
			} else {
				script = file;
			}
			this.encodeTrack(script,output);

			MediaTrack m = new MediaTrack();
			m.setRouteToTrack(output);
			if(videoTracks != null && videoTracks.size()>0){
				m.setInfoTrack(videoTracks.get(0));
			}
			ret.add(m);
		}
		return ret;
	}
}
