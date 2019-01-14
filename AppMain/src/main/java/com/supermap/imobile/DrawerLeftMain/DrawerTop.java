package com.supermap.imobile.DrawerLeftMain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.supermap.imobile.ActivityMain.ActivityRequestCodes;
import com.supermap.imobile.ActivitySetting.SettingsActivity;
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
        view.findViewById(R.id.imgBtn_User).setOnClickListener(this);
    }

    /********************* OnClickListener *************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_Setting:
                openSettng();
                break;
            case R.id.imgBtn_User:
//                login();
                break;
        }
    }

    private void login() {
        Intent intent = new Intent();
        Context context = mParent.getContext();
//        intent.setClass(context, LoginActivity.class);
        if(context instanceof Activity){
            ((Activity)context).startActivityForResult(intent, ActivityRequestCodes.REQUEST_LOGIN);
        }else {
            context.startActivity(intent);
        }

    }

    private void openSettng(){
        Intent intent = new Intent();
        intent.setClass(mParent.getContext(), SettingsActivity.class);
        mParent.getContext().startActivity(intent);

    }
    /**********************************************/
}
