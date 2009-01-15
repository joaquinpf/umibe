package ar.com.umibe.util;

import java.io.File;
import java.io.IOException;

import ar.com.umibe.core.execution.IExecutionEnvironment;
import ar.com.umibe.core.execution.WindowsCLIEnvironment;

public class VideoUtils {
	public static String takeScreenshot(String input){		
		//mplayer input-file -ss <position in seconds> -frames 1 -vo jpeg
		String tool = UmibeFileUtils.addComillas(UmibeFileUtils
				.getFullPath("./resources/mplayer.exe"));
		
		input = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(input));

		IExecutionEnvironment clienv = new WindowsCLIEnvironment();
		clienv.execute(tool + input + "-ss 5 -frames 1 -nosound -vo png" , true, false);

		File f = new File("00000001.png");
		if(f.exists()){
			return "00000001.png";
		} else
			return null;
	}	
	
	public static String getMediaInfo(String input){
		try {
			String tool = UmibeFileUtils.addComillas(UmibeFileUtils
					.getFullPath("./resources/MediaInfo/MediaInfo.exe"));

			String inputFilename = UmibeFileUtils.getFileName(input);
			input = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(input));
			File f = new File("./temp");
			String output;

			output = " > " + UmibeFileUtils.addComillas(f.getCanonicalPath() + "/" + inputFilename + "_mediainfo.txt");

			IExecutionEnvironment clienv = new WindowsCLIEnvironment();
			clienv.execute(tool + input + output, true, false);

			return "temp/" + inputFilename + "_mediainfo.txt";} 
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}		
}
