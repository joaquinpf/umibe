package ar.com.umibe.util;

import java.io.File;
import java.io.IOException;

import ar.com.umibe.core.DataModel;
import ar.com.umibe.core.execution.IExecutionEnvironment;
import ar.com.umibe.core.execution.WindowsCLIEnvironment;

public class VideoUtils {
	
	private static String mplayer = "./resorces/mencoder/mplayer.exe";
	private static String mediainfo = "./resorces/MediaInfo/MediaInfo.exe";
	
	public static String takeScreenshot(String input){	
		//mplayer input-file -ss <position in seconds> -frames 1 -vo jpeg
		String tool = UmibeFileUtils.addComillas(UmibeFileUtils
				.getFullPath(mplayer));
		
		input = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(input));
		String tempDir = DataModel.INSTANCE.getTempDir();
		
		IExecutionEnvironment clienv = new WindowsCLIEnvironment();
		clienv.execute(tool + input + "-ss 1 -frames 1 -nosound -vo png", 
				tempDir, true, false);

		File f = new File(tempDir + "00000001.png");
		if(f.exists()){
			return tempDir + "00000001.png";
		} else
			return null;
	}	
	
	public static String getMediaInfo(String input){
		try {
			String tool = UmibeFileUtils.addComillas(UmibeFileUtils
					.getFullPath(mediainfo));

			String inputFilename = UmibeFileUtils.getFileName(input);
			input = UmibeFileUtils.addComillas(UmibeFileUtils.getFullPath(input));
			File f = new File(DataModel.INSTANCE.getTempDir());
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

	public static void setMplayer(String mplayer) {
		VideoUtils.mplayer = mplayer;
	}

	public static void setMediainfo(String mediainfo) {
		VideoUtils.mediainfo = mediainfo;
	}		
}
