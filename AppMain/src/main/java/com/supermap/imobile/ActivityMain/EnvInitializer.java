package com.supermap.imobile.ActivityMain;

import android.content.Context;
import android.util.Log;

import com.supermap.data.DeviceIDType;
import com.supermap.data.Environment;
import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;
import com.supermap.imobile.Resource.DataPath;
import com.supermap.imobile.Worksapce.ValueName;

/**
 * Created by Jun on 2017/5/2.
 */

public class EnvInitializer {


    public static void init(Context context){
        String deviceIDType = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_DeviceIDType);
        if(deviceIDType.isEmpty()){
            deviceIDType = "MAC";
            SharedPreferenceManager.setString(SharedPreferenceManager.mKey_DeviceIDType, deviceIDType);
        }

        String licensePath = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_LicensePath);
        if(licensePath.isEmpty()){
            licensePath = DataPath.RootPath + "/SuperMap/License11";
            SharedPreferenceManager.setString(SharedPreferenceManager.mKey_LicensePath, licensePath);
        }

        String mLicenseBackupPath = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_LicenseBackupPath);
        if(mLicenseBackupPath.isEmpty()){
            mLicenseBackupPath = licensePath + "/Backups";
            SharedPreferenceManager.setString(SharedPreferenceManager.mKey_LicenseBackupPath, mLicenseBackupPath);
        }

        boolean isOpenGL = SharedPreferenceManager.getBoolean(SharedPreferenceManager.mKey_IsOpenGL);

        Environment.setOpenGLMode(isOpenGL);

//        if (!SharedPreferenceManager.getBoolean("BLogined", false)) {
            Environment.setLicensePath(licensePath);
//        }
        deviceIDType = "TMIMEI";
        setDeviceIDType(deviceIDType);

        Environment.initialization(context);

        String id0 = Environment.getDeviceID();

        Environment.setDeviceIDType(DeviceIDType.TMIMEI);
        String id3 = Environment.getDeviceID();
        Environment.setDeviceIDType(DeviceIDType.UUID);
        String id4 = Environment.getDeviceID();

        Environment.setDeviceIDType(DeviceIDType.IMEI);
        String id1 = Environment.getDeviceID();
        Environment.setDeviceIDType(DeviceIDType.MAC);
        String id2 = Environment.getDeviceID();

        Log.e("DeviceID", deviceIDType + ": " + id0 + ", TMIMEI: " + id3 + ", UUID: " + id4 + ", IMEI: " + id1 + ", MAC: " + id2);
    }

    public static void setDeviceIDType( String type) {
        DeviceIDType deviceIDType = ValueName.getDeviceIDType(type);
        if(deviceIDType != null)
            Environment.setDeviceIDType(deviceIDType);
    }


}
