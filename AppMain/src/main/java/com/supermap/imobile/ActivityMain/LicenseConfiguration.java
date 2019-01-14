package com.supermap.imobile.ActivityMain;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.jun.tools.Message.ShowMessage;
import com.jun.tools.filebrower.FilePickActivity;
import com.supermap.data.Environment;
import com.supermap.data.LicenseStatus;
import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;
import com.supermap.imobile.R;

/**
 * Created by Jun on 2017/11/1.
 */

public class LicenseConfiguration extends Activity implements View.OnClickListener {

    String tag = "LicenseConfiguration";
    final int mRequest_Code_Local = 1;
    final int mRequest_Code_Activate = 2;
    final int mRequest_Code_Cloud = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_layout_license_configuration);
    }

    @Override
    public void onClick(View v) {

//        ShowMessage.showInfo(tag, "id: " + v.getId(), this);
        switch (v.getId()) {
            case R.id.licence_local:{
                Intent intent = new Intent(this, LicenseConfiguration_Local.class);
                startActivityForResult(intent, mRequest_Code_Local);
            }
                break;

            case R.id.licence_activate: {
                Intent intent = new Intent(this, LicenseConfiguration_Activate.class);
                startActivityForResult(intent, mRequest_Code_Activate);
            }
                break;

            case R.id.licence_cloud:
                Dialog dialog = new Dialog_Login(this);
                dialog.setOnDismissListener(mOnDialogDismiss);
                dialog.show();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case mRequest_Code_Activate:
            case mRequest_Code_Cloud:
            case mRequest_Code_Local:
                if(checkLicense()){
                    ShowMessage.showInfo(tag, getString(R.string.license_valid));
                    finish();
                }else {
                    ShowMessage.showInfo(tag, getString(R.string.license_invalid), this);
                }
                break;
            default:
                break;
        }


    }

    private DialogInterface.OnDismissListener mOnDialogDismiss = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if(checkLicense()){
                ShowMessage.showInfo(tag, getString(R.string.license_valid));
                finish();
            }else {
                ShowMessage.showInfo(tag, getString(R.string.license_invalid), LicenseConfiguration.this);
            }
        }
    };

    /***********************************************/

    private void selectPath() {
        // intent.setType(“image/*”);
        // intent.setType(“audio/*”);
        // intent.setType(“video/*”);
        // intent.setType(“video/*;image/*”);=
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*"); // all
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, mRequest_Code_Local);

        Intent intent = new Intent();
        intent.setClass(this, FilePickActivity.class);
        intent.setType("dir");
        intent.setData(Uri.parse("dir://" + SharedPreferenceManager.getString(SharedPreferenceManager.mKey_LicensePath)));
        startActivityForResult(intent, mRequest_Code_Local);
    }

    private void getPath(int resultCode, Intent data) {

        if (RESULT_OK == resultCode) {
            LicenseStatus licenseStatus = Environment.getLicenseStatus();
            boolean isValid = licenseStatus.isLicenseValid();

            String dir = data.getData().getPath();
            Environment.setLicensePath(dir);

        }
    }

    private boolean checkLicense(){
        return Environment.getLicenseStatus().isLicenseValid();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.changeLanguage(newBase));
    }
}
