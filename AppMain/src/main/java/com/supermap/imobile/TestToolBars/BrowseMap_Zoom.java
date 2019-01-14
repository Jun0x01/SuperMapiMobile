package com.supermap.imobile.TestToolBars;

import android.os.SystemClock;

import com.supermap.data.Point2D;
import com.supermap.imobile.ActivityMain.DispathMessage;
import com.supermap.mapping.MapControl;


/**
 * Created by Jun on 2017/04/09.
 */

public class BrowseMap_Zoom extends BrowseMap{


    public BrowseMap_Zoom(MapControl mapControl){
        super(mapControl);
    }

    @Override
    protected void initViews() {
        frequency = 5000;
        super.initViews();
    }

    @Override
    void testContent() {
        broseMap_Zoom();
    }

    private void broseMap_Zoom(){
        mMap.setScale(visibleScales[visibleScales.length - 1]);
        mMap.refresh();
        SystemClock.sleep(1000);
        updateOriginalPoint();

        Point2D center = new Point2D();

        for(;indexScales < visibleScales.length && !bPaused && !bStopped; indexScales ++){
            mMap.setScale(visibleScales[indexScales]);
            mMap.refresh();
            SystemClock.sleep(refreshTime);
//            updateOriginalPoint();

            zoomCount ++;
            updateContent("平移：" + panCount + ", 放大：" + zoomCount);

            int pIndex = 0;
            long time0, time1;
            for(int i = 0; i < row && !bPaused && !bStopped; i++){
                for(int j=0; j < col && !bPaused && !bStopped; j++){
                    center.setX(startPoint.getX() + colDif * j);
                    center.setY(startPoint.getY() + rowDif * i);

                    mIsPainted = false;
                    mMap.setCenter(center);
                    mMap.refresh();
                    panCount ++;
                    updateContent("平移：" + panCount + ", 放大：" + zoomCount);

                    time0 = System.currentTimeMillis();
                    waitMapRefresh();
                    time1 = System.currentTimeMillis();
                    sleep(mPeriodTime - (time1 - time0));

                    if(pIndex == 0) {
                        for (int n = visibleScales.length - 1; n >= indexScales && !bPaused && !bStopped; n--) {
                            mIsPainted = false;
                            mMap.setScale(visibleScales[n]);
                            mMap.refresh();
                            zoomCount++;
                            updateContent("平移：" + panCount + ", 放大：" + zoomCount);

                            time0 = System.currentTimeMillis();
                            waitMapRefresh();
                            time1 = System.currentTimeMillis();
                            sleep(mPeriodTime - (time1 - time0));

                            if(checkBlankMap())
                                break;
                        }
                        pIndex = 1;
                    }else {
                        for (int n = indexScales; n < visibleScales.length && !bPaused && !bStopped; n++) {
                            mIsPainted = false;
                            mMap.setScale(visibleScales[n]);
                            mMap.refresh();
                            zoomCount++;
                            updateContent("平移：" + panCount + ", 放大：" + zoomCount);

                            time0 = System.currentTimeMillis();
                            waitMapRefresh();
                            time1 = System.currentTimeMillis();
                            sleep(mPeriodTime - (time1 - time0));

                            if(checkBlankMap())
                                break;
                        }
                        pIndex = 0;
                    }

                }
            }
        }

        DispathMessage.post(new Runnable() {

            @Override
            public void run() {
                test_Stop();
            }
        });
    }




}
