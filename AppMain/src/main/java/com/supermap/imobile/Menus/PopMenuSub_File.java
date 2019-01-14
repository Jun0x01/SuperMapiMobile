package com.supermap.imobile.Menus;

import android.content.Context;
import android.view.View;

import com.jun.tools.Message.ShowMessage;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.imobile.ActivityMain.MainActivity;
import com.supermap.imobile.DrawerLeftWorkspace.MapState;
import com.supermap.imobile.R;
import com.supermap.imobile.Worksapce.ValueName;

/**
 * Created by Jun on 2017/7/26.
 */

public class PopMenuSub_File extends PopBase {

    public PopMenuSub_File(Context context){
        super(context);
        tag = "MenuSub_File";

    }

    @Override
    protected void initView() {
        // Override the layout id
        mLayoutId = R.layout.layout_pop_menu_sub_file;
        super.initView();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        String key = null;
        switch (v.getId()){
            case R.id.btn_menu_sub_file_New:
                new Dialog_New(mContext).show();
                break;
            case R.id.btn_menu_sub_file_Open:
                Dialog_Open popDialog_open = new Dialog_Open(mContext);
                popDialog_open.show();
                break;

            case R.id.btn_menu_sub_file_Import:
                key = "Import";
                break;
            case R.id.btn_menu_sub_file_Export:
                break;
            case R.id.btn_menu_sub_file_SaveMap:
                if(MapState.isNewMap){
                    // go to next case, save as map
                }else {
                    if(MapState.isMapOpen){
                        MainActivity.mMainActivity.saveMap();
                    }
                    break;
                }
            case R.id.btn_menu_sub_file_SaveAsMap:
                new Dialog_SaveAsMap(mContext).show(MapState.mapName);
                break;
            case R.id.btn_menu_sub_file_CloseMap:
                MainActivity.mMainActivity.closeMap();
                break;
            case R.id.btn_menu_sub_file_SaveWorkspace:
                if(MapState.isWorkspaceOpen){
                    MainActivity.mMainActivity.saveWorkspace();
                    break;
                } else {
                    // go to next case, save as workspace
                }

            case R.id.btn_menu_sub_file_SaveAsWorkspace:
                WorkspaceConnectionInfo info = MainActivity.mMainActivity.getWorkspace().getConnectionInfo();
                String path = info.getServer();
                String name = info.getName();
                String version = ValueName.getWorkspaceVersionName(info.getVersion());
                new Dialog_SaveAsWorkspace(mContext).show(path, name, version);
                break;
            case R.id.btn_menu_sub_file_CloseWorkspace:
                MainActivity.mMainActivity.closeWorkspace();
                break;
            default:
                break;
        }

        dismiss();
        ShowMessage.showInfo(tag, key);
    }
}
