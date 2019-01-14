package com.jun.tools.toolkit;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class ImgOutputTool {

	 private static String tag = "ImgOutputTool";

	//打印出图片
	public static void outPicture(View v, String path) {
		new File(path).getParentFile().mkdirs();
		
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		Bitmap bitmap = v.getDrawingCache();
		if (bitmap != null) {
//			System.out.println("bitmap got!");
			try {
				FileOutputStream out = new FileOutputStream(path);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Log.d(tag, "Output img successed," + path);

		} else {
//			System.out.println("bitmap is null!");
			Log.e(tag , "bitmap is null," + path);
		}
		v.destroyDrawingCache();
		
	}

		
	
}
