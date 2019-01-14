package com.supermap.imobile.Menus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.jun.tools.Message.ShowMessage;
import com.supermap.imobile.ActivityMain.MainActivity;
import com.supermap.imobile.R;
import com.supermap.mapping.Layers;


/**
 * Created by Jun on 2017/8/3.
 */

public class PopMenuSub_Tools extends PopBase {

    public PopMenuSub_Tools(Context context){
        super(context);
        tag = "MenuSub_Edit";

    }

    @Override
    protected void initView() {
        // Override the layout id
        mLayoutId = R.layout.layout_pop_menu_sub_tools;
        super.initView();

    }

    @Override
    protected void onShow() {
        super.onShow();

        View v = mContentView.findViewById(R.id.btn_menu_sub_tools_MeasureLength);
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
            View view = MainActivity.mMainActivity.findViewById(R.id.layout_measure_length);
            if (view != null)
                checkBox.setChecked(view.getVisibility() == View.VISIBLE ? true : false);
        }
        v = mContentView.findViewById(R.id.btn_menu_sub_tools_MeasureArea);
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
            View view = MainActivity.mMainActivity.findViewById(R.id.layout_measure_area);
            if (view != null)
                checkBox.setChecked(view.getVisibility() == View.VISIBLE ? true : false);
        }

        v = mContentView.findViewById(R.id.btn_menu_sub_tools_MeasureAngle);
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
            View view = MainActivity.mMainActivity.findViewById(R.id.layout_measure_angle);
            if (view != null)
                checkBox.setChecked(view.getVisibility() == View.VISIBLE ? true : false);
        }

        v = mContentView.findViewById(R.id.btn_menu_sub_tools_Swipe);
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

        boolean isSwipe = false; //MainActivity.mMainActivity.getMapControl().getAction().equals(Action.SWIPE);

        if (checkBox != null) {
            checkBox.setChecked(isSwipe);
        }

        v = mContentView.findViewById(R.id.btn_menu_sub_tools_LayerSettings);
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
            if (isSwipe)
                checkBox.setChecked(isSwipe);
        }
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
            case R.id.btn_menu_sub_tools_save_refresh_time_log:
                if (checkBox != null) {
                    MainActivity.mMainActivity.enableSaveRefreshLog(checkBox.isChecked());
                }
                break;
            case R.id.btn_menu_sub_tools_add_call_out:
                if (checkBox != null) {
                    MainActivity.mMainActivity.enableMarker(checkBox.isChecked());
                }
                break;
            case R.id.btn_menu_sub_tools_other_functions:
//                Intent intent = new Intent();
//                intent.setClass(mContext, FunctionsControlActivity.class);
//                mContext.startActivity(intent);

                break;
            case R.id.btn_menu_sub_tools_MeasureLength:
                if (checkBox != null && checkBox.isChecked()) {
                    MainActivity.mMainActivity.measure(1);
                }else {
                    MainActivity.mMainActivity.measure(0);
                }
                break;
            case R.id.btn_menu_sub_tools_MeasureArea:
                if (checkBox != null && checkBox.isChecked()) {
                    MainActivity.mMainActivity.measure(2);
                }else {
                    MainActivity.mMainActivity.measure(0);
                }
                break;
            case R.id.btn_menu_sub_tools_MeasureAngle:
                if (checkBox != null && checkBox.isChecked()) {
                    MainActivity.mMainActivity.measure(3);
                }else {
                    MainActivity.mMainActivity.measure(0);
                }
                break;
            case R.id.btn_menu_sub_tools_Magnifier:
                if(checkBox != null)
                    MainActivity.mMainActivity.getMapControl().setMagnifierEnabled(checkBox.isChecked());
                break;
            case R.id.btn_menu_sub_tools_Swipe:
                if(checkBox != null) {
//                    MainActivity.mMainActivity.getMapControl().setAction(checkBox.isChecked() ? Action.SWIPE : Action.PAN);
                }
                break;
            case R.id.btn_menu_sub_tools_LayerSettings:
                Layers layers = MainActivity.mMainActivity.getMapControl().getMap().getLayers();
                for(int i=0; i<layers.getCount()/2; i++)
                layers.remove(0);
                MainActivity.mMainActivity.getMapControl().getMap().refresh();
                break;
            default:
                break;
        }

        dismiss();
        ShowMessage.showInfo(tag, key);
    }
}
