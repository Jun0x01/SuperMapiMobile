package com.jun.tools.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.jun.tools.FloatingWindow.FloatingWindowBuilder;
import com.jun.tools.R;

/**
 * Created by Jun on 2017/6/21.
 */

public class ActivityFloatingWindow extends Activity implements View.OnTouchListener{

    View mFloatingWindow;
    FloatingWindowBuilder mFloatingWindowBuilder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_floating_window);


        mFloatingWindow = findViewById(R.id.layout_floatingwindow);
        mFloatingWindow.setOnTouchListener(this);

        mFloatingWindowBuilder = new FloatingWindowBuilder(getApplicationContext());

        mFloatingWindowBuilder.createFloatingWindow(R.layout.layout_floating_window);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {


        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        mFloatingWindowBuilder.clear();
        super.onDestroy();
    }
}
