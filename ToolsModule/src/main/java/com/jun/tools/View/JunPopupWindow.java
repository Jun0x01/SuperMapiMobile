package com.jun.tools.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.jun.tools.R;

/**
 * PopupWindow interaction:
 * "exit" means exit from current Activity , "close" means close current PopupWindow,
 * "open" means current PopupWindow keeps open.
 * PopupWindow's default touchable is true.
 * -----------------------------------------------------------------------------------------------
 * | setBackground |    set    | setOutside |  click |  click  | dispatchTouch  | TouchListener  |
 * |   Drawable    | Focusable | Touchable  |  Back  | Outside |(inside/outside)|(inside/outside)|
 * -----------------------------------------------------------------------------------------------
 * |   not null    |    true   |    true   | | close |  close  |  false/false   |  false/false   |
 * -----------------------------------------------------------------------------------------------
 * |   not null    |    true   |    false  | | close |  close  |  false/false   |  false/false   |
 * -----------------------------------------------------------------------------------------------
 * |   not null    |    false  |    true   | |  exit |  close  |  false/true    |  false/true    |
 * -----------------------------------------------------------------------------------------------
 * |   not null    |    false  |    false  | |  exit |  open   |  false/true    |  true/false    |
 * -----------------------------------------------------------------------------------------------
 * |     null      |    true   |    true   | |  open |  open   |  false/false   |  true/true     |
 * -----------------------------------------------------------------------------------------------
 * |     null      |    true   |    false  | |  open |  open   |  false/false   |  true/true     |
 * -----------------------------------------------------------------------------------------------
 * |     null      |    false  |    true   | |  exit |  open   |  false/true    |  true/false    |
 * -----------------------------------------------------------------------------------------------
 * |     null      |    false  |    false  | |  exit |  open   |  false/true    |  true/false    |
 * ----------------------------------------------------------------------------------------------
 *
 *
 * Created by Jun on 2017/5/3.
 */

public class JunPopupWindow extends PopupWindow {

    private String tag = "JunPopupWindow";
    private LayoutInflater mInflater;
    public JunPopupWindow(Context context) {
        super(context);

        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.layout_popupwindow, null);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private View mContentView;

    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);

        mContentView = contentView;
        initView();
    }

    /************************************/
    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);

    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        addListener();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        addListener();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        addListener();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);

        addListener();
    }

    private void addListener() {
        if(mTouchListener == null){
            mTouchListener = new TouchListner();
            View parentView = mContentView;
            while (parentView.getParent() != null && parentView.getParent() instanceof View){
                parentView = (View) parentView.getParent();
            }

            parentView.setOnTouchListener(mTouchListener);
        }

        Log.d(tag, "Background: " + (getBackground() != null) +", Touchable: " + isTouchable() + ", Focusable: " + isFocusable() + ", OutSideTouchable: " + isOutsideTouchable());
    }


    private void initView(){
    }
    private TouchListner mTouchListener;

    private class TouchListner implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d(tag, "Action: " + event.getAction() + ", Touch: " + event.getX() + ", " + event.getY());
            return false;
        }
    }

}
