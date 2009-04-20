package ar.com.feedactionizer.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class StreamGobbler implements Runnable{
	private InputStream is;
	private String type;
	
	public StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(this.is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(type + ">" + line);
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