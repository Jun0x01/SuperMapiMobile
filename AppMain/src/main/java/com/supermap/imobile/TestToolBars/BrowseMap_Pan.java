package com.supermap.imobile.TestToolBars;

import com.supermap.data.Point2D;
import com.supermap.imobile.ActivityMain.DispathMessage;
import com.supermap.mapping.MapControl;


/**
 * Created by Jun on 2017/04/08.
 */

public class BrowseMap_Pan extends BrowseMap {

    public BrowseMap_Pan(MapControl mapControl){
        super(mapControl);
    }

    @Override
    protected void initViews() {
        frequency = 5000;
        super.initViews();
    }

    @Override
    void testContent() {
        broseMap_Pan();
    }


    private void broseMap_Pan(){
        Point2D center = new Point2D();
        long time0, time1;

        for(;indexScales < visibleScales.length && !bPaused && !bStopped; indexScales ++){
            mIsPainted =false;
            mMap.setScale(visibleScales[indexScales]);
            mMap.refresh();

            time0 = System.currentTimeMillis();
            waitMapRefresh();
            time1 = System.currentTimeMillis();
            sleep(mPeriodTime - (time1 - time0));

            updateOriginalPoint();

            zoomCount ++;
            updateContent("平移：" + panCount + ", 放大：" + zoomCount);

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

                    if(checkBlankMap())
                        break;
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
