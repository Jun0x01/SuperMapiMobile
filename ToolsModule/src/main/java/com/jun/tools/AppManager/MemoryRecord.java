package com.jun.tools.AppManager;

import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.jun.tools.logcat.LogSaveManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jun on 2017/6/21.
 */

public class MemoryRecord {

    LogSaveManager mLogSaveManager;
    ArrayList<MemoryListener> mMemoryListeners;
    boolean mIsTimerRunning;
    long mPeriod = 3; // Unit is second.

    private boolean mEnableSaveMemoryInfo;

    private boolean mEnableLogMemoryInfo;

    public MemoryRecord(Context context){
        mLogSaveManager = new LogSaveManager(null);
        String logPath = null;
        if(context != null) {
            logPath = mLogSaveManager.getLogDir() + "/MemoryLog/" + context.getPackageName();
        }else {
            logPath = mLogSaveManager.getLogDir() + "/MemoryLog/";
        }
        mLogSaveManager.setLogPath(logPath);

        mMemoryListeners = new ArrayList<>();
    }


    Timer mTimerMem;
    TimerTask mTimerTaskMem;

    public void startTimer(){
        cancelTimer();

        mTimerMem = new Timer();
        mTimerTaskMem = new TimerTask() {
            @Override
            public void run() {
                String mem1 = MemoryMonitor.getSystemMemory();
                String mem2 = MemoryMonitor.getMemoryInfo(Process.myPid());


                String[] mem1s = mem1.split(" +");
                long totalMem = Long.parseLong(mem1s[1]);
                long availMem = Long.parseLong(mem1s[3]);
                long usedMem = totalMem - availMem;

                String[] mem2s = mem2.split(" +");
                long appMem = Long.parseLong(mem2s[1]);

                double ratio = Math.round(usedMem*10000.0/totalMem)/100.00;
                double ratioApp = Math.round(appMem*1024*10000.0/totalMem)/100.00;
                String memory = /*appMem + "KB\n" + ratioApp + "%/" +*/ ratio + "%";

                int state = 0;
                if(ratio >= 70 && ratio < 90){
                    state = 1;
                }else if(ratio >= 90){
                    state = 2;
                }
                for (MemoryListener listener : mMemoryListeners){
                    listener.onGetMemory(state, memory);
                }

                if (mEnableLogMemoryInfo)
                    Log.d("MemoryMonitor", mem1 + " " + mem2 + "; " + memory);

                if (mEnableSaveMemoryInfo)
                    mLogSaveManager.saveLog("MemoryMonitor: " + mem1 + " " + mem2 + "; " + memory);

            }
        };

        mTimerMem.schedule(mTimerTaskMem, 100, mPeriod * 1000);
        mIsTimerRunning = true;
    }

    public void cancelTimer(){
        if(mTimerMem != null){
            mTimerMem.cancel();
            mTimerTaskMem.cancel();
            mTimerTaskMem = null;
            mTimerMem = null;

            mLogSaveManager.closeFile();
        }
        mIsTimerRunning = false;
    }

    public void createNewFile(){
        mLogSaveManager.createNewFile();
    }

    public boolean isStarted(){
        return mIsTimerRunning;
    }

    public void addMemoryListener(MemoryListener listener){
        if(listener != null && !mMemoryListeners.contains(listener))
            mMemoryListeners.add(listener);
    }

    public void removeMemoryListener(MemoryListener listener){
        if(listener != null)
            mMemoryListeners.remove(listener);
    }

    public boolean isEnableSaveMemoryInfo(){
        return mEnableSaveMemoryInfo;
    }

    public void enableSaveMemoryInfo(boolean enable){
        mEnableSaveMemoryInfo = enable;
    }

    public void enableLogMemoryInfo(boolean enable){
        mEnableLogMemoryInfo = enable;
    }

    public boolean isEnableLogMemoryInfo(){
        return mEnableLogMemoryInfo;
    }
}
