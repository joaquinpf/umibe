package ar.com.umibe.core.tool;

import java.util.ArrayList;
import java.util.Hashtable;

import ar.com.umibe.core.xml.ToolXMLParser;


public class Tool {
	private Hashtable<String,ToolOption> options;
	private ArrayList<ToolMode> modes;
	private String name;
	private String path;
	private String type;
	
	public Tool(String toolxml){
		ToolXMLParser txml = new ToolXMLParser(toolxml);
		options = txml.getToolOptions();
		modes = txml.getToolModes();
		path = txml.getNodeText("Path");
		name = txml.getNodeText("Name");
		setType(txml.getNodeText("Type"));
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

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public ToolMode getMode(String nodeText) {
		for(int i=0; i<modes.size(); i++){
			if(modes.get(i).name.equals(nodeText)){
				return modes.get(i);
			}
		}
		return null;
	}
}
