package ar.com.umibe.core;

import java.io.File;
import java.io.Serializable;

import ar.com.umibe.core.xml.XMLConfigLoader;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class VideoTask implements Comparable<VideoTask>, Serializable {

	protected static final long serialVersionUID = 8723856021264116783L;
	protected String route = " ";
	protected String vProfile;
	protected String aProfile;
	protected String aviSynthProfile;
	protected String outputFolder;
	protected String moveAfterDone;
	protected String ownerHost;
	protected boolean keepOriginalAudio;
	protected boolean keepOriginalVideo;
	protected int priority;
	protected Status status = Status.WAITING;
	protected String profile;
	protected boolean enabled = true;
	protected int filesize = 0;
	
	public VideoTask(String route, String profile) {
		this.route = route;
		File f = new File(route);
		this.filesize = (int) (f.length() / 1024 / 1024);
		loadProfiles(profile);
		this.ownerHost = DataModel.INSTANCE.getHostname();
	}
	
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

	public int getFilesize() {
		return filesize;
	}

	public void setKeepOriginalAudio(boolean keepOriginalAudio) {
		this.keepOriginalAudio = keepOriginalAudio;
	}

	public void setKeepOriginalVideo(boolean keepOriginalVideo) {
		this.keepOriginalVideo = keepOriginalVideo;
	}
}
