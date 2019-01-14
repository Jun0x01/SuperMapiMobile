package com.supermap.imobile.ActivityMain;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.jun.tools.View.JunBaseListAdapter;
import com.supermap.data.DeviceIDType;
import com.supermap.data.Environment;
import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;
import com.supermap.imobile.R;
import com.supermap.imobile.Worksapce.ValueName;

import java.util.ArrayList;

//import com.supermap.data.LicenseManager;

/**
 * Created by Jun on 2017/11/1.
 */

public class LicenseConfiguration_Activate extends Activity implements View.OnClickListener{

    AutoCompleteTextView mLicenseCodeEdit;
    ListView mListViewModules;
    JunBaseListAdapter mAdapter;

    ArrayAdapter<String> mAdapterCodes;
    ArrayList<String> mCodes;

    final String mKey_LicenseCodes = "LicenseCodes";
    final String tag = "LicenseActivation";
    private String mOldDeviceIDType;

    DeviceIDType mDeviceIDType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_layout_license_configuration_activation);

        mLicenseCodeEdit = (AutoCompleteTextView) findViewById(R.id.license_code_input);
        mListViewModules = (ListView) findViewById(R.id.list_modules);

        mAdapter = new JunBaseListAdapter(mListViewModules, R.layout.license_item_modules, R.id.text_LicModule);

        for(String module : ValueName.mModules){
            mAdapter.addItem(module);
        }

        mCodes = SharedPreferenceManager.getStrings(mKey_LicenseCodes, null);
        mAdapterCodes = new ArrayAdapter<String>(this, R.layout.listview_item_normal, R.id.text_ItemName, mCodes);
        mLicenseCodeEdit.setAdapter(mAdapterCodes);


        RadioGroup radioGroup = ((RadioGroup)findViewById(R.id.radio_group_device_id_types));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                String type = null;
                switch (checkedId){
                    case R.id.radio_btn_mac:
                        mDeviceIDType = DeviceIDType.MAC;
                        type = "MAC";
                        break;
                    case R.id.radio_btn_uuid:
                        mDeviceIDType = DeviceIDType.UUID;
                        type = "UUID";
                        break;
                    case R.id.radio_btn_imei:
                        mDeviceIDType = DeviceIDType.IMEI;
                        type = "IMEI";
                        break;
                    case R.id.radio_btn_tmimei:
                        mDeviceIDType = DeviceIDType.TMIMEI;
                        type = "TMIMEI";
                        break;
                }
                Environment.setDeviceIDType(mDeviceIDType);
//                ShowMessage.showInfo("Activate", type);
                if(type != null && !type.equals(mOldDeviceIDType))
                    SharedPreferenceManager.setString(SharedPreferenceManager.mKey_DeviceIDType, type);
            }
        });

        mOldDeviceIDType = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_DeviceIDType);
        if(mOldDeviceIDType.isEmpty())
            mOldDeviceIDType = "MAC";
        if (mOldDeviceIDType.equals("MAC")) {
            radioGroup.check(R.id.radio_btn_mac);
        } else if (mOldDeviceIDType.equals("UUID")) {
            radioGroup.check(R.id.radio_btn_uuid);
        } else if (mOldDeviceIDType.equals("IMEI")) {
            radioGroup.check(R.id.radio_btn_imei);
        } else if (mOldDeviceIDType.equals("TMIMEI")) {
            radioGroup.check(R.id.radio_btn_tmimei);
        }

        initLicenseManager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Cancel:
                finish();
                break;
            case R.id.btn_Confirm:
                activate();
                break;

            case R.id.license_code_input:
                mLicenseCodeEdit.showDropDown();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
        super.onDestroy();
    }

    /******************************* 8C ************************************/

//    LicenseManager mLicenseManager;
    private void initLicenseManager(){
//        mLicenseManager = LicenseManager.getInstance();
//        mLicenseManager.setActivateCallback(new LicenseManager.LicenseActivationCallback() {
//            @Override
//            public void activateSuccess(LicenseStatus licenseStatus) {
//                boolean isValid = licenseStatus.isLicenseValid();
//                if(isValid){
//
//                    SharedPreferenceManager.addString(mKey_LicenseCodes, mLicenseCodeEdit.getText().toString());
//                    finish();
//
//                } else {
//                    ShowMessage.showError(tag, getString(R.string.license_valid), LicenseConfiguration_Activate.this);
//                }
//
//                mProgressDialog.dismiss();
//            }
//
//            @Override
//            public void activateFailed(String s) {
//                ShowMessage.showError(tag, s, LicenseConfiguration_Activate.this);
//                mProgressDialog.dismiss();
//            }
//        });
    }

    ProgressDialog mProgressDialog;

    private void activate(){
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.license_activating));
            mProgressDialog.setCancelable(false);
        }

//        ArrayList<LicenseManager.Module> modules = new ArrayList<>();
//        String code = mLicenseCodeEdit.getText().toString();
//        ArrayList<Integer> checkedPoses = mAdapter.getCheckedPositions();
//        for(int i=0; i<checkedPoses.size(); i++){
//            String md = mAdapter.getItem(checkedPoses.get(i));
//            LicenseManager.Module module = ValueName.getModuleByName(md);
//            if(module != null)
//                modules.add(module);
//        }
//        mLicenseManager.activateDevice(code, modules);

        mProgressDialog.show();

    }
    /**********************************************************************/


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.changeLanguage(newBase));
    }

}
