package com.jun.tools.Message;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class ShowMessage {

	private static Context mContext;
	private static Activity mActivity;
	public ShowMessage () {
		
	}

	/**
	 * Set the application context for a message made by Toast
	 * @param context  application context
	 */
	public static void setContext(Context context){
		mContext = context;
	}

	public static void setActivity(Activity activity){
		mActivity = activity;
	}
	
	/**
	 * Show normal message
	 * @param tag
	 * @param info
	 */
	public static void showInfo(final String tag, final String info)
	{
		if(info == null)
			return;
		if (isMainThread()) {
			if(mContext != null) {
				Toast toast = Toast.makeText(mContext, info, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			} else if(mActivity != null){
				Toast toast = Toast.makeText(mActivity, info, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			}
			Log.d(tag, info);
		} else if(mActivity != null) {
			showInfo(tag, info, mActivity);
		}

	}
	
	/**
	 * Show error message
	 * @param tag
	 * @param err
	 */
	public static void showError (final String tag, String err)
	{
		if(err == null)
			return;
		if (isMainThread()){

			if(mContext != null) {
				Toast toast = Toast.makeText(mContext, "Error: " + err, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			} else if(mActivity != null){
				Toast toast = Toast.makeText(mActivity, "Error: " + err, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			}
			Log.e(tag, err);
		} else if(mActivity != null){
			showError(tag, err, mActivity);
		}

	}
	
	
	/**
	 * Show normal message
	 * @param tag
	 * @param info
	 * @param activity
	 */
	public static void showInfo(final String tag, final String info, final Activity activity)
	{
		if(activity != null && !isMainThread()){
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast toast = Toast.makeText(activity, info, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
					Log.d(tag, info);
				}
			});
		}else if(activity != null && isMainThread()) {
			Toast toast = Toast.makeText(activity, info, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			Log.d(tag, info);
		}else if(activity == null){
			showInfo(tag, info);
		}
	}
	
	/**
	 * Show error message
	 * @param tag
	 * @param info
	 * @param activity
	 */
	public static void showError (final String tag, final String info, final Activity activity) 
	{
		if(activity != null && !isMainThread()){
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast toast = Toast.makeText(activity, "Error: " + info, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
					Log.e(tag, info);
				}
			});
		}else if(activity != null && isMainThread()){
			Toast toast = Toast.makeText(activity, "Error: " + info, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			Log.e(tag, info);
		}else if( activity == null){
			showError(tag, info);
		}
		
	}
	
	public static boolean isMainThread(){
		return Thread.currentThread() == Looper.getMainLooper().getThread();
	}

}
