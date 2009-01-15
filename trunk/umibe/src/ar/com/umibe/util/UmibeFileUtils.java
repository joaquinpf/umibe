package ar.com.umibe.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class UmibeFileUtils {

	public static void copy(File source, File dest) {
		try {
			FileUtils.copyFile(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("UmibeFileUtils: Could not copy File");
		}
	}

	public static String getFileName(String input) {
		File f = new File(input);
		return f.getName();
	}

	public static String addComillas(String s) {
		return "\"" + s + "\" ";
	}

	public static String getFullPath(String route) {
		try {
			File f = new File(route);
			return f.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			return " ";
		}
	}

	public static String[] filterFiles(String dir, final String filterContains){
		File directory = new File(dir);

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().contains(filterContains);
			}
		};
		return directory.list(filter);
	}

	public static String[] filterFiles(String dir, final String filterContains, final String filterNotEnds){
		File directory = new File(dir);

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().contains(filterContains) && !name.toLowerCase().endsWith(filterNotEnds);
			}
		};
		return directory.list(filter);
	}
	
	public static void move(String from, String to) {
		try {
			FileUtils.moveFileToDirectory(new File(from), new File(to), true);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("UmibeFileUtils: Could not move File");
		}
	}

	public static boolean isMediaFile(String filename) {
		filename = filename.toLowerCase();
		if (filename.endsWith("mkv") || filename.endsWith("mp4")
				|| filename.endsWith("ogm") || filename.endsWith("avi")
				|| filename.endsWith("rm") || filename.endsWith("rmvb")
				|| filename.endsWith("flv") || filename.endsWith("3gp")
				|| filename.endsWith("wmv") || filename.endsWith("mpeg")
				|| filename.endsWith("mpg") || filename.endsWith("avs")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSpecificMediaFile(String filename, String extension) {
		if (filename.endsWith(extension) && (isMediaFile(filename))) {
			return true;
		} else {
			return false;
		}
	}

	public static void cleanUpDirectory(String directory) {
		try {
			FileUtils.cleanDirectory(new File(directory));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("UmibeFileUtils: Could not clean directory");
		}
	}

}