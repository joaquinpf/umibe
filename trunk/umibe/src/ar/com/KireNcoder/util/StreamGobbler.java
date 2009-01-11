package ar.com.KireNcoder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;

import ar.com.KireNcoder.core.DataModel;

public class StreamGobbler extends Observable implements Runnable{
	private InputStream is;
	private String type;
	private boolean verbosity = false;
	private boolean uiVerbosity = false;
	
	public StreamGobbler(InputStream is, String type, boolean verbosity, boolean uiVerbosity) {
		this.is = is;
		this.type = type;
		this.verbosity = verbosity;
		this.uiVerbosity = uiVerbosity;
		if(uiVerbosity == true && DataModel.INSTANCE.getUi() != null){
			this.addObserver(DataModel.INSTANCE.getUi());
		}
	}

	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(this.is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if(verbosity == true) 
					System.out.println(type + ">" + line);
				if(uiVerbosity == true){
					setChanged();
					notifyObservers(line);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void start() {
		Thread workerThread = new Thread(this);
		workerThread.start();
	}
}