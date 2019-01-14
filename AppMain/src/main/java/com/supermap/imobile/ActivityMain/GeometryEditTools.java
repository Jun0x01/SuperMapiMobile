package com.supermap.imobile.ActivityMain;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.supermap.data.Point2D;
import com.supermap.imobile.R;
import com.supermap.mapping.Action;
import com.supermap.mapping.GeometryAddedListener;
import com.supermap.mapping.GeometryDeletedListener;
import com.supermap.mapping.GeometryDeletingListener;
import com.supermap.mapping.GeometryEvent;
import com.supermap.mapping.GeometryModifiedListener;
import com.supermap.mapping.GeometryModifyingListener;
import com.supermap.mapping.GeometrySelectedEvent;
import com.supermap.mapping.GeometrySelectedListener;
//import com.supermap.mapping.GeometrySnappedListener;
import com.supermap.mapping.Layer;
import com.supermap.mapping.MapControl;

import java.util.ArrayList;

/**
 * Created by Jun on 2017/9/4.
 */

public class GeometryEditTools implements View.OnClickListener, GeometrySelectedListener, GeometryAddedListener, GeometryDeletedListener, GeometryDeletingListener, GeometryModifiedListener, GeometryModifyingListener/*, GeometrySnappedListener*/ {

    private MapControl mMapControl;
    private ViewGroup mRootLayout;
    private String mCheckTag;

    private static final  String mTag = "GeoEdit";

    public GeometryEditTools(ViewGroup rootLayout){
        mRootLayout = rootLayout;

        initView(mRootLayout);
    }
    public GeometryEditTools(MapControl mapControl, ViewGroup rootLayout){
        setMapControl(mapControl);
        mCurAction = Action.PAN;
        mRootLayout = rootLayout;
        initView(mRootLayout);
    }

    @Override
    protected void finalize() throws Throwable {
        resetViewState();
        mMapControl = null;
        mRootLayout = null;
        mCheckTag = null;
        super.finalize();
    }

    public void setMapControl(MapControl mapControl){
        if(mMapControl != null){
            mMapControl.removeGeometrySelectedListener(this);
        }
        mMapControl = mapControl;
        mCurAction = Action.PAN;
        if(mMapControl != null){
            mMapControl.addGeometrySelectedListener(this);
            mMapControl.addGeometryAddedListener(this);
            mMapControl.addGeometryDeletedListener(this);
            mMapControl.addGeometryDeletingListener(this);
            mMapControl.addGeometryModifiedListener(this);
            mMapControl.addGeometryModifyingListener(this);
//            mMapControl.addGeometrySnappedListener(this);
        }

    }

    public void resetViewState(){
        if(mLastSelectedView != null)
            mLastSelectedView.setSelected(false);
        mCurAction = Action.PAN;
    }

    private View mViewEditNode;
    private void initView(ViewGroup viewGroup){
        if (viewGroup != null) {
            mCheckTag = viewGroup.getContext().getResources().getString(R.string.tag_btn_edit_geo_item);
//            View.OnTouchListener listener = new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            };
//            viewGroup.setOnTouchListener(listener);

//            int count = viewGroup.getChildCount();
//            for (int i=0; i<count; i++){
//                viewGroup.getChildAt(i).setOnTouchListener(listener);
//                break;
//            }
        }
        initButtons(viewGroup);

        mViewEditNode = viewGroup.findViewById(R.id.btn_edit_geo_edit_node);
    }

    private void initButtons(ViewGroup viewGroup) {
        if(viewGroup != null){
            View child = null;
            int count = viewGroup.getChildCount();
            Object tag = null;
            for (int i= 0; i<count; i++){
                child = viewGroup.getChildAt(i);
                tag = child.getTag();
                if(tag != null && tag.equals(mCheckTag) && child.isClickable()){
                    child.setOnClickListener(this);
                }else if(child instanceof ViewGroup){
                    initButtons((ViewGroup) child);
                }

            }
        }
    }


    private Action mCurAction;
    private View mLastSelectedView;

