package com.jun.tools.Activity;

import android.content.Context;
import android.util.Log;

import java.io.File;
/**
 * Created by Jun on 2017/3/14.
 */

public class JniLibsManager {

    public static void loadLibraries(Context context){
        String libs = context.getFilesDir().getAbsolutePath().replace("files", "lib");
        File libsDir = new File(libs);
        String[] soes = libsDir.list();
        if(null != soes) {

            for (String so : soes) {
                String libName = so.substring(3, so.length() - 3); // libXXX.so
//                System.loadLibrary(libName);
                Log.d("JniLibsManager", "Lib: " + libName);
            }
        }else {
            Log.d("JniLibsManager", "libs list a null");
        }


//        System.loadLibrary("Part1");
//        System.loadLibrary("Part2");
//        System.loadLibrary("native-lib");
//        System.loadLibrary("MQJni");
        String root = context.getFilesDir().getAbsolutePath();
        File file = new File(root);
        String [] files = file.list();
        for (String str:files){
            Log.d("JniLibsManager", str);
        }

    }
}
