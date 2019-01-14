package com.supermap.imobile.Menus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.EditText;

import com.supermap.imobile.ActivityMain.MainActivity;
import com.supermap.imobile.R;

/**
 * Created by Jun on 2017/8/14.
 */

public class Dialog_SaveAsMap extends Dialog_Base {

    public Dialog_SaveAsMap(@NonNull Context context) {
        super(context);
    }

    public Dialog_SaveAsMap(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutId = R.layout.layout_pop_dialog_save_as_map;

        super.onCreate(savedInstanceState);
    }


    private EditText mEdit_Name;

    @Override
    protected void initView() {
        super.initView();

        mEdit_Name = (EditText) findViewById(R.id.save_as_map_name);
        if (mName != null && mEdit_Name != null)
            mEdit_Name.setText(mName);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_Confirm:
                String name = mEdit_Name.getText().toString();
                boolean isSaved = MainActivity.mMainActivity.saveAsMap(name);
                if(isSaved)
                    dismiss();
                break;
            case R.id.btn_Cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private String mName;
    public void show(String name){
        mName = name;

        show();
    }

}