    @Override
    public void onClick(View v) {
        if(mMapControl == null){
            return;
        }

//        ShowMessage.showInfo("Edit", "ID: " + v.getId());
        boolean isAction = false;
        switch (v.getId()) {
            case R.id.btn_edit_geo_undo:
                mMapControl.undo();
                break;
            case R.id.btn_edit_geo_redo:
                mMapControl.redo();
                break;
            case R.id.btn_edit_geo_cancel:
                mMapControl.cancel();
                mMapControl.setAction(mCurAction);
                break;
            case R.id.btn_edit_geo_done:
                mMapControl.submit();
                mMapControl.setAction(mCurAction);
                break;
            case R.id.btn_edit_geo_delete:
                mMapControl.deleteCurrentGeometry();
                break;
            default:
                isAction = true;
                break;
        }

        if(!isAction)
            return;

        boolean isChecked = v.isSelected();
        if(isChecked){
            v.setSelected(false);
            mMapControl.setAction(Action.PAN);
            mLastSelectedView = null;
            return;
        }

        Action action = null;
        switch (v.getId()){
            case R.id.btn_edit_geo_select:
                action = Action.SELECT;
                break;
            case R.id.btn_edit_geo_pan:
                action = Action.PAN;
                break;
            case R.id.btn_edit_geo_point:
                action = Action.CREATEPOINT;
                break;
            case R.id.btn_edit_geo_line:
                action = Action.CREATEPOLYLINE;
                break;
            case R.id.btn_edit_geo_region:
                action = Action.CREATEPOLYGON;
                break;
            case R.id.btn_edit_geo_freeline:
                action = Action.DRAWLINE;
                break;
            case R.id.btn_edit_geo_freeregion:
                action = Action.DRAWPLOYGON;
                break;
            case R.id.btn_edit_geo_freedraw:
                action = Action.FREEDRAW;
                break;
            case R.id.btn_edit_geo_edit_node:
                action = Action.VERTEXEDIT;
                break;
            case R.id.btn_edit_geo_move_common_node:
                action = Action.MOVE_COMMON_NODE;
                break;
            case R.id.btn_edit_geo_add_node:
                action = Action.VERTEXADD;
                break;
            case R.id.btn_edit_geo_delete_node:
                action = Action.VERTEXDELETE;
                break;
            case R.id.btn_edit_geo_rect_select:
                action = Action.SELECT_BY_RECTANGLE;
                break;
            case R.id.btn_edit_geo_mult_select:
                action = Action.MULTI_SELECT;
                break;
            case R.id.btn_edit_geo_edit_erase:
                action = Action.ERASE_REGION;
                break;
            case R.id.btn_edit_geo_edit_intersect:
                action = Action.INTERSECT_REGION;
                break;
            case R.id.btn_edit_geo_compose:
                action = Action.COMPOSE_REGION;
                break;
            case R.id.btn_edit_geo_union:
                action = Action.UNION_REGION;
                break;
            case R.id.btn_edit_geo_split_line:
                mMapControl.setAction(Action.SPLIT_BY_LINE);
                action = Action.SPLIT_BY_LINE;
                break;
            case R.id.btn_edit_geo_fill_hollow:
                action = Action.FILL_HOLLOW_REGION;
                break;
            case R.id.btn_edit_geo_split_hollow:
                action = Action.PATCH_HOLLOW_REGION;
                break;
            case R.id.btn_edit_geo_create_same_frame_region:
                action = Action.CREATE_POSITIONAL_REGION;
                break;
            case R.id.btn_edit_geo_multi_fill_hollow:
                action = Action.PATCH_POSOTIONAL_REGION;
                break;

// Since SuperMap iMobile 9D
/*
            case R.id.btn_edit_geo_split_region:
                action = Action.SPLIT_BY_REGION;
                break;
            case R.id.btn_edit_geo_draw_hollow:
                action = Action.DRAW_HOLLOW_REGION;
                break;
            case R.id.btn_edit_geo_compose_hollow:
                action = Action.COMPOSE_HOLLOW_REGION;
                break;
*/

            case R.id.btn_edit_geo_split_selfIntersect:
//                action = Action.SPLIT_SELFINTERSECT;
                break;
            case R.id.btn_edit_geo_move:
                action = Action.MOVE_GEOMETRY;
                break;
            default:
                break;

        }

        if(action != null){
            if(mCurAction != Action.SELECT && mCurAction != action && (action == Action.VERTEXADD || action == Action.VERTEXEDIT || action == Action.VERTEXDELETE)){
                mMapControl.setAction(Action.SELECT);
            }else {
                mMapControl.setAction(action);
            }

            mCurAction = action;
            v.setSelected(true);
            if(mLastSelectedView != null)
                mLastSelectedView.setSelected(false);
            mLastSelectedView = v;
        }

    }

    /****************** GeometrySelected *******************************/
    @Override
    public void geometrySelected(GeometrySelectedEvent geometrySelectedEvent) {

        Layer layer = geometrySelectedEvent.getLayer();
        if(layer.isEditable()){
            if(mCurAction == Action.SELECT){
//                mLastSelectedView.setSelected(false);
//                mViewEditNode.setSelected(true);


            }else if(mCurAction == Action.VERTEXADD || mCurAction == Action.VERTEXEDIT || mCurAction == Action.VERTEXDELETE){
                mMapControl.setAction(mCurAction);
            }

        }
        Log.d(mTag, "Selected: " + geometrySelectedEvent.getGeometryID() + ", " + geometrySelectedEvent.getLayer().getName());
    }

    @Override
    public void geometryMultiSelected(ArrayList<GeometrySelectedEvent> arrayList) {
        for(GeometrySelectedEvent event : arrayList){
            Log.d(mTag, "MultiSelected: " + event.getGeometryID() + ", " + event.getLayer().getName());
        }
    }

    @Override
    public void geometryAdded(GeometryEvent geometryEvent) {
        Log.d(mTag, "Added: " + geometryEvent.getID() + ", " + geometryEvent.getLayer().getName());
    }

    @Override
    public void geometryDeleted(GeometryEvent geometryEvent) {
        Log.d(mTag, "Deleted: " + geometryEvent.getID() + ", " + geometryEvent.getLayer().getName());
    }

    @Override
    public void geometryDeleting(GeometryEvent geometryEvent) {
        Log.d(mTag, "Deleting: " + geometryEvent.getID() + ", " + geometryEvent.getLayer().getName());
    }

    @Override
    public void geometryModified(GeometryEvent geometryEvent) {
        Log.d(mTag, "Modified: " + geometryEvent.getID() + ", " + geometryEvent.getLayer().getName());
    }

    @Override
    public void geometryModifying(GeometryEvent geometryEvent) {
        Log.d(mTag, "Modifying: " + geometryEvent.getID() + ", " + geometryEvent.getLayer().getName());
    }


//    @Override
//    public void geometrySnapped(Point2D point2D) {
//        Log.d(mTag, "Snapped Point: " + point2D.toJson());
//    }
}
