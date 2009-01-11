package ar.com.KireNcoder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {

	public static void copy(File source, File dest) {
		FileChannel in = null, out = null;
		try {
			in = new FileInputStream(source).getChannel();
			out = new FileOutputStream(dest).getChannel();

			long size = in.size();
			MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0,
					size);

			out.write(buf);
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
		File f = new File(from);
		f.renameTo(new File(to));
	}

	public static boolean isMediaFile(String filename) {
		filename = filename.toLowerCase();
		if (filename.endsWith("mkv") || filename.endsWith("mp4")
				|| filename.endsWith("ogm") || filename.endsWith("avi")
				|| filename.endsWith("rm") || filename.endsWith("rmvb")
				|| filename.endsWith("flv") || filename.endsWith("3gp")
				|| filename.endsWith("wmv") || filename.endsWith("mpeg")
				|| filename.endsWith("mpg")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSpecificMediaFile(String filename, String extension) {
		if (filename.endsWith(extension)) {
			return true;
		} else {
			return false;
		}
	}

	public static void cleanUpDirectory(String directory) {
		File dir = new File(directory);

		String[] children = dir.list();
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i = 0; i < children.length; i++) {
				File file = new File(directory,	children[i]);
				System.out.print(file + "  deleted : " + file.delete());
			}
		}
	}

}