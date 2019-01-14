package com.jun.tools.logcat;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LogcatService extends Service {

	/******************** Logcat Service Binder *********************/

	public class LogcatBinder extends Binder {

		public LogcatBinder() {
			super();
		}
		
		public LogcatService getService() {
			return LogcatService.this;
		}

	}

	/******************** End of Logcat Service Binder *********************/
	
	/**
	 * 
	 */
	private LogcatBinder mBinder;
	
	public LogcatService() {
		super();
	}

	/**
	 * ServiceConnection.onServiceConnected will not be called when return is null
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	
	@Override
	public void onCreate() {
		
		mBinder = null;
		mBinder = new LogcatBinder();
		
		super.onCreate();
		initNotify();
		startTask();
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
//		stopTask();
//		mLogSaveTask = null;
		stopTask();
		super.onDestroy();
	}

	/******************** Fore ground *************************/
	
	
	private void initNotify(){
		Notification notification = new Notification();
		notification.tickerText = "Logcat Running";
		notification.when = System.currentTimeMillis();
		startForeground(1, notification);
	}
	
	/********************************************/
	
	private LogcatSaveManager mLogcatSaveManager = null;
	private Thread mLocatThread = null;
	
	private void startTask(){
		
		if (mLogcatSaveManager == null)
			mLogcatSaveManager = new LogcatSaveManager(null);
		
		
		if(mLocatThread == null){
			mLocatThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					mLogcatSaveManager.doInBackground();
				}
			});
			
		}
		
		mLocatThread.start();
		
	}
	
	private void stopTask(){
		mLogcatSaveManager.stop();
		
	}
	
	
}
