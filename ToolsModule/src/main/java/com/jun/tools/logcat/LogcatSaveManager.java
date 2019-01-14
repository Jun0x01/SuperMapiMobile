package com.jun.tools.logcat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 保存日志
 * 参数为空或给定的目录不存在, 都会使用默认目录"../TestLog/"
 */
public class LogcatSaveManager {

	private boolean mIsStop;
	private String mLogPath = null;
	private String mRootPath = null;
	private String lineEnds = "\r\n"; 
	private String mFileType = ".log";
	private String mPackageName = null;
	
	private LogcatHelper mLogcatHelper = null;
	/**
	 * 保存日志
	 * @param logPath 日志保存目录, 参数为空或给定的目录不存在, 都会使用默认目录"../TestLog/"
	 */
	public LogcatSaveManager(String logPath) {
		setLogPath(logPath);
	}

	/**
	 * 保存日志的目录
	 * @param logPath 日志保存目录, 参数为空或给定的目录不存在, 都会使用默认目录"../TestLog/"
	 */
	public void setLogPath(String logPath) {
		mRootPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		File file = null;
		if(logPath == null){
			mLogPath = mRootPath + "/TestLog/";
		}else {
			file = new File(logPath);
			if (!file.exists()) {
				mLogPath = mRootPath + "/TestLog/";
			}
		}
		
		
		file = new File(mLogPath);
		file.mkdirs();
	}

	
	/**
	 * 通过logcat 命令获取日志,不同设备对获取日志的限制不同，可能有的设备获取不到，如华为M2 PAD
	 * @return
	 */
	public String doInBackground() {
		
		mIsStop = false;
		
		String strDate = DateHelper.getDate();
		String strTime = DateHelper.getTime();
		boolean isTimeCorrect = false;
		
		File file = new File(mLogPath + strDate + mFileType );
		String line = null;
		byte[] lineBytes = null;
		
		FileOutputStream logOutput = null;
		int lineCount = 0;
		int count = 10;
		mLogcatHelper = new LogcatHelper();
		int counter = 0;
		BufferedReader mLogcatReader = mLogcatHelper.getLogcatReader();
		if(mLogcatHelper== null)
			return null;
		
		try {
			logOutput = new FileOutputStream(file, true);
			while ((line = mLogcatReader.readLine()) != null && !mIsStop) {
				
				// 只保存开始记录日志后输出的日志, 如果是需要之前产生的日志缓存, 则去掉这个时间建成
				if (!isTimeCorrect) {
					isTimeCorrect = checkTime(strTime, line);
					if (!isTimeCorrect) {
						continue;
					}
				}
				
				lineBytes =  line.getBytes();
				logOutput.write(lineBytes);
				logOutput.write(lineEnds.getBytes());
				
				// 每count行保存一次
				if (lineCount<count) {
					lineCount++;
				}else {
					lineCount = 0;
					logOutput.close();
					logOutput = new FileOutputStream(file, true);
				}
				counter ++;
			}
			logOutput.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Saved log line count : " + counter);
		mLogcatHelper.closeReader();
		mLogcatHelper = null;
		return null;
	}

	private boolean checkTime(String time,String line) {
		boolean result = false;
		
		int index1=0, index2=0;
		String key1=" ", key2=".";
		index1= line.indexOf(key1);
		index2 = line.indexOf(key2, index1);
		if(index1 <0 || index2<0 || index2 <= index1)
			return false;
		
		String lineTime = line.substring(0, index2);
		if(time.length() != lineTime.length() || !lineTime.contains(":"))
			return false;
		
		int re = time.compareTo(lineTime);
		if(re<= 0){
			result = true;
		}
		
		return result;
	}

	public void stop(){
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mIsStop = true;
	}
	
	public boolean isStop(){
		return mIsStop;
	}
	
	@Override
	protected void finalize() throws Throwable {
		mIsStop = true;
		Thread.sleep(30);
		super.finalize();
	}
}
