package ar.com.umibe.core.tool;


/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class ToolOption {
	public String key;
	public String name;
	public String value;
	public String modes;
	public boolean mandatory;
	public boolean advanced;
	
	public String toString(){
		return name;		
	}
}
