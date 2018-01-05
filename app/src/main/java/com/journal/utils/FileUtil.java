package com.journal.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

public class FileUtil {
	public static String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
	public final static String APP = "journal";
	public final static String TMP = "tmp";

	public static String getAppRoot() {
		File file = new File(ROOT, APP);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	public static String getTmpDir() {
		File file = new File(getAppRoot(), TMP);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	public static String createTmpFile() {
		return getTmpDir() + File.separator + System.currentTimeMillis() + ".tmp";
	}
	
	public static String createTmpFile(String name) {
		return getTmpDir() + File.separator + name;
	}
	
	public static String getDownloadDir(){
		String dir = getAppRoot() + File.separator + "download";
		return checkDir(dir);
	}
	
	public static String getImgDir(){
		String dir = getAppRoot() + File.separator + "img";
		return checkDir(dir);
	}

	public static String getAudioDir(){
		String dir = getAppRoot() + File.separator + "audio";
		return checkDir(dir);
	}

	public static boolean hasAudioByFileName(String fileName){
		File audioFile = new File(getAudioDir() + File.separator + fileName);
		return audioFile.exists();
	}


	private static String checkDir(String dir) {
		File directory = new File(dir);
		if (!directory.exists() || !directory.isDirectory()) {
			directory.mkdirs();
		}
		return dir;
	}
	
	public static String getDownloadPath(String name){
		return getDownloadDir() + File.separator + name;
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
	
	public static String getFilePathByUri(Activity activity, Uri uri) {
		String path = null;
		Cursor cursor = activity.managedQuery(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
		if (cursor.moveToFirst()) {
			path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		}
		cursor = null;
		return path;
	}

	/**
	 * 递归清除某个文件夹下文件
	 *
	 * @param file
	 * @return
	 */
	public static void destoryFileByDirName(final File dirName) {
		if (dirName.isFile()) {
			dirName.delete();
		} else {
			final File[] children = dirName.listFiles();
			if (children != null)
				for (final File child : children) {
					destoryFileByDirName(child);
				}
		}
	}

	
}
