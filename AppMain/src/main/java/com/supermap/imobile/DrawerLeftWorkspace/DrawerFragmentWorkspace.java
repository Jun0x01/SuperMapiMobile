package com.supermap.imobile.DrawerLeftWorkspace;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.jun.tools.View.JunBaseListAdapter;
import com.jun.tools.View.JunPagerAdapter;
import com.jun.tools.View.JunPagerTitle;
import com.jun.tools.toolkit.CheckThread;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.Maps;
import com.supermap.data.Workspace;
import com.supermap.imobile.ActivityMain.MainActivity;
import com.supermap.imobile.R;
import com.supermap.mapping.Map;

import java.util.ArrayList;

/**
 *
 */
@TargetApi(11)
public class DrawerFragmentWorkspace extends Fragment implements View.OnClickListener, UpdateFragmentWorkspaceListener{
    private static final String tag = "DrawerFragmentWorkspace";

    private DrawerLayout mDrawerLayout;

	private View mRootView;

	private Activity mActivity;

	DrawerTop mTop;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(tag, "onAttach");
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(tag, "onStart");
		if(mWorkspace != null && mText_WorkspaceName != null){
			String name = mWorkspace.getConnectionInfo().getName();
			mText_WorkspaceName.setText(name);
		}

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
		if(mActivity instanceof MainActivity){
			MainActivity.mMainActivity.setUpdateFragmentWorkspaceListener(null);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(tag, "onDetach");
		mActivity = null;
	}

	/*****************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(tag, "onCreate");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(tag, "onCreateView");
		int layoutId = R.layout.drawer_left_workspace;
		mRootView = inflater.inflate(layoutId, container, false);

		mRootView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		initViewPager(mRootView, inflater);

		return mRootView;
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

		if(mActivity instanceof MainActivity){
			MainActivity.mMainActivity.setUpdateFragmentWorkspaceListener(this);
		}
	}

	/*************************************************/
	/**
	 * Set the parent which is a DrawerLayout. If the DrawerLayout has been parent of this fragment, this method may not be called.
	 * @param drawerLayout The DrawLayout view which contains this fragment.
	 */
	public void setUp(DrawerLayout drawerLayout) {
		mDrawerLayout = drawerLayout;
		mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				mDrawerLayout.requestLayout();
				if (mDrawerLayout.isDrawerOpen(mRootView))
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, Gravity.LEFT); // mRootView?
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			}

			@Override
			public void onDrawerStateChanged(int newState) {

			}
		});

		mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(mDrawerLayout.isDrawerOpen(mRootView) && (event.getAction()& MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					int left = mRootView.getLeft();
					int right = mRootView.getRight();
					int top = mRootView.getTop();
					int bot = mRootView.getBottom();
					float x = event.getX();
					float y = event.getY();
					if(x <left || x >right || y<top || y > bot){
						closeDrawer();
//						Log.d(tag, "id" + v.getClass().getName() + ", X: " + event.getX() + ", Y: " + event.getY());
						return true;
					}
				}
//				Log.d(tag, "id" + v.getClass().getName() + ", X: " + event.getX() + ", Y: " + event.getY());
				return false;
			}
		});

		mDrawerLayout.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK && mDrawerLayout.isDrawerOpen(mRootView)){
					mDrawerLayout.closeDrawer(mRootView);
					return true;
				}
				return false;
			}
		});
	}

	private void closeDrawer() {

		if(null != mDrawerLayout){
			mDrawerLayout.closeDrawer(mRootView);
		}

	}


/********************* Initialize View *************************/

	private TextView mText_WorkspaceName;
	/**
	 * Initialize views
	 */
	private void initView() {
		mText_WorkspaceName = (TextView) mRootView.findViewById(R.id.txt_WorkspaceName);
	}
	
/***************************************** OnClickListener **********************************************************/
	@Override
	public void onClick(View v) {
		switch (v.getId()){

		}
	}
