package com.jun.tools.Drawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

/**
 * Created by Jun on 8/26/2017.
 */

public class ImageDrawer extends View {

    float mDensity;
    int mDPI;
    String mTag="n";
    Context mContext;

    public ImageDrawer(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mDensity = metrics.density;
        mDPI = metrics.densityDpi;
//        setBackgroundColor(0xff88e0fa);

//        setBackgroundColor(0xFFc0c0c0);
        setBackgroundColor(0x0);
        if (getTag() != null)
            mTag = getTag().toString();

        mLineWidth = mDensity*3/2;
        if (mLineWidth < 2) {
            mLineWidth = 2;
        }

        mContext = context;
    }

    public ImageDrawer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageDrawer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public ImageDrawer(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        //width =192;  // 640dpi

        // draw a Dot
        if (mTag.equals("Point")) {
            draw1Point(canvas, width);
        } else if (mTag.equals("Line")) {
            draw1Line(canvas, width);
        } else if (mTag.equals("Region")) {
            draw1Region(canvas, width);
        } else if (mTag.equals("Edit")) {
            draw1EditVertex(canvas, width);
        } else if (mTag.equals("AddV")) {
            draw1AddVertex(canvas, width);
        } else if (mTag.equals("DeleteV")) {
            draw1DeleteVertex(canvas, width);
        } else if (mTag.equals("LineSplit")) {
            draw1LineSplit(canvas, width);
        } else if (mTag.equals("RegionSplit")) {
            draw1RegionSplit(canvas, width);
        } else if (mTag.equals("FreeLine")) {
            draw1FreeLine(canvas, width);
        } else if (mTag.equals("FreeRegion")) {
            draw1FreeRegion(canvas, width);
        } else if (mTag.equals("FreeDraw")) {
            draw1FreeDraw(canvas, width);
        } else if (mTag.equals("DeleteGeo")) {
            draw1Delete(canvas, width);
        } else if (mTag.equals("Compose")) {
            draw1Compose(canvas, width);
        } else if (mTag.equals("Union")) {
            draw1Union(canvas, width);
        } else if (mTag.equals("Erase")) {
            draw1Erase(canvas, width);
        } else if (mTag.equals("Intersect")) {
            draw1Intersect(canvas, width);
        } else if (mTag.equals("DrawHollow")) {
            draw1Hollow(canvas, width);
        } else if (mTag.equals("DrawFillHollow")) {
            draw1FillHollow(canvas, width);
        } else if (mTag.equals("DrawSplitHollow")) {
            draw1SplitHollow(canvas, width);
        } else if (mTag.equals("DrawComposeHollow")) {
            draw1ComposeHollow(canvas, width);
        }else if (mTag.equals("DrawMultiFillHollow")) {
            draw1MultiFillHollow(canvas, width);
        }else if (mTag.equals("MultiSelect")) {
            draw1MultiSelect(canvas, width);
        }else if (mTag.equals("RectSelect")) {
            draw1RectSelect(canvas, width);
        }else if (mTag.equals("EditSameNode")) {
            draw1EditSameNode(canvas, width);
        }else if (mTag.equals("CreateSameFrameRegion")) {
            draw1CreateSameFrameRegion(canvas, width);
        }else if (mTag.equals("Plus")) {
            draw1Plus(canvas, width);
        }else if (mTag.equals("Minus")) {
            draw1Minus(canvas, width);
        }else if (mTag.equals("Pan")) {
            draw1Pan(canvas, width);
        }
    }


    private int mLineColor = 0xffff0000;
    private int mFillColor = 0xffffffff;
    private int mDotColor = 0xff0000ff;
    private int mEditLineColor = 0xff0000ff;
    private int mMarkerColor = 0xffff0000;
    private int mDashColor = 0xffff0000;
    private int mZoomSymbolColor = 0xFF808080;
    private int mPanColor = 0xff0000ff;

    private float mLineWidth = 4;
    private float mMarginRate = 8;

    public void draw1Point(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;
        float r = x / 2;

        if (x >= 2 && x < 4)
            r = x - 1;
        if (x < 2)
            r = x;

        Paint paint = new Paint();
        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        canvas.drawCircle(x-r*2/3, y-r*2/3, r*3/4, paint);
        canvas.drawCircle(x+r/2, y+r/2, r/2, paint);

        canvas.drawCircle(x+4*r/3, y+4*r/3, r/3, paint);

    }

