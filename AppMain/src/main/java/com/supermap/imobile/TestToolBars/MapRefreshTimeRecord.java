package com.supermap.imobile.TestToolBars;

import android.support.annotation.NonNull;
import android.util.Log;

import com.supermap.imobile.Tools.SaveTestLog;
import com.supermap.mapping.PaintProfileListener;
//import com.supermap.mapping.PaintProfileListener;

/**
 * Created by Jun on 2017/9/5.
 * @since SuperMap iMobile 9D
 */

public class MapRefreshTimeRecord implements PaintProfileListener {

    String mTag = null;

    public MapRefreshTimeRecord(@NonNull String tag) {
        mTag = tag;
    }

    boolean mEnableSaveLog;

    public void enableSaveLog(boolean enable) {
        mEnableSaveLog = enable;
    }

//    @Override
    public void paintCost(int i) {
        Log.d(mTag, "Paint Cost Time: " + i);
        if (mEnableSaveLog)
            SaveTestLog.saveLog(mTag + " Paint Cost Time: " + i);
    }

}
