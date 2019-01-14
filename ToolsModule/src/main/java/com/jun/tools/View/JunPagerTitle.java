package com.jun.tools.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.LinearLayout;

public class JunPagerTitle extends LinearLayout implements OnClickListener{

	
	public JunPagerTitle(Context context) {
		super(context);
	}

	public JunPagerTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@SuppressLint("NewApi")
	public JunPagerTitle(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@SuppressLint("NewApi")
	public JunPagerTitle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}
	

	private ViewPager mPager;
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		int count = getChildCount();
		for(int i=0; i<count; i++){
			getChildAt(i).setOnClickListener(this);
		}
		// set default selected child
		onSelected(0);

		final ViewParent parent = getParent();
		if (!(parent instanceof ViewPager)) {
			return;
		}

		final ViewPager pager = (ViewPager) parent;

		setViewPager(pager);

	}
	
	@Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPager != null) {
//            mPager.setOnPageChangeListener(null);
			mPager.removeOnPageChangeListener(mPageChangedListener);
			mPager = null;
        }
        int count = getChildCount();
		for(int i=0; i<count; i++){
			getChildAt(i).setOnClickListener(null);
		}
    }

	/********************* OnPageChangeListener *************************/
	
	
	private int mLastSelected = -1;
	private static String tag = "JunPagerTitle";
	
	
	class PagerChangedListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			Log.d(tag, "Pos " + position);
			onSelected(position);

		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}
	/*************************************************/
	private void onSelected(int position) {
		if(mLastSelected != position && position < getChildCount()){
			if(mLastSelected > -1){
				View child = getChildAt(mLastSelected);
				child.setSelected(false);
			}
			
			View child = getChildAt(position);
			child.setSelected(true);
			
			mLastSelected = position;
		}
	}
	/******************* OnClickListener ******************************/

	@Override
	public void onClick(View v) {
		int index = indexOfChild(v);
		Log.d(tag, "View Index  = " + index);
		if (mPager != null && index < mPager.getAdapter().getCount()){ // mPager.getChildCount may not be equal with mPager.getAdapter().getCount();
			mPager.setCurrentItem(index, true);
			onSelected(index);
		}
	}

	/*************************************************/
	private PagerChangedListener mPageChangedListener;
	public void setViewPager(ViewPager pager){
		if(mPageChangedListener == null)
			mPageChangedListener = new PagerChangedListener();
		if (mPager != null) {
//            mPager.setOnPageChangeListener(null);
			mPager.removeOnPageChangeListener(mPageChangedListener);
            mPager = null;
        }
		mPager = pager;
		if (mPager != null) {
//			mPager.setOnPageChangeListener(mPageChangedListener);

			mPager.addOnPageChangeListener(mPageChangedListener);
		}
	}
/****************************************************/

}
