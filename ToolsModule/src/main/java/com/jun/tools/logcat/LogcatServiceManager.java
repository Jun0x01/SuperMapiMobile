package com.jun.tools.logcat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class LogcatServiceManager{

	
	
	private String tag;
	private boolean mIsBind = false;
	private Context mContext = null;
	private Intent mIntent = null;
	private ServiceConnection mLogcatConnection = null;
	
	public LogcatServiceManager(Context context) {
		init(context);
	}
	
	private void init(Context context) {
		tag = getClass().getSimpleName();
		mContext = context;
		
		mIntent = new Intent(mContext, LogcatService.class);
	}
	
	/**
	 * StartService --> StopService
	 * BindService --> UnbindService or Context is invalid
	 * StartService and BindService --> UnBindService and StopService -> stop; StopService and UnBindService -> stop; StopServie and Context is valid -> stop
	 */
	
	/**
	 * 启动非绑定服务
	 */
	public ComponentName startService(){
		ComponentName name = mContext.startService(mIntent);
		Log.d(tag, "启动非绑定服务: " + name.toString());
		
		return name;
	}
	
	/**
	 * 停止非绑定服务
	 */
	public boolean stopService(){
		boolean isStop = mContext.stopService(mIntent);
		Log.d(tag, "停止非绑定服务: " + isStop);
		return isStop;
	}
	
	/**
	 * 启动绑定服务
	 */
	public boolean bindService(){
		if(mLogcatConnection == null){
			mLogcatConnection = new LogcatServiceConnnection();
		}
		boolean isBind = mContext.bindService(mIntent, mLogcatConnection, Context.BIND_AUTO_CREATE);
		Log.d(tag, "启动绑定服务: " + isBind);
		mIsBind = isBind;
		
		return isBind;
	}
	
	/**
	 * 停止绑定服务
	 */
	public void unbindService(){
		if (mIsBind) {
			mContext.unbindService(mLogcatConnection);
			mIsBind = false;
		}
	}
	
	/**
	 * 设置ServiceConnetion
	 * @param connection
	 */
	public void setConnection(ServiceConnection connection){
		mLogcatConnection = connection;
	}
	
	
	/********************** LogcatServiceConnnection **************************/
	// Receives information as the service is started and stopped
	class LogcatServiceConnnection  implements ServiceConnection {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
			
		}
	};

}