/***************************************MultiSelectedListViewOnItemClickListener******************************************************/


    private ViewPager mViewPager;
	private PagerMaps mPagerMaps;
	private PagerDatasets mPagerDatasets;
	private PagerDatasources mPagerDatasources;
	private JunPagerAdapter mPagerAdapter;

	private void initViewPager(View root, LayoutInflater inflater) {
		mViewPager = (ViewPager) root.findViewById(R.id.viewPager_Workspace);

		mPagerDatasources = new PagerDatasources(this, inflater, (ViewGroup) root);
		mPagerDatasets = new PagerDatasets(this, inflater, (ViewGroup) root);
		mPagerMaps = new PagerMaps(this, inflater, (ViewGroup) root);

		mPagerAdapter = new JunPagerAdapter();
		ArrayList<View> list = mPagerAdapter.getViewList();
		list.add(mPagerDatasources.getView());
		list.add(mPagerDatasets.getView());
		list.add(mPagerMaps.getView());

		mViewPager.setAdapter(mPagerAdapter);

		JunPagerTitle pagerTitle = (JunPagerTitle) root.findViewById(R.id.pager_title_workspace);
		pagerTitle.setViewPager(mViewPager);
	}

	/***********************************************/
	private Map mMap;
	private Workspace mWorkspace;
	private String mPath;

	public void setMap(Map map){
		mMap = map;
		mWorkspace = mMap.getWorkspace();

		mPagerDatasources.setMap(mMap);
		mPagerMaps.setMap(mMap);
		mPagerDatasets.setMap(mMap);
	}

    public void resetWorkspace(Workspace workspace){
        mWorkspace = workspace;
        mPagerDatasources.setMap(mMap);
        mPagerMaps.setMap(mMap);
        mPagerDatasets.setMap(mMap);
    }

    public void setMapGL(Map map){
		mPagerDatasets.setGLMap(map);
	}

	/*************************************************/

	/**
	 * Update all ListViews when new workspace is opened.
	 */
	private void updateListViewInThread(final Workspace workspace){

		if(CheckThread.isMainThread()) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					updateListView(workspace);
				}
			});
			thread.start();
			try {
				thread.join();
			}catch (InterruptedException e){

			}
		}else {
			updateListView(workspace);
		}

	}

	private void updateListView(final Workspace workspace){

		if (workspace != null) {
			final Datasources datasources = workspace.getDatasources();
			int count = datasources.getCount();
			String name = null;
			mPagerDatasources.clearItems();
			for (int i = 0; i < count; i++) {
				name = datasources.get(i).getAlias();
				mPagerDatasources.addItem(name);
			}

			Maps maps = workspace.getMaps();
			count = maps.getCount();
			mPagerMaps.clearItems();
			for (int i = 0; i < count; i++) {
				name = maps.get(i);
				mPagerMaps.addItem(name);
			}

			if (count > 0)
				updateListView(datasources.get(0), false);

		}else{
			mPagerDatasources.clearItems();
			mPagerMaps.clearItems();
			mPagerDatasets.clearItems();
		}

		// Update Views
		if(CheckThread.isMainThread()){
			String name = null;
			if(workspace != null) {
				name = workspace.getCaption();
			}else {
				name = "";
			}
			if (mText_WorkspaceName != null)
				mText_WorkspaceName.setText(name);

			if (mPagerDatasources.getItemCount() > 0)
				mPagerDatasources.setItemChecked(0, true);

			mPagerDatasources.notifyDataSetChanged();
			mPagerDatasets.notifyDataSetChanged();
			mPagerMaps.notifyDataSetChanged();
		}else {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String name = null;
					if(workspace != null) {
						name = workspace.getCaption();
					}else {
						name = "";
					}
					if (mText_WorkspaceName != null)
						mText_WorkspaceName.setText(name);

					if (mPagerDatasources.getItemCount() > 0)
						mPagerDatasources.setItemChecked(0, true);

					mPagerDatasources.notifyDataSetChanged();
					mPagerDatasets.notifyDataSetChanged();
					mPagerMaps.notifyDataSetChanged();
				}
			});
		}

	}

	private void updateListViewInThread(final  Datasource datasource, final boolean isDisplay){

		if (CheckThread.isMainThread()) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					updateListView(datasource, isDisplay);
				}
			});
			thread.start();
			try {
				thread.join();
			}catch (InterruptedException e){

			}

		} else {
			updateListView(datasource, isDisplay);
		}

	}

	/**
	 * Update the ListView of Datasets
	 * @param datasource
	 */
	public void updateListView(final  Datasource datasource, final boolean isDisplay){

		mPagerDatasets.clearItems();
		if (datasource == null)
			return;

		// datasource != null
		Datasets datasets = datasource.getDatasets();
		int count = datasets.getCount();
		String name = null;

		for (int i = 0; i < count; i++) {
			name = datasets.get(i).getName();
			mPagerDatasets.addItem(name);
		}
		mPagerDatasets.setDatasource(datasource);

		if (CheckThread.isMainThread()) {
			mPagerDatasets.notifyDataSetChanged();
			if (isDisplay) {
				mViewPager.setCurrentItem(mPagerAdapter.getViewList().indexOf(mPagerDatasets.getView()));
			}
		} else {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mPagerDatasets.notifyDataSetChanged();
					if (isDisplay) {
						mViewPager.setCurrentItem(mPagerAdapter.getViewList().indexOf(mPagerDatasets.getView()));
					}
				}
			});
		}

	}

	/**
	 * Update the page
	 * @param page
	 */
	public void updatePage(final JunBaseListAdapter page, final boolean isDisplay){

		if(CheckThread.isMainThread()){
			page.notifyDataSetChanged();
			if (isDisplay) {
				mViewPager.setCurrentItem(mPagerAdapter.getViewList().indexOf(page.getView()));
			}
		}else {

			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					page.notifyDataSetChanged();
					if (isDisplay) {
						mViewPager.setCurrentItem(mPagerAdapter.getViewList().indexOf(page.getView()));
					}
				}
			});
		}
	}

	public void showPager(View pager){
		mViewPager.setCurrentItem(mPagerAdapter.getViewList().indexOf(pager));
	}

	public void showPager(int index){
		mViewPager.setCurrentItem(index);
	}

	public void showNextPager(View pager){
		mViewPager.setCurrentItem(mPagerAdapter.getViewList().indexOf(pager) + 1);
	}

