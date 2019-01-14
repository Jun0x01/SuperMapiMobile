package com.supermap.imobile.DrawerLeftWorkspace;

import android.content.Context;
import android.view.View;

import com.supermap.imobile.R;

/**
 * Created by XingJun on 2017/3/22.
 */

public class DrawerTop implements View.OnClickListener {

    Context context;
    View mParent;

    /**
     * Constructor
     * @param view container view, which contains view needed in here
     */
    public DrawerTop(View view){

        mParent = view;
        initView(view);
    }

    /********************* Initialize View *************************/
    /**
     * Initialize views
     */
    private void initView(View view){

        view.findViewById(R.id.imgBtn_Setting).setOnClickListener(this);
    }

    /********************* OnClickListener *************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_Setting:
                break;
        }
    }

    /**********************************************/
}
