package com.jun.tools.Activity;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;

import com.jun.tools.AppManager.AppInfoManager;
import com.jun.tools.View.JunBitmap;

/**
 * Created by Jun on 2017/6/20.
 */

public class ApplicationBase extends Application {

    public static DisplayMetrics mDisplayMetrics;

    private static String tag = "ApplicationBase";

    public static int mStatusBar_Height;

    @Override
    public void onCreate() {
        super.onCreate();
        // 1.
        new AppInfoManager(getApplicationContext());
        // 2.
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBar_Height = getResources().getDimensionPixelSize(resourceId);
        }
        // 3.
        mDisplayMetrics = getResources().getDisplayMetrics();

        int width = mDisplayMetrics.widthPixels;
        int height = mDisplayMetrics.heightPixels;
        long size = 0;
        size = JunBitmap.getBitMapSize(width, height);

        Log.d(tag, "Width: " + width + ", Height: " + height + ", Bitmap Size: " + size + " Bytes, " + size/1024.0 + " KB, " + size/(1024*1024.0) + " MB");

    }

    @Override
    public void onTerminate() {
        AppInfoManager.release();
        super.onTerminate();
    }
}
