package com.jun.tools.logcat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 保存日志
 * 参数为空或给定的目录不存在, 都会使用默认目录"../TestLog/"
 */
public class LogSaveManager {

	private String mLogPath = null;
	private String mRootPath = null;
	private String lineEnds = "\r\n"; 
	private String mFileType = ".log";
	private String mPackageName = null;
	private FileWriter writer;
	
	private String mLogFile = null;
	
	
//	private static LogSaveManager mInstance;
	/**
	 * 保存日志
	 * @param logPath 日志保存目录, 参数为空或给定的目录不存在, 都会使用默认目录"../TestLog/"
	 */
	 public LogSaveManager(String logPath) {
		
		setLogPath(logPath);
		
		mLogFile = mLogPath + DateHelper.getDate() + mFileType;
	}
	
//	public static LogSaveManager getInstance(){
//		if(mInstance == null){
//			mInstance = new LogSaveManager(null);
//		}
//		return mInstance;
//	}

	/**
	 * 保存日志的目录
	 * @param logPath 日志保存目录, 参数为空或给定的目录不存在, 都会使用默认目录"../TestLog/"
	 */
	public void setLogPath(String logPath) {
		mRootPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		File file = null;
		if(logPath == null || logPath.isEmpty()){
			mLogPath = mRootPath + "/DebugLog/";
		}else {
			
			mLogPath = logPath;
		}
		
		
		file = new File(mLogPath);
		file.mkdirs();
		if(!mLogPath.endsWith("/"))
			mLogPath += "/";
		
		mLogFile = mLogPath +DateHelper.getDate() + mFileType;
	}
	
	/**
	 * 设置日志文件名
	 * @param fileName
	 */
	public void setLogFile (String fileName){
		if (fileName != null && !fileName.isEmpty()){
			int index =  fileName.lastIndexOf('/');
			String subDir = null;
			if(index >0){
				subDir = fileName.substring(0, index+1);
			}
			if(subDir == null){
				mLogFile = mLogPath + fileName + mFileType;
			} else if (!mLogPath.contains(subDir)) {
				mLogFile = mLogPath + fileName + mFileType;
				mLogPath += subDir;
			}else  {
				mLogFile = mLogPath + fileName.substring(index + 1);
			}
			File parentFile = new File(mLogFile).getParentFile();
			parentFile.mkdirs();

			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				writer = null;
			}
		}
	}
	
	/**
	 * 保存日志
	 * @param log
	 */
	public void saveLog(String log){
		
		writer = null;
		try {
			if (writer == null) {
				File file = new File(mLogFile);
				writer = new FileWriter(file, true);
			}
			writer.append(DateHelper.getTime() + "  " + log + lineEnds);
			writer.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		finally {
//			closeFile();
//		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		closeFile();
		super.finalize();
	}

	public void closeFile() {
		if(writer != null){
			try {
				writer.close();
				writer = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getLogDir(){
		return mLogPath;
	}


	public void createNewFile(){
		closeFile();
		mLogFile = mLogPath +DateHelper.getDate() + mFileType;
	}
}
