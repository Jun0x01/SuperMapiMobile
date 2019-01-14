package com.supermap.imobile.Menus;

import android.content.Context;
import android.view.View;

import com.supermap.imobile.DrawerLeftWorkspace.UpdateFragmentWorkspaceListener;
import com.supermap.imobile.R;

/**
 * Created by Jun on 2017/7/26.
 */

public class MenuBar implements View.OnClickListener {

    private Context mContext;
    private View mRootView;



    /**
     *
     * @param context  activity's context
     * @param rootView the menu bar's root view
     */
    public MenuBar(Context context, View rootView){
        mContext = context;
        mRootView = rootView;

        initView();
    }
    PopMenuSub_File mMenuSub_File;
    PopMenuSub_Edit mMenuSub_Edit;
    PopMenuSub_Navigation mMenuSub_Navigation;
    PopMenuSub_Tools mMenuSub_Tools;

    private void initView(){

        mMenuSub_File = new PopMenuSub_File(mContext);
        mMenuSub_Edit = new PopMenuSub_Edit(mContext);
        mMenuSub_Navigation = new PopMenuSub_Navigation(mContext);
        mMenuSub_Tools = new PopMenuSub_Tools(mContext);

        mRootView.findViewById(R.id.text_File).setOnClickListener(this);
        mRootView.findViewById(R.id.text_Edit).setOnClickListener(this);
        mRootView.findViewById(R.id.text_Navigation).setOnClickListener(this);
        mRootView.findViewById(R.id.text_Tools).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.text_File:
                mMenuSub_File.showAsDropDown(v);
                break;

            case R.id.text_Edit:
                mMenuSub_Edit.showAsDropDown(v);
                break;
            case R.id.text_Navigation:
                mMenuSub_Navigation.showAsDropDown(v);
                break;
            case R.id.text_Tools:
                mMenuSub_Tools.showAsDropDown(v);
                break;
            default:
//                mMenuSub_File.onClick(v);
                break;
        }
    }

    private UpdateFragmentWorkspaceListener mUpdateFragmentWorkspaceListener;

    public void setUpdateFragmentWorkspaceListener(UpdateFragmentWorkspaceListener listener){
        if(listener != null && listener != mUpdateFragmentWorkspaceListener){
            mUpdateFragmentWorkspaceListener = listener;

        }
    }
}
