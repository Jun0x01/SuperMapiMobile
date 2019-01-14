package com.supermap.imobile.TestToolBars;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.jun.android.device.CpuUsageMonitor;
import com.jun.android.device.MemoryUsageMonitor;
import com.jun.tools.logcat.DateHelper;
import com.jun.tools.logcat.LogSaveManager;
import com.jun.tools.toolkit.CheckThread;
import com.jun.tools.toolkit.TimeControl;
import com.supermap.data.Color;
import com.supermap.data.Environment;
import com.supermap.data.GeoStyle;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Workspace;
import com.supermap.imobile.ActivityMain.DispathMessage;
import com.supermap.imobile.R;
import com.supermap.imobile.Resource.DataPath;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.PaintProfileListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Jun on 2017/04/08.
 */

public abstract class BrowseMap implements View.OnClickListener {

    private final String tag = "BrowseMap";

    private String logDir;

    public BrowseMap(MapControl mapControl){
        String graphics = "MEM";
        if(Environment.isOpenGLMode())
            graphics = "OpenGL";
        logDir = DataPath.LogPath + "/" +  getClass().getSimpleName() + "/" + graphics;
        loadLayout(mapControl.getContext());
        setMapControl(mapControl);
        initLogManager();
        initMonitors();

    }

    /************************** Views ********************************/
    private Context mContext;
    private LayoutInflater mInflater;
    private View mViewContainer;
    private void loadLayout(Context context){
        int layoutId = R.layout.test_pop_browsemap;

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mViewContainer = mInflater.inflate(layoutId, null);

        initViews();
    }

    public View getView(){
        return  mViewContainer;
    }

    // Tools
    private TextView m_TitleFrequency;
    private EditText m_EditFrequency;
    private TextView m_TextUnit;

    // Test information
    private TextView m_TitleTestInfo;
    private TextView m_TextTestInfo;
    private TextView m_TextTestTime;

    // Button
    private Button mBtn_Start;
    private Button mBtn_Pause;
    private Button mBtn_Stop;

    // 测试要求时长
    private long mTestDuration = 15 * 3600 * 1000; // 单位ms, 默认15小时

    private View mLayout_Choices;
    protected boolean mIsChangeMap;
    /**
     * ms
     */
    protected long frequency = 1000;

