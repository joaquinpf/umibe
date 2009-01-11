package ar.com.KireNcoder.util;

import java.io.IOException;

import org.grlea.log.SimpleLogger;
import org.jvnet.winp.WinProcess;

public class CLIUtils {

	private final String CONSOLE_ENV = "cmd /c start \"KireNcoder\" /LOW /B /WAIT ";
	private final Runtime runtime = Runtime.getRuntime();
	private final SimpleLogger log = new SimpleLogger(CLIUtils.class);
	private Process process = null;
	

	public int executeCommand(String command, boolean verbosity, boolean uiVerbosity) {
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

	public void killProcess(String proc) {
		if(process != null){
			WinProcess wp = new WinProcess(process);	
			wp.killRecursively();
		}
		/*String tool = FileUtils.addComillas(FileUtils
				.getFullPath("./resources/pskill.exe"));
		executeCommand(tool	+ FileUtils.addComillas(FileUtils
								.getFileName(process)), false, false);*/
	}
	
}