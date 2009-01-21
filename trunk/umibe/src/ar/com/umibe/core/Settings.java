package ar.com.umibe.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Settings {
	public String tempDir = "./temp/";
	public String doneDir = "./done/";
	public String moveAfterDone = " ";
	public String vProfile = "profiles/Video_X264_AnimeHD_1PassCQ.xml";
	public String aProfile = "profiles/Audio_NeroAACEnc_CBR_64kbps.xml";
	public String aviSynthProfile = "profiles/AviSynth_Default.xml";
	public int priority = 3;
	public String hostname;
	public String profilesDir = "./profiles/";
	public boolean keepOriginalAudio = false;
	public boolean keepOriginalVideo = false;
		
	
	public Settings(){
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
