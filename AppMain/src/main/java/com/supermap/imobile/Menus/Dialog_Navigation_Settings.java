package com.supermap.imobile.Menus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.jun.tools.Message.ShowMessage;
import com.jun.tools.filebrower.FilePickActivity;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.Workspace;
import com.supermap.imobile.ActivityMain.ActivityRequestCodes;
import com.supermap.imobile.ActivityMain.MainActivity;
import com.supermap.imobile.ActivityMain.MainApplication;
import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;
import com.supermap.imobile.R;
import com.supermap.imobile.Resource.DataPath;
import com.supermap.mapping.MapControl;

/**
 * Created by Jun on 2017/8/3.
 */

public class Dialog_Navigation_Settings extends Dialog_Base {

    public Dialog_Navigation_Settings(@NonNull Context context) {
        super(context);
    }

    public Dialog_Navigation_Settings(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutId = R.layout.layout_pop_dialog_navigation_setting;

        super.onCreate(savedInstanceState);
    }

    private Workspace mWorkspace;
    private MapControl mMapControl;
    public void setMapControl(MapControl mapControl){
        mMapControl = mapControl;
        if(mapControl != null)
        mWorkspace = mapControl.getMap().getWorkspace();
    }

    private EditText mEdit_Navi_Path;
    private EditText mEdit_Navi2_Path;
    private EditText mEdit_Navi2_Datasource;
    private EditText mEdit_Navi2_Dataset;
    private EditText mEdit_Navi3_Datasource;

    private PopList mPopList_Navi2_Datasource;
    private PopList mPopList_Navi2_Dataset;
    private PopList mPopList_Navi3_Datasource;


