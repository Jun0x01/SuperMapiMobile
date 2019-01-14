package com.jun.tools.AppManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Debug;
import android.os.PowerManager;

import java.util.List;

/**
 * Created by Jun on 2017/6/19.
 */

public class AppInfoManager {

    /**
     * Application Context
     */
    public static Context mAppContext;

    static PackageManager mPackageMaanger;

    static ActivityManager mActivityManager;
    static PowerManager mPowerManager;
    static AudioManager mAudioManager;

    /**
     * Initialize this class when application is created.
     * @param context
     */
    public AppInfoManager(Context context){
        mAppContext = context;
        mPackageMaanger = mAppContext.getPackageManager();
        mActivityManager = (ActivityManager) mAppContext.getSystemService(Context.ACTIVITY_SERVICE);
        mPowerManager = (PowerManager) mAppContext.getSystemService(Context.POWER_SERVICE);
        mAudioManager = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }

    /**
     * Release the context
     */
    public static void release(){
        mAppContext = null;
        mPackageMaanger = null;
        mPackageMaanger = null;
        mAudioManager = null;
    }

    private static void checkStatus(){
        if(mAppContext == null){
            throw new IllegalArgumentException("The Context is null, please call the constructor first.");
        }
    }

    /************************* Apps' Package ****************************/


    public static PackageManager getPackageManager(){
        return mPackageMaanger;
    }
    public static List<PackageInfo> getInstalledPachage(){
        checkStatus();
        return mPackageMaanger.getInstalledPackages(0);
    }

    /************************** Running Progress *****************************/

    public static ActivityManager getActivityManager(){
        return mActivityManager;
    }
    public static List<ActivityManager.RunningAppProcessInfo> getRunningApp(){
        checkStatus();
        return mActivityManager.getRunningAppProcesses();
    }

    public static List<ActivityManager.RunningServiceInfo> getRunningService(int maxNum){
        checkStatus();
        return mActivityManager.getRunningServices(maxNum);
    }


    /************************* MemoryInfo *******************************/

    public static ActivityManager.MemoryInfo getMemoryInfo(){
        checkStatus();
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }
    public static Debug.MemoryInfo[] getMemoryInfo(int[] pids){
        checkStatus();
        return mActivityManager.getProcessMemoryInfo(pids);
    }

}
