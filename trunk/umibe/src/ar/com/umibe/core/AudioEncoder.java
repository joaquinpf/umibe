package ar.com.umibe.core;

import java.util.ArrayList;

import ar.com.umibe.core.matroska.InfoTrack;
import ar.com.umibe.core.matroska.MatroskaUtils;
import ar.com.umibe.core.matroska.TracksInfoParser;
import ar.com.umibe.util.AviSynthUtils;
import ar.com.umibe.util.UmibeFileUtils;

public class AudioEncoder extends Encoder {

	public AudioEncoder(String config, String avsProfile, String tempdir, boolean verbosity) {
		super(config, avsProfile, tempdir, verbosity);
	}

	@Override
	public ArrayList<MediaTrack> encode(String file, TracksInfoParser tip) {
		ArrayList<MediaTrack> ret = new ArrayList<MediaTrack>();
		if(UmibeFileUtils.isSpecificMediaFile(file,"mkv") == true){
		
			//Audio, encodea cada track del idioma JPN o todas si no hay ninguna JPN
			ArrayList<InfoTrack> audioTracks = tip.getTracks("audio","jpn");
			if(audioTracks.size() == 0) {
				audioTracks = tip.getTracks("audio");
			}
			MatroskaUtils.demux(file, tempDir, audioTracks , verbosity);
			String[] audios = UmibeFileUtils.filterFiles(tempDir, "audio");

			for(int i=0; i<audios.length; i++){
				//Generacion de script de audio. Encoding de audio
				MatroskaUtils.mux(UmibeFileUtils.getFullPath(tempDir + audios[i]),UmibeFileUtils.getFullPath(tempDir + audios[i]) + ".mka");
				String script = AviSynthUtils.generateAudioScript(UmibeFileUtils.getFullPath(tempDir + audios[i]) + ".mka",
						tempDir, avsProfile);
				this.encodeTrack(script,tempDir + "encodedaudio_" + i + ".m4a");
				MediaTrack m = new MediaTrack();
				m.setRouteToTrack(tempDir + "encodedaudio_" + i + ".m4a");
				m.setInfoTrack(audioTracks.get(i));
				ret.add(m);
			}
		} else {
			//Generacion de script de audio. Encoding de audio
			String script = AviSynthUtils.generateAudioScript(file,
					tempDir, avsProfile);
			this.encodeTrack(script,tempDir + "encodedaudio_" + 0 + ".m4a");
			MediaTrack m = new MediaTrack();
			m.setRouteToTrack(tempDir + "encodedaudio_" + 0 + ".m4a");
			ret.add(m);
		}
		return ret;
	}
}
