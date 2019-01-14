package com.jun.tools.logcat;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageHelper {

	private static PackageHelper mPackageHelper;

	private static String mPackageVersion;
	private static String mPackageName;
	private static Context mContext;
	
	public PackageHelper(Context context) {
		if (context != null) {
			mPackageName = context.getPackageName();
			try {
				mPackageVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mPackageVersion = "UnKnown";
			}
		}else {
			mPackageName = "UnKnown";
			mPackageVersion = "UnKnown";
		}
		
		mContext = context;
		mPackageHelper = this;
	}
	
	public PackageHelper getInstance(){
		if(mContext == null)
			return null;
		return mPackageHelper;
	}

	public  String getPackageVersion(){
		return mPackageVersion;
	}
	
	public String getPackageName(){
		return mPackageName;
	}
}
