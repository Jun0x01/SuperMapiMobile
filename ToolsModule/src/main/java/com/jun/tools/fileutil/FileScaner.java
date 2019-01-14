package com.jun.tools.fileutil;

import java.io.File;
import java.util.ArrayList;

/**
 * 扫描目录
 * @author XingJun
 * 2016.08.29
 *
 */
public class FileScaner {
	
	/**
	 * 
	 * @param dir
	 * @param suffix
	 * @return
	 */
	public static ArrayList<String>  getFiles( String dir, String suffix){
		
		if(dir == null)
			return null;
		
		File dirFile = new File(dir);
		File[] files = dirFile.listFiles();

		if(files == null)
			return null;
		
		ArrayList<String> filePathList = new ArrayList<String>();

		getFiles(filePathList, dir, suffix);
		
		if(filePathList.size() == 0)
			filePathList = null;
		return filePathList;
	}
	
	protected static void getFiles(ArrayList<String> filePathList,  String dir, String suffix){
		if(filePathList == null || dir == null)
			return;
		
		File dirFile = new File(dir);
		File[] files = dirFile.listFiles();

		if(files == null)
			return;
		
		String fileSuffix = null;
		int index = -1;
		String filePath = null;
		File file = null;
		for (int i = 0; i < files.length; i++) {
			file = files[i];
			
			if (file.isDirectory()) {
				getFiles(filePathList, file.getAbsolutePath(), suffix);
			}else {
				filePath = file.getAbsolutePath();
				if (suffix != null && !suffix.isEmpty()) {
					index = filePath.lastIndexOf('.');
					if (index == -1)
						continue;

					fileSuffix = filePath.substring(index + 1);
					if (fileSuffix.compareToIgnoreCase(suffix) == 0) {
						filePathList.add(filePath);
					}
				} else {
					filePathList.add(filePath);
				}
			}
			
		}

		return ;
	}

	/**
	 * 
	 * @param dir
	 * @param suffix
	 * @return
	 */
	public static ArrayList<String>  getFiles( String dir, String[] suffixs){
		
		if(dir == null)
			return null;
		
		File dirFile = new File(dir);
		File[] files = dirFile.listFiles();

		if(files == null)
			return null;
		
		ArrayList<String> filePathList = new ArrayList<String>();

		getFiles(filePathList, dir, suffixs);
		
		if(filePathList.size() == 0)
			filePathList = null;
		return filePathList;
	}
	
	protected static void getFiles(ArrayList<String> filePathList,  String dir, String[] suffixs){
		if(filePathList == null || dir == null)
			return;
		
		File dirFile = new File(dir);
		File[] files = dirFile.listFiles();

		if(files == null)
			return;
		
		String fileSuffix = null;
		int index = -1;
		String filePath = null;
		File file = null;
		for (int i = 0; i < files.length; i++) {
			file = files[i];
			
			if (file.isDirectory()) {
				getFiles(filePathList, file.getAbsolutePath(), suffixs);
			}else {
				filePath = file.getAbsolutePath();
				
				if (suffixs != null && suffixs.length > 0) {
					index = filePath.lastIndexOf('.');
					if (index == -1)
						continue;
					
//					for (String suffix : suffixs) {
					String suffix = null;
					for (int j=0; j<suffixs.length; j++) {
						suffix = suffixs[j];
						fileSuffix = filePath.substring(index + 1);
						if (fileSuffix.compareToIgnoreCase(suffix) == 0) {
							filePathList.add(filePath);
						}
					}
				} else {
					filePathList.add(filePath);
				}
			}
			
		}

		return ;
	}

	public static void deleteFiles(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files == null || files.length == 0) {
				file.delete();
				return;
			}
			for (File file2 : files) {
				deleteFiles(file2);
			}
			file.delete();
		}

	}

}
