package com.supermap.imobile.ActivityMain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.jun.tools.Message.ShowMessage;
import com.jun.tools.filebrower.FilePickActivity;
import com.supermap.data.DeviceIDType;
import com.supermap.data.Environment;
import com.supermap.data.LicenseStatus;
import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;
import com.supermap.imobile.R;
import com.supermap.imobile.Worksapce.ValueName;

/**
 * Created by Jun on 2017/11/9.
 */

public class LicenseConfiguration_Local extends Activity implements View.OnClickListener{

    EditText mLicensePathInput;

    final int mRequest_Select_Dir = 0x1;

    DeviceIDType mDeviceIDType; // The changed DeviceIDType.
    String mLicensePath; // The changed license path.

    final String tag = "LicenseLocal";
    String mOldDeviceIDTypeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_layout_license_configuration_local);

        mLicensePathInput = (EditText) findViewById(R.id.license_path_input);

        RadioGroup radioGroup = ((RadioGroup)findViewById(R.id.radio_group_device_id_types));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                String type = null;

                switch (checkedId){
                    case R.id.radio_btn_mac:
                        mDeviceIDType = DeviceIDType.MAC;
                        break;
                    case R.id.radio_btn_uuid:
                        mDeviceIDType = DeviceIDType.UUID;
                        break;
                    case R.id.radio_btn_imei:
                        mDeviceIDType = DeviceIDType.IMEI;
                        break;
                    case R.id.radio_btn_tmimei:
                        mDeviceIDType = DeviceIDType.TMIMEI;
                        break;
                }

                type = ValueName.getDeviceIDTypeName(mDeviceIDType);
                ShowMessage.showInfo("Activate", type);
                if(type != null)
                    SharedPreferenceManager.setString(SharedPreferenceManager.mKey_DeviceIDType, type);
            }
        });

        String deviceIDTypeStr = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_DeviceIDType);
        if(deviceIDTypeStr.isEmpty())
            deviceIDTypeStr = "MAC";
        if (deviceIDTypeStr.equals("MAC")) {
            radioGroup.check(R.id.radio_btn_mac);
        } else if (deviceIDTypeStr.equals("UUID")) {
            radioGroup.check(R.id.radio_btn_uuid);
        } else if (deviceIDTypeStr.equals("IMEI")) {
            radioGroup.check(R.id.radio_btn_imei);
        } else if (deviceIDTypeStr.equals("TMIMEI")) {
            radioGroup.check(R.id.radio_btn_tmimei);
        }

        mOldDeviceIDTypeStr = deviceIDTypeStr;

        String licensePath = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_LicensePath);
        mLicensePathInput.setText(licensePath);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.license_path_input:
                selectPath();
                break;
            case R.id.btn_Cancel:
                finish();
                break;
            case R.id.btn_Confirm:
                confirm();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == mRequest_Select_Dir){
            getPath(resultCode, data);
        }
    }


    private void selectPath() {

        Intent intent = new Intent();
        intent.setClass(this, FilePickActivity.class);
        intent.setType("dir");
        intent.setData(Uri.parse("dir://" + SharedPreferenceManager.getString(SharedPreferenceManager.mKey_LicensePath)));
        startActivityForResult(intent, mRequest_Select_Dir);
    }

    private void getPath(int resultCode, Intent data) {

        if (RESULT_OK == resultCode) {

            String dir = data.getData().getPath();
            if(dir != null) {
                String oldPath = Environment.getLicensePath();
                if(!oldPath.equals(dir)) {
                    mLicensePathInput.setText(dir);
                    mLicensePath = dir;
                }
            }
        }
    }

    private void confirm(){
        if (mLicensePath != null) {
            Environment.setLicensePath(mLicensePath);
            SharedPreferenceManager.setString(SharedPreferenceManager.mKey_LicensePath, mLicensePath);
        }

        // Whether has DeviceIDType been changed ?
        String type = ValueName.getDeviceIDTypeName(mDeviceIDType);
        if (type != null && mDeviceIDType != null && !mOldDeviceIDTypeStr.equals(type)){
            SharedPreferenceManager.setString(SharedPreferenceManager.mKey_DeviceIDType, type);
            Environment.setDeviceIDType(mDeviceIDType);

            ShowMessage.showInfo(tag, getString(R.string.license_deviceidtype_changed), this);
            return;
        }

        LicenseStatus licenseStatus = Environment.getLicenseStatus();
        if(licenseStatus.isLicenseValid()){
            finish();
        } else {
            if(mLicensePath != null) {
                ShowMessage.showInfo(tag, getString(R.string.license_invalid), this);
            } else {
                ShowMessage.showInfo(tag, getString(R.string.license_invalid), this);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.changeLanguage(newBase));
    }
}
