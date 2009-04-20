package ar.com.umibe.core.monitor;

import java.io.File;
import java.io.IOException;

import ar.com.umibe.commons.monitor.BaseListener;
import ar.com.umibe.commons.monitor.DirectoryWatcher;
import ar.com.umibe.commons.monitor.IFileListener;
import ar.com.umibe.commons.util.UmibeFileUtils;
import ar.com.umibe.core.DataModel;
import ar.com.umibe.core.VideoTask;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class MediaFolderWatcher extends BaseListener implements IFileListener {

	private String directory;
	private int pollInterval;
	private String profile;

	/**
	 * Connstructor
	 */
	/*public MediaFolderWatcher(String directory, int pollInterval) {
		super();
		this.directory = directory;
		this.profile = this.directory + "/config/config.xml";
		this.pollInterval = pollInterval;
	}*/

	public MediaFolderWatcher(String directory, int pollInterval, String profile) {
		super();
		this.directory = directory;
		this.profile = profile;
		this.pollInterval = pollInterval;
	}
	
	public void start(){
		DirectoryWatcher dw = new DirectoryWatcher(UmibeFileUtils.getFullPath(directory),
				pollInterval);
		dw.addListener(this);
		dw.start();
	}
	
	public int getPollinterval() {
		return this.pollInterval;
	}

	public String getDirectory() {
		return this.directory;
	}
	
	public void onStart(Object monitoredResource) {
		// On startup
		if (monitoredResource instanceof File) {
			File resource = (File) monitoredResource;
			if (resource.isDirectory()) {
				File[] files = resource.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {
						try {
							DataModel.INSTANCE.addToQueue(files[i].getCanonicalPath(), profile);
							System.out.println("<MediaFolderWatcher> Added: "
									+ files[i].getAbsolutePath());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				System.out.println("<MediaFolderWatcher> Start: "
						+ resource.getAbsolutePath());
			}
		}
	}

	public void onStop(Object notMonitoredResource) {

	}

	public void onAdd(Object newResource) {
		if (newResource instanceof File) {
			File file = (File) newResource;
			if (file.isFile()) {
				try {
					DataModel.INSTANCE.addToQueue(file.getCanonicalPath(), profile);
					System.out.println("<MediaFolderWatcher> Added: "
							+ file.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void onChange(Object changedResource) {
		if (changedResource instanceof File) {
			File file = (File) changedResource;
			if (file.isFile()) {
				System.out.println(file.getAbsolutePath() + " is changed");
			}

		}
	}

	public void onDelete(Object deletedResource) {
		if (deletedResource instanceof String) {
			String deletedFile = (String) deletedResource;
			DataModel.INSTANCE.deleteFromQueue(new VideoTask(deletedFile, DataModel.INSTANCE.getProfilesDir() + "Profile_Default.xml"));
			System.out.println("<MediaFolderWatcher> Deleted: " + deletedFile);
		}
	}
	
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
}
