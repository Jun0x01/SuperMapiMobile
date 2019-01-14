package com.supermap.imobile.ActivityMain;


import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.jun.tools.Activity.ApplicationBase;
import com.jun.tools.Message.ShowMessage;
import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import dalvik.system.DexFile;


public class MainApplication extends ApplicationBase {

	private static MainApplication mInstance = null;

	/**
	 * Device density
	 */
	public static float mDensity;
	public static final String m_SharedPreferenceName = "PreferenceSuperMapiMobile";
	public static final String mKey_LastPath = "LastPath";

	public static String mGraphicType = null;
	ArrayList<Intent> mIntentServices;

	public static SharedPreferenceManager mSharedPreferenceManager;
	@Override
	public void onCreate() {
		super.onCreate();
		Resources resources = getResources();
		mDensity = resources.getDisplayMetrics().density;
		mInstance = this;

		mIntentServices = new ArrayList<Intent>();

		ShowMessage.setContext(getApplicationContext());
		mSharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());

		String language = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Locale_Language);
		Locale locale = null;
		if (language != null && !language.isEmpty()){
			if (0 == language.compareToIgnoreCase("English")) {
				locale = Locale.ENGLISH;
			} else if(0 == language.compareToIgnoreCase("Chinese")) {
				locale = Locale.CHINESE;
			}
		}
        Configuration config = resources.getConfiguration();

		if(locale != null) {
			if(Build.VERSION.SDK_INT < 17){
				config.locale = locale;

				resources.updateConfiguration(config, resources.getDisplayMetrics());
			} else {
				config.setLocale(locale);
				getApplicationContext().createConfigurationContext(config);
				resources.updateConfiguration(config, resources.getDisplayMetrics());
			}
		}

		String libsDir = this.getFilesDir().getAbsolutePath().replace("files", "lib");
        File libsFile = new File(libsDir);
        File[] files = libsFile.listFiles();
//        AssetManager assetManager = this.getAssets();
//        try {
//            InputStream inputStream = assetManager.open("ver/ver.txt");
//        }catch (Exception e){
//            e.printStackTrace();
//        }


      //  getClasss();
	}

	@Override
	public void onTerminate() {

		for(int i=0; i<mIntentServices.size(); i++){
			stopService(mIntentServices.get(i));
		}
		mIntentServices.clear();
		mSharedPreferenceManager = null;

		SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi2_Dataset, null);
		SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi2_Datasource, null);
		SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi3_Datasource, null);
		super.onTerminate();

	}

	public static MainApplication getInstance(){
		return mInstance;
	}

	public void registerServiceIntent(Intent intent){
		if(intent != null && !mIntentServices.contains(intent)) {
			startService(intent);
			mIntentServices.add(intent);
		}
	}

    private void getClasss(){
        String packageStr = getPackageCodePath();
        Log.d("Package", packageStr);

		ClassLoader loader = getClassLoader();
        String loaderStr = loader.toString();
        String[] loaderStrs = loaderStr.split("[\\[\\]]");

        String zipStr = null;
        for(String zip : loaderStrs){
            if(zip.startsWith("zip file")){
                zipStr = zip;
                break;
            }
        }

        String[] zipStrs = zipStr.split("\"");
        for (int i=1; i<zipStrs.length; i+=2){
            String zip = zipStrs[i];
            Log.d("Package", zip);
            try {
                DexFile dx = new DexFile(zip);
                Enumeration<String> enums = dx.entries();
                while (enums.hasMoreElements()){
                    String cls = enums.nextElement();


					Class<?> c = null;
					try {
						c = loader.loadClass(cls);
					} catch (ClassNotFoundException e){
						e.printStackTrace();
					}
					if(c != null){
						Log.d("Package", cls + ", Loaded........");
					} else {
						Log.d("Package", cls);
					}
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }



    }
}

