package com.jun.tools.DrawerLeft;

import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by XingJun on 2017/3/22.
 */

public class DrawerLeftTop implements View.OnClickListener {

    Context context;
    View mParent;

    /**
     * Constructor
     * @param view container view, which contains view needed in here
     */
    public DrawerLeftTop(View view){

        mParent = view;
        initView(view);
    }

    /********************* Initialize View *************************/
    /**
     * Initialize views
     */
    private void initView(View view){

    }

    /********************* OnClickListener *************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void openSettng(){
        Intent intent = new Intent();
//        intent.setClass(mParent.getContext(), SettingsActivity.class);
        mParent.getContext().startActivity(intent);

    }
    /**********************************************/
}
