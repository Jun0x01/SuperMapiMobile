package com.jun.tools.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * Inflate a new view hierarchy form the specified xml resource
 *
 * Created by Jun on 2017/4/26.
 */

public class ViewInflater {

    /**
     * The root view in the specified xml resource
     */
    private View mViewRoot;

    /**
     * Constructor, and inflate a new view hierarchy form the specified xml resource.
     * @param inflater  The LayoutInflater to inflate a new view.
     * @param resource  ID for layout resource to load.
     * @param root      Optional parent view of the new view which will be inflated.
     */
    public ViewInflater(LayoutInflater inflater, int resource, ViewGroup root){
        mViewRoot = inflater.inflate(resource, root);
    }

    /**
     * Constructor, and inflate a new view hierarchy form the specified xml resource.
     * @param inflater  The LayoutInflater to inflate a new view.
     * @param resource  ID for layout resource to load.
     * @param root      Optional parent view of the new view which will be inflated.
     * @param attachToRoot Whether the inflated view should be attached to the root.
     */
    public ViewInflater(LayoutInflater inflater, int resource, ViewGroup root, boolean attachToRoot){

        mViewRoot = inflater.inflate(resource, root, attachToRoot);
    }

    /**
     * Get the inflated view hierarchy, it is the root view of the specified xml resource
     * @return  Root view in the specified xml resource
     */
    public View getView(){

        return mViewRoot;
    }
}
