package ar.com.umibe.commons.execution;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public interface IExecutionEnvironment {
	public int execute(String command);
	public int execute(String command, String workingDir);
	public void killProcess();
}
