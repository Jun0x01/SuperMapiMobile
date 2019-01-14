package com.jun.tools.FloatingWindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jun.tools.AppManager.MemoryListener;
import com.jun.tools.AppManager.MemoryRecord;
import com.jun.tools.R;

/**
 * Created by Jun on 2017/6/15.
 * android.permission.SYSTEM_ALERT_WINDOW is required.
 */

public class FloatingWindowBuilder {

    static String tag = "FloatingWindowBuilder";
    WindowManager mWindowManager;
    LayoutInflater mInflater;
    View mFloatingWindow;
    WindowManager.LayoutParams mParams;
    Context mContext;

    MemoryRecord mMemoryRecord;

    Handler mHandler;
    final int UPDATETEXTVIEW = 0x1;
    long mFirstTouchDownTime = 0;
    long mSecondTouchDownTime = 0;
    long mThirdTouchDOwnTime = 0;
    int mIndexTouchDown = 0;
    final int mSingleClickTime = 100; // ms
    final int mDoubleClickTime = 300; // ms

    MemoryListener mMemoryListener = new MemoryListener() {
        @Override
        public void onGetMemory(int state, String memoryInfo) {

           Message message = Message.obtain();
            message.what = UPDATETEXTVIEW;
            message.obj = memoryInfo;
            message.arg1 = state;
            mHandler.sendMessage(message);
        }
    };

