package com.jun.tools.filebrower;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 扫描目录
 * @author XingJun
 * 2016.08.29
 *
 */
public class FileScaner {

	public static ArrayList<FileInfo> listFiles(String dir, ArrayList<FileInfo> list ){

		if(dir == null){
			return  list;
		}
		File dirFile = new File(dir);
		if(!dirFile.exists()){
			return  list;
		}

		ArrayList<FileInfo> fileInfos = null;

		if(list == null) {
			fileInfos = new ArrayList<>();
		}else {
			fileInfos = list;
		}


//		File[] files = dirFile.listFiles();
//
//		if(files == null)
//			return fileInfos;
//
//		for (File file : files) {
//			FileInfo fileInfo = createFileInfo(file);
//			fileInfos.add(fileInfo);
//		}

		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.isHidden()){
					return false;
				}

				return true;
			}
		};
		File[] files = dirFile.listFiles(fileFilter);
		String[] arrFile = dirFile.list();
		ArrayList<String> listDir = new ArrayList<>();
		ArrayList<String> listFile = new ArrayList<>();
		if(arrFile == null)
			return fileInfos;
		String name = null;
		for (File file : files){
			name = file.getName();
			if(file.isDirectory()) {
				listDir.add(name);
			}if(file.isFile()){
				listFile.add(name);
			}
		}

		Collections.sort(listDir);
		Collections.sort(listFile);
		for (String fileName : listDir){
			FileInfo fileInfo = createFileInfo(new File(dir + "/" + fileName));
			fileInfos.add(fileInfo);
		}
		for (String fileName : listFile){
			FileInfo fileInfo = createFileInfo(new File(dir + "/" + fileName));
			fileInfos.add(fileInfo);
		}

		return  fileInfos;
	}

	private static FileInfo createFileInfo(File file){
		FileInfo fileInfo = new FileInfo();
		String path = file.getAbsolutePath();
		fileInfo.mPath = path;
		fileInfo.mName = file.getName();
		if(file.isDirectory()) {
			fileInfo.mIsDir = true;
		}else {
			int index = path.lastIndexOf('.');
			if(index > 0){
				fileInfo.mType = path.substring(index);
			}
		}

		return fileInfo;
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
