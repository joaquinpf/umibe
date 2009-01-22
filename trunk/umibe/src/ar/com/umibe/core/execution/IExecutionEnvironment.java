package ar.com.umibe.core.execution;

/**
 * @author Joaqu�n Alejandro P�rez Fuentes
 */
public interface IExecutionEnvironment {
	public int execute(String command, boolean verbosity, boolean uiVerbosity);
	public int execute(String command, String workingDir, boolean verbosity, boolean uiVerbosity);
	public void killProcess();
}
