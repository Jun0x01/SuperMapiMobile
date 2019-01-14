package com.jun.tools.View;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Jun on 2017/4/26.
 */

public class JunDrawerLayout extends DrawerLayout {

    final String tag = "JunDrawerLayout";
    public JunDrawerLayout(Context context) {
        super(context);
    }

    public JunDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JunDrawerLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        Log.d(tag, "onTouchEvent: " + result);
        return result;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        Log.d(tag, "onTouchEvent: " + result);
        return result;
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean intercepted = false;
        intercepted = super.onInterceptTouchEvent(ev);
        Log.d(tag, "Intercepted: " + intercepted);
        return intercepted;
    }

}