    public void draw1Line(Canvas canvas, int width) {
        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float x0, y0, x1, y1, x2, y2, x3, y3, x4, y4;
        float margin = width / mMarginRate;

        x0 = margin;
        y0 = y;
        x1 = x0;
        y1 = width - margin;
        x2 = width - margin;
        y2 = x2;
        x3 = x2;
        y3 = y0;
        x4 = x;
        y4 = margin;

        Path path = new Path();
        path.moveTo(x0, y0);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);

        canvas.drawPath(path, paint);


    }

    public void draw1Region(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float x0, y0, x1, y1, x2, y2, x3, y3, x4, y4;
        float margin = width / mMarginRate;

        x0 = margin;
        y0 = y;
        x1 = x0;
        y1 = width - margin;
        x2 = width - margin;
        y2 = x2;
        x3 = x2;
        y3 = y0;
        x4 = x;
        y4 = margin;

        Path path = new Path();
        path.moveTo(x0, y0);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        path.close();

        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mLineColor);
        canvas.drawPath(path, paint);


    }

    public void draw1AddVertex(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float x0, y0, x1, y1, x2, y2, x3, y3, x4, y4;
        float margin = width / mMarginRate;

        x0 = margin;
        y0 = y;
        x1 = x0;
        y1 = width - margin;
        x2 = width - margin;
        y2 = x2;
        x3 = x2;
        y3 = y0;
        x4 = x;
        y4 = margin;

        float r = mLineWidth * 2;
        float w2 = mLineWidth / 2;

        Path path = new Path();
        path.moveTo(x0, y0);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        path.close();

        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mLineColor);
        canvas.drawPath(path, paint);

        // draw dot
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mDotColor);


        canvas.drawCircle(x0 + w2, y0 + w2, r, paint);
        canvas.drawCircle(x1 + w2, y1 + w2, r, paint);
        canvas.drawCircle(x2 + w2, y2 + w2, r, paint);
        canvas.drawCircle(x3 + w2, y3 + w2, r, paint);
        canvas.drawCircle(x4 + w2, y4, r, paint);

        // added point
        float x5 = x + (x - margin) / 2;
        float y5 = y - (y - margin) / 2;
        paint.setColor(mMarkerColor);
        canvas.drawCircle(x5, y5, r, paint);

        // add marker
        float l = 5 * mLineWidth;
        float x6 = width - l - margin;
        float y6 = margin + l / 2;
        float x7 = x6 + l;
        float y7 = y6;

        float x8 = width - margin - l / 2;
        float y8 = margin;
        float x9 = x8;
        float y9 = y8 + l;

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mMarkerColor);
        paint.setPathEffect(null);
        canvas.drawLine(x6, y6, x7, y7, paint);
        canvas.drawLine(x8, y8, x9, y9, paint);

    }

    public void draw1EditVertex(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float x0, y0, x1, y1, x2, y2, x3, y3, x4, y4;
        float margin = width / mMarginRate;

        x0 = margin;
        y0 = y;
        x1 = x0;
        y1 = width - margin;
        x2 = width - margin;
        y2 = x2;
        x3 = x2;
        y3 = y0;
        x4 = x;
        y4 = margin;

        float r = mLineWidth * 2;
        float w2 = mLineWidth / 2;

        Path path = new Path();
        path.moveTo(x0, y0);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4 + x / 2);
        path.close();

        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mLineColor);
        canvas.drawPath(path, paint);


        Path path2 = new Path();
        path2.moveTo(x0, y0);
        path2.lineTo(x4, y4);
        path2.lineTo(x3, y3);
        float[] dash = {mLineWidth * 2, mLineWidth};
        float offset = 0;
        DashPathEffect effect = new DashPathEffect(dash, offset);
        paint.setPathEffect(effect);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mDashColor);
        canvas.drawPath(path2, paint);

        // edited point, old point
        paint.setStyle(Paint.Style.FILL);
        paint.setPathEffect(null);
        paint.setColor(mMarkerColor);
        canvas.drawCircle(x4 + w2, y4 + w2, r, paint);

        // draw dot
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mDotColor);


        canvas.drawCircle(x0 + w2, y0 + w2, r, paint);
        canvas.drawCircle(x1 + w2, y1 + w2, r, paint);
        canvas.drawCircle(x2 + w2, y2 + w2, r, paint);
        canvas.drawCircle(x3 + w2, y3 + w2, r, paint);
        canvas.drawCircle(x4 + w2, y4 + x / 2, r, paint);

        float l = 6 * mLineWidth;
        float w = 3 * mLineWidth;
        float x5 = width - w - margin;
        float y5 = margin;
        float x6 = x5;
        float y6 = y5 + l;
        float x7 = x6 - w;
        float y7 = y6 - w;
        float x8 = x6 + w;
        float y8 = y6 - w;

        path.reset();
        path.moveTo(x7, y7);
        path.lineTo(x6, y6);
        path.lineTo(x8, y8);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mMarkerColor);
        paint.setPathEffect(null);
        canvas.drawPath(path, paint);
        canvas.drawLine(x5, y5, x6, y6, paint);

    }

    public void draw1DeleteVertex(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float x0, y0, x1, y1, x2, y2, x3, y3, x4, y4;
        float margin = width / mMarginRate;

        x0 = margin;
        y0 = y;
        x1 = x0;
        y1 = width - margin;
        x2 = width - margin;
        y2 = x2;
        x3 = x2;
        y3 = y0;
        x4 = x;
        y4 = margin;

        float r = mLineWidth * 2;
        float w2 = mLineWidth / 2;

        Path path = new Path();
        path.moveTo(x0, y0);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
//        path.lineTo(x3,y3);
        path.lineTo(x4, y4);
        path.close();

        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mLineColor);
        canvas.drawPath(path, paint);

        Path path2 = new Path();
        path2.moveTo(x2, y2);
        path2.lineTo(x3, y3);
        path2.lineTo(x4, y4);
        float[] dash = {mLineWidth * 2, mLineWidth};
        float offset = 0;
        DashPathEffect effect = new DashPathEffect(dash, offset);
        paint.setPathEffect(effect);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mDashColor);
        canvas.drawPath(path2, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mMarkerColor);

        // deleted point
        canvas.drawCircle(x3 + w2, y3 + w2, r, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mDotColor);

        // draw dots
        canvas.drawCircle(x0 + w2, y0 + w2, r, paint);
        canvas.drawCircle(x1 + w2, y1 + w2, r, paint);
        canvas.drawCircle(x2 + w2, y2 + w2, r, paint);
