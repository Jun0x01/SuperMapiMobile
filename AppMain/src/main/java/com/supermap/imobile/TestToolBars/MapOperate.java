package com.supermap.imobile.TestToolBars;

import android.util.DisplayMetrics;
import android.util.Log;

import com.jun.tools.toolkit.ImgEqualAssert;
import com.supermap.data.Environment;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Workspace;
import com.supermap.imobile.ImgOutput;
import com.supermap.imobile.Resource.DataPath;
import com.supermap.imobile.TestContentUpdateListener;
import com.supermap.imobile.Tools.SaveTestLog;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;

import java.io.File;

/**
 * Open,Close,Zoom,Pan map randomly
 */
public class MapOperate {
	MapControl m_mapControl;
	Workspace m_workspace;

	Rectangle2D maxViewBounds;

	double minScale;
	double maxScale;
    boolean mIsChangeMap;

    Map mMap;

    double srcScale;

    private DisplayMetrics mDisplayMetrics;

	public MapOperate(MapControl mapControl, Workspace workspace) {
		mDisplayMetrics = mapControl.getContext().getResources().getDisplayMetrics();

		m_mapControl = mapControl;
		m_workspace = workspace;

        mMap = m_mapControl.getMap();
        mIsChangeMap = false;

        if(mMap.getName() != null){
            srcScale = mMap.getScale();
        }
//		Left=1.2898511847332578E7,Bottom=4779017.056563509,Right=1.301407882908275E7,Top=4955834.538641269
        maxViewBounds = new Rectangle2D();
		maxViewBounds.setLeft(1.2898511847332578E7);
		maxViewBounds.setBottom(4779017.056563509);
		maxViewBounds.setRight(1.301407882908275E7);
        maxViewBounds.setTop(4955834.538641269);

	}
	
	private int mPanCount;
	private int mZoomCount;
	private int mOpenMapCount=1;
	private TestContentUpdateListener mContentUpdate;
	
	public void resetTestCount(){
		mPanCount = 0;
		mZoomCount = 0;
		mOpenMapCount = 1;
	}

	/**
	 * The max view bounds will be displayed, it will be disabled if allow change map
	 * @param bounds
	 */
	public void setMaxViewBounds(Rectangle2D bounds){
        maxViewBounds = bounds;
    }


    /**
     * if it is enabled and there are more than two map in the workspace, maxVieBounds will be disabled
     * @param isChangeMap default value is true
     */
    public void setEnableChangeMap(boolean isChangeMap){
        mIsChangeMap = isChangeMap;
    }

    private void checkMapView(){
        Map map = m_mapControl.getMap();

        Rectangle2D viewBounds = map.getViewBounds();
        Rectangle2D mapBounds = map.getBounds();
        if(maxViewBounds != null && !maxViewBounds.isEmpty() && map.getName().contains("beijing")){
            mapBounds = maxViewBounds;
        }

        // if map is invisible , reset map's bounds
        if(!mapBounds.contains(viewBounds)){
            map.setViewBounds(mapBounds);
            map.refresh();
        }else {
            String path = DataPath.TestScreenShotDir + "/TempCheckMapView/new.png";
            ImgOutput.ouputPicture(m_mapControl, path);

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
		m_mapControl.getMap().pan(offsetX, offsetY);
		m_mapControl.getMap().refresh();
		
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
		m_mapControl.getMap().zoom(fRatio);
		m_mapControl.getMap().refresh();
		mZoomCount++;
		updateTestContent();
	}

	/**
	 * Open another map
	 */
	private synchronized void openMap() {
		int nCount = m_workspace.getMaps().getCount();
		if(nCount <=0 || !mIsChangeMap)
			return;
		Log.d("Debug", "Open");
		SaveTestLog.saveLog("Open");
		System.out.println("关闭地图");
		m_mapControl.getMap().close();
		System.out.println("打开地图");
		Log.d("Debug", "Open");

		int index = (int)(Math.random()*nCount);

		if(index<0)
			index = 0;
		if(index >= nCount)
			index = nCount -1;
		if (index>=0 && index<nCount) {
			String name = m_workspace.getMaps().get(index);
           
			boolean isOpenMap = m_mapControl.getMap().open(name); 
            if(isOpenMap){
                m_mapControl.getMap().refresh();
                srcScale = mMap.getScale();
            }
            mOpenMapCount ++;
            updateTestContent();
		}

	}
	private synchronized void viewEntire(){
		Log.d("Debug", "ViewEntire");
		m_mapControl.getMap().viewEntire();
		m_mapControl.getMap().refresh();
	}
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

	public void setTestContentUpdateListener(TestContentUpdateListener listener){
		mContentUpdate = listener;
	}
	
	private void updateTestContent(){
		
		if(mContentUpdate != null){
			String content = "切图: " + mOpenMapCount + "  平移: " + mPanCount + "  放大: " + mZoomCount;
			mContentUpdate.updateContent(content);
		}
	}
	
	private double pixelToMap(int tolerance) {
		Point2D ptStart = m_mapControl.getMap().pixelToMap(new Point(0, 0));
		Point2D ptEnd = m_mapControl.getMap().pixelToMap(new Point(0, tolerance));
		
		double distence = Math.abs(ptEnd.getY() - ptStart.getY());
		return distence;
	}

	Rectangle2D mapBounds;
	double[] visibleScales;
	double curScale;
	Point2D lefTop;
	Point2D rightBot;
	long row, col, indexRow, indexCol;
	double rowDif, colDif;
	Point2D startPoint = new Point2D();
	int indexScale;

	public void init(){
		mMap.viewEntire();
		mMap.refresh();
		mapBounds = mMap.getBounds();
		visibleScales = mMap.getVisibleScales();

		lefTop = new Point2D(mapBounds.getLeft(), mapBounds.getTop());
		rightBot = new Point2D(mapBounds.getRight(), mapBounds.getBottom());


		curScale = mMap.getScale();
		Point pLeftTop = mMap.mapToPixel(lefTop);
		Point pRightBot = mMap.mapToPixel(rightBot);
		double mapWidth = pRightBot.getX() - pLeftTop.getX();
		double mapHeight = pLeftTop.getY() - pRightBot.getY();

		row = Math.abs((int)(mapHeight/mDisplayMetrics.heightPixels));
		col = Math.abs((int)(mapWidth/mDisplayMetrics.widthPixels));

		colDif = (rightBot.getX() - lefTop.getX())/col;
		rowDif = (rightBot.getY() - lefTop.getY())/row;

		startPoint.setX(lefTop.getX() + colDif/2);
		startPoint.setY(lefTop.getY() + colDif/2);

	}

	private void broseMap(){

		boolean isFinish = false;
		while (!isFinish){

		}

	}



}
