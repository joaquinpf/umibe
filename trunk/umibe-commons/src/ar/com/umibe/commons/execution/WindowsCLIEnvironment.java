package ar.com.umibe.commons.execution;

import java.io.File;
import java.io.IOException;

import org.jvnet.winp.WinProcess;

import ar.com.umibe.commons.execution.IExecutionEnvironment;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class WindowsCLIEnvironment implements IExecutionEnvironment {

	private final String CONSOLE_ENV = "cmd /c start \"Umibe\" /LOW /B /WAIT ";
	private final Runtime runtime = Runtime.getRuntime();
	private Process process = null;
	
	@Override
	public int execute(String command, String workingDir) {
		try {
			process = runtime.exec(CONSOLE_ENV + command,null,new File(workingDir));
			
			StreamGobbler errorGobbler = new StreamGobbler(process
					.getErrorStream(), "ERROR");

			StreamGobbler outputGobbler = new StreamGobbler(process
					.getInputStream(), "OUTPUT");

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

	@Override
	public int execute(String command) {
		try {
			process = runtime.exec(CONSOLE_ENV + command);
			
			StreamGobbler errorGobbler = new StreamGobbler(process
					.getErrorStream(), "ERROR");

			StreamGobbler outputGobbler = new StreamGobbler(process
					.getInputStream(), "OUTPUT");

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