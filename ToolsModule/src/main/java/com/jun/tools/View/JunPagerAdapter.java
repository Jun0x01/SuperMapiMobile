package com.jun.tools.View;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/26.
 */

public class JunPagerAdapter extends PagerAdapter {


    private ArrayList<View> mViews;
    private ArrayList<String> mTitles;

    @Override
    public int getCount() {

        return mViews != null ? mViews.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view==object;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mTitles != null && position < mTitles.size() ? mTitles.get(position) : null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mViews.get(position);
        container.removeView(view);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        container.addView(view);
        return view;
    }


    /**
     * Get the list of title resources which will be used as pager's title if needed. You can change
     * the content of the list. But, the method, {@link #notifyDataSetChanged()}, should be called
     * by the application if the data set has changed.
     * @return A list of String for pager title
     */
    public ArrayList<String> getTitleList(){
        if(mTitles == null)
            mTitles = new ArrayList<String>();

        return mTitles;
    }

    /**
     * Get the list of View resources which will be be shown page by page. You can change the
     * content of the list. But, the method, {@link #notifyDataSetChanged()}, should be called
     * by the application if the data set has changed.
     * @return A list of View to be shown page by page.
     */
    public ArrayList<View> getViewList(){
        if(mViews == null)
            mViews = new ArrayList<View>();

        return mViews;
    }

//    public void setTitles(ArrayList<String> titles){
//        mTitles = titles;
//
//        notifyDataSetChanged();
//    }
//
//    /**
//     * Set view resources to be shown.
//     * @param views A list of View to be shown page by page.
//     */
//    public void setViews(ArrayList<View> views){
//        mViews = views;
//        notifyDataSetChanged();
//    }
}
