package com.jun.tools.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jun.tools.FloatingWindow.FloatingWindowBuilder;
import com.jun.tools.R;
import com.jun.tools.logcat.LogSaveManager;

/**
 * Created by Jun on 2017/6/15.
 */

public class FloatingWindowService extends Service {

    FloatingWindowBuilder mFloatingWindowBuilder;
    LogSaveManager mLogSaveManager;
    private Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        mFloatingWindowBuilder = new FloatingWindowBuilder(getApplicationContext());

        mFloatingWindowBuilder.createFloatingWindow(R.layout.layout_floating_window_memory);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mFloatingWindowBuilder.clear();
        super.onDestroy();
    }


}
