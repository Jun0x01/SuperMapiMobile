package com.supermap.imobile.Menus;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.supermap.imobile.R;

/**
 * Created by Jun on 2017/7/26.
 */

public class PopBase extends PopupWindow implements View.OnClickListener{

    protected String tag;
    protected Context mContext;
    protected LayoutInflater mInflater;
//    protected PopupWindow mPopupWindow;
    protected View mContentView;
    protected int mLayoutId = R.layout.layout_popupwindow;

    public PopBase(Context context) {
        super(context);
        mContext = context;
        tag = getClass().getSimpleName();
        initView();
    }

    /**
     * Initialize the PopupWindow. if this method is override, super must be call before using the view.
     * If need to change the mLayoutId , change it before calling super.initView()
     */
    protected void initView(){
//        mPopupWindow = new PopupWindow(mContext);
        mInflater = LayoutInflater.from(mContext);
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        View view = mInflater.inflate(mLayoutId, null);
        mContentView = view;

        setContentView(mContentView);
        setWidth(width);
        setHeight(height);

        ColorDrawable colorDrawable = new ColorDrawable(0x00000000);

        setBackgroundDrawable(colorDrawable);
        setFocusable(true);
//        setOutsideTouchable(true);
        setTouchable(true);

        View view1 = mContentView.findViewById(R.id.menu_sub_btn_container);
        if(view1 != null && view1 instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view1;

            int count = viewGroup.getChildCount();
            for (int i = 0; i<count; i++){
                viewGroup.getChildAt(i).setOnClickListener(this);
            }
        }

    }

    /************** Settings ******************/
    public void setBackgroundDrawable(Drawable background){
        super.setBackgroundDrawable(background);
    }

    public void setFocusable(boolean focusable){
        super.setFocusable(focusable);
    }

    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
    }

    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        onShow();
    }

    // api >= 19
    public void showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        if (Build.VERSION.SDK_INT >= 19)
            super.showAsDropDown(anchor, xOff, yOff, gravity);
        onShow();
    }

    public void showAsDropDown(View anchor, int xOff, int yOff) {
        super.showAsDropDown(anchor, xOff, yOff);
        onShow();
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        onShow();
    }

    @Override
    public void onClick(View v){

    }

    protected void onShow(){

    }
}
