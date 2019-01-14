package com.supermap.imobile.Resource;

/**
 * 
 * 定义基本数据路径
 *
 * 
 * Created on: 2016.08.12
 * @author XingJun
 * 
 *
 */
public class DataPath {

	public final static String RootPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath().toString();
	
	public final static String TestDataDir = RootPath + "/iMobile/02_FunctionData";
	
	public final static String TestScreenShotDir = RootPath + "/iMobile/TestScreenShot/02_FunctionData";
	public final static String TestExpectScreenShotDir = RootPath + "/iMobile/TestExpectScreenShot/02_FunctionData";
	
	public final static String WebCachePath = RootPath + "/iMobile/WebCache/";

	public final static String LogPath = RootPath + "/iMobile/TestLog/";
	
	public final static String WK_Navi = TestDataDir + "/NavigationData/Beijing/beijing.smwu";
	public final static String DB_NaviPoint = TestDataDir + "/NavigationData/Beijing/navi_point.udb";
	
	public final static String WK_MapOperate = RootPath + "/iMobile/StabilityTest/Mapping/MapManipulation/worldoverweb.smwu";
}
