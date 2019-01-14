package com.jun.tools.toolkit;

import android.os.Looper;

public class CheckThread {

	
	public static boolean isMainThread(){
		return Thread.currentThread() == Looper.getMainLooper().getThread();
	}
}