//        canvas.drawCircle(x3+w2,y3+w2, r, paint); // deleted
        canvas.drawCircle(x4 + w2, y4 + w2 * 3, r, paint);

        // draw marker
        float l = 3 * mLineWidth;
        float x5 = width - l - margin;
        float y5 = margin;
        float x6 = x5 + l;
        float y6 = y5;

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mMarkerColor);
        paint.setPathEffect(null);
        canvas.drawLine(x5, y5, x6, y6, paint);

    }

    public void draw1LineSplit(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float x0, y0, x1, y1, x2, y2, x3, y3, x4, y4;
        float margin = width / mMarginRate;

        x0 = margin;
        y0 = y;
        x1 = x0;
        y1 = width - margin;
        x2 = width - margin;
        y2 = x2;
        x3 = x2;
        y3 = y0;
        x4 = x;
        y4 = margin;

        Path path = new Path();
        path.moveTo(x0, y0);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        path.close();

        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mLineColor);
        canvas.drawPath(path, paint);

        float x5 = width - margin;
        float y5 = margin;
        float x6 = x + (x - margin) / 2;
        float y6 = margin + (x - margin) / 2;

        float x7 = x2;
        float y7 = y2;


        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mEditLineColor);
        canvas.drawLine(x5, y5, x6, y6, paint);

        path.reset();
        path.moveTo(x6, y6);
        path.lineTo(x, y);
        path.lineTo(x7, y7);
        canvas.drawPath(path, paint);

        canvas.drawLine(x7, y7, x7 + margin / 2, y7 + margin / 2, paint);


    }

    public void draw1RegionSplit(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float x0, y0, x1, y1, x2, y2, x3, y3, x4, y4;
        float margin = width / mMarginRate;

        x0 = margin;
        y0 = y;
        x1 = x0;
        y1 = width - margin;
        x2 = width - margin;
        y2 = x2;
        x3 = x2;
        y3 = y0;
        x4 = x;
        y4 = margin;

        Path path = new Path();
        path.moveTo(x0, y0);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        path.close();

        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mLineColor);
        canvas.drawPath(path, paint);

        float x5 = width - margin / 2;
        float y5 = margin;
        float x6 = x + margin;
        float y6 = margin;

        float x7 = x6;
        float y7 = width - margin / 2;
        float x8 = width - margin / 2;
        float y8 = x8;


        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mEditLineColor);

        path.reset();
        path.moveTo(x5, y5);
        path.lineTo(x6, y6);
        path.lineTo(x7, y7);
        path.lineTo(x8, y8);
        path.close();
        canvas.drawPath(path, paint);

    }

    private void draw1FreeLine(Canvas canvas, int width) {
        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;


        RectF rect1 = new RectF();
        rect1.set(left, top, right, y);

        RectF rect2 = new RectF();
        rect2.set(left + margin, y, right - margin, bottom);

        Path path = new Path();
        path.addArc(rect1, 270, -180);
        path.addArc(rect2, 270, 150);
        path.lineTo(x, bottom);
        path.lineTo(left, bottom);
        path.lineTo(left, y + (y - margin) / 2);

        canvas.drawPath(path, paint);


    }

    public void draw1FreeRegion(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;

        RectF rect1 = new RectF();
        rect1.set(left, top, right, y);

        RectF rect2 = new RectF();
        rect2.set(left + margin, y, right - margin, bottom);

        Path path = new Path();

        path.addArc(rect2, 270, 150);
        path.lineTo(x, bottom);
        path.lineTo(left, bottom);
        path.lineTo(left, y + (y - margin) / 2);

        path.addArc(rect1, 90, 180);
        path.lineTo(left, y + (y - margin) / 2);


        canvas.drawPath(path, paint);

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

    }

    public void draw1FreeDraw(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;


        RectF rect1 = new RectF();
        rect1.set(left + margin, top, right - margin, y);

        RectF rect2 = new RectF();
        rect2.set(left + margin, y, right - margin, bottom);

        Path path = new Path();
        path.addArc(rect1, 270, -180);
        path.addArc(rect2, 270, 150);
        path.lineTo(x, bottom);

        canvas.drawPath(path, paint);

        canvas.drawLine(left, bottom, x + (x - margin) / 2, y + (y - margin) / 2, paint);

        canvas.drawCircle(x + (x - margin) / 2, y - (y - margin) / 2, 3 * mLineWidth, paint);

    }

    public void draw1Delete(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;


        canvas.drawRect(left, top, x - margin / 2, y - margin / 2, paint);
        canvas.drawRect(left, y + margin / 2, x - margin / 2, bottom, paint);
        canvas.drawRect(x + margin / 2, y + margin / 2, right, bottom, paint);
//        canvas.drawRect(x + margin/2, top, right, y - margin/2, paint );

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(left, top, x - margin / 2, y - margin / 2, paint);
        canvas.drawRect(left, y + margin / 2, x - margin / 2, bottom, paint);
        canvas.drawRect(x + margin / 2, y + margin / 2, right, bottom, paint);

        float w = 2 * mLineWidth;

        float x0 = x + (x - margin) / 2 + margin / 2;
        float y0 = y - (y - margin) / 2 - margin / 2;

        paint.setColor(mMarkerColor);

        canvas.drawLine(x0 - w, y0 - w, x0 + w, y0 + w, paint);
        canvas.drawLine(x0 - w, y0 + w, x0 + w, y0 - w, paint);

        float[] dash = {mLineWidth * 2, mLineWidth};
        float offset = 0;
        DashPathEffect effect = new DashPathEffect(dash, offset);
        paint.setPathEffect(effect);
        paint.setColor(mDashColor);
        canvas.drawRect(x + margin / 2, top, right, y - margin / 2, paint);


    }

    public void draw1Compose(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;

        float w = (x - left) / 2;

        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, x + w, y + w, paint);

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left, top, x + w, y + w, paint);

        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x - w, y - w, right, bottom, paint);

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x - w, y - w, right, bottom, paint);

        float r = mLineWidth * 2;
        float w2 = mLineWidth / 2;

        // draw dot
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mDotColor);

        canvas.drawCircle(left + w2, top + w2, r, paint);
        canvas.drawCircle(left + w2, bottom + w2, r, paint);
        canvas.drawCircle(right + w2, bottom + w2, r, paint);
        canvas.drawCircle(right + w2, top + w2, r, paint);

    }

    public void draw1Union(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;

        float w = (x - left) / 2;

        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(x + w, top);
        path.lineTo(x + w, top + w);
        path.lineTo(right, top + w);
        path.lineTo(right, bottom);
        path.lineTo(x - w, bottom);
        path.lineTo(x - w, y + w);
        path.lineTo(left, y + w);
        path.close();

        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        RectF rectF = new RectF();
        rectF.set(x - w, y - w, x + w, y + w);

        // draw changed rect
        float[] dash = {mLineWidth * 2, mLineWidth};
        float offset = 0;
        DashPathEffect effect = new DashPathEffect(dash, offset);
        paint.setPathEffect(effect);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mDashColor);
        canvas.drawRect(rectF, paint);

        float r = mLineWidth * 2;
        float w2 = mLineWidth / 2;

        // draw dot
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mDotColor);

        canvas.drawCircle(left + w2, top + w2, r, paint);
        canvas.drawCircle(x + w + w2, top + w2, r, paint);
        canvas.drawCircle(x + w + w2, top + w + w2, r, paint);
        canvas.drawCircle(right + w2, top + w + w2, r, paint);
        canvas.drawCircle(right + w2, bottom + w2, r, paint);
        canvas.drawCircle(x - w + w2, bottom + w2, r, paint);
        canvas.drawCircle(x - w + w2, y + w + w2, r, paint);
        canvas.drawCircle(left + w2, y + w + w2, r, paint);
    }

    public void draw1Erase(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;

        float w = (x - left) / 2;

        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(x + w, top);
        path.lineTo(x + w, top + w);
        path.lineTo(x - w, top + w);
        path.lineTo(x - w, y + w);
        path.lineTo(left, y + w);
        path.close();


        RectF rectF = new RectF();

        // erased
        rectF.set(x - w, y - w, x + w, y + w);
        float[] dash = {mLineWidth * 2, mLineWidth};
        float offset = 0;
        DashPathEffect effect = new DashPathEffect(dash, offset);
        paint.setPathEffect(effect);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mDashColor);
        canvas.drawRect(rectF, paint);

        paint.setPathEffect(null);
        // Region filled
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);

        // Region frame     `
        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        // edit rect
        rectF.set(x - w, y - w, right, bottom);
        paint.setColor(mEditLineColor);
        canvas.drawRect(rectF, paint);

        float r = mLineWidth * 2;
        float w2 = mLineWidth / 2;

        // draw dot
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mDotColor);

        canvas.drawCircle(left + w2, top + w2, r, paint);
        canvas.drawCircle(x + w + w2, top + w2, r, paint);
        canvas.drawCircle(x + w + w2, top + w + w2, r, paint);
        canvas.drawCircle(x - w + w2, top + w + w2, r, paint);
        canvas.drawCircle(x - w + w2, y + w + w2, r, paint);
        canvas.drawCircle(left + w2, y + w + w2, r, paint);

    }

    public void draw1Intersect(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;

        float w = (x - left) / 2;

        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(x + w, top);
        path.lineTo(x + w, top + w);
        path.lineTo(right, top + w);
        path.lineTo(right, bottom);
        path.lineTo(x - w, bottom);
        path.lineTo(x - w, y + w);
        path.lineTo(left, y + w);
        path.close();


        float[] dash = {mLineWidth * 2, mLineWidth};
        float offset = 0;
        DashPathEffect effect = new DashPathEffect(dash, offset);
        paint.setPathEffect(effect);
        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
        paint.setPathEffect(null);


        float r = mLineWidth * 2;
        float w2 = mLineWidth / 2;

        // intersect
        RectF rectF = new RectF();
        rectF.set(x - w, y - w, x + w, y + w);

        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectF, paint);

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectF, paint);

        // draw dot
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mDotColor);

        canvas.drawCircle(x - w + w2, y - w + w2, r, paint);
        canvas.drawCircle(x + w + w2, y - w + w2, r, paint);
        canvas.drawCircle(x + w + w2, y + w + w2, r, paint);
        canvas.drawCircle(x - w + w2, y + w + w2, r, paint);


    }

    public void draw1Hollow(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;
        float r = (x - left) / 2;

        Path path = new Path();
        path.moveTo(left, y);
        path.lineTo(left, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, y);
        path.lineTo(x, top);
        path.close();

        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mLineColor);
        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0x00ffffff);
        canvas.drawCircle(x, y + r / 2, r, paint);

        paint.setColor(mEditLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x, y + r / 2, r, paint);

    }

    public void draw1FillHollow(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;
        float r = (x - left) / 2;

        Path path = new Path();
        path.moveTo(left, y);
        path.lineTo(left, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, y);
        path.lineTo(x, top);
        path.close();

//        path.addCircle(x, y+r/2, r, Path.Direction.CCW);

        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mLineColor);
        canvas.drawPath(path, paint);

        float[] dash = {mLineWidth * 2, mLineWidth};
        float offset = 0;
        DashPathEffect effect = new DashPathEffect(dash, offset);
        paint.setPathEffect(effect);

        canvas.drawCircle(x, y + r / 2, r, paint);

        paint.setColor(mEditLineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(null);
        canvas.drawLine(left + r / 2, bottom + margin / 2, right - r / 2, top + r / 2, paint);

    }

    public void draw1SplitHollow(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;
        float r = (x - left) / 2;

        Path path = new Path();
        path.moveTo(left, y);
        path.lineTo(left, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, y);
        path.lineTo(x, top);
        path.close();

//        path.addCircle(x, y+r/2, r, Path.Direction.CCW);

        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mLineColor);
        canvas.drawPath(path, paint);

        canvas.drawCircle(x, y + r / 2, r, paint);

        // edit line
        paint.setColor(mEditLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(left + r / 2, bottom + margin / 2, right - r / 2, top + r / 2, paint);

    }

    public void draw1ComposeHollow(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;
        float r = (x - left) / 2;

        Path path = new Path();
        path.moveTo(left, y);
        path.lineTo(left, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, y);
        path.lineTo(x, top);
        path.close();

        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mLineColor);
        canvas.drawPath(path, paint);

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(x, y + r / 2, r, paint);

        // add marker
        float l = 5 * mLineWidth;
        float x6 = width - l - margin;
        float y6 = margin + l / 2;
        float x7 = x6 + l;
        float y7 = y6;

        float x8 = width - margin - l / 2;
        float y8 = margin;
        float x9 = x8;
        float y9 = y8 + l;

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mEditLineColor);
        canvas.drawLine(x6, y6, x7, y7, paint);
        canvas.drawLine(x8, y8, x9, y9, paint);
    }

    public void draw1MultiFillHollow(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;
        float w = (x-left)/2;

        canvas.drawRect(left, top, x-w, bottom, paint);
        canvas.drawRect(x-w, top, right, y-w, paint);
        canvas.drawRect(x+w, y-w, right, y+w, paint);
        canvas.drawRect(x-w, y+w, right, bottom, paint );

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(left, top, x-w, bottom, paint);
        canvas.drawRect(x-w, top, right, y-w, paint);
        canvas.drawRect(x+w, y-w, right, y+w, paint);
        canvas.drawRect(x-w, y+w, right, bottom, paint );

        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x-w, y -w, x+w, y+w, paint);

        paint.setColor(mEditLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x-w, y -w, x+w, y+w, paint);
        float[] dash = {mLineWidth * 2, mLineWidth};
        float offset = 0;
        DashPathEffect effect = new DashPathEffect(dash, offset);
        paint.setPathEffect(effect);
        canvas.drawRect(x-w*3/2, y -w*3/2, x+w*3/2, y+w*3/2, paint);


    }

    public void draw1MultiSelect(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;


        canvas.drawRect(left, top, x - margin / 2, y - margin / 2, paint);
        canvas.drawRect(left, y + margin / 2, x - margin / 2, bottom, paint);
        canvas.drawRect(x + margin / 2, y + margin / 2, right, bottom, paint);
        canvas.drawRect(x + margin/2, top, right, y - margin/2, paint );

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(left, top, x - margin / 2, y - margin / 2, paint);
        canvas.drawRect(left, y + margin / 2, x - margin / 2, bottom, paint);

        paint.setColor(mEditLineColor);
        canvas.drawRect(x + margin / 2, y + margin / 2, right, bottom, paint);
        canvas.drawRect(x + margin / 2, top, right, y - margin / 2, paint);


    }

    public void draw1RectSelect(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;


        canvas.drawRect(left, top, x - margin / 2, y - margin / 2, paint);
        canvas.drawRect(left, y + margin / 2, x - margin / 2, bottom, paint);
        canvas.drawRect(x + margin / 2, y + margin / 2, right, bottom, paint);
//        canvas.drawRect(x + margin/2, top, right, y - margin/2, paint );

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(left, top, x - margin / 2, y - margin / 2, paint);
        canvas.drawRect(left, y + margin / 2, x - margin / 2, bottom, paint);
        paint.setColor(mEditLineColor);
        canvas.drawRect(x + margin / 2, y + margin / 2, right, bottom, paint);

        // draw point
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);

        float w = (x-left)/4;
        canvas.drawCircle(x+w, y-w, w*2/3, paint);
        canvas.drawCircle(right-w, top + w, w*2/3, paint);

        paint.setColor(mEditLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x+w, y-w, w*2/3, paint);
        canvas.drawCircle(right-w, top + w, w*2/3, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mLineWidth/2);
        canvas.drawRect(x, top - margin/2, right + margin/2, bottom + margin/2, paint);

    }

    public void draw1EditSameNode(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;

        float w = (x-left)/4;

        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        path.lineTo(right,bottom);
        path.lineTo(x,y-w);
        path.close();
        path.moveTo(x, y-w);
        path.lineTo(right,top);
        path.lineTo(right,bottom);
        path.close();

        canvas.drawPath(path, paint);
        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        path.reset();
        path.moveTo(left, top);
        path.lineTo(x-w, y+w);
        path.lineTo(right,bottom);
        paint.setColor(mLineColor);
        float[] dash = {mLineWidth * 2, mLineWidth};
        float offset = 0;
        DashPathEffect effect = new DashPathEffect(dash, offset);
        paint.setPathEffect(effect);
        canvas.drawPath(path, paint);

        float r = mLineWidth * 2;
        float w2 = mLineWidth / 2;

        path.reset();
        path.addCircle(left+w2,top+w2,r, Path.Direction.CCW);
        path.addCircle(left+w2,bottom+w2,r, Path.Direction.CCW);
        path.addCircle(right+w2,bottom+w2,r, Path.Direction.CCW);
        path.addCircle(right+w2,top+w2,r, Path.Direction.CCW);
        path.addCircle(x,y-w+w2,r, Path.Direction.CCW);

        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mEditLineColor);
        canvas.drawPath(path, paint);

        path.reset();

        paint.setColor(mLineColor);
        canvas.drawCircle(x-w+w2,y+w+w2, r, paint);

        float l = 6 * mLineWidth;
        w = 3 * mLineWidth;
        float x5 = x;
        float y5 = top;
        float x6 = x5;
        float y6 = y5 + l;
        float x7 = x5 - w;
        float y7 = y5 + w;
        float x8 = x5 + w;
        float y8 = y5 + w;

        path.reset();
        path.moveTo(x7, y7);
        path.lineTo(x5, y5);
        path.lineTo(x8, y8);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mMarkerColor);
        paint.setPathEffect(null);
        canvas.drawPath(path, paint);
        canvas.drawLine(x5, y5, x6, y6, paint);

    }

    public void draw1CreateSameFrameRegion(Canvas canvas, int width) {

        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;
        float w = (x-left)/2;

        canvas.drawRect(left, top, x-w, bottom, paint);
        canvas.drawRect(x-w, y+w, right, bottom, paint );

        paint.setColor(mFillColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x-w, y -w, x+w, y+w, paint);

        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(left, top, x-w, bottom, paint);
        canvas.drawRect(x-w, y+w, right, bottom, paint );

        paint.setColor(mEditLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(x-w*3/2, y -w, x+w, y -w, paint);
        canvas.drawLine(x+w, y+w*3/2, x+w, y -w, paint);

    }

    private void draw1Plus(Canvas canvas, int width) {
        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mZoomSymbolColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;

        float lineWidth = (right - left)/8;
        paint.setStrokeWidth(lineWidth);
        canvas.drawLine(left, y, right, y, paint);
        canvas.drawLine(x,top,x,bottom,paint);
    }

    private void draw1Minus(Canvas canvas, int width) {
        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mZoomSymbolColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;

        float lineWidth = (right - left)/8;
        paint.setStrokeWidth(lineWidth);
        canvas.drawLine(left, y, right, y, paint);
    }

    private void draw1Pan(Canvas canvas, int width) {
        float x = width / 2;
        float y = width / 2;

        Paint paint = new Paint();
        paint.setColor(mPanColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mLineWidth);

        // M23,5.5 V20
        // c0,2.2 -1.8,4 -4,4 h-7.3
        // c-1.08,0 -2.1,-0.43 -2.85,-1.19
        // L1,14.83
        // s1.26,-1.23 1.3,-1.25
        // c0.22,-0.19 0.49,-0.29 0.79,-0.29 0.22,0 0.42,0.06 0.6,0.16 0.04,0.01 4.31,2.46 4.31,2.46
        // V4
        // c0,-0.83 0.67,-1.5 1.5,-1.5
        // S11,3.17 11,4
        // v7 h1 V1.5
        // c0,-0.83 0.67,-1.5 1.5,-1.5
        // S15,0.67 15,1.5
        // V11 h1 V2.5
        // c0,-0.83 0.67,-1.5 1.5,-1.5
        // s1.5,0.67 1.5,1.5
        // V11 h1 V5.5
        // c0,-0.83 0.67,-1.5 1.5,-1.5
        // s1.5,0.67 1.5,1.5z"/>

        float left, top, right, bottom;
        float margin = width / mMarginRate;

        left = top = margin;
        right = bottom = width - margin;
        left = left + margin*0.5f;
        right = right - margin*0.5f;
        float r2,r3, r4,r5;
        float w = (x -left)/2;
        r2 = w*4/12;
        r3 = w*5/13;
        r4 = w*4/13;
        r5 = w*3/13;
        float wh = (y-top)/2;
        float h = 1.5f;
        float up1 = 7*wh/8;
        float up2 = 4*wh/8;
        float up3 = -wh*2/3;

        float l, t, r, b, x0, y0;

        Path path = new Path();

        x0 = right;
        y0 = y;
        path.moveTo(x0, y0);
        RectF rectF = new RectF();

        // r5
        l = x0 - 2*r5;
        t = y0-h*r5;
        r = x0;
        b = y0+h*r5;
        rectF.set(l, t, r, b);
        path.addArc( rectF, 0, -180);

        x0 = x0 - 2*r5;
        y0 = y0 - up1;
        path.lineTo(x0,y0);
        l = x0 - 2*r4;
        t = y0 - h*r4;
        r = x0;
        b = y0 + h*r4;
        rectF.set(l, t, r, b);
        path.addArc(rectF, 0, -180);

        x0 = x0 - 2*r4;
        y0 = y0 - up2;
        path.lineTo(x0,y0);
        l = x0 - 2*r3;
        t = y0 - h*r3;
        r = x0;
        b = y0 + h*r3;
        rectF.set(l, t, r, b);
        path.addArc(rectF, 0, -180);

        x0 = x0 - 2*r3;
        y0 = y0 - up3;
        path.lineTo(x0,y0);
        l = x0 - 2*r2;
        t = y0 - h*r2;
        r = x0;
        b = y0 + h*r2;
        rectF.set(l, t, r, b);
        path.addArc(rectF, 0, -180);

        x0 = x0 - 2*r2;
        y0 = y+w*0.4f;
        path.lineTo(x0,y0);

        float r1 = w*3/10;
        l = x0 - 2*r1;
        t = y0 - h*r1;
        r = x0;
        b = y0 + h*r1;
        rectF.set(l, t, r, b);
        path.addArc(rectF, 0, 145);

        float r0 = w*5/10;
        x0 = x-w*1.5f;
        y0 = y + r0*0.2f;
        path.lineTo(x0, y0);

        float x1 = x0 - r0*2.5f;
        float y1 = y0 - r0*1.8f;
        float x2 = left+w*0.6f;
        float y2 = bottom;

        path.quadTo(x1, y1, x2, y2);
        path.lineTo(x+w*1.6f, bottom);
        path.lineTo(right,y);

//        paint.setColor(mFillColor);
//        paint.setStyle(Paint.Style.FILL);
//
//        canvas.drawPath(path, paint);

        paint.setColor(mEditLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);


    }


    public void ouputImage(String path, int size){
        Method[] methods = getClass().getMethods();
        String name = null;
        String fileName = null;

        Bitmap bitmap = Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (Method method:methods){
            name = method.getName();

            if(name.contains("draw1")){
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                fileName = name.substring(5);
                try {
                    method.invoke(this,canvas,size);
                    saveBitmap(bitmap, path + "/" + fileName + ".png");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }

    }



    private void saveBitmap(Bitmap bitmap, String path){
        File file = new File(path);
        file.getParentFile().mkdirs();

        Bitmap.CompressFormat format = null;
        format = Bitmap.CompressFormat.PNG;

        FileOutputStream fout =null;
        try {
            fout = new FileOutputStream(file);
            bitmap.compress(format, 100, fout);
            fout.flush();

        }catch (Exception e){
            e.printStackTrace();
        }

        finally {
            try {
                if (fout != null)
                    fout.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


}