    public FloatingWindowBuilder(Context context){

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case UPDATETEXTVIEW:
                        mFloatText.setText(msg.obj.toString());
                        if(msg.arg1 == 1){
                            mFloatText.setSelected(true);
                            mFloatText.setActivated(false);
                        }else if(msg.arg1 == 2){
                            mFloatText.setSelected(false);
                            mFloatText.setActivated(true);
                        }else {
                            mFloatText.setSelected(false);
                            mFloatText.setActivated(false);
                        }
                        break;
                }
            }
        };
        if(context != null) {

            mWindowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);

            if (mWindowManager != null) {
                mParams = new WindowManager.LayoutParams();

                mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                mParams.format = PixelFormat.RGBA_8888;
                mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                mParams.gravity = Gravity.LEFT | Gravity.TOP;

                mParams.x = mWindowManager.getDefaultDisplay().getWidth() -(int) context.getResources().getDimension(R.dimen.margin_2dp);
                mParams.y = (int) context.getResources().getDimension(R.dimen.margin_2dp);

                mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                mContext = context;
            }

            mMemoryRecord = new MemoryRecord(mContext);
            mMemoryRecord.addMemoryListener(mMemoryListener);
        }
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

    TextView mFloatText;
    String mFloatTextContent;
    public void createFloatingWindow(int layoutId){
        if(mContext != null){


            mInflater = LayoutInflater.from(mContext);

            mFloatingWindow = mInflater.inflate(layoutId, null);

            mWindowManager.addView(mFloatingWindow, mParams);

            mFloatText = (TextView) mFloatingWindow.findViewById(R.id.floating_text);

            mFloatingWindow.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            Log.d(tag, "width: " + mFloatingWindow.getMeasuredWidth());
            Log.d(tag, "height: " + mFloatingWindow.getMeasuredHeight());

            if (Build.VERSION.SDK_INT >= 11)
                mFloatingWindow.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        if (Build.VERSION.SDK_INT >= 11) {

                            if (event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {

                            }
                        }
                        return false;
                    }
                });

            mFloatingWindow.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float xInView = event.getX(); // relative to parent
                    float yInView = event.getY();
                    float xInScreen = event.getRawX(); // relative to screen
                    float yInScreen = event.getRawY(); // include status bar's height

                    int left = mFloatingWindow.getLeft();
                    int top = mFloatingWindow.getTop();
                    int right = mFloatingWindow.getRight();
                    int bot = mFloatingWindow.getBottom();
                    long curTime = System.currentTimeMillis();
                    long delta = 0;
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                mXDown = xInScreen;
                                mYDown = yInScreen;
                                mXLast = mXDown;
                                mYLast = mYDown;
                                mXPrePos = mParams.x;
                                mYPrePos = mParams.y;
                                mIsActionMoved = false;


                                mIndexTouchDown ++;

                                if(mIndexTouchDown == 1){
                                    mFirstTouchDownTime = curTime;
                                } else if(mIndexTouchDown == 2 && (curTime - mFirstTouchDownTime)<mDoubleClickTime){
                                    mSecondTouchDownTime = curTime;
                                } else{
                                    mIndexTouchDown = 0;
                                    mFirstTouchDownTime = 0;
                                    mSecondTouchDownTime = 0;
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                mXMove = xInScreen;
                                mYMove = yInScreen;
                                mXDelta = mXMove - mXLast;
                                mYDelta = mYMove - mYLast;
                                mXLast = mXMove;
                                mYLast = mYMove;

                                mIsActionMoved = true;
                                mParams.x += mXDelta;
                                mParams.y += mYDelta;
                                mParams.x = (int) (mXPrePos + (mXMove - mXDown));
                                mParams.y = (int) (mYPrePos + (mYMove - mYDown));
                                mWindowManager.updateViewLayout(mFloatingWindow, mParams);
                                break;
                            case MotionEvent.ACTION_UP:
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
                                    mWindowManager.updateViewLayout(mFloatingWindow, mParams);
                                }

                                if(mIndexTouchDown == 1){

                                    delta = curTime - mFirstTouchDownTime;
                                    Log.d("Touch", "FirstClick firstTime: " + delta);
                                    if(delta < mSingleClickTime){
                                        // singleClick

                                    } else {
                                        mIndexTouchDown = 0;
                                        mFirstTouchDownTime = 0;
                                        mSecondTouchDownTime = 0;
                                        mThirdTouchDOwnTime = 0;
                                    }
                                } else if(mIndexTouchDown == 2){
                                    delta = curTime - mFirstTouchDownTime;
                                    Log.d("Touch", "SecondClick firstTime: " + (curTime - mFirstTouchDownTime) + ", secondTime: " + delta);
                                    if(delta < mDoubleClickTime){
                                        // doubleClick

                                        if(mMemoryRecord != null){
                                            mMemoryRecord.enableSaveMemoryInfo(!mMemoryRecord.isEnableSaveMemoryInfo());
                                        }
                                    } else {
                                        mIndexTouchDown = 0;
                                        mFirstTouchDownTime = 0;
                                        mSecondTouchDownTime = 0;
                                        mThirdTouchDOwnTime = 0;
                                    }

                                }else {
                                    mIndexTouchDown = 0;
                                    mFirstTouchDownTime = 0;
                                    mSecondTouchDownTime = 0;
                                    mThirdTouchDOwnTime = 0;
                                }

                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                break;
                        }

                        Log.d(tag, "Action: " + event.getAction() +
                                      " InViewX: " + xInView + " InViewY: " + yInView +
                                      " InScreenX: " + xInScreen + " InScreenY: " + yInScreen +
                                      " Left: " + left + " Top: " + top + " Right: " +right + " Bot: " + bot +
                                      " PreX: " + mXPrePos + " PreY: " + mYPrePos +
                                      " DownX: " + mXDown + " DownY: " + mYDown +
                                      " MoveX: " + mXMove + " MoveY: " + mYMove +
                                      " UpX: " + mXUp + " UpY: " + mYUp +
                                      " DeltaX: " + mXDelta + " DeltaY: " + mYDelta);

                    return false;
                }
            });

//            mFloatingWindow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mMemoryRecord != null) {
//                        if(mMemoryRecord.isStarted()) {
//                            mMemoryRecord.cancelTimer();
//                        } else {
//                            mMemoryRecord.startTimer();
//                        }
//                    }
//                    Log.d(tag, "OnClicked");
//                }
//            });
            mFloatingWindow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mMemoryRecord.enableLogMemoryInfo(!mMemoryRecord.isEnableLogMemoryInfo());
                    return true;
                }
            });

            // 启动内存检查
            if (mMemoryRecord != null) {
                if (mMemoryRecord.isStarted()) {
                    mMemoryRecord.cancelTimer();
                } else {
                    mMemoryRecord.startTimer();
                }
            }
        }
    }

    public void clear(){
        if (mWindowManager != null)
            mWindowManager.removeView(mFloatingWindow);

        if(mMemoryRecord != null)
            mMemoryRecord.cancelTimer();

    }

    @Override
    protected void finalize() throws Throwable {
        if(mMemoryRecord != null)
            mMemoryRecord.cancelTimer();
        super.finalize();
    }

    public View getView(){
        return mFloatingWindow;
    }

}
