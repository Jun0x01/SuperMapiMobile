package com.jun.tools.logcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LogcatHelper {

	private Process mLogcatProcess;
	private BufferedReader mLogcatReader;
	
	public LogcatHelper() {
		
	}
	
	public ArrayList<String> getLogcatArgs(String filter){
		ArrayList<String> args = new ArrayList<String>();
		args.add("logcat");
		args.add("-v");   // 将 -v 改为 -d, 将获取一次日志并退出
		args.add("time");
		if(filter != null)
			args.add(filter);
		
		return args;
	}

	public BufferedReader getLogcatReader(){
		closeReader();
		
		ArrayList<String> args = getLogcatArgs(null);
		mLogcatProcess = RuntimeHelper.execute(args);
		if(mLogcatProcess == null){
			return null;
		}
		int size = 1024 * 8;
		mLogcatReader = new BufferedReader(new InputStreamReader(mLogcatProcess.getInputStream()), size);
		if(mLogcatReader == null)
			mLogcatProcess.destroy();
		
		return mLogcatReader;
	}
	
	public void closeReader(){
		if(mLogcatProcess != null){
			try {
				mLogcatReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mLogcatProcess.destroy();
		}
		
		mLogcatProcess = null;
		mLogcatReader = null;
	}
	
}