    protected void initViews(){
        ((TextView) mViewContainer.findViewById(R.id.txt_Title1)).setText(getClass().getSimpleName());
        // Tools
        m_TitleFrequency = (TextView) mViewContainer.findViewById(R.id.txt_frequency);
        m_TextUnit = (TextView) mViewContainer.findViewById(R.id.txt_unit);
        m_EditFrequency = (EditText) mViewContainer.findViewById(R.id.edit_frequency);

        // Test information
        m_TitleTestInfo = (TextView) mViewContainer.findViewById(R.id.txt_titileTestInfo);
        m_TextTestInfo = (TextView) mViewContainer.findViewById(R.id.txt_TestInfo);
        m_TextTestTime = (TextView) mViewContainer.findViewById(R.id.txt_TestTime);

        // Button
        mBtn_Start = (Button) mViewContainer.findViewById(R.id.btn_start);
        mBtn_Pause = (Button) mViewContainer.findViewById(R.id.btn_pause);
        mBtn_Stop = (Button) mViewContainer.findViewById(R.id.btn_stop);

        mBtn_Start.setOnClickListener(this);
        mBtn_Pause.setOnClickListener(this);
        mBtn_Stop.setOnClickListener(this);

        mBtn_Start.setEnabled(true);
        mBtn_Pause.setEnabled(false);
        mBtn_Stop.setEnabled(false);

        // 设置初始界面
        m_TitleFrequency.setText("频率");
        m_EditFrequency.setText("" + frequency);
        m_TextUnit.setText("ms/次");

        m_TitleTestInfo.setText("计数: ");
        m_TextTestInfo.setText("0");
        m_TextTestTime.setText("00小时00分00秒");

        ((CheckBox) mViewContainer.findViewById(R.id.checkbox_IsWaitEachPaint)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsWaitPaint = isChecked;
            }
        });
        ((CheckBox) mViewContainer.findViewById(R.id.checkbox_IsChangeMap)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsChangeMap = isChecked;
            }
        });
        ((CheckBox) mViewContainer.findViewById(R.id.checkbox_SaveLog)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bSaveMonitorData = isChecked;
            }
        });

        mLayout_Choices =  mViewContainer.findViewById(R.id.layout_BrowseMapChoices);
    }


    /****************** OnClickListener**********************/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                test_Start();
                break;
            case R.id.btn_pause:
                mLayout_Choices.setVisibility(View.VISIBLE);
                test_Pause();
                break;
            case R.id.btn_stop:
                mLayout_Choices.setVisibility(View.VISIBLE);
                test_Stop();
                break;
            default:
                break;
        }

    }


    /******************************************/


    public void updateView(final TextView textView, final String content){
        if(CheckThread.isMainThread()){
            textView.setText(content);
        }else {
            DispathMessage.post(new Runnable() {

                @Override
                public void run() {
                    textView.setText(content);
                }
        });
        }
    }

    public void updateContent(String content) {
        updateView(m_TextTestInfo, content);
    }


    /*******************************************/

    protected MapControl mMapControl;
    protected Workspace mWorkspace;
    protected Map mMap;

    private void setMapControl(MapControl mapControl){
        mMapControl = mapControl;
        mMap = mMapControl.getMap();
        mMapControl.setPaintProfileListener(mPaintListener);
    }

     abstract void testContent();

    private boolean checkInit(){
        mWorkspace = mMap.getWorkspace();
        if(mWorkspace != null && mWorkspace.getMaps().getCount() > 0 && mMap.getLayers().getCount() > 0){
            mIsInited = true;
        } else {
            mIsInited = false;
        }
        if(!mIsInited){
            DispathMessage.toastError(tag,"没有打开地图，功能不可用");
        }
        return mIsInited;
    }

    protected boolean bPaused = true;
    protected boolean bStopped = true;
    protected boolean bStarted = false;

    protected long mStartTime;
    protected long mCurTime;
    protected long mLastTime;
    protected long mTestTime;
    protected long mPeriodTime = 1000; // ms
    private String mStrTestTime;


    protected boolean mIsInited;
    protected boolean bTesting;


    public void test_Start(){

        if(!checkInit()){
            DispathMessage.toastWarning(tag,"请先打开地图");
            return;
        }

        bTesting = true;

        bPaused = false;
        bStopped = false;
        mLayout_Choices.setVisibility(View.GONE);

        if(!bStarted){  // 如果是暂停了,就不用重置
            bStarted = true;
            mLastTime = 0;
            mTestTime = 0;

            m_EditFrequency.setEnabled(false);
            String content = m_EditFrequency.getText().toString();
            if(content != null && !content.isEmpty()){
                mPeriodTime = Long.valueOf(content);
            }

//			content = m_EditTestDuration.getText().toString();
            content = "15";
            if(content != null && !content.isEmpty()){
                mTestDuration = (long)(Double.valueOf(content) * (3600*1000));
            }

            normalLog("测试开始: " + "IsOpenGL: " + Environment.isOpenGLMode());

            init();
        }
        //
        mBtn_Start.setEnabled(false);
        mBtn_Pause.setEnabled(true);
        mBtn_Stop.setEnabled(true);

        startMonitors();
        mStartTime = System.currentTimeMillis();
        testThread();

    }

    public void test_Pause(){

        if(bPaused || !bStarted)
            return;
        if(!checkInit())
            return;
        bPaused = true;

        mBtn_Start.setEnabled(true);
        mBtn_Pause.setEnabled(false);
        mBtn_Stop.setEnabled(true);

        mLastTime = mTestTime;
        stopMonitor();

    }

    public void test_Stop(){
        if(!checkInit())
            return;

        if (bStopped || !bStarted) {
            return;
        }
        bStopped = true;
        bPaused = true;
        bStarted = false;

        stopMonitor();

        normalLog("CPU: " + countCpu + ", " + averCpu + ", " + minCpu + ", " + maxCpu);
        normalLog("MEM: " + countMem + ", " + averMem + ", " + minMem + ", " + maxMem);
        normalLog("Refresh: " + paintCount + ", " + averPaintCost + ", " + minPaintCost + ", " + maxPaintCost);
        normalLog(m_TextTestInfo.getText() + ", " + m_TextTestTime.getText());
        normalLog("测试结束");
        closeLogFile();

        mBtn_Start.setEnabled(true);
        mBtn_Pause.setEnabled(false);
        mBtn_Stop.setEnabled(false);

        bTesting = false;

        m_EditFrequency.setEnabled(true);

    }

    private void testThread(){

        Thread threadTest = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while (bStarted && !bPaused && !bStopped) {
                    testContent();
                    SystemClock.sleep(mPeriodTime);
                }
            }
        });

        Thread threadTimer = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while (bStarted && !bPaused && !bStopped) {
                    // 更新测试时间
                    mCurTime = System.currentTimeMillis();

                    mTestTime = mLastTime + mCurTime - mStartTime;

                    mStrTestTime = DateHelper.formatTime(mTestTime, "HH时mm分ss秒");
                    updateView(m_TextTestTime, cpuUsage + " " + memUsage + " P:" + paintCost + "  " +mStrTestTime);

                    TimeControl.sleep(1);
                }
            }
        });

        threadTest.start();
        threadTimer.start();
    }

    Rectangle2D mapBounds;
    double[] visibleScales;
    double curScale;
    Point2D mapLefTop;
    Point2D mapRightBot;
    long row, col, indexRow, indexCol;
    double rowDif, colDif;
    Point2D startPoint = new Point2D();
    int indexScales;
    boolean bFixedScales;
    double[] defaultScales = {
            1/295829355.454155,
            1/147914677.727296,
            1/73957338.8636482,
            1/36978669.4318241,
            1/18489334.715912,
            1/9244667.35795517,
            1/4622333.67897758,
            1/2311166.83948879,
            1/1155583.4197444,
            1/577791.709872198,
            1/288895.854936099,
            1/144447.92746805,
            1/72223.9637340248,
            1/36111.9818670124,
            1/18055.9909335062,
            1/9027.9954667531,
            1/4513.99773337655,
            1/2256.99886668828,
            1/1128.49943334414,};

    private void init(){
        mMapControl.setPaintProfileListener(mPaintListener);
        panCount = 0;
        zoomCount = 0;
        averPaintCost = minPaintCost = maxPaintCost = paintCount = 0;
        minCpu = maxCpu = averCpu = 0;
        minMem = maxMem = 0;
        averMem = 0;
        countCpu = countMem = 0;

        initBackgroundBitmap();

        mapBounds = mMap.getViewBounds();//mMap.getBounds();
        bFixedScales = mMap.isVisibleScalesEnabled();
        if(!bFixedScales) {
            mMap.setVisibleScalesEnabled(true);
            mMap.setVisibleScales(defaultScales);
        }

        bFixedScales = true;
//        mMap.viewEntire();
        mMap.refresh();
        SystemClock.sleep(1000);

        visibleScales = mMap.getVisibleScales();

        mapLefTop = new Point2D(mapBounds.getLeft(), mapBounds.getTop());
        mapRightBot = new Point2D(mapBounds.getRight(), mapBounds.getBottom());


        curScale = mMap.getScale();

        if(visibleScales != null && visibleScales.length > 0){
            int index = 0;
            for(double scale : visibleScales){
                if(Math.abs(scale - curScale) < 1.E-18){
                    indexScales = index - 2;
                    break;
                }
                index ++;
            }
        }

    }

    protected void updateOriginalPoint() {

        Point pLeftTop = new Point(0, 0);
        Point pRightBot = new Point(mMapControl.getWidth(), mMapControl.getHeight());

        Point2D leftTop = mMap.pixelToMap(pLeftTop);
        Point2D rightBot = mMap.pixelToMap(pRightBot);

        double mapViewWidth = rightBot.getX() - leftTop.getX();
        double mapViewHeight = leftTop.getY() - rightBot.getY();

        row = Math.abs(Math.round(mapBounds.getHeight()/mapViewHeight));
        col = Math.abs(Math.round(mapBounds.getWidth()/mapViewWidth));

        colDif = (mapRightBot.getX() - mapLefTop.getX())/col;
        rowDif = (mapRightBot.getY() - mapLefTop.getY())/row;

        startPoint.setX(mapLefTop.getX() + colDif/2);
        startPoint.setY(mapLefTop.getY() + rowDif/2);
    }

    long panCount, zoomCount;

    long refreshTime = 2*1000;


    private LogSaveManager mNormalLog;
    private LogSaveManager mCpuLog;
    private LogSaveManager mMemLog;
    private LogSaveManager mRefreshTimeLog;

    private void initLogManager(){
        mNormalLog = new LogSaveManager(logDir + "/NormalLog/");
        mCpuLog    = new LogSaveManager(logDir + "/CpuLog/");
        mMemLog    = new LogSaveManager(logDir + "/MemLog/");
        mRefreshTimeLog = new LogSaveManager(logDir + "/RefreshTimeLog/");
    }

    private void closeLogFile() {
        mNormalLog.closeFile();
        mCpuLog.closeFile();
        mMemLog.closeFile();
        mRefreshTimeLog.closeFile();
    }
    private void normalLog(String content){
        mNormalLog.saveLog(content);
    }

    private void cpuLog(String content) {
        if (bSaveMonitorData)
            mCpuLog.saveLog(content);
    }

    private void memLog(String content) {
        if (bSaveMonitorData)
            mMemLog.saveLog(content);
    }

    private void refreshTimeLog(String content) {
        if (bSaveMonitorData)
            mRefreshTimeLog.saveLog(content);
    }


    boolean bSaveMonitorData;

    volatile boolean mIsWaitPaint = false;
    volatile boolean mIsPainted = false;
    volatile long minPaintCost, maxPaintCost, paintCount, paintCost;
    volatile double averPaintCost;

    private PaintListener mPaintListener = new PaintListener();

    class PaintListener implements PaintProfileListener {
        @Override
        public void paintCost(int i) {

            paintCount ++;
            averPaintCost = averPaintCost * ((paintCount-1f)/paintCount) + i/paintCount;
            if(paintCount == 1){
                minPaintCost = maxPaintCost = i;
            }else {
                if(i<minPaintCost)
                    minPaintCost = i;
                if(i>maxPaintCost)
                    maxPaintCost = i;
            }
            paintCost = i;
            Log.d(tag, "PaintCost: " + i);
            refreshTimeLog("PaintCost: " + i);
            mIsPainted = true;
        }
    }
    private CpuUsageMonitor mCPUMonitor;
    private MemoryUsageMonitor mMemMonitor;
    volatile double minCpu, maxCpu, averCpu;
    volatile long minMem, maxMem;
    volatile long countCpu, countMem;
    volatile double averMem;

    private void initMonitors(){
        mCPUMonitor = new CpuUsageMonitor();
        mCPUMonitor.setListener(new CPUListener());
        mCPUMonitor.setPeriod(1);

        mMemMonitor = new MemoryUsageMonitor(mContext);
        mMemMonitor.setListener(new MemListener());
        mMemMonitor.setPeriod(1);

    }

    private void startMonitors(){
        mCPUMonitor.start();
        mMemMonitor.start();
    }

    private void stopMonitor(){
        mCPUMonitor.cancel();
        mMemMonitor.cancel();
    }

    private volatile String cpuUsage;
    class CPUListener implements CpuUsageMonitor.CpuUsageRateListener {
        @Override
        public void onReceive(double sysCpuUsageRate, double pidCpuUsageRate) {

            countCpu ++;
            if(countCpu == 1){
                minCpu = maxCpu = pidCpuUsageRate;
            }else {
                if(pidCpuUsageRate < minCpu)
                    minCpu = pidCpuUsageRate;
                if(pidCpuUsageRate > maxCpu)
                    maxCpu = pidCpuUsageRate;
            }
            averCpu = averCpu * ((countCpu-1f)/countCpu) + pidCpuUsageRate/countCpu;
            cpuUsage = "CPU: " + sysCpuUsageRate + "%, " + pidCpuUsageRate + "%";
            cpuLog(cpuUsage);
        }
    }

    private volatile String memUsage;

    class MemListener implements MemoryUsageMonitor.MemoryUsageListener{
        @Override
        public void onReceive(long sysTotalMem, long sysAvailableMem, long pidTotalPSS, long pidDalvikPSS, long pidNativePSS) {
            memUsage = "PSS:" + pidTotalPSS + "KB";
            String log = "MEM: SysTotal: " + sysTotalMem + ", available: " + sysAvailableMem +
                    "; PSS Rate: " + Math.round(10000.00 * pidTotalPSS/sysTotalMem + 0.5)/100.00 +
                    "%, TotalPSS: " + pidTotalPSS + ", dalvikPSS: " + pidDalvikPSS + ", nativePss: " + pidNativePSS;

            countMem ++;
            if(countMem == 1){
                minMem = maxMem = pidTotalPSS;
            }else {
                if(pidTotalPSS < minMem)
                    minMem = pidTotalPSS;
                if(pidTotalPSS > maxMem)
                    maxMem = pidTotalPSS;
            }
            averMem = averMem * ((countMem -1f)/countMem) + pidTotalPSS/countMem;
            memLog(log);
        }
    }

    protected void waitMapRefresh(){
        if(mIsWaitPaint){
            while (!mIsPainted){
                SystemClock.sleep(50);
            }
        }
    }

    protected void sleep(long time){
        if(time >0)
            SystemClock.sleep(time);
    }

    private Bitmap mBitMapOfMap;
    private Bitmap mBitMapOfMapBackground;
    private void initBackgroundBitmap(){
        mBitMapOfMapBackground = Bitmap.createBitmap(mMapControl.getWidth(), mMapControl.getHeight(), Bitmap.Config.ARGB_8888);


        GeoStyle style = mMap.getBackgroundStyle();
        Color color = style.getFillForeColor();
        int paintColor = (color.getA()<<24) + (color.getR()<<16) + (color.getG()<<8) + color.getB();
//        Canvas canvasBackground = new Canvas(mBitMapOfMapBackground);
//        Paint paint = new Paint();
//        paint.setColor(paintColor);
//        canvasBackground.drawPaint(paint);

        mBitMapOfMapBackground.eraseColor(paintColor);
        saveBitmap(mBitMapOfMapBackground, DataPath.LogPath + "/mapbackground.png");

    }
    protected boolean checkBlankMap(){
        if(mBitMapOfMap == null)
            mBitMapOfMap = Bitmap.createBitmap(mMapControl.getWidth(), mMapControl.getHeight(), Bitmap.Config.ARGB_8888);

        boolean isOutput = mMapControl.outputMap(mBitMapOfMap);
        if(isOutput){
            saveBitmap(mBitMapOfMap, DataPath.LogPath + "/map.png");
            return mBitMapOfMap.sameAs(mBitMapOfMapBackground);
        }else {
            return false;
        }

    }

    /**
     * 将bitmap 保存为文件
     * @param bitmap
     * @param path
     * @return
     */
    public boolean  saveBitmap(Bitmap bitmap, String path){
        boolean result = false;
        if(bitmap == null || path == null){
            Log.e(tag,"One or two params are null");
            return result;
        }
        // Create directories
        File file = new File(path);
        file.getParentFile().mkdirs();

        // Get file format
        Bitmap.CompressFormat comFormat = null;

        int index = path.lastIndexOf('.');
        if(index == -1){
            file = new File(path + ".png");
            comFormat = Bitmap.CompressFormat.PNG;
        }else if(index == path.length() -1){
            file = new File(path + "png");
            comFormat = Bitmap.CompressFormat.PNG;
        }else {
            String suffix = path.substring( index+1);


            if(suffix.equalsIgnoreCase("png")){
                comFormat = Bitmap.CompressFormat.PNG;
            }else if (suffix.equalsIgnoreCase("jpg")){
                comFormat = Bitmap.CompressFormat.JPEG;
            }else {
                file = new File(path + ".png");
                comFormat = Bitmap.CompressFormat.PNG;
            }
        }


        // Create image file
        try {
            FileOutputStream fOS = new FileOutputStream(file);

            boolean isTrue = bitmap.compress(comFormat, 100, fOS);

            if(!isTrue)
                Log.e(tag, "Failed to create image file : " + path);

            fOS.flush();
            fOS.close();

            result = true;

            Log.d(tag, "Output img successed, " + path);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return result;

    }

    @Override
    protected void finalize() throws Throwable {
        startMonitors();
        if (mBitMapOfMap != null && !mBitMapOfMap.isRecycled())
            mBitMapOfMap.recycle();
        if (mBitMapOfMapBackground != null && !mBitMapOfMapBackground.isRecycled())
            mBitMapOfMapBackground.recycle();
        super.finalize();
    }
}
