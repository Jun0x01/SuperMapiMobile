package com.supermap.imobile.TestToolBars;

import android.os.SystemClock;
import android.util.Log;

import com.jun.tools.toolkit.ImgEqualAssert;
import com.supermap.data.Environment;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Rectangle2D;
import com.supermap.imobile.ImgOutput;
import com.supermap.imobile.Resource.DataPath;
import com.supermap.imobile.Tools.SaveTestLog;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;

import java.io.File;

//import com.supermap.mapping.PaintProfileListener;

/**
 * Created by Jun on 2017/5/3.
 */

public class BrowseMap_Random extends BrowseMap{


    public BrowseMap_Random(MapControl mapControl){
        super(mapControl);
    }

    @Override
    protected void initViews() {
        frequency = 1000;
        super.initViews();
    }

    @Override
    protected void testContent(){
        if(mIsWaitPaint) {
            if (mIsPainted) {
                doWork();
                mIsPainted = false;
            }
            SystemClock.sleep(50);
        }else {
            doWork();
        }
    }

    private int mPanCount;
    private int mZoomCount;
    private int mOpenMapCount=1;
    
    /**
     * Execute a random action which will be zoom , pan, or change a map
     */
    public void doWork() {
        int index = (int)(Math.random()*100);
//		System.out.println(index + "");
        String name = mMap.getName();
        if((name == null || name.isEmpty()) && mIsChangeMap){
            openMap();
            return;
        }

        if (!Environment.isOpenGLMode())
            checkMapView();
        int d = 10;
        if (index%d == 0){                     // 1/index
            openMap();
        } else if (index%d==1){                // 1/index
            viewEntire();
        } else if(index%d>=2 && index%d <6){
            zoomMap();
        } else {
            moveMap();
        }
    }

    private void checkMapView(){
        Map map =mMap;

        Rectangle2D viewBounds = map.getViewBounds();
        Rectangle2D mapBounds = map.getBounds();
        

        // if map is invisible , reset map's bounds
        if(!mapBounds.contains(viewBounds)){
            map.setViewBounds(mapBounds);
            map.refresh();
        }else {
            String path = DataPath.TestScreenShotDir + "/TempCheckMapView/new.png";
            ImgOutput.ouputPicture(mMapControl, path);

            String path2 = DataPath.TestScreenShotDir + "/TempCheckMapView/old.png";

            String result = ImgEqualAssert.imageEqual(path, path2);
            if(null == result){
                double scale = mMap.getScale();
                if(scale < srcScale){
                    mMap.zoom(0.5);
                    mMap.refresh();
                }
            }else {
                File file1 = new File(path);
                File file2 = new File(path2);
                file2.delete();
                if(file1.exists()){
                    file1.renameTo(file2);
                }
            }
        }

    }

    /**
     * Pan map
     */
    private synchronized void moveMap() {
        int x = (int)(Math.random()*400);
        int y = (int)(Math.random()*400);

        double offsetX = pixelToMap(x);
        double offsetY = pixelToMap(y);
        if (Math.random()>0.5) {
            offsetX = -offsetX;
            offsetY = -offsetY;
        }
        Log.d("Debug", "Pan");
        SaveTestLog.saveLog("Pan");
        System.out.println("平移地图 X="+offsetX+"Y="+offsetY);
       mMap.pan(offsetX, offsetY);
       mMap.refresh();

        mPanCount ++;
        updateTestContent();
    }

    /**
     * Zoom map
     */
    private synchronized void zoomMap() {
        Log.d("Debug", "Zoom");
        SaveTestLog.saveLog("Zoom");
        int ratio = (int)(Math.random()*10);
        double fRatio = 0;
        if(ratio%2 == 0){
            fRatio = 1.5;
        }else {
            fRatio = 0.5;
        }
       mMap.zoom(fRatio);
       mMap.refresh();
        mZoomCount++;
        updateTestContent();
    }

    double srcScale;

    /**
     * Open another map
     */
    private synchronized void openMap() {
        int nCount = mWorkspace.getMaps().getCount();
        if(nCount <=0 || !mIsChangeMap)
            return;
        Log.d("Debug", "Open");
        SaveTestLog.saveLog("Open");
        System.out.println("关闭地图");
        mMap.close();
        System.out.println("打开地图");
        Log.d("Debug", "Open");

        int index = (int)(Math.random()*nCount);

        if(index<0)
            index = 0;
        if(index >= nCount)
            index = nCount -1;
        if (index>=0 && index<nCount) {
            String name = mWorkspace.getMaps().get(index);

            boolean isOpenMap = mMap.open(name);
            if(isOpenMap){
                mMap.refresh();
                srcScale = mMap.getScale();
            }
            mOpenMapCount ++;
            updateTestContent();
        }

    }
    private synchronized void viewEntire(){
        Log.d("Debug", "ViewEntire");
        mMap.viewEntire();
        mMap.refresh();
    }

    private void updateTestContent(){

        String content = "切图: " + mOpenMapCount + "  平移: " + mPanCount + "  放大: " + mZoomCount;
        updateContent(content);

    }

    private double pixelToMap(int tolerance) {
        Point2D ptStart = mMap.pixelToMap(new Point(0, 0));
        Point2D ptEnd = mMap.pixelToMap(new Point(0, tolerance));

        double distance = Math.abs(ptEnd.getY() - ptStart.getY());
        return distance;
    }
}
