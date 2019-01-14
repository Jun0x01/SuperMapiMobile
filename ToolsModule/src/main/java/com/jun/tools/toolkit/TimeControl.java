package com.jun.tools.toolkit;

import android.os.SystemClock;

public class TimeControl {

	
	public static void sleep(int s){
		SystemClock.sleep(1000*s);
	}
	
	public static void sleepMS(long ms){
		SystemClock.sleep(ms);
	}
}
