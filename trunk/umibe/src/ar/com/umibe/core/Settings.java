package ar.com.umibe.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

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
	public HashMap<String, String> tools = new HashMap<String, String>();
		
	
	public Settings(){
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		tools.put("x264.exe", "./resources/x264.exe");
		tools.put("neroAacEnc.exe", "./resources/neroAacEnc.exe");
		tools.put("mkvextract.exe", "./resources/mkvtools/mkvextract.exe");
		tools.put("mkvinfo.exe", "./resources/mkvtools/mkvinfo.exe");
		tools.put("mkvmerge.exe", "./resources/mkvtools/mkvmerge.exe");
		tools.put("MediaInfo.exe", "./resources/MediaInfo/MediaInfo.exe");
		tools.put("BePipe.exe", "./resources/BePipe.exe");
		tools.put("mplayer.exe", "./resources/mencoder/mplayer.exe");
		tools.put("mencoder.exe", "./resources/mencoder/mencoder.exe");
	}
}
