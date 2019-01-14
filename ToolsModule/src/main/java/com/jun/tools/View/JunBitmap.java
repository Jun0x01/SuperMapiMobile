package com.jun.tools.View;

import android.graphics.Bitmap;
import android.os.Build;

/**
 * Created by Jun on 2017/6/23.
 */

public class JunBitmap {

    public static long getBitMapSize(int width, int height){
        long size = 0;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1){
            size = bitmap.getRowBytes() * bitmap.getHeight();
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1 &&Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            size = bitmap.getByteCount();
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                size= bitmap.getAllocationByteCount();
        }

        bitmap.recycle();

        return size;
    }
}
