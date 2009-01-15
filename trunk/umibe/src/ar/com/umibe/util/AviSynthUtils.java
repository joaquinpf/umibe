package ar.com.umibe.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ar.com.umibe.core.XMLConfigLoader;

public class AviSynthUtils {
	public static String generateVideoScript(String input, String tempDir,
			String profile) {
		if(UmibeFileUtils.isSpecificMediaFile(input, "avs")){
			return input;
		}
		try {
			input = UmibeFileUtils.getFullPath(input);
			tempDir += UmibeFileUtils.getFileName(input);

			BufferedWriter out = new BufferedWriter(new FileWriter(tempDir
					+ ".avs"));
			out.write("DirectShowSource(\"" + input + "\",audio=false)");
			out.newLine();

			XMLConfigLoader p = new XMLConfigLoader(profile);
			ArrayList<String> options = p.getOptions("VideoOption");

			for (int i = 0; i < options.size(); i++) {
				out.write(options.get(i));
				out.newLine();
			}
			out.close();
			return tempDir + ".avs";

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String generateAudioScript(String input, String tempDir,
			String profile) {
		if(UmibeFileUtils.isSpecificMediaFile(input, "avs")){
			return input;
		}
		try {
			input = UmibeFileUtils.getFullPath(input);
			tempDir += UmibeFileUtils.getFileName(input);

			BufferedWriter out = new BufferedWriter(new FileWriter(tempDir
					+ "audio.avs"));
			out.write("DirectShowSource(\"" + input + "\", audio=true)");
			out.newLine();

			XMLConfigLoader p = new XMLConfigLoader(profile);
			ArrayList<String> options = p.getOptions("AudioOption");

			for (int i = 0; i < options.size(); i++) {
				out.write(options.get(i));
				out.newLine();
			}

			out.close();
			return tempDir + "audio.avs";

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}