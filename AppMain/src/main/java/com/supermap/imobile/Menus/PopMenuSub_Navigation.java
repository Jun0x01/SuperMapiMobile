package com.supermap.imobile.Menus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.jun.tools.Message.ShowMessage;
import com.supermap.imobile.ActivityMain.MainActivity;
import com.supermap.imobile.R;

/**
 * Created by Jun on 2017/8/3.
 */

public class PopMenuSub_Navigation extends PopBase {

    public PopMenuSub_Navigation(Context context){
        super(context);
        tag = "MenuSub_Edit";

    }

    @Override
    protected void initView() {
        // Override the layout id
        mLayoutId = R.layout.layout_pop_menu_sub_navigation;
        super.initView();

    }


    private CheckBox mCheckBoxLast;
    @Override
    public void onClick(View v) {
        super.onClick(v);

        CheckBox checkBox = null;
        if(v instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v;
            View view = viewGroup.getChildAt(0);
            if (view != null && view instanceof CheckBox) {
                checkBox = (CheckBox) view;
//                checkBox.setChecked(!checkBox.isChecked());
            }else {
                int count = viewGroup.getChildCount();
                if(count > 1){
                    view = viewGroup.getChildAt(count -1);
                    if (view != null && view instanceof CheckBox) {
                        checkBox = (CheckBox) view;
//                        checkBox.setChecked(!checkBox.isChecked());
                    }
                }
            }
        }
        if(checkBox != null) {
            if(mCheckBoxLast != null && !mCheckBoxLast.equals(checkBox)){
                mCheckBoxLast.setChecked(false);
            }
            checkBox.setChecked(!checkBox.isChecked());
            mCheckBoxLast = checkBox;
        }

        String key = null;
        switch (v.getId()){
            case R.id.btn_menu_sub_navi_tradition:
                if (checkBox != null) {
                    int type = checkBox.isChecked() ? 1 :0;
                    MainActivity.mMainActivity.setNaviType(type);
                }
                break;
            case R.id.btn_menu_sub_navi_industry:
                if (checkBox != null) {
                    int type = checkBox.isChecked() ? 2 :0;
                    MainActivity.mMainActivity.setNaviType(type);
                }
                break;
            case R.id.btn_menu_sub_navi_indoor:
                if (checkBox != null) {
                    int type = checkBox.isChecked() ? 3 :0;
                    MainActivity.mMainActivity.setNaviType(type);
                }
                break;
            case R.id.btn_menu_sub_navigation_settings:
                Dialog_Navigation_Settings naviSetingDialog = new Dialog_Navigation_Settings(mContext);
                naviSetingDialog.setMapControl(MainActivity.mMainActivity.getMapControl());
                naviSetingDialog.show();
                break;
            default:
                break;
        }

        dismiss();
        ShowMessage.showInfo(tag, key);
    }
}
