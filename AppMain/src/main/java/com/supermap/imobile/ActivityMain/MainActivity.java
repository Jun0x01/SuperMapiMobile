package com.supermap.imobile.ActivityMain;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jun.tools.Location.Location;
import com.jun.tools.Location.LocationService;
import com.jun.tools.Message.ShowMessage;
import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTransParameter;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetImage;
import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceVersion;
import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;
import com.supermap.imobile.DrawerLeftMain.DrawerFragment;
import com.supermap.imobile.DrawerLeftWorkspace.DrawerFragmentWorkspace;
import com.supermap.imobile.DrawerLeftWorkspace.MapState;
import com.supermap.imobile.DrawerLeftWorkspace.UpdateFragmentWorkspaceListener;
import com.supermap.imobile.DrawerRightLayers.DrawerFragmentLayers;
import com.supermap.imobile.Menus.Dialog_Base;
import com.supermap.imobile.Menus.MenuBar;
import com.supermap.imobile.R;
import com.supermap.imobile.TestToolBars.MapRefreshTimeRecord;
import com.supermap.imobile.Worksapce.WorkspaceManager;
import com.supermap.mapping.Action;
import com.supermap.mapping.CallOut;
import com.supermap.mapping.CalloutAlignment;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapLoadedListener;
import com.supermap.mapping.MapView;
import com.supermap.mapping.MeasureListener;
import com.supermap.mapping.view.LayerListView;
import com.supermap.navi.NaviInfo;
import com.supermap.navi.NaviListener;
import com.supermap.navi.Navigation;
import com.supermap.navi.Navigation2;
import com.supermap.navi.Navigation3;
import com.supermap.plugin.SpeakPlugin;
import com.supermap.plugin.Speaker;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    public static Handler mHandler = null;
    public static MainActivity mMainActivity;

    MenuBar mMenuBar;
    String tag = "MainActivity";
    ProgressDialog mProgressDialog;

    private boolean mIsSetLayerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = this;

        ShowMessage.setActivity(this);
        setContentView(R.layout.activity_main);

        initViews();
        initFragments();

        mHandler = new MainHandler();

        MapState.isMapOpen = false;

        initLocation();
        initListener();
        initRefreshTimeLog();
        initNavi();
        initMeasure();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!mIsSetLayerListener) {
            View child = ((ViewGroup)((ViewGroup) ((ViewGroup) mLayersListView.getChildAt(0)).getChildAt(1))).getChildAt(0);
            if (child != null && child instanceof ListView) {
                ((ListView) child).setOnItemLongClickListener(new OnLayerItemLongClicked());
                ((ListView) child).setOnItemClickListener(new OnLayerItemClicked());
            }
            mIsSetLayerListener = true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }



    @Override
    protected void onDestroy() {
        MapState.isMapOpen = false;

        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        ShowMessage.setActivity(null);

        destroyMeasure();
        clearNavigation();

        dispsose();

        if(mLocationServiceCon != null) {
            unbindService(mLocationServiceCon);
            mLocationService = null;
        }

        super.onDestroy();
    }

    private long mLastClickTime;
    private boolean mIsClicked;

    @Override
    public void onBackPressed() {

        //
        if (isClosedDrawer())
            return;

        long curTime = SystemClock.currentThreadTimeMillis();
        if (!mIsClicked) {
            mLastClickTime = SystemClock.currentThreadTimeMillis();
            mIsClicked = true;
        } else if ((curTime - mLastClickTime) < 1000) {
            mIsClicked = false;
            super.onBackPressed();
        } else {
            mIsClicked = false;
        }
    }

    /**
     * Check and close opened Drawers
     *
     * @return
     */
    boolean isClosedDrawer() {
        boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(Gravity.LEFT)
                | mDrawerLayout.isDrawerOpen(Gravity.RIGHT);
        isDrawerOpen |= mDrawerMapView.isDrawerOpen(Gravity.LEFT)
                | mDrawerMapView.isDrawerOpen(Gravity.RIGHT);

        mDrawerLayout.closeDrawers();
        mDrawerMapView.closeDrawers();
        return isDrawerOpen;
    }

    /*********************** Initialize Views*************************/

    private MapView mMapView;
    private MapControl mMapControl;
    private Map mMap;
    private Workspace mWorkspace;

    private MapView mMapViewGL;
    private MapControl mMapControlGL;
    private Map mMapGL;

    private void dispsose() {
        if (mMap != null) {
            mMap.close();
            mMap.dispose();
            mMap = null;
        }
        if (mMapControl != null) {
            mMapControl.dispose();
            mMapControl = null;
        }
        if (mMapGL != null) {
            mMapGL.close();
            mMapGL.dispose();
            mMapGL = null;
        }
        if (mMapControlGL != null) {
            mMapControlGL.dispose();
            mMapControlGL = null;
        }

        if (mWorkspace != null) {
            mWorkspace.close();
            mWorkspace.dispose();
            mWorkspace = null;
        }
    }

    private void resetWorkspace(Workspace workspace) {
        if (mMap != null) {
            mMap.close();
            mMap.refresh();
        }
        if (mMapGL != null) {
            mMapGL.close();
            mMapGL.refresh();
        }

        mWorkspace.close();

        MapState.isNewMap = true;
        MapState.mapName = null;

        mMap.setWorkspace(workspace);
        if (mMapGL != null)
            mMapGL.setWorkspace(workspace);

        mWorkspace.dispose();
        mWorkspace = workspace;
    }

    private View mHeader;
    private View mMenu;
    private LayerListView mLayersListView;

    private ViewGroup mLayoutEditTools;
    private GeometryEditTools mEditTools;

    private void initViews() {

        mMapView = (MapView) findViewById(R.id.view_MapView);
        mMapControl = mMapView.getMapControl();
        mMap = mMapControl.getMap();

        mWorkspace = mMap.getWorkspace();
        mLayersListView = (LayerListView) findViewById(R.id.layerList);

//        mMapViewGL = (MapView) findViewById(R.id.view_MapView_Background);
//        mMapControlGL = mMapViewGL.getMapControl();
//        mMapGL = mMapControlGL.getMap();
//        mMapGL.setWorkspace(mWorkspace);

//        mMapViewGL.addOverlayMap(mMapControl);
//        mMapControl.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (v instanceof MapControl)(
//                    mMapView.onTouchEvent(event);
//                    mMapViewGL.onTouchEvent(event);
//                }
//                return false;
//            }
//        });



        initDrawerLayout();

        mHeader = findViewById(R.id.layout_main_header);
        mMenu = findViewById(R.id.text_Menu);
        mMapView.getMapControl().setOnTouchListener(new TouchListner());

        mMenuBar = new MenuBar(this, mHeader);

        findViewById(R.id.text_Menu).setOnClickListener(this);

        findViewById(R.id.zoom_in).setOnClickListener(this);
        findViewById(R.id.zoom_out).setOnClickListener(this);
        findViewById(R.id.location).setOnClickListener(this);
        findViewById(R.id.fullscreen).setOnClickListener(this);
        findViewById(R.id.refresh).setOnClickListener(this);
        findViewById(R.id.btn_navi_dest_point).setOnClickListener(this);
        findViewById(R.id.btn_navi_start_navi).setOnClickListener(this);
        findViewById(R.id.btn_navi_start_point).setOnClickListener(this);
        findViewById(R.id.btn_navi_clear).setOnClickListener(this);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mLayoutEditTools = (ViewGroup) findViewById(R.id.layout_edit_tools);

        mEditTools = new GeometryEditTools(mMapControl, mLayoutEditTools);

        mMap.setMapLoadedListener(new MapLoadedListener() {
            @Override
            public void onMapLoaded() {
                long curTime = System.currentTimeMillis();
                Log.d("MaoLoad", "MapLoad Time : " + (curTime - MapState.mapLoadStartTime) + "ms");
            }
        });

    }

    /**
     * @since SuperMap iMobile 9D
     * @param enable
     */
   public void enableMapViewGL(boolean enable) {
  /*       if (enable) {

            mMapViewGL = (MapView) findViewById(R.id.view_MapView_Background);
            mMapViewGL.setVisibility(View.VISIBLE);
            mMapControlGL = mMapViewGL.getMapControl();
            mMapGL = mMapControlGL.getMap();
            if (mWorkspace != null)
                mMapGL.setWorkspace(mWorkspace);

            mMapViewGL.addOverlayMap(mMapControl);

            if (mFragmentWorkspace != null)
                mFragmentWorkspace.setMapGL(mMapGL);

        } else {

            mMapViewGL.setVisibility(View.GONE);

            mMapViewGL.removeOverlayMap(mMapControl);
            if (mFragmentWorkspace != null)
                mFragmentWorkspace.setMapGL(null);

            mMapViewGL = null;
            mMapControlGL = null;
            mMapGL = null;
        }

        if (mFragmentLeft != null)
            mFragmentLeft.enableMapViewGL(true);

        if(mFragmentLayers != null)
            mFragmentLayers.setMapGL(mMapGL);
*/
    }

    public void enableEditTools(boolean enable) {
        if (enable) {
            mLayoutEditTools.setVisibility(View.VISIBLE);
            measure(0);
        } else {
            mLayoutEditTools.setVisibility(View.GONE);
            mEditTools.resetViewState();
        }
    }

    public Workspace getWorkspace() {
        return mWorkspace;
    }

    public Map getMap() {
        return mMap;
    }

    public MapControl getMapControl() {
        return mMapControl;
    }

    class TouchListner implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            Log.d("JunPMainActivity", "Action: " + event.getAction() + ", Touch: " + event.getX() + ", " + event.getY());
            return false;
        }
    }

    /*********************************************/
    private DrawerLayout mDrawerLayout;
    private DrawerLayout mDrawerMapView;

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mDrawerMapView = (DrawerLayout) findViewById(R.id.drawer_layout_MapView);
        mDrawerMapView.setScrimColor(0x00FFFFFF);

        mDrawerLayout.setOnTouchListener(new TouchListner());
    }

    private void closeDrawer() {
        View view = findViewById(R.id.fragment_drawer_left_Main);
        if (mDrawerLayout.isDrawerOpen(view)) {
            mDrawerLayout.closeDrawer(view);
        } else {
            mDrawerLayout.openDrawer(view);
        }
    }

    /*********************** Initialize Fragments*************************/

    private DrawerFragment mFragmentLeft;
    private DrawerFragmentWorkspace mFragmentWorkspace;
    private DrawerFragmentLayers mFragmentLayers;

    private void initFragments() {
        FragmentManager manager = getFragmentManager();
        mFragmentLeft = (DrawerFragment) manager.findFragmentById(R.id.fragment_drawer_left_Main);

        mFragmentWorkspace = (DrawerFragmentWorkspace) manager.findFragmentById(R.id.fragment_drawer_left_Workspace);

        mFragmentWorkspace.setMap(mMap);
        mFragmentWorkspace.setMapGL(mMapGL);

        mFragmentLayers = (DrawerFragmentLayers) manager.findFragmentById(R.id.fragment_drawer_right_Layers);
        mFragmentLayers.setMap(mMap);

    }

    /******************** OnClickListener **********************/
    @Override
    public void onClick(View v) {

        int zoomTime = 100;
        switch (v.getId()) {
            case R.id.text_Menu:
                closeDrawer();
                break;

            case R.id.zoom_in:
                double scale = mMap.getScale();
                mMapControl.zoomTo(scale * 2, zoomTime);
//                mMap.zoom(2);
                mMap.refresh();
                if (mMapGL != null) {
//                    mMapGL.zoom(2);
                    mMapControlGL.zoomTo(mMapGL.getScale() * 2, zoomTime);
                    mMapGL.refresh();
                }
                break;
            case R.id.zoom_out:
                mMap.zoom(0.5);
                mMap.refresh();
                if (mMapGL != null) {
                    mMapGL.zoom(0.5);
                    mMapGL.refresh();
                }
                break;
            case R.id.fullscreen:
                mMap.viewEntire();
                mMap.refresh();
                if (mMapGL != null) {
                    mMapGL.viewEntire();
                    mMapGL.refresh();
                }
                break;
            case R.id.refresh:
//                mMap.setCenter(mMapGL.getCenter());
//                mMap.setScale(mMapGL.getScale());
                mMap.refresh();
                if (mMapGL != null)
                    mMapGL.refresh();
                break;
            case R.id.location:
                if(mLocationService != null){
                    Location location = mLocationService.getLocation();
                    located(location.getX(), location.getY());
                }
                break;

            case R.id.btn_navi_start_navi:
                startNavi();
                break;
            case R.id.btn_navi_dest_point:
                enableNaviDest(true);
                ShowMessage.showInfo(getString(R.string.tag_navi), getString(R.string.long_touch_map));
                break;
            case R.id.btn_navi_start_point:
                enableNaviStart(true);
                ShowMessage.showInfo(getString(R.string.tag_navi), getString(R.string.long_touch_map));
                break;
            case R.id.btn_navi_clear:
                resetNavi();
                break;
            default:
                break;
        }
    }


    /*******************************************************************/


    /******************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (ActivityRequestCodes.SELECT_FILE_CODE_MAIN == requestCode && RESULT_OK == resultCode) {

            Uri uri = data.getData();
            String path = null;
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = {"_data"};
                Cursor cursor = null;
                cursor = this.getContentResolver().query(uri, projection, null, null, null);
                int columnIndex = cursor.getColumnIndex("_data");
                if (columnIndex > -1) {
                    path = cursor.getString(columnIndex);

                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            }
            if (path != null && !path.isEmpty()) {

                SharedPreferences preferences = getSharedPreferences(MainApplication.m_SharedPreferenceName, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                File file = new File(path);
                if (file.isDirectory()) {
                    editor.putString(MainApplication.mKey_LastPath, path);
                } else {
                    editor.putString(MainApplication.mKey_LastPath, file.getParent());
                }
                editor.apply();
                editor.commit();

                if (mPopDialog != null)
                    mPopDialog.onFileSelected(path);

            }
            return;
        } else if (ActivityRequestCodes.REQUEST_LOGIN == requestCode && RESULT_OK == resultCode) {
            String name = data.getStringExtra("User");
            ((TextView) findViewById(R.id.text_UserName)).setText(name);
            if (name != null && !name.isEmpty()) {
                findViewById(R.id.imgBtn_User).setActivated(true);
            } else {
                findViewById(R.id.imgBtn_User).setActivated(true);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private Dialog_Base mPopDialog;

    /**
     * Set a dialog object when showing, and set null when dismissing.
     *
     * @param dialog
     */
    public void setOnFileSelectedDialog(Dialog_Base dialog) {
        mPopDialog = dialog;
    }

    /************** MainHandler *****************/
    public class MainHandlerTag {
        public static final int RELOADLAYERS = 0x1;
        public static final int TOAST_ERROR = 0x2;
        public static final int TOAST_WARNING = 0x3;
        public static final int DATAOPEN = 0x4;
        public static final int MAPOPEN = 0x5;
    }

    private class MainHandler extends Handler {

        public MainHandler() {
        }

        public MainHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MainHandlerTag.RELOADLAYERS:
                    mFragmentLayers.reloadLayers();
                    mDrawerMapView.openDrawer(findViewById(R.id.fragment_drawer_right_Layers));
                    mDrawerMapView.closeDrawer(findViewById(R.id.fragment_drawer_left_Workspace));
                    break;

                case MainHandlerTag.TOAST_ERROR: {
                    Bundle data = msg.getData();
                    String tag = data.get("tag").toString();
                    String content = data.get("content").toString();
                    ShowMessage.showError(tag, content, MainActivity.this);
                }
                break;
                case MainHandlerTag.TOAST_WARNING: {
                    Bundle data = msg.getData();
                    String tag = data.get("tag").toString();
                    String content = data.get("content").toString();
                    ShowMessage.showInfo(tag, content, MainActivity.this);
                }
                break;
                case MainHandlerTag.DATAOPEN:
                    mDrawerMapView.openDrawer(findViewById(R.id.fragment_drawer_left_Workspace));
                    mDrawerMapView.closeDrawer(findViewById(R.id.fragment_drawer_right_Layers));
                    break;
                case MainHandlerTag.MAPOPEN:
                    mDrawerMapView.openDrawer(findViewById(R.id.fragment_drawer_right_Layers));
                    break;
                default:
                    break;

            }
        }


    }

    /**************************** ------------------- *************************/
    /*************************** -- WorkspaceManger -- *************************/

    private ArrayList<String> mListOpenedFile = new ArrayList<>();

    private UpdateFragmentWorkspaceListener mUpdateFragmentWorkspaceListener;

    /**
     * @param listener
     */
    public void setUpdateFragmentWorkspaceListener(UpdateFragmentWorkspaceListener listener) {
        if (listener != null && listener != mUpdateFragmentWorkspaceListener) {
            mUpdateFragmentWorkspaceListener = listener;
        }
    }


    public String getValidDatasourceName(String name) {
        return WorkspaceManager.getValidDatasourceName(mWorkspace, name);
    }

    /**************************** Create Workspace and Create Datasource *********************************/
    public void createWorkspaceInThread(final String path, final String name, final String password, final WorkspaceVersion version) {
        if (mListOpenedFile.contains(path)) {
            ShowMessage.showInfo(tag, getString(R.string.workspace_was_opened));
            return;
        }
        mProgressDialog.setMessage(getString(R.string.creating_workspace));
        mProgressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Workspace workspace = new Workspace();
                boolean isCreated = WorkspaceManager.createWorkspace(workspace, path, name, password, version);

                if (isCreated) {
                    MapState.isWorkspaceOpen = true;
                    resetWorkspace(workspace);
                    mFragmentLayers.reloadLayers();

                    if (mUpdateFragmentWorkspaceListener != null)
                        mUpdateFragmentWorkspaceListener.onNewWorkspace(workspace);
                    if (mPopDialog != null)
                        mPopDialog.dismiss();

                    mListOpenedFile.add(path);

                    Message msg = new Message();
                    msg.what = MainHandlerTag.DATAOPEN;
                    mHandler.sendMessage(msg);

                    ShowMessage.showInfo(tag, getString(R.string.create_workspace_success));
                } else {
                    ShowMessage.showInfo(tag, getString(R.string.create_workspace_fail));
                }

                mProgressDialog.dismiss();
            }
        });
        thread.start();
    }

    public void createDatasourceInThread(final String path, final String password, final EngineType engineType, final String name) {
        if (mListOpenedFile.contains(path)) {
            ShowMessage.showInfo(tag, getString(R.string.datasource_was_opened));
            return;
        }
        mProgressDialog.setMessage(getString(R.string.creating_datasource));
        mProgressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Datasource datasource = WorkspaceManager.createDatasource(mWorkspace, path, password, engineType, name);

                if (datasource != null) {

                    if (mUpdateFragmentWorkspaceListener != null)
                        mUpdateFragmentWorkspaceListener.onAddDatasource(datasource);
                    if (mPopDialog != null)
                        mPopDialog.dismiss();

                    mListOpenedFile.add(path);

                    Message msg = new Message();
                    msg.what = MainHandlerTag.DATAOPEN;
                    mHandler.sendMessage(msg);

                    ShowMessage.showInfo(tag, getString(R.string.create_datasource_success));
                } else {
                    ShowMessage.showInfo(tag, getString(R.string.create_datasource_fail));
                }
                mProgressDialog.dismiss();

            }
        });
        thread.start();
    }

    /*************************** Open Workspace and Open Datasource ***************************/
    public void openWorkspaceInThread(final String path, final String password, final WorkspaceVersion version) {
        if (mListOpenedFile.contains(path)) {
            ShowMessage.showInfo(tag, getString(R.string.workspace_was_opened));
            return;
        }
        mProgressDialog.setMessage(getString(R.string.open_workspace));
        mProgressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Workspace workspace = new Workspace();
                boolean isOpen = WorkspaceManager.openWorkspace(workspace, path, password, version);

                if (isOpen) {
                    MapState.isWorkspaceOpen = true;
                    resetWorkspace(workspace);
                    mFragmentLayers.reloadLayers();

                    if (mUpdateFragmentWorkspaceListener != null)
                        mUpdateFragmentWorkspaceListener.onNewWorkspace(workspace);
                    if (mPopDialog != null)
                        mPopDialog.dismiss();

                    mListOpenedFile.add(path);

                    Message msg = new Message();
                    msg.what = MainHandlerTag.DATAOPEN;
                    mHandler.sendMessage(msg);

                    ShowMessage.showInfo(tag, getString(R.string.open_workspace_success));
                } else {
                    ShowMessage.showError(tag, getString(R.string.open_workspace_fail));
                }

                mProgressDialog.dismiss();
            }
        });
        thread.start();
    }

    public void openDatasourceInThread(final String path, final EngineType engineType, final String password, final String name, final  boolean isPyramid) {
        if (mListOpenedFile.contains(path)) {
            if (path.startsWith("http")) {
                ShowMessage.showInfo(tag, getString(R.string.datasource_was_opened_web));
            } else {
                ShowMessage.showInfo(tag, getString(R.string.datasource_was_opened));
            }

            return;
        }
        mProgressDialog.setMessage(getString(R.string.open_datasource));
        mProgressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Datasource datasource = WorkspaceManager.openDatasource(mWorkspace, path, name, password, engineType);
                if (datasource != null) {
                    if(isPyramid) {
                        DatasetImage datasetImage = (DatasetImage) datasource.getDatasets().get(0);
                        datasetImage.setOriginalPyramid(true);
                        if(!datasetImage.getHasPyramid()){
                            datasetImage.close();
                            boolean isTrue = datasetImage.buildPyramid();

                            ShowMessage.showError("Main", "Build Pyramid: " + isTrue);
                        }
                    }
                    if (mUpdateFragmentWorkspaceListener != null)
                        mUpdateFragmentWorkspaceListener.onAddDatasource(datasource);
                    if (mPopDialog != null)
                        mPopDialog.dismiss();

                    mListOpenedFile.add(path);

                    Message msg = new Message();
                    msg.what = MainHandlerTag.DATAOPEN;
                    mHandler.sendMessage(msg);

                    ShowMessage.showInfo(tag, getString(R.string.open_datasource_success));
                } else {
                    ShowMessage.showError(tag, getString(R.string.open_datasource_fail));
                }

                mProgressDialog.dismiss();

            }
        });
        thread.start();
    }


    /********************************* Import and Export ***********************************************/
    public void importFile(final String path) {

    }

    public void exportFile(final Dataset dataset, final String path) {
    }


    /*************************** Save and Close Map *****************************/
    public boolean saveMap() {
        boolean result = false;
        try {
            result = mMap.save();

            if (result) {
                ShowMessage.showInfo(tag, getString(R.string.saved_map) + mMap.getName());
            } else {
                ShowMessage.showError(tag, getString(R.string.save_map_failed));
            }
        } catch (Exception e) {
            e.printStackTrace();

            ShowMessage.showError(tag, getString(R.string.save_map_exception) + "\n" + e.toString());
        }

        return result;
    }

    public boolean saveAsMap(String name) {
        boolean result = false;
        if (name == null || name.isEmpty()) {
            ShowMessage.showInfo(tag, getString(R.string.input_name));
            return result;
        }
        if (mWorkspace.getMaps().indexOf(name) >= 0) {
            ShowMessage.showInfo(tag, getString(R.string.map_existed));
            return result;
        }
        try {
            if (MapState.isNewMap) {
                if (mMap.getLayers().getCount() > 0) {
                    result = mMap.save(MapState.mapName);
                    if(result) {
                        mUpdateFragmentWorkspaceListener.onSaveNewMap(name);
                        mFragmentLayers.reloadLayers();
                        ShowMessage.showInfo(tag, getString(R.string.saved_new_map) + MapState.mapName);
                    }else {
                        ShowMessage.showError(tag, getString(R.string.save_map_failed));
                    }
                } else {
                    ShowMessage.showError(tag, getString(R.string.save_map_not_necessary));
                }
            } else {
                result = mMap.saveAs(name);

                if (result) {
                    MapState.isNewMap = false;
                    MapState.mapName = name;
                    mUpdateFragmentWorkspaceListener.onSaveNewMap(name);
                    mFragmentLayers.reloadLayers();
                    ShowMessage.showInfo(tag, getString(R.string.saved_as_map) + name);
                } else {
                    ShowMessage.showError(tag, getString(R.string.save_map_failed));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage.showError(tag, getString(R.string.save_map_exception) + "\n" + e.toString());
        }
        return result;
    }

    public void closeMap() {
        if (MapState.isMapOpen) {
            String name = mMap.getName();
            if (MapState.isNewMap)
                name = "New Map";

            mMap.close();
            clearTempViews();
            if (mMapGL != null) {
                mMapGL.close();
                mMapGL.refresh();
            }

            MapState.isNewMap = true;
            MapState.mapName = null;
            mFragmentLayers.reloadLayers();
            ShowMessage.showInfo(tag, getString(R.string.closed_map) + name);
        }
    }

    private void clearTempViews() {
        mMapView.removeAllCallOut();
        mMapView.removeAllDynamicView();
        if (mMapViewGL != null) {
            mMapViewGL.removeAllCallOut();
            mMapViewGL.removeAllDynamicView();
        }
    }

    /*************************** Save and Close Workspace *********************************/
    public boolean saveWorkspace() {
        boolean result = false;
        try {
            result = mWorkspace.save();
            if (result) {
                ShowMessage.showInfo(tag, getString(R.string.saved_workspace) + mWorkspace.getCaption());
            } else {
                ShowMessage.showError(tag, getString(R.string.save_workspace_failed));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage.showError(tag, getString(R.string.save_workspace_exception) + "\n" + e.toString());
        }
        return result;
    }

    public boolean saveAsWorkspace(String path, String name, String password, WorkspaceVersion version) {
        boolean result = false;
        WorkspaceConnectionInfo info = mWorkspace.getConnectionInfo();
        String oldPath = info.getServer();
        if (path == null || path.isEmpty()) {
            ShowMessage.showError(tag, getString(R.string.empty_path));
            return result;
        }

        if (oldPath != null && !oldPath.isEmpty() && oldPath.equals(path)) {
            ShowMessage.showError(tag, getString(R.string.path_not_change));
            return result;
        }

        if (new File(path).exists()) {
            ShowMessage.showError(tag, getString(R.string.path_existed));
            return result;
        }

        int index1 = path.lastIndexOf('/');
        int index2 = path.lastIndexOf('.');
        if (name == null && index1 > 0 && index2 > index1) {
            name = path.substring(index1 + 1, index2);
        }

        info.setServer(path);
        info.setPassword(password);
        info.setName(name);

        if (version != null)
            info.setVersion(version);
        mWorkspace.setCaption(name);

        try {
            result = mWorkspace.save();
            if (result) {
                ShowMessage.showInfo(tag, getString(R.string.saved_workspace) + mWorkspace.getCaption());
            } else {
                ShowMessage.showError(tag, getString(R.string.save_workspace_failed));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage.showError(tag, getString(R.string.save_workspace_exception) + "\n" + e.toString());
        }

        return result;
    }

    public void closeWorkspace() {
        String name = mWorkspace.getCaption();
        mMap.close();
        clearTempViews();

        if (mMapGL != null) {
            mMapGL.close();
            mMapGL.refresh();
        }

        mWorkspace.getDatasources().closeAll();
        mWorkspace.close();

        MapState.isNewMap = true;
        MapState.isWorkspaceOpen = false;
        MapState.isMapOpen = false;
        MapState.mapName = null;
        mFragmentLayers.reloadLayers();
        ShowMessage.showInfo(tag, getString(R.string.close_workspace) + name);

        mListOpenedFile.clear();

        mFragmentWorkspace.onCloseWorkspace();
        mFragmentWorkspace.setMap(mMap);

        SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi2_Dataset, "");
        SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi2_Datasource, "");
        SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi3_Datasource, "");
    }

    /*******************************************************************/

    private Intent mIntentLocationService;
    private LocationServiceConnection mLocationServiceCon;
    private LocationService mLocationService;

    private void initLocation() {

        mIntentLocationService = new Intent();
        mIntentLocationService.setClass(getApplicationContext(), LocationService.class);
        mLocationServiceCon = new LocationServiceConnection();
        bindService(mIntentLocationService, mLocationServiceCon, Context.BIND_AUTO_CREATE);

    }

    class LocationServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(service instanceof LocationService.ServiceBinder){
                LocationService.ServiceBinder binder = (LocationService.ServiceBinder) service;
                mLocationService = binder.getService();
                Log.d("Main", "Binder bind");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    CallOut mDynamicLocation;
    private void located(double x, double y) {
        Point2D pos = new Point2D(x, y);
        PrjCoordSys mapPrj = mMap.getPrjCoordSys();
        if (mapPrj.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
            PrjCoordSys srcPrj = new PrjCoordSys(PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE);

            Point2Ds pts = new Point2Ds();
            pts.add(pos);
            CoordSysTranslator.convert(pts, srcPrj,
                    mapPrj, new CoordSysTransParameter(),
                    CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);

            pos = pts.getItem(0);
        }

        Rectangle2D bounds = mMap.getBounds();
        if (bounds.contains(pos)) {
            if(mDynamicLocation == null){
                mDynamicLocation = new CallOut(this);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_dot);
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(bitmap);
                mDynamicLocation.setCustomize(true);
                mDynamicLocation.setContentView(imageView);
            }

            mDynamicLocation.setLocation(pos.getX(), pos.getY());
            mMapView.removeCallOut("Location");
            mMapView.addCallout(mDynamicLocation, "Location");

            mMapControl.panTo(pos, 500);
        } else {
            ShowMessage.showInfo("Location", getString(R.string.location_outside_map));
        }

    }


    /****************************** Navigation ********************************************/

    // Navigation
    private Navigation mNavigation;
    private Navigation2 mNavigation2;
    private Navigation3 mNavigation3;

    private volatile boolean mIsNavi_Data_Connected, mIsNavi2_Data_Loaded, mIsNavi3_Data_Connected;

    private void initNavi() {
        mNavigation = mMapControl.getNavigation();
        mNavigation2 = mMapControl.getNavigation2();
        mNavigation3 = mMapControl.getNavigation3();
//        mNavigation.setPathVisible(true);
//        mNavigation2.setPathVisible(true);
//        mNavigation3.setPathVisible(true);
        NavigationListener listener = new NavigationListener();
        mNavigation.addNaviInfoListener(listener);
        mNavigation2.addNaviInfoListener(listener);
        mNavigation3.addNaviInfoListener(listener);

        SpeakPlugin.getInstance().setSpeaker(Speaker.CONGLE);

        String navi_DataPath_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi_Path);
        String navi2_DataPath_Stored = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Navi2_Path);
        if (!navi_DataPath_Stored.isEmpty())
            mIsNavi_Data_Connected = mNavigation.connectNaviData(navi_DataPath_Stored);

        Log.d("Navi", "isConnected: " + mIsNavi_Data_Connected + ", isLoaded: " + mIsNavi_Data_Connected);
    }

    class NavigationListener implements NaviListener {

        @Override
        public void onNaviInfoUpdate(NaviInfo naviInfo) {

        }

        @Override
        public void onStartNavi() {
            findViewById(R.id.layout_map_tools).setVisibility(View.GONE);
        }

        @Override
        public void onAarrivedDestination() {
            findViewById(R.id.layout_map_tools).setVisibility(View.VISIBLE);
        }

        @Override
        public void onStopNavi() {
            findViewById(R.id.layout_map_tools).setVisibility(View.VISIBLE);
        }

        @Override
        public void onAdjustFailure() {

        }

        @Override
        public void onPlayNaviMessage(String s) {

        }
    }

    public void updateNavi_DataState(boolean state) {
        mIsNavi_Data_Connected = state;
    }

    public void updateNavi2_DataState(boolean state) {
        mIsNavi2_Data_Loaded = state;
    }

    public void updateNavi3_DataState(boolean state) {
        mIsNavi3_Data_Connected = state;
    }

    public void resetNavi() {
        mNavigation.cleanPath();
        mNavigation2.cleanPath();
        mNavigation3.cleanPath();
        mMapView.removeAllCallOut();
        if (mMapViewGL != null)
            mMapViewGL.removeAllCallOut();

        resetTouchState();
    }

    // TouchListener
    private void initListener() {
        mMapControl.setGestureDetector(new GestureDetector(new MapTouchListener()));
    }

    private boolean mIsNavi;
    private boolean mIsNavi_2;
    private boolean mIsnavi_3;
    private int mNaviType = 0; // 1, 2, 3 -> Navi, Navi2, Navi3;

    public void setNaviType(int type) {
        mNaviType = type;
        if (type == 0) {
            findViewById(R.id.layout_navigation_bar).setVisibility(View.GONE);
        } else {
            findViewById(R.id.layout_navigation_bar).setVisibility(View.VISIBLE);
        }
    }

    private void clearNavigation() {
        SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi2_Dataset, null);
        SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi2_Datasource, null);
        SharedPreferenceManager.setString(SharedPreferenceManager.mKey_Navi3_Datasource, null);
        resetNavi();
        resetTouchState();
    }

    private boolean mIsMarker;
    private boolean mIsNavi_Start;
    private boolean mIsNavi_Dest;
    private boolean mIsNavi_Way;

    private void resetTouchState() {
        mIsMarker = false;
        mIsNavi_Dest = false;
        mIsNavi_Start = false;
        mIsNavi_Way = false;
    }

    public void enableMarker(boolean enable) {
        resetTouchState();
        mIsMarker = enable;
    }

    public void enableNaviStart(boolean enable) {
        resetTouchState();
        mIsNavi_Start = enable;
    }

    public void enableNaviDest(boolean enable) {
        resetTouchState();
        mIsNavi_Dest = enable;
    }

    public void enableNaviWay(boolean enable) {
        resetTouchState();
        mIsNavi_Way = enable;
    }

    public void startNavi() {

        mProgressDialog.setMessage(getString(R.string.loading_navigation));
        mProgressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startNavi0();
                mProgressDialog.dismiss();
            }
        });
        thread.start();
    }

    private void startNavi0() {
        int route = -1;

        if (1 == mNaviType) {
            if (!mIsNavi_Data_Connected) {
                ShowMessage.showInfo("Navi: " + mNaviType, getString(R.string.update_navi_data_needed));
            }
            route = mNavigation.routeAnalyst(0);
            if (route == 1) {
                mNavigation.startGuide(1);
            } else if (route == -1) {
                ShowMessage.showError("Navi: " + mNaviType, getString(R.string.no_path_start_position));

            } else if (route == -2) {
                ShowMessage.showError("Navi: " + mNaviType, getString(R.string.no_path_destnation));

            } else if (route == 0) {
                ShowMessage.showError("Navi: " + mNaviType, getString(R.string.route_analyst_failed));
            }
        } else if (2 == mNaviType) {
            if (!mIsNavi2_Data_Loaded) {
                ShowMessage.showInfo("Navi: " + mNaviType, getString(R.string.update_navi_data_needed));
            }
            boolean isRoute = mNavigation2.routeAnalyst();
            if (isRoute) {
                mNavigation2.startGuide(1);
            } else {
                ShowMessage.showError("Navi: " + mNaviType, getString(R.string.route_analyst_failed));
            }
        } else if (3 == mNaviType) {
            if (!mIsNavi3_Data_Connected) {
                ShowMessage.showInfo("Navi: " + mNaviType, getString(R.string.update_navi_data_needed));
            }
            boolean isRoute = mNavigation3.routeAnalyst();
            if (isRoute) {
                mNavigation3.startGuide(1);
            } else {
                ShowMessage.showError("Navi: " + mNaviType, getString(R.string.route_analyst_failed));
            }
        } else {
            ShowMessage.showInfo("Navi: " + mNaviType, getString(R.string.choose_a_navigation));
        }
    }

    // 长按监听事件
    class MapTouchListener extends GestureDetector.SimpleOnGestureListener {
        public void onLongPress(MotionEvent event) {
            if (mIsMarker) {
                showPointByCallout(event, "Marker", R.drawable.marker);
            } else if (mIsNavi_Start) {
                Point2D gpsPoint = getGPSPoint(event, "NaviStart", R.drawable.startpoint);
                double x = gpsPoint.getX();
                double y = gpsPoint.getY();
                if (1 == mNaviType) {
                    mNavigation.setStartPoint(x, y);
                } else if (2 == mNaviType) {
                    mNavigation2.setStartPoint(x, y);
                } else if (3 == mNaviType) {
                    mNavigation3.setStartPoint(x, y, "Start");
                }
            } else if (mIsNavi_Dest) {
                Point2D gpsPoint = getGPSPoint(event, "NaviDest", R.drawable.destpoint);
                double x = gpsPoint.getX();
                double y = gpsPoint.getY();

                if (1 == mNaviType) {
                    mNavigation.setDestinationPoint(x, y);
                } else if (2 == mNaviType) {
                    mNavigation2.setDestinationPoint(x, y);
                } else if (3 == mNaviType) {
                    mNavigation3.setDestinationPoint(x, y, "Destnation");
                }
            } else if (mIsNavi_Way) {
                Point2D gpsPoint = getGPSPoint(event, "NaviDest", R.drawable.startpoint);
                double x = gpsPoint.getX();
                double y = gpsPoint.getY();
                if (1 == mNaviType) {
                    mNavigation.addWayPoint(x, y);
                } else if (2 == mNaviType) {
//                    mNavigation2.set;
                } else if (3 == mNaviType) {
                    mNavigation3.addWayPoint(x, y, "Way");
                }
            }

        }

        // 地图漫游
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

    }

    ;


    /**
     * 将屏幕上的点转换为经纬坐标点
     *
     * @param event
     * @param pointName
     * @param idDrawable
     * @return
     */
    private Point2D getGPSPoint(MotionEvent event, final String pointName, final int idDrawable) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Point point = new Point();
        point.setX(x);
        point.setY(y);

        Point2D point2D = null;

        // 转换为地图上的二维点
        point2D = mMap.pixelToMap(point);
        if (idDrawable != 0 && mNaviType != 3) {
            showPointByCallout(point2D, pointName, idDrawable);
        }

        if (mMap.getPrjCoordSys().getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
            PrjCoordSys srcPrjCoordSys = mMap.getPrjCoordSys();
            Point2Ds point2Ds = new Point2Ds();
            point2Ds.add(point2D);
            PrjCoordSys desPrjCoordSys = new PrjCoordSys();
            desPrjCoordSys.setType(PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE);
            // 转换投影坐标
            CoordSysTranslator.convert(point2Ds, srcPrjCoordSys,
                    desPrjCoordSys, new CoordSysTransParameter(),
                    CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);

            point2D = point2Ds.getItem(0);
        }

        return point2D;
    }

    /**
     * 显示Callout
     *
     * @param event
     * @param pointName
     * @param idDrawable
     */
    private void showPointByCallout(MotionEvent event, final String pointName, final int idDrawable) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Point point1 = new Point();
        point1.setX(x);
        point1.setY(y);
        Point2D point = mMap.pixelToMap(point1);

        CallOut callOut = new CallOut(this);
        callOut.setStyle(CalloutAlignment.BOTTOM);
        callOut.setCustomize(true);
        callOut.setLocation(point.getX(), point.getY());
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(idDrawable);
        callOut.setContentView(imageView);

        if (mMapViewGL != null && mMapGL != null && mMapViewGL.getVisibility() == View.VISIBLE) {
            mMapViewGL.removeCallOut(pointName);
            mMapViewGL.addCallout(callOut, pointName);
        } else if (mMapView.getVisibility() == View.VISIBLE) {
            mMapView.removeCallOut(pointName);
            mMapView.addCallout(callOut, pointName);
        }
    }

    /**
     * 显示Callout
     *
     * @param point
     * @param pointName
     * @param idDrawable
     */
    private void showPointByCallout(Point2D point, final String pointName, final int idDrawable) {
        CallOut callOut = new CallOut(this);
        callOut.setStyle(CalloutAlignment.BOTTOM);
        callOut.setCustomize(true);
        callOut.setLocation(point.getX(), point.getY());
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(idDrawable);
        callOut.setContentView(imageView);

        if (mMapViewGL != null && mMapGL != null && mMapViewGL.getVisibility() == View.VISIBLE) {
            mMapViewGL.removeCallOut(pointName);
            mMapViewGL.addCallout(callOut, pointName);
        } else if (mMapView.getVisibility() == View.VISIBLE) {
            mMapView.removeCallOut(pointName);
            mMapView.addCallout(callOut, pointName);
        }
    }


    /******************** -- Refresh Time Log -- ******************************/

    private MapRefreshTimeRecord mMapRefreshTimeLog;
    private MapRefreshTimeRecord mMapRefreshTimeLogGL;

    private void initRefreshTimeLog() {
        mMapRefreshTimeLog = new MapRefreshTimeRecord("RefreshVector");
        mMapRefreshTimeLogGL = new MapRefreshTimeRecord("RefreshGL");

        // since 9D
        mMapControl.setPaintProfileListener(mMapRefreshTimeLog);
        if (mMapControlGL != null)
            mMapControlGL.setPaintProfileListener(mMapRefreshTimeLogGL);
    }

    public void enableSaveRefreshLog(boolean enable) {
        mMapRefreshTimeLog.enableSaveLog(enable);
        mMapRefreshTimeLogGL.enableSaveLog(enable);
    }

    /*************************** Measure ******************************/

    private MeasureListener mMeasuerListener;
    private EditText mEdit_MeasureLength, mEdit_MeasureArea, mEdit_MeasureAngle;
    private TextView mText_MeasureLengthUnit, mText_MeasureAreaUnit, mText_MeasureAngleUnit;
    private void initMeasure(){
        mEdit_MeasureLength = (EditText) findViewById(R.id.edit_measured_length);
        mEdit_MeasureArea = (EditText) findViewById(R.id.edit_measured_area);
        mEdit_MeasureAngle = (EditText) findViewById(R.id.edit_measured_angle);

        mText_MeasureLengthUnit = (TextView) findViewById(R.id.text_measure_length_unit);
        mText_MeasureAreaUnit = (TextView) findViewById(R.id.text_measure_area_unit);

        mMeasuerListener = new MeasureListener() {
            @Override
            public void lengthMeasured(double v, Point point) {
                mEdit_MeasureLength.setText(v + "");
            }

            @Override
            public void areaMeasured(double v, Point point) {
                mEdit_MeasureArea.setText(v + "");
            }

            @Override
            public void angleMeasured(double v, Point point) {
                mEdit_MeasureAngle.setText(v + "");
            }
        };
        mMapControl.addMeasureListener(mMeasuerListener);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapControl.cancel();
                switch (v.getId()){
                    case R.id.btn_measure_angle_clear:
                        mMapControl.setAction(Action.MEASUREANGLE);
                        mEdit_MeasureAngle.setText("");
                        break;
                    case R.id.btn_measure_area_clear:
                        mMapControl.setAction(Action.MEASUREAREA);
                        mEdit_MeasureArea.setText("");
                        break;
                    case R.id.btn_measure_length_clear:
                        mMapControl.setAction(Action.MEASURELENGTH);
                        mEdit_MeasureLength.setText("");
                        break;
                }
            }
        };

        findViewById(R.id.btn_measure_angle_clear).setOnClickListener(listener);
        findViewById(R.id.btn_measure_area_clear).setOnClickListener(listener);
        findViewById(R.id.btn_measure_length_clear).setOnClickListener(listener);

    }

    private void destroyMeasure(){
        if(mMeasuerListener != null)
            mMapControl.removeMeasureListener(mMeasuerListener);
    }


    /**
     * id = 0, not measure; id = 1, measure length; id = 2, measure area; id = 3, measure angle
     * @param id
     */
    public void measure(int id){
        View measureLength = findViewById(R.id.layout_measure_length);
        View measureArea= findViewById(R.id.layout_measure_area);
        View measureAngle= findViewById(R.id.layout_measure_angle);
        measureLength.setVisibility(View.GONE);
        measureArea.setVisibility(View.GONE);
        measureAngle.setVisibility(View.GONE);
        if(1==id){
            enableEditTools(false);
            measureLength.setVisibility(View.VISIBLE);
            mMapControl.setAction(Action.MEASURELENGTH);
            mEdit_MeasureLength.setText("");
        } else if (2==id){
            enableEditTools(false);
            measureArea.setVisibility(View.VISIBLE);
            mMapControl.setAction(Action.MEASUREAREA);
            mEdit_MeasureArea.setText("");
        } else if(3==id){
            enableEditTools(false);
            measureAngle.setVisibility(View.VISIBLE);
            mMapControl.setAction(Action.MEASUREANGLE);
            mEdit_MeasureAngle.setText("");
        }else {
            mMapControl.setAction(Action.PAN);
        }
    }

    class OnLayerItemLongClicked implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ShowMessage.showInfo("LongClick", "LongClick " + position);
//            mMapControl.setAction(Action.SWIPE);
//            mMap.getLayers().get(position).setIsSwipe(true);
            return false;
        }
    }

    class OnLayerItemClicked implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ShowMessage.showInfo("Click", "Click " + position);
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.changeLanguage(newBase));
    }
}
