package com.supermap.imobile.Menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jun.tools.Message.ShowMessage;
import com.jun.tools.filebrower.FilePickActivity;
import com.supermap.data.EngineType;
import com.supermap.imobile.ActivityMain.ActivityRequestCodes;
import com.supermap.imobile.ActivityMain.MainActivity;
import com.supermap.imobile.ActivityMain.MainApplication;
import com.supermap.imobile.R;
import com.supermap.imobile.Resource.DataPath;
import com.supermap.imobile.Worksapce.ValueName;

/**
 * Created by Jun on 2017/8/3.
 */

public class Dialog_Open extends Dialog_Base {

    public Dialog_Open(@NonNull Context context) {
        super(context);
    }

    public Dialog_Open(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutId = R.layout.layout_pop_dialog_open;

        super.onCreate(savedInstanceState);
    }


    private EditText mEdit_Path;
    private EditText mEdit_Type;
    private EditText mEdit_Password;
    private EditText mEdit_Name;
    private EditText mEdit_Version;
    private View     mLayout_Version;

    private PopList mPopList_Type;
    private PopList mPopList_Version;
    private CheckBox mCheck_Pyramid;

    @Override
    protected void initView() {
        super.initView();

        mEdit_Path = (EditText) findViewById(R.id.open_edit_Path);
//        mEdit_Path.setOnClickListener(this);

        mEdit_Type = (EditText) findViewById(R.id.open_edit_Type);
        mEdit_Type.setOnClickListener(this);
        mEdit_Type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String type = s.toString();
                int index = type.indexOf('(');
                if(index > 0){
                    type = type.substring(0, index);
                }
                mEngineType = ValueName.getEngineTypeByName(type);
            }
        });

        mEdit_Password = (EditText) findViewById(R.id.open_edit_Password);

        mEdit_Name = (EditText) findViewById(R.id.open_edit_Name);

        findViewById(R.id.btn_select_path_open).setOnClickListener(this);

        // PopList
        mCheck_Pyramid = (CheckBox) findViewById(R.id.checkbox_Pyramid);
        mPopList_Type = new PopList(mContext);
        mPopList_Type.setOnItemClickedListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                mEdit_Type.setText(mPopList_Type.getItem(position));
                String type = mPopList_Type.getItem(position);
                String path = "";
                if(type.equals("BaiDu")){
                    path = "http://map.baidu.com";
                    mEdit_Path.setText(path);
                    mEdit_Path.setSelection(path.length());
                }else if(type.equals("OpenStreetMaps")){
                    path = "http://openstreetmap.org";
                    mEdit_Path.setText(path);
                    mEdit_Path.setSelection(path.length());
                }else if(type.equals("SuperMapCloud")){
                    path = "http://t2.supermapcloud.com";
                    mEdit_Path.setText(path);
                    mEdit_Path.setSelection(path.length());
                }else if(type.equals("BingMaps")){
                    path = "https://www.microsoft.com/maps";
                    mEdit_Path.setText(path);
                    mEdit_Path.setSelection(path.length());
                }else if(type.equals("GoogleMaps")){
                    path = "https://www.google.cn/maps";
                    mEdit_Path.setText(path);
                    mEdit_Path.setSelection(path.length());
                }else if(type.contains("Rest")){
                    path = "http://support.supermap.com.cn:8090/iserver/services/map-china400/rest/maps/China";
                }else if(type.contains("wmts")){
                    path = "http://t0.tianditu.com/vec_c/wmts";
                }else if(type.contains("wcs")){
                    path = "http://support.supermap.com.cn:8090/iserver/services/data-jingjin/wcs111";
                }else if(type.contains("wms")){
                    path = "http://support.supermap.com.cn:8090/iserver/services/maps/wms130/长春市区图";
                }else if(type.contains("wfs")){
                    path = "http://support.supermap.com.cn:8090/iserver/services/data-world/wfs100/gb18030";
                }

                if(type.contains("ImagePlugins")){
                    mCheck_Pyramid.setVisibility(View.VISIBLE);
                }else {
                    mCheck_Pyramid.setVisibility(View.GONE);
                }

                String oldPath = mEdit_Path.getText().toString();
                if(oldPath.isEmpty()) { // set default path
                    mEdit_Path.setText(path);
                    mEdit_Path.setSelection(path.length());
                }
                mPopList_Type.dismiss();
            }
        });
        for(String type : ValueName.mWorksapceTypeArr){
            mPopList_Type.addItem(type);
        }
        for(String type : ValueName.mEngineTypeArr){
            mPopList_Type.addItem(type);
        }

        mLayout_Version = findViewById(R.id.layout_Workspace_Version);
        mEdit_Version = (EditText) findViewById(R.id.open_edit_Version);
        mEdit_Version.setOnClickListener(this);

        mPopList_Version = new PopList(mContext);
        mPopList_Version.setOnItemClickedListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEdit_Version.setText(mPopList_Version.getItem(position));
                mPopList_Version.dismiss();
            }
        });

        for (String ver : ValueName.mWorksapceVerArr) {
            mPopList_Version.addItem(ver);
        }

        mEdit_Version.setText(mPopList_Version.getItem(0));
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_Confirm:
                String path = mEdit_Path.getText().toString();
                String name = mEdit_Name.getText().toString();
                String password = mEdit_Password.getText().toString();
                String version = mEdit_Version.getText().toString();
                if (mIsWorkspace) {
                    MainActivity.mMainActivity.openWorkspaceInThread(path, password, ValueName.getWorkspaceVersionByName(version));
                } else if (mEngineType != null || mEngineType == null) {
                    MainActivity.mMainActivity.openDatasourceInThread(path, mEngineType, password, name, mCheck_Pyramid.isChecked());
                } else if (path.isEmpty()) {
                    ShowMessage.showInfo(tag, mContext.getString(R.string.enter_select_path_open));
                } else if (!path.isEmpty()) {
                    ShowMessage.showInfo(tag, mContext.getString(R.string.unsupported_type));
                }
                break;
            case R.id.btn_Cancel:
                dismiss();
                break;
            case R.id.open_edit_Path:
            case R.id.btn_select_path_open:
                selectFile();
                break;
            case R.id.open_edit_Type:
                mPopList_Type.setWidth(v.getWidth());
                mPopList_Type.showAsDropDown(v);
                break;
            case R.id.open_edit_Version:
                mPopList_Version.setWidth(v.getWidth());
                mPopList_Version.showAsDropDown(v);
            default:
                break;
        }

    }

    /**
     * To select a file
     */
    private void selectFile() {

        SharedPreferences preferences = mContext.getSharedPreferences(MainApplication.m_SharedPreferenceName, Context.MODE_PRIVATE);
        String lastPath = preferences.getString(MainApplication.mKey_LastPath, DataPath.RootPath);

        if(!lastPath.contains("emulated")){
            lastPath = DataPath.RootPath;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, FilePickActivity.class);
        intent.setType("file");
        intent.setData(Uri.parse("dir://" + lastPath));

        if (mContext instanceof Activity)
            ((Activity) mContext).startActivityForResult(intent, ActivityRequestCodes.SELECT_FILE_CODE_MAIN);
    }

    private boolean mIsWorkspace;
    private EngineType mEngineType;

    @Override
    public void onFileSelected(String path) {
        mEdit_Path.setText(path);
        mEdit_Path.setSelection(path.length());

        checkFileType(path);
        if (mIsWorkspace) {
            mLayout_Version.setVisibility(View.VISIBLE);
        } else {
            mLayout_Version.setVisibility(View.GONE);
        }

    }

    private void checkFileType(String path) {
        mIsWorkspace = false;
        mEngineType = null;
        if (path.startsWith("http:") || path.startsWith("https:")) {

        } else {
            int index = path.lastIndexOf('.');
            if (index > 0) {
                String suffix = path.substring(index + 1);
                String lowerSuffix = suffix.toLowerCase();
                // if it is a workspace file, open it and return.
                for (String type : ValueName.mWorksapceTypeArr) {
                    if (type.equals(lowerSuffix)) {
                        mIsWorkspace = true;
                        mEdit_Type.setText(type);
                        String name = path.substring(path.lastIndexOf("/") + 1, index);
                        mEdit_Name.setText(name);
                        return;
                    }
                }

                // Open a datasource if valid
                if (suffix.compareToIgnoreCase("udb") == 0) {
                    mEngineType = EngineType.UDB;
                } else if (suffix.compareToIgnoreCase("sci") == 0) {
                    mEngineType = EngineType.Rest;
                } else if (path.endsWith("VectorCache.xml")) {
                    mEngineType = EngineType.OpenGLCache;
                }else if (suffix.compareToIgnoreCase("bmp") == 0 ||
                          suffix.compareToIgnoreCase("jpg") == 0 ||
                          suffix.compareToIgnoreCase("png") == 0 ||
                          suffix.compareToIgnoreCase("sci") == 0 ||
                          suffix.compareToIgnoreCase("sit") == 0 ||
                          suffix.compareToIgnoreCase("tif") == 0 ||
                          suffix.compareToIgnoreCase("tiff") == 0 ) { //bmp,jpg,png,sci,sit,tiff,tif
                    mEngineType = EngineType.IMAGEPLUGINS;
                }  else {
                    mEngineType = null;
                }

                String type = ValueName.getEngineTypeName(mEngineType);
                if(suffix.compareToIgnoreCase("sci") == 0){
                    type = type + "(sci)";
                }
                mEdit_Type.setText(type);
                if(type.contains("ImagePlugins")){
                    mCheck_Pyramid.setVisibility(View.VISIBLE);
                }else {
                    mCheck_Pyramid.setVisibility(View.GONE);
                }
                if (mEngineType != null) {
                    String name = path.substring(path.lastIndexOf("/") + 1, index);
                    name = MainActivity.mMainActivity.getValidDatasourceName(name);
                    mEdit_Name.setText(name);
                } else {
                    mEdit_Name.setText("");
                }
            }
        }
    }


}
