package com.supermap.imobile.Menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.jun.tools.Message.ShowMessage;
import com.jun.tools.filebrower.FilePickActivity;
import com.supermap.imobile.ActivityMain.ActivityRequestCodes;
import com.supermap.imobile.ActivityMain.MainActivity;
import com.supermap.imobile.ActivityMain.MainApplication;
import com.supermap.imobile.R;
import com.supermap.imobile.Resource.DataPath;
import com.supermap.imobile.Worksapce.ValueName;

/**
 * Created by Jun on 2017/8/14.
 */

public class Dialog_SaveAsWorkspace extends Dialog_Base {

    public Dialog_SaveAsWorkspace(@NonNull Context context) {
        super(context);
    }

    public Dialog_SaveAsWorkspace(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutId = R.layout.layout_pop_dialog_save_as_workspace;

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

    @Override
    protected void initView() {
        super.initView();

        mEdit_Path = (EditText) findViewById(R.id.new_edit_Path);

        mEdit_Type = (EditText) findViewById(R.id.new_edit_Type);
        mEdit_Type.setOnClickListener(this);

        mEdit_Password = (EditText) findViewById(R.id.new_edit_Password);

        mEdit_Name = (EditText) findViewById(R.id.new_edit_Name);

        findViewById(R.id.btn_select_path_new).setOnClickListener(this);

        // PopList
        mPopList_Type = new PopList(mContext);
        mPopList_Type.setOnItemClickedListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEdit_Type.setText(mPopList_Type.getItem(position));
                mPopList_Type.dismiss();
            }
        });

        mPopList_Type.addItem(".smwu");
        mPopList_Type.addItem(".sxwu");
        mEdit_Type.setText(mPopList_Type.getItem(0));

        mLayout_Version = findViewById(R.id.layout_Workspace_Version);
        mEdit_Version = (EditText) findViewById(R.id.new_edit_Version);
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


        if(mOldVersion != null &&(mOldVersion.equals(ValueName.mWorksapceVerArr[0]) || mOldVersion.equals(ValueName.mWorksapceVerArr[1]))){
            mEdit_Version.setText(mOldVersion);
        }
        if(mOldPath != null && !mOldPath.isEmpty()){
            int index1 = mOldPath.lastIndexOf('/');
            int index2 = mOldPath.lastIndexOf('.');
            String dir = mOldPath.substring(0, index1);
            String type = mOldPath.substring(index2);
            String name1 = mOldPath.substring(index1 + 1, index2);

            mEdit_Type.setText(type);
            mEdit_Path.setText(dir);
            mEdit_Name.setText(name1);
        }
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
                String type = mEdit_Type.getText().toString();
                if(type.isEmpty()){
                    ShowMessage.showInfo(tag, mContext.getString(R.string.file_type_choose));
                }else if (path.isEmpty()) {
                    ShowMessage.showInfo(tag, mContext.getString(R.string.dir_input_for_storing));
                }else {
                    boolean isSaved = MainActivity.mMainActivity.saveAsWorkspace(path + "/" + name + type, name, password, ValueName.getWorkspaceVersionByName(version));
                    if(isSaved)
                        dismiss();
                }
                break;
            case R.id.btn_Cancel:
                dismiss();
                break;
            case R.id.btn_select_path_new:
                selectDirectory();
                break;
            case R.id.new_edit_Type:
                mPopList_Type.setWidth(v.getWidth());
                mPopList_Type.showAsDropDown(v);
                break;
            case R.id.new_edit_Version:
                mPopList_Version.setWidth(v.getWidth());
                mPopList_Version.showAsDropDown(v);
            default:
                break;
        }

    }

    /**
     * To select a file
     */
    private void selectDirectory() {

        SharedPreferences preferences = mContext.getSharedPreferences(MainApplication.m_SharedPreferenceName, Context.MODE_PRIVATE);
        String lastPath = preferences.getString(MainApplication.mKey_LastPath, DataPath.RootPath);

        Intent intent = new Intent();
        intent.setClass(mContext, FilePickActivity.class);
        intent.setType("dir");
        intent.setData(Uri.parse("dir://" + lastPath));

        if (mContext instanceof Activity)
            ((Activity) mContext).startActivityForResult(intent, ActivityRequestCodes.SELECT_FILE_CODE_MAIN);
    }

    @Override
    public void onFileSelected(String path) {
        mEdit_Path.setText(path);
        mEdit_Path.setSelection(path.length());
    }


    private String mOldPath;
    private String mName;
    private String mOldVersion;
    public void show(String oldPath, String name, String oldVersion){
        mOldPath = oldPath;
        mName = name;
        mOldVersion = oldVersion;

        show();
    }
}
