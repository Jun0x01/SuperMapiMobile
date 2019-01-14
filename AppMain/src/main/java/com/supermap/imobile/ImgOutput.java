package com.supermap.imobile;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImgOutput {

	 private static String tag = "ImgOutput";
	 public static String mDefaultIMGType = ".png";

	/**
	 * 将View输出为图片，该view可可获得DrawingCache bitmap
	 * @param v
	 * @param path
	 */
	public static void outputPicture(View v, String path) {
		new File(path).getParentFile().mkdirs();
		
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		Bitmap bitmap = v.getDrawingCache();
		
		
		if (bitmap != null) {

			boolean isSaved = saveBitmap(bitmap, path);

			if(!isSaved)
				Log.e(tag , "bitmap is null," + path);
		} else {
//			System.out.println("bitmap is null!");
			Log.e(tag , "bitmap is null," + path);
		}
		v.destroyDrawingCache();
		
	}

	/**
	 * 将普通View和MapControl的Bitmap叠加，输出图片，并且MapControl显示的地图在最下层, upView的在上层, upView 覆盖整个地图
	 * 先绘制地图，再绘制MapView中其他View,然后是上层用户view, headerView 位于map的上端
	 * @param upView
	 * @param headerView
	 * @param mapView
	 * @param path
	 * @param isMapViewRepeated 指明upView中是否包含了MapView
	 * @return
	 */
	public static boolean ouputPicture(View upView, View headerView, MapView mapView, String path, boolean isMapViewRepeated){
		
		boolean result = false;
		if(upView == null || mapView == null || path == null){
			Log.e(tag,"One or two params are null");
			return result;
		}
		Bitmap bitmap = null;
		Bitmap bitmapUp = null;
		Bitmap bitmapMap = null;
		int bitmapW = 0;
		int bitmapH = 0;
		
		// 空白bitmap, 大小和upView一致
		bitmapW = upView.getWidth();
		bitmapH = upView.getHeight();
		bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Config.ARGB_8888);
		
		upView.setDrawingCacheEnabled(true);
		upView.buildDrawingCache();
		bitmapUp = upView.getDrawingCache();
		if(bitmapUp == null)
			return result;
		
		int mapW = mapView.getWidth();
		int mapH = mapView.getHeight();
		
		bitmapMap = Bitmap.createBitmap(mapW, mapH, Config.ARGB_8888);
		if (bitmapMap == null) {
			// Failed to create bitmap because of out of memory
			bitmapUp.recycle();
			bitmapUp = null;
			return result;
		}
		MapControl mapControl = mapView.getMapControl();
		mapControl.outputMap(bitmapMap);
		
		int left = mapView.getLeft();
		int top = mapView.getTop();
		ViewParent parent = mapView.getParent();
		if(parent != null && parent instanceof View){
			left = ((View)parent).getLeft();
			top = ((View)parent).getTop();
		}
		// Compose bitmaps
		Canvas canvas = new Canvas(bitmap);
		if(headerView != null){
			headerView.setDrawingCacheEnabled(true);
			headerView.buildDrawingCache();
			Bitmap header = headerView.getDrawingCache();
			
			canvas.save();
			canvas.drawBitmap(header, 0, 0, null);
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			
			headerView.destroyDrawingCache();
		}
		
		canvas.save();
		canvas.drawBitmap(bitmapMap, left, top, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		
		if (!isMapViewRepeated) {
			mapView.setDrawingCacheEnabled(true);
			mapView.buildDrawingCache();
			Bitmap bitmapMapView = mapView.getDrawingCache();
			
			canvas.save();
			canvas.drawBitmap(bitmapMapView, left, top, null);
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			
			mapView.destroyDrawingCache();
		}
		
		canvas.save();
		canvas.drawBitmap(bitmapUp, 0, 0, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		
		result = saveBitmap(bitmap, path);
		
		// recycle memory
		bitmapMap.recycle();
//		bitmapUp.recycle(); // 普通View的bitmap不调用recyle,对象属于view
		upView.destroyDrawingCache();  // 通过这个接口销毁创建的缓存，否则同一个View再次获取bitmap不会更新
		
		bitmapMap = null;
		bitmapUp = null;
		
		return result;
	}
	
	/**
	 * 将bitmap 保存为文件
	 * @param bitmap
	 * @param path
	 * @return
	 */
	public static boolean  saveBitmap(Bitmap bitmap, String path){
		boolean result = false;
		if(bitmap == null || path == null){
			Log.e(tag,"One or two params are null");
			return result;
		}
		// Create directories
		File file = new File(path);
		file.getParentFile().mkdirs();
		
		// Get file format
		CompressFormat comFormat = null;
		
		int index = path.lastIndexOf('.');
		if(index == -1){
			file = new File(path + ".png");
			comFormat = CompressFormat.PNG;
		}else if(index == path.length() -1){
			file = new File(path + "png");
			comFormat = CompressFormat.PNG;
		}else {
			String suffix = path.substring( index+1);
			
			
			if(suffix.equalsIgnoreCase("png")){
				comFormat = CompressFormat.PNG;
			}else if (suffix.equalsIgnoreCase("jpg")){
				comFormat = CompressFormat.JPEG;
			}else {
				file = new File(path + ".png");
				comFormat = CompressFormat.PNG;
			}
		}
		
		
		// Create image file
		try {
			FileOutputStream fOS = new FileOutputStream(file);
			
			boolean isTrue = bitmap.compress(comFormat, 100, fOS);
			
			if(!isTrue)
				Log.e(tag, "Failed to create image file : " + path);
			
			fOS.flush();
			fOS.close();
			
			result = true;
			
			Log.d(tag, "Output img successed, " + path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return result;
		
	}

	public static boolean ouputPicture(MapControl mapControl, String path) {
		
		boolean result = false;
		if(mapControl == null || path == null){
			Log.e(tag,"One or two params are null");
			return result;
		}
		Bitmap bitmapMap = null;
		int bitmapW = 0;
		int bitmapH = 0;
		
		bitmapW = mapControl.getWidth();
		bitmapH = mapControl.getHeight();
		
		bitmapMap = Bitmap.createBitmap(bitmapW, bitmapH, Config.ARGB_8888);
		if (bitmapMap == null) {
			return result;
		}
		
		mapControl.outputMap(bitmapMap);
		
		// Compose bitmaps
//		Canvas canvas = new Canvas(bitmapMap);
//		canvas.save();
//		canvas.drawBitmap(bitmapUp, 0, 0, null);
//		canvas.save(Canvas.ALL_SAVE_FLAG);
//		canvas.restore();
		
		
		result = saveBitmap(bitmapMap, path);
		
		// recycle memory
		bitmapMap.recycle();
		bitmapMap = null;
		
		return result;
	}

	
   public static boolean ouputPicture(MapView mapView, String path) {
		
		boolean result = false;
		if(mapView == null || path == null){
			Log.e(tag,"One or two params are null");
			return result;
		}
		Bitmap bitmapMap = null;
		int bitmapW = 0;
		int bitmapH = 0;
		
		bitmapW = mapView.getWidth();
		bitmapH = mapView.getHeight();
		
		bitmapMap = Bitmap.createBitmap(bitmapW, bitmapH, Config.ARGB_8888);
		if (bitmapMap == null) {
			return result;
		}
		MapControl mapControl = mapView.getMapControl();
		mapControl.outputMap(bitmapMap);
		
		mapView.setDrawingCacheEnabled(true);
		mapView.buildDrawingCache();
		Bitmap bitmapUp = mapView.getDrawingCache();
		
		// Compose bitmaps
		Canvas canvas = new Canvas(bitmapMap);
		canvas.save();
		canvas.drawBitmap(bitmapUp , 0, 0, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		
		
		result = saveBitmap(bitmapMap, path);
		
		// recycle memory
		bitmapMap.recycle();
		bitmapMap = null;
		mapView.destroyDrawingCache();
		
		return result;
	}
   
}
