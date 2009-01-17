package ar.com.umibe.core.tool;

import java.util.ArrayList;
import java.util.Hashtable;


public class Tool {
	private Hashtable<String,ToolOption> options;
	private ArrayList<ToolMode> modes;
	private String name;
	private String path;
	
	public Tool(String toolxml){
		ToolXMLParser txml = new ToolXMLParser(toolxml);
		options = txml.getToolOptions();
		modes = txml.getToolModes();
		path = txml.getNodeText("Path");
		name = txml.getNodeText("Name");
	}

	public String toString(){
		return name;		
	}
	
    public static void main(String[] args)
    {
    	Tool t = new Tool("x264.xml");
    }
	
	public Hashtable<String, ToolOption> getOptions() {
		return options;
	}

	public void setOptions(Hashtable<String, ToolOption> options) {
		this.options = options;
	}

	public ArrayList<ToolMode> getModes() {
		return modes;
	}

	public void setModes(ArrayList<ToolMode> modes) {
		this.modes = modes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
