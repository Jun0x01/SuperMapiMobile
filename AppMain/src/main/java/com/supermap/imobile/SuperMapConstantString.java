package com.supermap.imobile;

public class SuperMapConstantString {

	public static final String rootPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath().toString();
	
	public static final String str_path_SuperMapLicense = rootPath + "/SuperMap/License/";
	
	private static final String str_path_MyTestData =rootPath +  "/SuperMap/Data/MyTestData";
	
	public static final String str_path_wk_yuanqu =str_path_MyTestData +  "/yuanqu/track.smwu";
	
    private static final String str_path_3d_CherryData = str_path_MyTestData + "/CherryData";
	
	private static final String str_path_3d_CherryScenCache = str_path_3d_CherryData + "/CherrySceneCache/Cherry";
	
	public static final String str_path_wk_3d_Cherry = str_path_MyTestData + "/CherryData/Cherry.smwu";
	
	public static final String str_path_wk_3d_sceneCache_Cherry = str_path_MyTestData + "/CBD/CBD.sxwu";/*"/cmccbase/cmccbase.sxwu";*/ /*"/CherryData/CherrySceneCache/Cherry/Cherry.sxwu";*/
	
	public static final String str_path_wk_layerseting_grid_image = str_path_MyTestData + "/Network/Jun_SuperMap_NetworkMap.smwu";
	
//	public static final String str_path_wk_layer_cad_geostyle = str_path_MyTestData + "/CAD/CAD.smwu";
	
	public static final String str_path_wk_layer_Networ = str_path_MyTestData + "/Network/Jun_SuperMap_NetworkMap.smwu";
	
}
