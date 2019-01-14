package com.supermap.imobile.Tools;

import com.jun.tools.logcat.LogSaveManager;

/**
 * Created by Jun on 2017/4/17.
 */

public class SaveTestLog {

    private static LogSaveManager mLogSaver;

    public static String PaintCost = "BrowseMap: ";


    /**
     *
     * @param path absolutely directory path for saving log file
     */
    public static void setLogPath(String path){
        if(mLogSaver == null)
            mLogSaver = new LogSaveManager(null);
        mLogSaver.setLogPath(path);

    }

    public synchronized static void saveLog(String log){
        if(mLogSaver == null)
            mLogSaver = new LogSaveManager(null);
        mLogSaver.saveLog(PaintCost + log);
    }

    public static void closeFile(){
        if(mLogSaver != null)
            mLogSaver.closeFile();
    }

    public static void createNewFile(){
        if(mLogSaver != null)
            mLogSaver.createNewFile();
    }

    public static void setLogFile(String fileName){
        if(mLogSaver != null)
            mLogSaver.setLogFile(fileName);
    }

    @Override
    protected void finalize() throws Throwable {
        closeFile();
        super.finalize();
    }
}