///  Update listener
	@Override
	public void onNewWorkspace(Workspace workspace) {
		resetWorkspace(workspace);
		updateListViewInThread(mWorkspace);
	}
	@Override
	public void onCloseWorkspace() {
		updateListViewInThread(null);
	}

	// New or open a datasource
	@Override
	public void onAddDatasource(Datasource datasource) {
		if(datasource != null) {
			mPagerDatasources.addItem(datasource.getAlias());
			updatePage(mPagerDatasources, false);
		}
	}
	@Override
	public void onCloseDatasource(String name) {
		if(name != null && mPagerDatasources.removeItem(name)){

			if(CheckThread.isMainThread()){
				mPagerDatasources.notifyDataSetChanged();
			}else {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mPagerDatasources.notifyDataSetChanged();
					}
				});
			}
		}
	}
	@Override
	public void onUpdateDatasource(Datasource datasource) {
		if(datasource != null && mPagerDatasets.isSameDatasource(datasource)){
			updateListView(datasource, false);
		}
	}

	@Override
	public void onSaveNewMap(String name) {
		if(name != null && mPagerMaps.addItem(name)){

			if(CheckThread.isMainThread()){
				mPagerMaps.notifyDataSetChanged();
				mPagerMaps.selectNew();
			}else {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mPagerMaps.notifyDataSetChanged();
						mPagerMaps.selectNew();
					}
				});

			}
		}
	}

	@Override
	public void onRemoveMap(String name) {
		if(name != null && mPagerMaps.removeItem(name)){
			if(CheckThread.isMainThread()){
				mPagerMaps.notifyDataSetChanged();
			}else {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mPagerMaps.notifyDataSetChanged();
					}
				});
			}
		}
	}
}
