package ar.com.umibe.core.execution;

import java.io.File;
import java.io.IOException;

import org.grlea.log.SimpleLogger;
import org.jvnet.winp.WinProcess;

public class WindowsCLIEnvironment implements IExecutionEnvironment {

	private final String CONSOLE_ENV = "cmd /c start \"Umibe\" /LOW /B /WAIT ";
	private final Runtime runtime = Runtime.getRuntime();
	private final SimpleLogger log = new SimpleLogger(WindowsCLIEnvironment.class);
	private Process process = null;
	
	public int execute(String command, String workingDir, boolean verbosity, boolean uiVerbosity) {
		try {
			process = runtime.exec(CONSOLE_ENV + command,null,new File(workingDir));

			log.info(command);

			//TODO Mejorar UIVERBOSITY
			
			StreamGobbler errorGobbler = new StreamGobbler(process
					.getErrorStream(), "ERROR", verbosity, uiVerbosity);

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(process
					.getInputStream(), "OUTPUT", verbosity, false);

			// kick them off
			errorGobbler.start();
			outputGobbler.start();
		
			return process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int execute(String command, boolean verbosity, boolean uiVerbosity) {
		try {
			process = runtime.exec(CONSOLE_ENV + command);

			log.info(command);

			//TODO Mejorar UIVERBOSITY
			
			StreamGobbler errorGobbler = new StreamGobbler(process
					.getErrorStream(), "ERROR", verbosity, uiVerbosity);

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(process
					.getInputStream(), "OUTPUT", verbosity, false);

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			return process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void killProcess() {
		if(process != null){
			WinProcess wp = new WinProcess(process);	
			wp.killRecursively();
		}
	}
}