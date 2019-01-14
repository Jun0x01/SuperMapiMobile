package com.jun.tools.AppManager;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Parcel;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.util.List;

/**
 * Created by Jun on 2017/6/19.
 */

public class MemoryMonitor {


    static String tag = "MemoryMonitor";
    /**
     * ActivityManager.MemoryInfo;ActivityManager.getSystemMemory()
     * @param context
     */
    public static String getSystemMemory(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);
        long availMem = 0;
        long totalMem = -1; //memInfo.totalMem; // api 16
        long threshold = 0;
        boolean lowMemory = false;
        long hiddenAppThreshold = 0;
        long visibleAppThreshold = 0;
        long secondaryServerThreshold = 0;
        long foregroundAppThreshold = 0;

        Parcel parcel = Parcel.obtain();
        memInfo.writeToParcel(parcel, 0);

//        memInfo.threshold;
        parcel.setDataPosition(0);
        availMem = parcel.readLong();
        if (Build.VERSION.SDK_INT >= 16)
            totalMem = parcel.readLong();
        threshold = parcel.readLong();
        lowMemory = parcel.readInt() != 0;
        hiddenAppThreshold = parcel.readLong();
        secondaryServerThreshold = parcel.readLong();
        visibleAppThreshold = parcel.readLong();
        foregroundAppThreshold = parcel.readLong();

//        Log.d(tag, "availMem: " + availMem + ", totalMem: " + totalMem + ", lowMemory: " + lowMemory
//                + ", threshold: " + threshold + ", hiddenAppThreshold: " + hiddenAppThreshold
//                + ", visibleAppThreshold: " + visibleAppThreshold + ", secondaryServerThreshold: + " + secondaryServerThreshold
//                + ", foregroundAppThreshold: + " + foregroundAppThreshold);
        String memory = "totalMem: " + totalMem + " availMem: " + availMem + " threshold: " + threshold;

