package com.supermap.imobile.DrawerLeftMain;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.jun.tools.Message.ShowMessage;
import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;
import com.supermap.imobile.R;
import com.supermap.imobile.TestToolBars.BrowseMap;
import com.supermap.imobile.TestToolBars.BrowseMap_Pan;
import com.supermap.imobile.TestToolBars.BrowseMap_Random;
import com.supermap.imobile.TestToolBars.BrowseMap_Zoom;
import com.supermap.mapping.LegendView;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;
import com.supermap.mapping.ScaleType;
import com.supermap.mapping.ScaleView;

/**
 *
 */
@TargetApi(11)
public class DrawerFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String tag = "DrawerFragmentMain";

    private DrawerLayout mDrawerLayout;

    private View mRootView;

    DrawerTop mTop;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(tag, "onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(tag, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(tag, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(tag, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(tag, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(tag, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        destroy();
        super.onDestroy();
        Log.d(tag, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(tag, "onDetach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "onCreate");

    }

    public void setUp(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.requestLayout();
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(tag, "onCreateView");
        int layoutId = R.layout.drawer_left_main;
        View layout = inflater.inflate(layoutId, container, false);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Can not use activity as parameter
        mTop = new DrawerTop(layout);
        mRootView = layout;
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(tag, "onActivityCreated");

        ViewParent viewParent = mRootView.getParent();
        if (viewParent instanceof DrawerLayout) {
            setUp((DrawerLayout) viewParent);
        }

        initView();

//		closeDrawer();
    }

    private void closeDrawer() {

        if (null != mDrawerLayout) {
            mDrawerLayout.closeDrawer(mRootView);
        }

    }

    /********************* Initialize View *************************/

    private Activity mActivity;
    private ViewGroup mLayout_TestToolGroup;

    private ScaleView mMapScaleView;
    private LegendView mMapLegendView;

    private MapView mMapView;
    private MapControl mMapControl;
    private Map mMap;

    private MapView mMapViewGL;
    private MapControl mMapControlGL;
    private Map mMapGL;

    private Switch mSwitch_MapScaleView;
    private Switch mSwitch_MapLegendView;
    private Switch mSwitch_OpenGL;
    private Switch mSwitch_RotateMap;
    private Switch mSwitch_SlantMap;
    private Switch mSwitch_DynamicProjection;
    private CheckBox mCheckBrowseMap_Random, mCheckBrowseMap_Pan, mCheckBrowseMap_Zoom, mCheckBrowseMap_LastChecked;
    private BrowseMap mBrowseMap_Pan, mBrowseMap_Zoom, mBrowseMap_Random, mBroseMap_Last;

    private void destroy() {
        mActivity = null;
        mMap = null;
        mMapControl = null;
        mMapView = null;

        mMapGL = null;
        mMapControlGL = null;
        mMapViewGL = null;
    }

    /**
     * Initialize views, called in {@link #onActivityCreated(Bundle)}
     */
    private void initView() {
        mActivity = getActivity();
        mMapView = (MapView) mActivity.findViewById(R.id.view_MapView);
        mMapControl = mMapView.getMapControl();
        mMap = mMapControl.getMap();

        mMapScaleView = (ScaleView) mActivity.findViewById(R.id.mapScaleView);
        mMapLegendView = (LegendView) mActivity.findViewById(R.id.mapLegendView);

        mMapScaleView.setMapView(mMapView);
        mMapScaleView.setScaleType(ScaleType.Global);
//        mMapScaleView.setLevelEnable(true);

        mMap.getLegend().connectLegendView(mMapLegendView);


        // BrowseMap
        mCheckBrowseMap_Random =  (CheckBox) mRootView.findViewById(R.id.checkbox_RandomBrowser);
        mCheckBrowseMap_Pan =  (CheckBox) mRootView.findViewById(R.id.checkbox_AutoPan);
        mCheckBrowseMap_Zoom =  (CheckBox) mRootView.findViewById(R.id.checkbox_AutoZoom);

        mCheckBrowseMap_Random.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (mCheckBrowseMap_LastChecked != null && mCheckBrowseMap_LastChecked != mCheckBrowseMap_Random)
                        mCheckBrowseMap_LastChecked.setChecked(false);

                    if (mBrowseMap_Random == null)
                        mBrowseMap_Random = new BrowseMap_Random(mMapControl);
                    mLayout_TestToolGroup.addView(mBrowseMap_Random.getView());
                    mCheckBrowseMap_LastChecked = mCheckBrowseMap_Random;
                }else {
                    mLayout_TestToolGroup.removeView(mBrowseMap_Random.getView());
                }
            }
        });
        mCheckBrowseMap_Pan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (mCheckBrowseMap_LastChecked != null && mCheckBrowseMap_LastChecked != mCheckBrowseMap_Pan)
                        mCheckBrowseMap_LastChecked.setChecked(false);

                    if (mBrowseMap_Pan == null) {
                        mBrowseMap_Pan = new BrowseMap_Pan(mMapControl);
                    }
                    mLayout_TestToolGroup.addView(mBrowseMap_Pan.getView());
                    mCheckBrowseMap_LastChecked = mCheckBrowseMap_Pan;
                }else {
                    mLayout_TestToolGroup.removeView(mBrowseMap_Pan.getView());
                }
            }
        });

        mCheckBrowseMap_Zoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (mCheckBrowseMap_LastChecked != null && mCheckBrowseMap_LastChecked != mCheckBrowseMap_Zoom)
                        mCheckBrowseMap_LastChecked.setChecked(false);

                    if (mBrowseMap_Zoom == null)
                        mBrowseMap_Zoom = new BrowseMap_Zoom(mMapControl);
                    mLayout_TestToolGroup.addView(mBrowseMap_Zoom.getView());
                    mCheckBrowseMap_LastChecked = mCheckBrowseMap_Zoom;
                }else {
                    mLayout_TestToolGroup.removeView(mBrowseMap_Zoom.getView());
                }
            }
        });

        mLayout_TestToolGroup = (ViewGroup) mActivity.findViewById(R.id.layout_TestToolGroup);

        // Switch button
        mSwitch_MapScaleView = (Switch) mActivity.findViewById(R.id.switch_ScaleView);
        mSwitch_MapLegendView = (Switch) mActivity.findViewById(R.id.switch_LegendView);
        mSwitch_OpenGL = (Switch) mActivity.findViewById(R.id.switch_OpenGL);
        mSwitch_RotateMap = (Switch) mActivity.findViewById(R.id.switch_RotateMap);
        mSwitch_SlantMap = (Switch) mActivity.findViewById(R.id.switch_SlantMap);
        mSwitch_DynamicProjection = (Switch) mActivity.findViewById(R.id.switch_DynamicProjection);

        boolean isVisible = SharedPreferenceManager.getBoolean("MapScaleViewVisibility", true);
        mSwitch_MapScaleView.setChecked(isVisible);
        if (isVisible) {
            mMapScaleView.setVisibility(View.VISIBLE);
        } else {
            mMapScaleView.setVisibility(View.GONE);
        }
        isVisible = SharedPreferenceManager.getBoolean("MapLegendViewVisibility", false);
        mSwitch_MapLegendView.setChecked(isVisible);

        if (isVisible) {
            mMapLegendView.setVisibility(View.VISIBLE);
        } else {
            mMapLegendView.setVisibility(View.GONE);
        }

        boolean isOpenGL = SharedPreferenceManager.getBoolean(SharedPreferenceManager.mKey_IsOpenGL);
        mSwitch_OpenGL.setChecked(isOpenGL);

        mSwitch_MapLegendView.setOnCheckedChangeListener(this);
        mSwitch_MapScaleView.setOnCheckedChangeListener(this);
        mSwitch_OpenGL.setOnCheckedChangeListener(this);
        mSwitch_RotateMap.setOnCheckedChangeListener(this);
        mSwitch_SlantMap.setOnCheckedChangeListener(this);
        mSwitch_DynamicProjection.setOnCheckedChangeListener(this);
        mSwitch_DynamicProjection.setChecked(mMapControl.getMap().isDynamicProjection());

    }

    public void enableMapViewGL(boolean enable) {
        if (enable) {
            mMapViewGL = (MapView) mActivity.findViewById(R.id.view_MapView_Background);
            mMapControlGL = mMapViewGL.getMapControl();
            mMapGL = mMapControlGL.getMap();
        }else {
            mMapViewGL = null;
            mMapControlGL = null;
            mMapGL = null;
        }
    }


    /************************************ OnClickListener *************************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    /*************************************** OnCheckedChangedListener ******************************************************/
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.switch_LegendView:
                setViewVisibility(mMapLegendView, isChecked);
                SharedPreferenceManager.getBoolean("MapLegendViewVisibility", isChecked);
                break;
            case R.id.switch_ScaleView:
                setViewVisibility(mMapScaleView, isChecked);
                SharedPreferenceManager.getBoolean("MapScaleViewVisibility", isChecked);
                break;
            case R.id.switch_OpenGL:
                ShowMessage.showInfo(tag, getString(R.string.device_id_type_note), mActivity);
                SharedPreferenceManager.setBoolean(SharedPreferenceManager.mKey_IsOpenGL, isChecked);
                break;
            case R.id.switch_RotateMap:
                mMapControl.enableRotateTouch(isChecked);
                if (mMapControl != null)
                    mMapControlGL.enableRotateTouch(isChecked);
                break;
            case R.id.switch_SlantMap:
                mMapControl.enableSlantTouch(isChecked);
                if (mMapControl != null)
                    mMapControlGL.enableSlantTouch(isChecked);
                break;
            case R.id.switch_DynamicProjection:
                mMap.setDynamicProjection(isChecked);
                if (mMapGL != null)
                    mMapGL.setDynamicProjection(isChecked);
                break;
            default:
                break;
        }
    }

    private void setViewVisibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

}
