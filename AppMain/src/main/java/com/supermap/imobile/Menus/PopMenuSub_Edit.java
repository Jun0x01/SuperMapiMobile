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

public class PopMenuSub_Edit extends PopBase {

    public PopMenuSub_Edit(Context context){
        super(context);
        tag = "MenuSub_Edit";

    }

    @Override
    protected void initView() {
        // Override the layout id
        mLayoutId = R.layout.layout_pop_menu_sub_edit;
        super.initView();
    }

    @Override
    protected void onShow() {
        super.onShow();
        View v = mContentView.findViewById(R.id.btn_menu_sub_edit_mapview_gl_visible);
        CheckBox checkBox = null;
        if(v != null && v instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v;
            View view = viewGroup.getChildAt(0);
            if (view != null && view instanceof CheckBox) {
                checkBox = (CheckBox) view;
            }else {
                int count = viewGroup.getChildCount();
                if(count > 1){
                    view = viewGroup.getChildAt(count -1);
                    if (view != null && view instanceof CheckBox) {
                        checkBox = (CheckBox) view;
                    }
                }
            }
        }

        if (checkBox != null) {
            View view = MainActivity.mMainActivity.findViewById(R.id.view_MapView_Background);
            if (view != null)
                checkBox.setChecked(view.getVisibility() == View.VISIBLE ? true : false);
        }

        v = mContentView.findViewById(R.id.btn_menu_sub_edit_mapview_visible);
        checkBox = null;
        if(v != null && v instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v;
            View view = viewGroup.getChildAt(0);
            if (view != null && view instanceof CheckBox) {
                checkBox = (CheckBox) view;
            }else {
                int count = viewGroup.getChildCount();
                if(count > 1){
                    view = viewGroup.getChildAt(count -1);
                    if (view != null && view instanceof CheckBox) {
                        checkBox = (CheckBox) view;
                    }
                }
            }
        }

        if (checkBox != null) {
            View view = MainActivity.mMainActivity.findViewById(R.id.view_MapView);
            if (view != null)
                checkBox.setChecked(view.getVisibility() == View.VISIBLE ? true : false);
        }

        v = mContentView.findViewById(R.id.btn_menu_sub_edit_EditGeo);
        checkBox = null;
        if(v != null && v instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v;
            View view = viewGroup.getChildAt(0);
            if (view != null && view instanceof CheckBox) {
                checkBox = (CheckBox) view;
            }else {
                int count = viewGroup.getChildCount();
                if(count > 1){
                    view = viewGroup.getChildAt(count -1);
                    if (view != null && view instanceof CheckBox) {
                        checkBox = (CheckBox) view;
                    }
                }
            }
        }

        if (checkBox != null) {
            View view = MainActivity.mMainActivity.findViewById(R.id.layout_edit_tools);
            if (view != null)
                checkBox.setChecked(view.getVisibility() == View.VISIBLE ? true : false);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        CheckBox checkBox = null;
        if(v instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v;
            View view = viewGroup.getChildAt(0);
            if (view != null && view instanceof CheckBox) {
                checkBox = (CheckBox) view;
                checkBox.setChecked(!checkBox.isChecked());
            }else {
                int count = viewGroup.getChildCount();
                if(count > 1){
                    view = viewGroup.getChildAt(count -1);
                    if (view != null && view instanceof CheckBox) {
                        checkBox = (CheckBox) view;
                        checkBox.setChecked(!checkBox.isChecked());
                    }
                }
            }
        }

        String key = null;
        switch (v.getId()){
            case R.id.btn_menu_sub_edit_EditGeo:
                if (checkBox != null)
                    MainActivity.mMainActivity.enableEditTools(checkBox.isChecked());
                break;
            case R.id.btn_menu_sub_edit_Snap:
                new Dialog_SnapSetting(mContext).show(MainActivity.mMainActivity.getMapControl(), MainActivity.mMainActivity.getMapControl().getSnapSetting());
                break;
            case R.id.btn_menu_sub_edit_AddPlotLibrary:
                break;
            case R.id.btn_menu_sub_edit_RemovePlotLibrary:

                break;
            case R.id.btn_menu_sub_edit_mapview_visible:
                if(checkBox != null){
                    MainActivity.mMainActivity.enableMapViewGL(checkBox.isChecked());
                    View view = MainActivity.mMainActivity.findViewById(R.id.view_MapView);
                    if (view != null)
                        view.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);
                }
                break;
            case R.id.btn_menu_sub_edit_mapview_gl:
                if(checkBox != null){
                    MainActivity.mMainActivity.enableMapViewGL(checkBox.isChecked());
                    View view = MainActivity.mMainActivity.findViewById(R.id.layout_layerListGL);
                    if (view != null)
                        view.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);
                }
                break;
            case R.id.btn_menu_sub_edit_mapview_gl_visible:
                if(checkBox != null){
                    View view = MainActivity.mMainActivity.findViewById(R.id.view_MapView_Background);
                    if (view != null)
                        view.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);
                    view = MainActivity.mMainActivity.findViewById(R.id.layout_layerListGL);
                    if (view != null)
                        view.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);
                }
                break;
            default:
                break;
        }

        dismiss();
        ShowMessage.showInfo(tag, key);
    }
}
