package ar.com.umibe.commons.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class UmibeFileUtils {

	public static boolean exists(String input){
		File f = new File(input);
		if(f.exists() == true){
			return true;
		} else {
			return false;
		}
	}
	
	public static void copy(File source, File dest) {
		try {
			FileUtils.copyFile(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("UmibeFileUtils: Could not copy File");
		}
	}

	public static String getFileNameFromURL(String input) {
		int slashIndex = input.lastIndexOf('/');
		return input.substring(slashIndex + 1);
	}
	
	public static String getFileName(String input) {
		File f = new File(input);
		return f.getName();
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
    
	public static File[] getSubDirectories(String dir){
		File directory = new File(dir);

	    FileFilter filter = new FileFilter() {
	        public boolean accept(File file) {
	            return file.isDirectory();
	        }
	    };
		return directory.listFiles(filter);
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

	public static void delete(String filename){
		if(filename!=null){
			File f = new File(filename);
			if(f.exists()){
				f.delete();
			}
		}
	}
	
	public static boolean isSpecificMediaFile(String filename, String extension) {
		if (filename.endsWith(extension) && (isMediaFile(filename))) {
			return true;
		} else {
			return false;
		}
	}

	public static void cleanUpDirectory(File directory) {
		try {
			FileUtils.cleanDirectory(directory);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("UmibeFileUtils: Could not clean directory");
		}
	}
	
	public static long getFreeSpace(String directory){
		try {
			return FileSystemUtils.freeSpaceKb(directory);
		} catch (IOException e) {
			return -1;
		}
	}
}