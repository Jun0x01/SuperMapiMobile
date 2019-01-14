package com.jun.tools.logcat;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Parcel;

/**
 * Created by Administrator on 2017/3/21.
 */

public class MemoryMonitor {

    public static void getMemoryInfo(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);
        long availMem = memInfo.availMem;
        long totalMem = memInfo.totalMem; // api 16
        long threshold = memInfo.threshold;
        boolean isLowMemory = memInfo.lowMemory;

        Parcel parcel = Parcel.obtain();
        memInfo.writeToParcel(parcel, 0);

    }
}