    @Override
    protected void initView() {
        super.initView();

        mEdit_Navi_Path = (EditText) findViewById(R.id.navi_data_path);
        mEdit_Navi2_Path = (EditText) findViewById(R.id.navi2_data_path);
        mEdit_Navi_Path.setOnClickListener(this);
        mEdit_Navi2_Path.setOnClickListener(this);

        mEdit_Navi2_Datasource = (EditText) findViewById(R.id.navi2_datasource);
        mEdit_Navi2_Datasource.setOnClickListener(this);
        mEdit_Navi2_Datasource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String name = s.toString();
                int index = name.indexOf('(');
                if(index > 0){
                    name = name.substring(0, index);
                }
                mPopList_Navi2_Dataset.clearItems();
                if(!name.isEmpty() && mWorkspace != null){
                    Datasource dataSource = mWorkspace.getDatasources().get(name);
                    if(dataSource != null){
                        Datasets datasets = dataSource.getDatasets();
                        int count = datasets.getCount();
                        for (int i=0; i<count; i++){
                            mPopList_Navi2_Dataset.addItem(datasets.get(i).getName());
                        }
                    }
                }
            }
        });

        mEdit_Navi2_Dataset = (EditText) findViewById(R.id.navi2_net_dataset);
        mEdit_Navi2_Dataset.setOnClickListener(this);

        mEdit_Navi3_Datasource = (EditText) findViewById(R.id.navi3_datasource);
        mEdit_Navi3_Datasource.setOnClickListener(this);

        // PopList
        mPopList_Navi2_Datasource = new PopList(mContext);
        mPopList_Navi2_Datasource.setOnItemClickedListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mEdit_Navi2_Datasource.setText(mPopList_Navi2_Datasource.getItem(position));
                mPopList_Navi2_Datasource.dismiss();
            }
        });

        mPopList_Navi2_Dataset = new PopList(mContext);
        mPopList_Navi2_Dataset.setOnItemClickedListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEdit_Navi2_Dataset.setText(mPopList_Navi2_Dataset.getItem(position));
                mPopList_Navi2_Dataset.dismiss();
            }
        });

        mPopList_Navi3_Datasource = new PopList(mContext);
        mPopList_Navi3_Datasource.setOnItemClickedListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mEdit_Navi3_Datasource.setText(mPopList_Navi2_Datasource.getItem(position));
                mPopList_Navi3_Datasource.dismiss();
            }
        });
        findViewById(R.id.btn_select_path_open).setOnClickListener(this);
        findViewById(R.id.btn_select_path_open2).setOnClickListener(this);

        if(mWorkspace != null){
            Datasources datasources = mWorkspace.getDatasources();
            int count = datasources.getCount();
            String name = null;
            for (int i = 0; i<count; i++){
                name = datasources.get(i).getAlias();
                mPopList_Navi2_Datasource.addItem(name);
                mPopList_Navi3_Datasource.addItem(name);
            }
        }

        String navi_DataPath_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi_Path);
        String navi2_DataPath_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi2_Path);
        String navi2_Datasource_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi2_Datasource);
        String navi2_Dataset_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi2_Dataset);
        String navi3_Datasource_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi3_Datasource);

        mEdit_Navi_Path.setText(navi_DataPath_Stored);
        mEdit_Navi2_Path.setText(navi2_DataPath_Stored);
        mEdit_Navi2_Datasource.setText(navi2_Datasource_Stored);
        mEdit_Navi2_Dataset.setText(navi2_Dataset_Stored);
        mEdit_Navi3_Datasource.setText(navi3_Datasource_Stored);
    }

    int mNaviType = 0; // 1, 2, 3;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        mNaviType = 0;
        switch (v.getId()) {
            case R.id.btn_Confirm:
                onConfirm();
                break;
            case R.id.btn_Cancel:
                dismiss();
                break;
            case R.id.btn_select_path_open:
            case R.id.navi_data_path:
                mNaviType = 1;
                selectFile("dir");
                break;
            case R.id.btn_select_path_open2:
            case R.id.navi2_data_path:
                mNaviType = 2;
                selectFile("file");
                break;
            case R.id.navi2_datasource:
                mPopList_Navi2_Datasource.setWidth(v.getWidth());
                mPopList_Navi2_Datasource.showAsDropDown(v);
                break;
            case R.id.navi2_net_dataset:
                mPopList_Navi2_Dataset.setWidth(v.getWidth());
                mPopList_Navi2_Dataset.showAsDropDown(v);
                break;
            case R.id.navi3_datasource:
                mPopList_Navi3_Datasource.setWidth(v.getWidth());
                mPopList_Navi3_Datasource.showAsDropDown(v);
                break;
            default:
                break;
        }

    }

    /**
     * To select a file
     */
    private void selectFile(String type) {

        SharedPreferences preferences = mContext.getSharedPreferences(MainApplication.m_SharedPreferenceName, Context.MODE_PRIVATE);
        String lastPath = preferences.getString(MainApplication.mKey_LastPath, DataPath.RootPath);

        if(!lastPath.contains("emulated")){
            lastPath = DataPath.RootPath;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, FilePickActivity.class);
        intent.setType("dir");
        intent.setData(Uri.parse("dir://" + lastPath));

        if (mContext instanceof Activity)
            ((Activity) mContext).startActivityForResult(intent, ActivityRequestCodes.SELECT_FILE_CODE_MAIN);
    }


    @Override
    public void onFileSelected(String path) {

        if(mNaviType == 1){
            mEdit_Navi_Path.setText(path);
            mEdit_Navi_Path.setSelection(path.length());
        }else if(mNaviType == 2){
            mEdit_Navi2_Path.setText(path);
            mEdit_Navi2_Path.setSelection(path.length());
        }

    }

    boolean mIsLoadedModel;
    boolean mIsConnected;
    boolean mIsNavi3Set;

    private void onConfirm(){
        if(mMapControl != null) {

            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getString(R.string.navigation_data_setting));
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (mIsLoadedModel&&mIsConnected&&mIsNavi3Set) {
//                        dismiss();
                    }
                    dismiss();
                    ShowMessage.showInfo("NaviSettings", "Navigation connect: " + mIsConnected + ", Navigation2 load: " + mIsLoadedModel + ", Navigation3 datasource: " + mIsNavi3Set);

                }
            });
            dialog.show();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    confirm0();

                    dialog.dismiss();
                }
            });
            thread.start();

        }else {
            ShowMessage.showInfo("NaviSettings", "No target to set the navi params.");
        }

    }

    private void confirm0() {
        String navi_DataPath = mEdit_Navi_Path.getText().toString();
        String navi2_DataPath = mEdit_Navi2_Path.getText().toString();
        String navi2_Datasource = mEdit_Navi2_Datasource.getText().toString();
        String navi2_Dataset = mEdit_Navi2_Dataset.getText().toString();
        String navi3_Datasource = mEdit_Navi3_Datasource.getText().toString();

        String navi_DataPath_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi_Path);
        String navi2_DataPath_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi2_Path);
        String navi2_Datasource_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi2_Datasource);
        String navi2_Dataset_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi2_Dataset);
        String navi3_Datasource_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi3_Datasource);

        // if changed

        mIsConnected = false;
        mIsLoadedModel = false;
        mIsNavi3Set = false;
        //Navigation3
        Datasource datasource3 = mWorkspace.getDatasources().get(navi3_Datasource);

        if(!navi3_Datasource.isEmpty()){
            if (datasource3 != null) {
                mMapControl.getNavigation3().setDatasource(datasource3);
                mIsNavi3Set = true;
            }else {
                mIsNavi3Set = false;
               Log.e("Navi", "Navigation3: datasource is null");
            }
            MainActivity.mMainActivity.updateNavi3_DataState(mIsNavi3Set);
        }else {
            mIsNavi3Set = true;
        }

        if (!navi3_Datasource.equals(navi3_Datasource_Stored))
            SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi3_Datasource, navi3_Datasource);


        if(!navi2_Datasource.equals(navi2_Datasource_Stored))
            SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi2_Datasource, navi2_Datasource);
        if(!navi2_Dataset.equals(navi2_Dataset_Stored))
            SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi2_Dataset, navi2_Dataset);

        // Navigation
        if (!navi_DataPath.isEmpty()) {
            mIsConnected = mMapControl.getNavigation().connectNaviData(navi_DataPath);
            MainActivity.mMainActivity.updateNavi_DataState(mIsConnected);
        } else {
            MainActivity.mMainActivity.updateNavi_DataState(false);
            mIsConnected = true;
        }

        // Navigation2
        Datasource datasource2 = mWorkspace.getDatasources().get(navi2_Datasource);
        if(datasource2 != null) {
            Dataset dataset = datasource2.getDatasets().get(navi2_Dataset);
            mMapControl.getNavigation2().setNetworkDataset((DatasetVector) dataset);
        }
        // If not setNetWorkDataset, loadModel() will return false;
        if (!navi2_DataPath.isEmpty()) {
            mIsLoadedModel = mMapControl.getNavigation2().loadModel(navi2_DataPath);
            MainActivity.mMainActivity.updateNavi2_DataState(mIsLoadedModel);
        } else {
            MainActivity.mMainActivity.updateNavi2_DataState(false);
            mIsLoadedModel = true;
        }

        if (mIsLoadedModel && !navi_DataPath.equals(navi_DataPath_Stored)) {
            SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi2_Path, navi2_DataPath);
        }
        if (mIsConnected && !navi2_DataPath.equals(navi2_DataPath_Stored))
            SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi_Path, navi_DataPath);
    }


}
