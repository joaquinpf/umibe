package ar.com.KireNcoder.core;

import java.util.ArrayList;

import ar.com.KireNcoder.util.CLIUtils;
import ar.com.KireNcoder.util.FileUtils;

public class Encoder {

	private String executable;
	private String BePipe;
	private XMLConfigLoader pl;
	private boolean verbosity = false;
	private CLIUtils clienv = null;
	
	public Encoder(String config, boolean verbosity) {
		this.pl = new XMLConfigLoader(config);
		this.executable = this.pl.getNodeText("EncoderRoute");
		this.executable = FileUtils.getFullPath(this.executable);
		this.BePipe = FileUtils.getFullPath("./resources/BePipe.exe") + " ";
		this.verbosity = verbosity;
		this.clienv = new CLIUtils();
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

	public int encode(String input, String output) {

		int result = 0;
		for (int i = 1; i <= Integer.parseInt(this.pl
				.getNodeText("EncoderPassNumber")); i++) {

			ArrayList<String> a = this.pl.getOptions("PassOption" + i);

			String options = mergeOptions(a);

			options = replace(options, "REPLACESTATS", "\""
					+ FileUtils.getFullPath(output) + ".stats" + "\"");
			options = replace(options, "REPLACEINPUT", "\""
					+ FileUtils.getFullPath(input) + "\"");
			options = replace(options, "REPLACEOUTPUT", "\""
					+ FileUtils.getFullPath(output) + "\"");

			result += clienv.executeCommand(FileUtils
					.addComillas(this.executable)
					+ options, verbosity, true);
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
		clienv.killProcess(executable);
	}

	public int pipedEncode(String input, String output) {

		String pipe = FileUtils.addComillas(this.BePipe) + " --script \"import(^"
				+ FileUtils.getFullPath(input) + "^)\" | ";
		int result = 0;
		for (int i = 1; i <= Integer.parseInt(this.pl
				.getNodeText("EncoderPassNumber")); i++) {

			ArrayList<String> a = this.pl.getOptions("PassOption" + i);

			String options = mergeOptions(a);

			options = replace(options, "REPLACEINPUT", "-");
			options = replace(options, "REPLACEOUTPUT", "\""
					+ FileUtils.getFullPath(output) + "\"");

			result += clienv.executeCommand(pipe
					+ FileUtils.addComillas(this.executable) + options, verbosity, true);
		}
		return result;
	}
}
