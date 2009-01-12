package ar.com.umibe.core.execution;

public interface IExecutionEnvironment {
	public int execute(String command, boolean verbosity, boolean uiVerbosity);
	public void killProcess();
}
