package ar.com.umibe.core.filter;
import java.util.HashMap;
import java.util.Iterator;


/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class Filter {
	private String name;
	private String window;
	private String command;
	private HashMap<String, String> parameters;
	private HashMap<String, String> parameterDescriptions;
	
	public Filter(){
		parameters = new HashMap<String, String>();
		parameterDescriptions = new HashMap<String, String>();
	}	
	public String toString(){
		return getCompletedCommand();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWindow() {
		return window;
	}
	public void setWindow(String window) {
		this.window = window;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public HashMap<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}
	public void setParameter(String key, String value){
		this.parameters.remove(key);
		this.parameters.put(key, value);
	}
	public void setParameter(String key, String value, String description){
		this.parameters.remove(key);
		this.parameters.put(key, value);
		this.parameterDescriptions.remove(key);
		this.parameterDescriptions.put(key, description);
	}
	public String getCompletedCommand() {
		Iterator<String> it = parameters.keySet().iterator();
		String completedCommand = new String(command);
		while(it.hasNext()){
			String key = it.next();
			completedCommand = completedCommand.replaceAll(key, parameters.get(key));
		}
		return completedCommand;
	}
	public void setParameterDescriptions(HashMap<String, String> parameterDescriptions) {
		this.parameterDescriptions = parameterDescriptions;
	}
	public HashMap<String, String> getParameterDescriptions() {
		return parameterDescriptions;
	}
}
