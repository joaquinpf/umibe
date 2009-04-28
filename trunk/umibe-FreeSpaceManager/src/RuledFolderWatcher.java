

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ar.com.umibe.commons.monitor.BaseListener;
import ar.com.umibe.commons.monitor.DirectoryWatcher;
import ar.com.umibe.commons.monitor.IResourceListener;
import ar.com.umibe.commons.regex.matching.MatchingRule;
import ar.com.umibe.commons.util.UmibeFileUtils;


/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class RuledFolderWatcher extends BaseListener implements IResourceListener {

	private String directory;
	private int pollInterval;
	private ArrayList<MatchingRule> rules = new ArrayList<MatchingRule>();

	/**
	 * Connstructor
	 */
	public RuledFolderWatcher(String directory, int pollInterval) {
		super();
		this.directory = directory;
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
							for(MatchingRule rule: rules){
								rule.matchRule(files[i].getCanonicalPath());
							}
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
					for(MatchingRule rule: rules){
						rule.matchRule(file.getCanonicalPath());
					}
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
				try {
					for(MatchingRule rule: rules){
						rule.matchRule(file.getCanonicalPath());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onDelete(Object deletedResource) {		
	}
	
	public ArrayList<MatchingRule> getRules() {
		return rules;
	}

	public void setRules(ArrayList<MatchingRule> rules) {
		this.rules = rules;
	}
	
	public void addRule(MatchingRule rule) {
		this.rules.add(rule);
	}
}
