package com.jun.tools.FloatingWindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by Jun on 2017/6/21.
 */

public class FloatingView extends RelativeLayout {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    public FloatingView(Context context) {
        this(context, null);
        resetWindowLayoutParams();
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        resetWindowLayoutParams();
    }


    boolean mIsActionDown = false;
    boolean mIsActionMoved = false;
    boolean mIsActionUp = false;
    float mXDown = 0;
    float mYDown = 0;
    float mXMove = 0;
    float mYMove = 0;
    float mXUp = 0;
    float mYUp = 0;
    float mXLast = 0;
    float mYLast = 0;
    float mXDelta = 0;
    float mYDelta = 0;
    int mXPrePos = 0;
    int mYPrePos = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xInView = event.getX(); // relative to parent
        float yInView = event.getY();
        float xInScreen = event.getRawX(); // relative to screen
        float yInScreen = event.getRawY(); // include status bar's height

        int left = getLeft();
        int top = getTop();
        int right = getRight();
        int bot = getBottom();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mXDown = xInScreen;
                mYDown = yInScreen;
                mXLast = mXDown;
                mYLast = mYDown;
                mXPrePos = mParams.x;
                mYPrePos = mParams.y;
                break;
            case MotionEvent.ACTION_MOVE:
                mIsActionMoved = true;
                mXMove = xInScreen;
                mYMove = yInScreen;
                mXDelta = mXMove - mXLast;
                mYDelta = mYMove - mYLast;
                mXLast = mXMove;
                mYLast = mYMove;

                mParams.x += mXDelta;
                mParams.y += mYDelta;
                mParams.x = (int) (mXPrePos + (mXMove - mXDown));
                mParams.y = (int) (mYPrePos + (mYMove - mYDown));
                updateLayout();
                break;
            case MotionEvent.ACTION_UP:
                mIsActionMoved = false;
                mXUp = xInScreen;
                mYUp = yInScreen;
                mXDelta = mXUp - mXLast;
                mYDelta = mYUp - mYLast;
                mXLast = mXUp;
                mYLast = mYUp;
                if (mIsActionMoved) {
                    mParams.x += mXDelta;
                    mParams.y += mYDelta;
                    mParams.x = (int) (mXPrePos + (mXUp - mXDown));
                    mParams.y = (int) (mYPrePos + (mYUp - mYDown));
                    return updateLayout();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }

        Log.d("Move", "Action: " + event.getAction() +
                " InViewX: " + xInView + " InViewY: " + yInView +
                " InScreenX: " + xInScreen + " InScreenY: " + yInScreen +
                " Left: " + left + " Top: " + top + " Right: " +right + " Bot: " + bot +
                " PreX: " + mXPrePos + " PreY: " + mYPrePos +
                " DownX: " + mXDown + " DownY: " + mYDown +
                " MoveX: " + mXMove + " MoveY: " + mYMove +
                " UpX: " + mXUp + " UpY: " + mYUp +
                " DeltaX: " + mXDelta + " DeltaY: " + mYDelta);

        return super.onTouchEvent(event);

    }


    /************************************************************/

    /**
     * Update the layout when LayoutParams is changed.
     */
    private boolean updateLayout(){
        if(mWindowManager != null && mParams != null){
            mWindowManager.updateViewLayout(this, mParams);
            return true;
        }else {
            return false;
        }
    }


    /**
     * Rest the LayoutParams which used in the WindowManager by this view.
     */
    public void resetWindowLayoutParams(){
        mParams = new WindowManager.LayoutParams();

        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.x = 0;
        mParams.y = 0;

        updateLayout();
    }

    /**
     * Set the LayoutParams which used in the WindowManager by this view.
     * @param params
     */
    public void setWindowLayoutParams(WindowManager.LayoutParams params){
        if(params != null){
            mParams.copyFrom(params);
            updateLayout();
        }else {
            resetWindowLayoutParams();
        }

    }
    /**
     * Attach this view to the WindowManager as a floating window
     * @param windowManager
     */
    public void attachToWindowManager(WindowManager windowManager){
        if(windowManager != null) {
            if(windowManager != mWindowManager) {
                detachFromWindowManger();
                mWindowManager = windowManager;

                mWindowManager.addView(this, mParams);

            }
        }else {
            detachFromWindowManger();
        }
    }
    /**
     * Attach this view to WindowManager as a floating window
     * @param windowManager
     */
    public void attachToWindowManager(WindowManager windowManager, WindowManager.LayoutParams params){
        setWindowLayoutParams(params);

        if(windowManager != null) {
            if(windowManager != mWindowManager) {
                detachFromWindowManger();
                mWindowManager = windowManager;
                mWindowManager.addView(this, mParams);
            }
        }else {
            detachFromWindowManger();
        }
    }


    /**
     * Detach this view from WindowManager
     */
    public void detachFromWindowManger(){
        if(mWindowManager != null) {
            mWindowManager.removeView(this);
            mWindowManager = null;
        }
    }

}
