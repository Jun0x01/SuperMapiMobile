package com.supermap.imobile.ActivitySetting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jun.tools.Message.ShowMessage;
import com.jun.tools.filebrower.FilePickActivity;
import com.supermap.data.Environment;
import com.supermap.imobile.ActivityMain.ActivityRequestCodes;
import com.supermap.imobile.ActivityMain.LanguageUtil;
import com.supermap.imobile.R;
import com.supermap.imobile.Resource.DataPath;

import java.io.File;
import java.util.Locale;

public class SettingsActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

	TextView mText_LicensePath;
	RadioGroup mRadioSwitch_DviceIDType;

	String[] mDeviceIDType = {"MAC", "IMEI", "UUID"};
	private String tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = getClass().getSimpleName();
		
		initView();
		
	}

	private void initView() {
		setContentView(R.layout.activity_setting);
		
		mText_LicensePath = (TextView) findViewById(R.id.txt_licence_path);
		mRadioSwitch_DviceIDType = (RadioGroup) findViewById(R.id.radioSwitchGroup_DeviceIDType);
		
		if (!SharedPreferenceManager.isInited())
			new SharedPreferenceManager(getApplicationContext());
		
		String deviceIDType = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_DeviceIDType);
		if(deviceIDType.isEmpty()){
			deviceIDType = "MAC";
			SharedPreferenceManager.setString(SharedPreferenceManager.mKey_DeviceIDType, deviceIDType);
		}
		int index = 0;
		for(String type : mDeviceIDType){
			
			if(type.equals(deviceIDType)){
				
				((RadioButton) mRadioSwitch_DviceIDType.getChildAt(index)).setChecked(true);
				break;
			}
			index++;
		}
	
		String licensePath = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_LicensePath);
		if(licensePath.isEmpty()){
			licensePath = DataPath.RootPath + "/SuperMap/License";
			SharedPreferenceManager.setString(SharedPreferenceManager.mKey_LicensePath, licensePath);
		}
		mText_LicensePath.setText(licensePath);
		
		// after set default checked button
		mRadioSwitch_DviceIDType.setOnCheckedChangeListener(this);

		// Set app information
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String ver = packageInfo.versionName;
			String buildVer = Environment.getBuildVersion(this);
			((TextView)findViewById(R.id.txt_AppVersion)).setText(getString(R.string.app_ver) + ver);
			((TextView)findViewById(R.id.txt_iMobileVersion)).setText(getString(R.string.imobile_ver) + buildVer);
		}catch (Exception e){

		}
		findViewById(R.id.layout_licencePath).setOnClickListener(this);

		Configuration config = getResources().getConfiguration();

		RadioButton chineseRadio = (RadioButton) findViewById(R.id.btn_radio_chinese);
		RadioButton englishRadio = (RadioButton) findViewById(R.id.btn_radio_english);
		Locale locale = null;
		LocaleList localeList = null;
		if(Build.VERSION.SDK_INT < 24){
			locale = config.locale;

			if(locale.equals(Locale.CHINA) || locale.equals(Locale.CHINESE) || locale.equals(Locale.SIMPLIFIED_CHINESE) || locale.equals(Locale.TRADITIONAL_CHINESE)) {
				chineseRadio.setChecked(true);
			}else {
				englishRadio.setChecked(true);
			}
		}else {
			localeList = config.getLocales();
			int count = localeList.size();

			if(count>0){
				locale = localeList.get(0);
				if(locale.getCountry().compareToIgnoreCase("cn")==0 || locale.toString().contains("_cn_") || locale.toString().contains("_CN_") || locale.equals(Locale.CHINA) || locale.equals(Locale.CHINESE) || locale.equals(Locale.SIMPLIFIED_CHINESE) || locale.equals(Locale.TRADITIONAL_CHINESE)) {
					chineseRadio.setChecked(true);
				}else {
					englishRadio.setChecked(true);
				}
			}
		}



		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioSwitchGroup_Language);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			int mOldCheckedId = -1;
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				Locale locale = null;
				String localeStr = null;
				switch (checkedId){
					case R.id.btn_radio_chinese:
						locale = Locale.CHINESE;
						localeStr = "Chinese";
						break;
					case R.id.btn_radio_english:
						locale = Locale.ENGLISH;
						localeStr = "English";
						break;
				}

				Resources resources = getResources();
				Configuration config = resources.getConfiguration();
				if(locale != null) {
					if(Build.VERSION.SDK_INT < 17){
						config.locale = locale;
					} else {
						config.setLocale(locale);
					}
					resources.updateConfiguration(config, resources.getDisplayMetrics());

				}

				SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Locale_Language, localeStr);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_licencePath:
			selectData();
			break;

		default:
			break;
		}
		
	}

	private void selectData() {
//
		String lastPath = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_LicensePath);

		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), FilePickActivity.class);
		intent.setType("dir");
		intent.setData(Uri.parse("dir://" + lastPath));

		startActivityForResult(intent, ActivityRequestCodes.SELECT_DIR_CODE_SETTING);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(ActivityRequestCodes.SELECT_DIR_CODE_SETTING == requestCode && RESULT_OK == resultCode){

			Uri uri = data.getData();
			String path = null;
			if("content".equalsIgnoreCase(uri.getScheme())){
				String[] projection = {"_data"};
				Cursor cursor = null;
				cursor = this.getContentResolver().query(uri, projection, null, null, null);
				int columnIndex = cursor.getColumnIndex("_data");
				if(columnIndex > -1){
					path = cursor.getString(columnIndex);

				}
			}else if("file".equalsIgnoreCase(uri.getScheme()) || "dir".equalsIgnoreCase(uri.getScheme()) ){
				path = uri.getPath();
			}
			if(path != null && !path.isEmpty()){

				File file = new File(path);
				if(file.isDirectory()) {
					SharedPreferenceManager.setString(SharedPreferenceManager.mKey_LicensePath, path);
					mText_LicensePath.setText(path);
				}else {
					SharedPreferenceManager.setString(SharedPreferenceManager.mKey_LicensePath, file.getParent());
					mText_LicensePath.setText(file.getParent());
				}
			}
			return;
		}
	}

	int mOldCheckedId = -1;
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int index = group.indexOfChild(group.findViewById(checkedId));
		if(mOldCheckedId != -1)
		ShowMessage.showInfo(tag, getString(R.string.device_id_type_note), this);
		SharedPreferenceManager.setString(SharedPreferenceManager.mKey_DeviceIDType, mDeviceIDType[index]);
		mOldCheckedId = checkedId;
	}

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.changeLanguage(newBase));
    }
}
