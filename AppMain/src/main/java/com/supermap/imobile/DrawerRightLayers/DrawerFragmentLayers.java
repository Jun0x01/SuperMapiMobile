package com.supermap.imobile.DrawerRightLayers;

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
import android.widget.TextView;

import com.jun.tools.toolkit.CheckThread;
import com.supermap.data.Workspace;
import com.supermap.imobile.DrawerLeftWorkspace.MapState;
import com.supermap.imobile.R;
import com.supermap.mapping.Map;
import com.supermap.mapping.view.LayerListView;

/**
 *
 */
@TargetApi(11)
public class DrawerFragmentLayers extends Fragment implements View.OnClickListener{
    private static final String tag = "DrawerFragmentLayers";

    private DrawerLayout mDrawerLayout;

	private View mRootView;

	DrawerTop mTop;
	private Activity mActivity;
    
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(tag, "onCreateView");
		int layoutId = R.layout.drawer_right_layers;
		View layout = inflater.inflate(layoutId, container, false);

		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		// Can not use activity as parameter
//		mTop = new DrawerTop(layout);
		mRootView = layout;
		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(tag, "onActivityCreated");
		mActivity = getActivity();
		closeDrawer();
		initView();

		ViewParent viewParent = mRootView.getParent();
		if(viewParent instanceof DrawerLayout){
			setUp((DrawerLayout) viewParent);
		}


	}

	private void closeDrawer() {

		if(null != mDrawerLayout){
			mDrawerLayout.closeDrawer(mRootView);
		}
	}

	public void setUp(DrawerLayout drawerLayout) {
		mDrawerLayout = drawerLayout;
		mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {

			}

			@Override
			public void onDrawerOpened(View drawerView) {
//				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				mDrawerLayout.requestLayout();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
//				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}

			@Override
			public void onDrawerStateChanged(int newState) {

			}
		});
	}

	private Map mMap;
	private Workspace mWorkspace;

	public void setMap(Map map) {
		mMap = map;
		mWorkspace = mMap.getWorkspace();
	}

	public void setMapGL(Map mapGL){
		if(mapGL != null){
			mLayersListViewGL.loadMap(mapGL);
		}else {

		}
	}

/********************* Initialize View *************************/
	private LayerListView mLayersListView;
	private TextView mTextView_MapName;
	private LayerListView mLayersListViewGL;
	private TextView mTextView_MapNameGL;
	/**
	 * Initialize views
	 */
	private void initView() {
		mLayersListView = (LayerListView) mRootView.findViewById(R.id.layerList);
		mTextView_MapName = (TextView) mRootView.findViewById(R.id.txt_Layers_MapName);

		if(mMap != null)
			mLayersListView.loadMap(mMap);

		mLayersListViewGL = (LayerListView) mRootView.findViewById(R.id.layerListGL);
	}

	public void reloadLayers(){
		if (mLayersListView != null) {
			if (CheckThread.isMainThread()) {
				mLayersListView.reload();
				mLayersListViewGL.reload();
				if(MapState.isNewMap) {
					mTextView_MapName.setText(MapState.mapName);
				} else {
					mTextView_MapName.setText(mMap.getName());
				}
			} else {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mLayersListView.reload();
						mLayersListViewGL.reload();
						if(MapState.isNewMap) {
							mTextView_MapName.setText(MapState.mapName);
						} else {
							mTextView_MapName.setText(mMap.getName());
						}
					}
				});
			}
		}
	}
	
/***************************************** OnClickListener **********************************************************/
	@Override
	public void onClick(View v) {
		switch (v.getId()){

		}
	}
/***************************************MultiSelectedListViewOnItemClickListener******************************************************/

}