        return memory;
    }
    /**
     * Debug.MemoryInfo; ActivityManager.gerProcessMemoryInfo();
     * @param context
     */
    public static String getMemoryInfo_2(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();

        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        int size = list.size();

        ActivityManager.RunningAppProcessInfo info = null;
        for (int i=0; i<size; i++){
            info = list.get(i);
        }

        int[] ids= new int[1];

        ids[0] = Process.myPid();
        Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(ids);


        Debug.MemoryInfo memoryInfo = memoryInfos[0];
        return getParcelOfMemoryInfo(memoryInfo);

    }


    /**
     * Debug.MemoryInfo
     * @param context
     */
    public static String getMemoryInfo_Debug(Context context){
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return getParcelOfMemoryInfo(memoryInfo);
    }

    /**
     * Debug.MemoryInfo
     * @param pid
     * @return String format: "TotalPss: " + totalPss +" DalvikPss: " + dalvikPss + " NativePss: " + nativePss + " OtherPss: " + otherPss + " OtherSwappedOutPss: " + otherSwappedOutPss
     * Unit:KB
     */
    public static String getMemoryInfo(int pid){
        ActivityManager activityManager = AppInfoManager.getActivityManager();
        if(activityManager ==  null)
            return null;
        int[] ids= new int[1];

        ids[0] = pid;
        Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(ids);
        
        Debug.MemoryInfo memoryInfo = memoryInfos[0];
        return getParcelOfMemoryInfo(memoryInfo);
    }

    /**
     *
     * @return   format: String memory = "totalMem: " + memInfo.totalMem + " availMem: " + memInfo.availMem +
     * " threshold: " + memInfo.threshold + " isLowMemory: " + memInfo.lowMemory;
     * unit:Bit
     */
    public static String getSystemMemory(){
        ActivityManager activityManager = AppInfoManager.getActivityManager();
        if(activityManager ==  null)
            return null;

        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);
        String memory = "totalMem: " + memInfo.totalMem + " availMem: " + memInfo.availMem +
                " threshold: " + memInfo.threshold + " isLowMemory: " + memInfo.lowMemory;

        return memory;
    }

    /**
     *
     * @param memoryInfo
     * @return String format: "TotalPss: " + totalPss +" DalvikPss: " + dalvikPss + " NativePss: " + nativePss + " OtherPss: " + otherPss + " OtherSwappedOutPss: " + otherSwappedOutPss
     */
    private static String getParcelOfMemoryInfo(Debug.MemoryInfo memoryInfo) {
        Parcel source = Parcel.obtain();

        memoryInfo.writeToParcel(source, 0);
        source.setDataPosition(0);

        int dalvikPss = 0;
        int dalvikSwappablePss = 0;
        int dalvikPrivateDirty = 0;
        int dalvikSharedDirty = 0;
        int dalvikPrivateClean = 0;
        int dalvikSharedClean = 0;
        int dalvikSwappedOut = 0;

        int nativePss = 0;
        int nativeSwappablePss = 0;
        int nativePrivateDirty = 0;
        int nativeSharedDirty = 0;
        int nativePrivateClean = 0;
        int nativeSharedClean = 0;
        int nativeSwappedOut = 0;

        int otherPss = 0;
        int otherSwappablePss = 0;
        int otherPrivateDirty = 0;
        int otherSharedDirty = 0;
        int otherPrivateClean = 0;
        int otherSharedClean = 0;
        int otherSwappedOut = 0;
        boolean hasSwappedOutPss = false;
        int otherSwappedOutPss = 0;
        int[] otherStats = null;

        dalvikPss = source.readInt();
        if (Build.VERSION.SDK_INT >= 19)
            dalvikSwappablePss = source.readInt();
        dalvikPrivateDirty = source.readInt();
        dalvikSharedDirty = source.readInt();
        if (Build.VERSION.SDK_INT >= 19) {
            dalvikPrivateClean = source.readInt();
            dalvikSharedClean = source.readInt();
            dalvikSwappedOut = source.readInt();
        }

        nativePss = source.readInt();
        if (Build.VERSION.SDK_INT >= 19)
            nativeSwappablePss = source.readInt();
        nativePrivateDirty = source.readInt();
        nativeSharedDirty = source.readInt();
        if (Build.VERSION.SDK_INT >= 19) {
            nativePrivateClean = source.readInt();
            nativeSharedClean = source.readInt();
            nativeSwappedOut = source.readInt();
        }

        otherPss = source.readInt();
        if (Build.VERSION.SDK_INT >= 19)
            otherSwappablePss = source.readInt();
        otherPrivateDirty = source.readInt();
        otherSharedDirty = source.readInt();
        if (Build.VERSION.SDK_INT >= 19) {
            otherPrivateClean = source.readInt();
            otherSharedClean = source.readInt();
            otherSwappedOut = source.readInt();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            hasSwappedOutPss = source.readInt() != 0;
            otherSwappedOutPss = source.readInt();
        }else {
            otherSwappedOutPss = memoryInfo.getTotalPss() - dalvikPss - nativePss - otherPss;
        }
        otherStats = source.createIntArray();


        String memory = "TotalPss: " + memoryInfo.getTotalPss() +" DalvikPss: " + dalvikPss + " NativePss: " + nativePss + " OtherPss: " + otherPss + " OtherSwappedOutPss: " + otherSwappedOutPss;
//        Log.d(tag, memory);

        return memory;

    }

    private static void getDetailMemoryInfo(Debug.MemoryInfo memoryInfo){

//        stats.put("summary.java-heap", Integer.toString(getSummaryJavaHeap()));
//        stats.put("summary.native-heap", Integer.toString(getSummaryNativeHeap()));
//        stats.put("summary.code", Integer.toString(getSummaryCode()));
//        stats.put("summary.stack", Integer.toString(getSummaryStack()));
//        stats.put("summary.graphics", Integer.toString(getSummaryGraphics()));
//        stats.put("summary.private-other", Integer.toString(getSummaryPrivateOther()));
//        stats.put("summary.system", Integer.toString(getSummarySystem()));
//        stats.put("summary.total-pss", Integer.toString(getSummaryTotalPss()));
//        stats.put("summary.total-swap", Integer.toString(getSummaryTotalSwap()));
        /*App Summary
        Pss(KB)
                ------
        Java Heap:     5944
        Native Heap:   104980
        Code:    27292
        Stack:      228
        Graphics:    12552
        Private Other:     1236
        System:     4453

        TOTAL:   156685      TOTAL SWAP (KB):     2320*/
        String[] names ={"summary.java-heap",
                "summary.native-heap",
                "summary.code",
                "summary.stack",
                "summary.graphics",
                "summary.private-other",
                "summary.system",
                "summary.total-pss",
                "summary.total-swap"};

        if(Build.VERSION.SDK_INT >=23) {

            int sum_java_heap = Integer.parseInt(memoryInfo.getMemoryStat(names[0]));
//            memoryInfo.getMemoryStats();
            int sum_native_heap = Integer.parseInt(memoryInfo.getMemoryStat(names[1]));
            int sum_code = Integer.parseInt(memoryInfo.getMemoryStat(names[2]));
            int sum_stack = Integer.parseInt(memoryInfo.getMemoryStat(names[3]));
            int sum_graphics = Integer.parseInt(memoryInfo.getMemoryStat(names[4]));
            int sum_private_other = Integer.parseInt(memoryInfo.getMemoryStat(names[5]));
            int sum_system = Integer.parseInt(memoryInfo.getMemoryStat(names[6]));
            int sum_total_pass = Integer.parseInt(memoryInfo.getMemoryStat(names[7]));
            int sum_total_swap = Integer.parseInt(memoryInfo.getMemoryStat(names[8]));
        }
    }


    /************************** Command Line*****************************/


    /**
     * Dump device's memory using command line, "adb shell cat /proc/memoinfo"
     * (IOException: Error running exec(). Command: [adb, shell, cat, /proc/memoinfo] Working Directory: null Environment: null)
     * @hide
     * @return
     */
    public static String getDeviceMemory(){

        String command = "cat /proc/memoinfo";
        BufferedReader reader = RuntimeHelper.execute_Reader(command);


        StringBuilder content = new StringBuilder();
        try {
            String line = null;
            int i = 0;
            while ((line = reader.readLine()) != null && i<3) {
                content.append(content + " ");
                i++;
            }
            if (reader != null)
                reader.close();
        }catch (Exception e){
            e.printStackTrace();

            try {
                if (reader != null)
                    reader.close();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }

        Log.d(tag, "DeviceMemory: " + content.toString());
        return content.toString();
    }

    /**
     * Dump app's memory using command line, "adb shell dumpsys meminfo <package_name>"
     *(IOException: Error running exec(). Command: [adb, shell, dumpsys, meminfo, com.supermap.imobile] Working Directory: null Environment: null)
     * @hide
     * @return
     */
    public static String getAppMemory(String packageName){

        String command = "dumpsys meminfo " + packageName;
        BufferedReader reader = RuntimeHelper.execute_Reader(command);

        StringBuilder content = new StringBuilder();
        try {
            String line = null;
            int i = 0;
            boolean isSummary = false;
            while ((line = reader.readLine()) != null && i<2) {
                if(line.contains("App Summary"))
                    isSummary = true;

                if (isSummary)
                    i++;
            }
            while ((line = reader.readLine()) != null) {
                if(line.contains("Objects"))
                    break;

                content.append(content + " ");
            }

            if (reader != null)
                reader.close();

        }catch (Exception e){
            e.printStackTrace();
            try {
                if (reader != null)
                    reader.close();
            }catch (Exception e1){
                e1.printStackTrace();
            }

        }

        Log.d(tag, "AppMemory: "  + packageName + ", " + content.toString());

        return content.toString();
    }

    /**
     * 06-23 09:34:02.946 16437-21978/com.supermap.imobile.qualitytest W/dalvikvm: VFY: unable to resolve virtual method 347: Landroid/os/Debug$MemoryInfo;.getMemoryStat (Ljava/lang/String;)Ljava/lang/String;
     06-23 09:34:37.359 16437-21978/com.supermap.imobile.qualitytest W/System.err: java.io.IOException: read failed: EBADF (Bad file number)
     06-23 09:34:37.360 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at libcore.io.IoBridge.read(IoBridge.java:479)
     06-23 09:34:37.361 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at java.io.FileInputStream.read(FileInputStream.java:179)
     06-23 09:34:37.361 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at java.io.InputStreamReader.read(InputStreamReader.java:231)
     06-23 09:34:37.362 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at java.io.BufferedReader.fillBuf(BufferedReader.java:145)
     06-23 09:34:37.363 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at java.io.BufferedReader.readLine(BufferedReader.java:397)
     06-23 09:34:37.363 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at com.supermap.imobile.AppManager.MemoryMonitor.getDeviceMemory(MemoryMonitor.java:291)
     06-23 09:34:37.364 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at com.supermap.imobile.AppManager.MemoryMonitor.getMemoryInfo(MemoryMonitor.java:115)
     06-23 09:34:37.365 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at com.supermap.imobile.AppManager.MemoryRecord$1.run(MemoryRecord.java:45)
     06-23 09:34:37.365 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at java.util.Timer$TimerImpl.run(Timer.java:284)
     06-23 09:34:37.366 16437-21978/com.supermap.imobile.qualitytest W/System.err: Caused by: libcore.io.ErrnoException: read failed: EBADF (Bad file number)
     06-23 09:34:37.367 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at libcore.io.Posix.readBytes(Native Method)
     06-23 09:34:37.368 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at libcore.io.Posix.read(Posix.java:128)
     06-23 09:34:37.368 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at libcore.io.BlockGuardOs.read(BlockGuardOs.java:149)
     06-23 09:34:37.369 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at libcore.io.IoBridge.read(IoBridge.java:469)
     06-23 09:34:37.369 16437-21978/com.supermap.imobile.qualitytest W/System.err: 	... 8 more
     06-23 09:37:01.728 16437-21978/com.supermap.imobile.qualitytest W/System.err: java.io.IOException: read failed: EBADF (Bad file number)
     06-23 09:37:01.729 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at libcore.io.IoBridge.read(IoBridge.java:479)
     06-23 09:37:01.730 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at java.io.FileInputStream.read(FileInputStream.java:179)
     06-23 09:37:01.731 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at java.io.InputStreamReader.read(InputStreamReader.java:231)
     06-23 09:37:01.732 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at java.io.BufferedReader.fillBuf(BufferedReader.java:145)
     06-23 09:37:01.733 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at java.io.BufferedReader.readLine(BufferedReader.java:397)
     06-23 09:37:01.734 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at com.supermap.imobile.AppManager.MemoryMonitor.getAppMemory(MemoryMonitor.java:328)
     06-23 09:37:01.735 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at com.supermap.imobile.AppManager.MemoryMonitor.getMemoryInfo(MemoryMonitor.java:115)
     06-23 09:37:01.736 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at com.supermap.imobile.AppManager.MemoryRecord$1.run(MemoryRecord.java:45)
     06-23 09:37:01.737 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at java.util.Timer$TimerImpl.run(Timer.java:284)
     06-23 09:37:01.738 16437-21978/com.supermap.imobile.qualitytest W/System.err: Caused by: libcore.io.ErrnoException: read failed: EBADF (Bad file number)
     06-23 09:37:01.739 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at libcore.io.Posix.readBytes(Native Method)
     06-23 09:37:01.740 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at libcore.io.Posix.read(Posix.java:128)
     06-23 09:37:01.741 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at libcore.io.BlockGuardOs.read(BlockGuardOs.java:149)
     06-23 09:37:01.741 16437-21978/com.supermap.imobile.qualitytest W/System.err:     at libcore.io.IoBridge.read(IoBridge.java:469)
     06-23 09:37:01.742 16437-21978/com.supermap.imobile.qualitytest W/System.err: 	... 8 more
     */
}
