package com.jun.tools.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Jun on 2017/4/26.
 */

public class JunViewPager extends ViewPager {

    final String tag = "JunViewPager";
    public JunViewPager(Context context) {
        super(context);
    }

    public JunViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
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
