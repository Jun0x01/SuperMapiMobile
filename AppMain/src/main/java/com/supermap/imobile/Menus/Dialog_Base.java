package com.supermap.imobile.Menus;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;

import com.supermap.imobile.ActivityMain.MainActivity;
import com.supermap.imobile.R;

/**
 * Created by Jun on 2017/8/7.
 */

public class Dialog_Base extends Dialog implements View.OnClickListener{
    protected String tag;
    public Dialog_Base(@NonNull Context context) {
        this(context, 0);
    }

    public Dialog_Base(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }


    protected Context mContext;
    private void init(Context context){
        mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        tag = getClass().getSimpleName();
        // Remove the default title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }
    @Override
    public void show() {
        super.show();
        MainActivity.mMainActivity.setOnFileSelectedDialog(this);
    }

    @Override
    public void dismiss() {
        MainActivity.mMainActivity.setOnFileSelectedDialog(null);
        super.dismiss();
    }

    protected int mLayoutId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mLayoutId != -1)
            setContentView(mLayoutId);

        initView();
    }

    @Override
    protected void onStop() {
        mContext = null;
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Call before returning from {@link #onCreate(Bundle)}
     */
    protected void initView(){
        View btn1 = findViewById(R.id.btn_Confirm);
        if (btn1 != null)
            btn1.setOnClickListener(this);
        btn1 = findViewById(R.id.btn_Cancel);
        if (btn1 != null)
            btn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Called in Activity.onActivityResult()
     * @param path
     */
    public void onFileSelected(String path){

    }

    public void onConfirmFinished(){
        dismiss();
    }
}
