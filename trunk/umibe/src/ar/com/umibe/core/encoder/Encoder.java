package ar.com.umibe.core.encoder;

import java.util.ArrayList;

import ar.com.umibe.core.DataModel;
import ar.com.umibe.core.MediaTrack;
import ar.com.umibe.core.execution.IExecutionEnvironment;
import ar.com.umibe.core.execution.WindowsCLIEnvironment;
import ar.com.umibe.core.matroska.TracksInfoParser;
import ar.com.umibe.core.tool.Tool;
import ar.com.umibe.core.tool.ToolMode;
import ar.com.umibe.core.xml.XMLConfigLoader;
import ar.com.umibe.util.UmibeFileUtils;

public abstract class Encoder {

	protected Tool executable;
	protected ToolMode mode = null;
	protected String BePipe;
	protected XMLConfigLoader pl;
	protected boolean verbosity = false;
	protected IExecutionEnvironment clienv = null;
	protected String tempDir = null;
	protected String avsProfile = null;
	
	public Encoder(String config, String avsProfile, String tempdir,
			boolean verbosity) {
		this.pl = new XMLConfigLoader(config);
		
		this.executable = DataModel.INSTANCE.getTool(this.pl.getNodeText("EncoderName"));
		this.mode = executable.getMode(this.pl.getNodeText("Mode"));
		
		this.BePipe = UmibeFileUtils.getFullPath(DataModel.INSTANCE
				.getTool("BePipe.exe").getPath())	+ " ";
		this.verbosity = verbosity;
		this.clienv = new WindowsCLIEnvironment();
		this.tempDir = tempdir;
		this.avsProfile = avsProfile;
	}

	private String replace(String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();

		// return str.replaceAll(pattern, replace);
	}

	public abstract ArrayList<MediaTrack> encode(String file, TracksInfoParser tip);
	
	protected int encodeTrack(String input, String output) {

		int result = 0;
		for (int i = 0; i < mode.steps.size(); i++) {

			String command = mode.steps.get(i);

			ArrayList<String> a = this.pl.getOptions("PassOption" + (i + 1));
			String options = mergeOptions(a);

			command = replace(command, "REPLACEOPTIONS", options);
			command = replace(command, "REPLACESTATS", "\""
					+ UmibeFileUtils.getFullPath(output) + ".stats" + "\"");
			command = replace(command, "REPLACEINPUT", "\""
					+ UmibeFileUtils.getFullPath(input) + "\"");
			command = replace(command, "REPLACEOUTPUT", "\""
					+ UmibeFileUtils.getFullPath(output) + "\"");
			
			String pipe = UmibeFileUtils.addComillas(this.BePipe) + " --script \"import(^"
			+ UmibeFileUtils.getFullPath(input) + "^)\" | ";

			command = replace(command, "REPLACEBEPIPE", pipe);	

			String exectubale = UmibeFileUtils
				.addComillas(UmibeFileUtils.getFullPath(this.executable.getPath()));

			command = replace(command, "REPLACEEXECUTABLE", exectubale);	
			
			result += clienv.execute(command, verbosity, true);/*UmibeFileUtils
					.addComillas(UmibeFileUtils.getFullPath(this.executable.getPath()))
					+ command, verbosity, true);*/
		}
		return result;
	}

	private String mergeOptions(ArrayList<String> a) {
		String ret = new String();
		for (int i = 0; i < a.size(); i++) {
			ret += " " + a.get(i);
		}
		return ret;
	}

	public void stop() {
		clienv.killProcess();
	}

	/*protected int pipedEncodeTrack(String input, String output) {

		String pipe = UmibeFileUtils.addComillas(this.BePipe) + " --script \"import(^"
				+ UmibeFileUtils.getFullPath(input) + "^)\" | ";
		int result = 0;
		for (int i = 1; i <= Integer.parseInt(this.pl
				.getNodeText("EncoderPassNumber")); i++) {

			ArrayList<String> a = this.pl.getOptions("PassOption" + i);

			String options = mergeOptions(a);

			options = replace(options, "REPLACEINPUT", "-");
			options = replace(options, "REPLACEOUTPUT", "\""
					+ UmibeFileUtils.getFullPath(output) + "\"");

			result += clienv.execute(pipe
					+ UmibeFileUtils.addComillas(this.executable) + options, verbosity, true);
		}
		return result;
	}*/
}
