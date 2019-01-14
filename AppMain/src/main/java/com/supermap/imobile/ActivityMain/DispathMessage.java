package com.supermap.imobile.ActivityMain;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Jun on 2017/5/2.
 */

public class DispathMessage {
    public static void reloadLayers(){
        Handler handler = MainActivity.mHandler;
        Message message = handler.obtainMessage(MainActivity.MainHandlerTag.RELOADLAYERS);
        handler.sendMessage(message);
    }

    public static void post(final  Runnable runnable){
        Handler handler = MainActivity.mHandler;

        handler.post(runnable);
    }

    public static void toastError(String tag, String content){
        Bundle data = new Bundle();
        data.putString("tag", tag);
        data.putString("content", content);

        Handler handler = MainActivity.mHandler;
        Message message = handler.obtainMessage(MainActivity.MainHandlerTag.TOAST_ERROR);

        message.setData(data);
        handler.sendMessage(message);
    }

    public static void toastWarning(String tag, String content){
        Bundle data = new Bundle();
        data.putString("tag", tag);
        data.putString("content", content);

        Handler handler = MainActivity.mHandler;
        Message message = handler.obtainMessage(MainActivity.MainHandlerTag.TOAST_ERROR);

        message.setData(data);
        handler.sendMessage(message);
    }

}
