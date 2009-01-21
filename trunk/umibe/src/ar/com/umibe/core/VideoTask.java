package ar.com.umibe.core;

import java.io.File;
import java.io.Serializable;

import ar.com.umibe.core.xml.XMLConfigLoader;


public class VideoTask implements Comparable<VideoTask>, Serializable {

	private static final long serialVersionUID = 8723856021264116783L;
	private String route = " ";
	private String vProfile;
	private String aProfile;
	private String aviSynthProfile;
	private String outputFolder;
	private String moveAfterDone;
	private String ownerHost;
	private boolean keepOriginalAudio;
	private boolean keepOriginalVideo;
	private int priority;
	private Status status = Status.WAITING;
	private String profile;
	private boolean enabled = true;
	
	public VideoTask(String route, String profile) {
		this.route = route;
		loadProfiles(profile);
		this.ownerHost = DataModel.INSTANCE.getHostname();
	}
	
	/*public VideoFile(String route, String vProfile, String aProfile,
			String aviSynthProfile, String outputFolder, String moveAfterDone, int priority,
			String ownerHost) {
		this.route = route;
		this.vProfile = vProfile;
		this.aProfile = aProfile;
		this.aviSynthProfile = aviSynthProfile;
		this.outputFolder = outputFolder;
		this.moveAfterDone = moveAfterDone;
		this.priority = priority;
		this.ownerHost = ownerHost;
	}*/

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status a) {
		this.status = a;
	}

	public String getRoute() {
		return this.route;
	}

	public String getVProfile() {
		return this.vProfile;
	}

	public String getAProfile() {
		return this.aProfile;
	}

	public String getAviSynthProfile() {
		return this.aviSynthProfile;
	}

	public int getPriority() {
		return this.priority;
	}

	public String getOutputFolder() {
		return this.outputFolder;
	}

	public String getMoveAfterDone() {
		return this.moveAfterDone;
	}

	public boolean canIDoYou() {
		if(DataModel.INSTANCE.getHostname().equals(this.ownerHost))
			return true;
		else
			if(this.route.startsWith("\\\\") && this.outputFolder.startsWith("\\\\")){
				return true;
			}
		return false;
	}

	public String getOwnerHost(){
		return this.ownerHost;
	}

	@Override
	public int compareTo(VideoTask o) {
		if (this.getRoute().equals(o.getRoute())) {
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {
		return this.route;
	}
	
	public void loadProfiles(String profile) {
		this.profile = profile;
		File f = new File(profile);
		if (f.exists()) {
			XMLConfigLoader xml = new XMLConfigLoader(profile);
			String s = xml.getNodeText("DoneDirectory");
			if (s != null) {
				this.outputFolder = s;
			} else {
				this.outputFolder = DataModel.INSTANCE.getDoneDir();
			}
			s = xml.getNodeText("DefaultAudioProfile");
			if (s != null) {
				this.aProfile = s;
			} else {
				this.aProfile = DataModel.INSTANCE.getAProfile();
			}
			s = xml.getNodeText("DefaultVideoProfile");
			if (s != null) {
				this.vProfile = s;
			} else {
				this.vProfile = DataModel.INSTANCE.getVProfile();
			}
			s = xml.getNodeText("DefaultAviSynthProfile");
			if (s != null) {
				this.aviSynthProfile = s;
			} else {
				this.aviSynthProfile = DataModel.INSTANCE.getAviSynthProfile();
			}
			s = xml.getNodeText("DefaultPriority");
			if (s != null) {
				this.priority = Integer.parseInt(s);
			} else {
				this.priority = DataModel.INSTANCE.getPriority();
			}
			s = xml.getNodeText("MoveToAfterDone");
			if (s != null) {
				this.moveAfterDone = s;
			} else {
				this.moveAfterDone = DataModel.INSTANCE.getMoveAfterDone();
			}
			s = xml.getNodeText("KeepOriginalAudio");
			if (s != null) {
				this.keepOriginalAudio = Boolean.parseBoolean(s);
			} else {
				this.keepOriginalAudio = DataModel.INSTANCE.getKeepOriginalAudio();
			}
			s = xml.getNodeText("KeepOriginalVideo");
			if (s != null) {
				this.keepOriginalVideo = Boolean.parseBoolean(s);
			} else {
				this.keepOriginalVideo = DataModel.INSTANCE.getKeepOriginalVideo();
			}
		} else {
			this.moveAfterDone = DataModel.INSTANCE.getMoveAfterDone();
			this.priority = DataModel.INSTANCE.getPriority();
			this.aviSynthProfile = DataModel.INSTANCE.getAviSynthProfile();
			this.vProfile = DataModel.INSTANCE.getVProfile();
			this.aProfile = DataModel.INSTANCE.getAProfile();
			this.outputFolder = DataModel.INSTANCE.getDoneDir();
			this.keepOriginalAudio = DataModel.INSTANCE.getKeepOriginalAudio();
			this.keepOriginalVideo = DataModel.INSTANCE.getKeepOriginalVideo();
		}
	}

	public boolean isKeepOriginalAudio() {
		return keepOriginalAudio;
	}

	public String getProfile() {
		return profile;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isKeepOriginalVideo() {
		return keepOriginalVideo;
	}

}
