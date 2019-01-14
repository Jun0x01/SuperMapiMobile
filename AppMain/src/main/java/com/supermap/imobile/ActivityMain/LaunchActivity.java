package com.supermap.imobile.ActivityMain;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.jun.tools.Message.ShowMessage;
import com.jun.tools.Services.FloatingWindowService;
import com.supermap.data.Environment;
import com.supermap.data.LicenseStatus;
import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;
import com.supermap.imobile.BuildConfig;
import com.supermap.imobile.R;
import com.supermap.imobile.Resource.DataPath;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {

    /**
     * User Permissions
     */
    String[] permissions =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW, // always -1
            };

    private boolean mIsFirstRun;
    private boolean mIsFirstResume = true;
    private long mStartTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
//        setContentView(R.layout.layout_image_drawer);

        mStartTime = System.currentTimeMillis();

        checkEnvironment();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30 * 1000);
                    saveImage();

                } catch (Exception e) {

                }
            }
        });
//        thread.start();
    }

    private void checkEnvironment(){
        mIsFirstRun = SharedPreferenceManager.getBoolean(SharedPreferenceManager.mKey_FirstRun, true);
        if(mIsFirstRun){
            SharedPreferenceManager.setBoolean(SharedPreferenceManager.mKey_FirstRun, false);
        }

        // Check and request some user permissions, since api 23.
        if(checkUserPermissions()){
            if(checkOverlaySettings()){
                startMemoryMonitorService();

                initAll();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void saveImage() {
        ViewGroup parent = (ViewGroup) findViewById(R.id.parent);
        int count = parent.getChildCount();
        View view = null;
        Bitmap bitmap = null;
        String path = DataPath.TestDataDir + "/ICON_1/";
        String tag = null;
        for (int i = 0; i < count; i++) {
            view = parent.getChildAt(i);
            tag = view.getTag().toString().toLowerCase();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            bitmap = view.getDrawingCache();

            saveBitmap(bitmap, path + "/" + tag + ".png");

            view.destroyDrawingCache();
            bitmap.recycle();

        }
        ShowMessage.showInfo("Launch", "Count: " + count, this);
    }

    private void saveBitmap(Bitmap bitmap, String path) {
        File file = new File(path);
        file.getParentFile().mkdirs();

        Bitmap.CompressFormat format = null;
        format = Bitmap.CompressFormat.PNG;

        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            bitmap.compress(format, 100, fout);
            fout.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null)
                    fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Check and request some user permissions, since api 23.
     */
    @TargetApi(23)
    private boolean checkUserPermissions() {
        // Check Android Version
        if (Build.VERSION.SDK_INT >= 23) {
            // Check whether danger permission is granted or not
//            requestPermissions(permissions, ActivityRequestCodes.REQUEST_USER_PERMISSIONS);
            String permission = null;
            int checkCallPhonePermission = 0;
            for (int i = 0; i < permissions.length; i++) {
                permission = permissions[i];
                checkCallPhonePermission = checkSelfPermission(permission);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED)
                    break;
            }

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, ActivityRequestCodes.REQUEST_USER_PERMISSIONS);
                return false;
            } else {
                return true;
            }

//            return false;

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ActivityRequestCodes.REQUEST_USER_PERMISSIONS) {
            if(checkOverlaySettings()){
                startMemoryMonitorService();

                initAll();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityRequestCodes.REQUEST_SYSTEM_SETTING) {
            startMemoryMonitorService();
            initAll();
        }else if(ActivityRequestCodes.REQUEST_LICENSE_CONFIGURATION == requestCode){
            if(Environment.getLicenseStatus().isLicenseValid())
                launchMainActivity();
        }
    }


    private  boolean checkOverlaySettings(){
        if (!BuildConfig.DEBUG || !mIsFirstRun)
            return true;

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ActivityRequestCodes.REQUEST_SYSTEM_SETTING);

                return false;
            }

        }
        return true;
    }
    /**
     * Start a service to monitor system's memory state in debug mode, and show it in floating window.
     */
    private void startMemoryMonitorService(){
        if(!BuildConfig.DEBUG)
            return;

        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)){
                Intent service = new Intent(getApplicationContext(), FloatingWindowService.class);

                MainApplication.getInstance().registerServiceIntent(service);
            }
        }else {

            Intent service = new Intent(getApplicationContext(), FloatingWindowService.class);

            MainApplication.getInstance().registerServiceIntent(service);
        }
    }


    private void initAll(){
        EnvInitializer.init(getApplicationContext());

        if(checkLicense()){
            launchMainActivity();
        }
    }

    private boolean checkLicense(){
        LicenseStatus licenseStatus = Environment.getLicenseStatus();
        if(licenseStatus.isLicenseValid()){
            return true;
        }else {
            Intent intent = new Intent();
            intent.setClass(this, LicenseConfiguration.class);
            startActivityForResult(intent, ActivityRequestCodes.REQUEST_LICENSE_CONFIGURATION);
        }
        return false;
    }
    /**
     * Launch main activity
     */
    private void launchMainActivity() {

        Log.d("Launch", "Time: " + (System.currentTimeMillis() - mStartTime) + " ms");
        boolean test = false;

        if(test)
            return;



        final Class<?> targetClass = MainActivity.class;
        final Intent intent = new Intent();
        intent.setClass(getApplicationContext(), targetClass);

        long delay = 5*1000;
        long curTime = System.currentTimeMillis();
        delay = delay - (curTime - mStartTime);


        delay = 5 * 1000;
        if(delay<=500){
            startActivity(intent);
            finish();
        }else {

            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    startActivity(intent);
                    finish();
                }
            };

            Timer timer = new Timer();
            timer.schedule(task, delay);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.changeLanguage(newBase));
    }

}
