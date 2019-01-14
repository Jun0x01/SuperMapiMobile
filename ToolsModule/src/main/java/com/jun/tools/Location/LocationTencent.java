package com.jun.tools.Location;

import android.content.Context;

import com.jun.tools.Message.ShowMessage;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import java.util.ArrayList;

import static com.tencent.map.geolocation.TencentLocation.ERROR_OK;

/**
 * Created by Jun on 2017/8/24.
 */

class LocationTencent implements TencentLocationListener{

    TencentLocationManager mLocationManager;
    TencentLocationRequest mRequest;

    boolean mIsLocating;

    public LocationTencent(Context context){

        initLocationer(context);

    }
    private ArrayList<LocationListener> mLocationListeners;

    private void initLocationer(Context context){
        mLocationManager = TencentLocationManager.getInstance(context);
        mRequest = TencentLocationRequest.create();
        mRequest.setAllowCache(true);
        mRequest.setInterval(2000);  // 2s
        mRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);

        mLocationListeners = new ArrayList<>();

    }

    /**
     *
     * @param interval location update interval, unit is s.
     */
    public void setInterval(int interval){
        mRequest.setInterval(interval);
    }

    public boolean addLocationListener(LocationListener listener){

        return mLocationListeners.add(listener);

    }

    public boolean removeLocationListener(LocationListener listener){
        return mLocationListeners.remove(listener);
    }

    public boolean mIsSinglerequest;

    public boolean startLocation(){
        if(mIsLocating)
            return true;

        int result = mLocationManager.requestLocationUpdates(mRequest, this);
        String error = null;
        if(0 == result){
            mIsLocating = true;
        }else if(1 == result){
            error = "This device doesn't support the TencentLocation SDK.";
        }else if(2 == result){
            error = "The TencentLocation Key is not correct.";
        }else if(3 == result){
            error = "Couldn't load the libtencentloc.so, please check your development environment.";
        }

        if(error != null) {
            ShowMessage.showError("LocationTencent", "Requesting location failure: " + error);
        }

        return mIsLocating;
    }

    public boolean startLocation(int interval, boolean isSingleRequest){
        if (interval > 0)
            mRequest.setInterval(interval);

        mIsSinglerequest = isSingleRequest;

        return startLocation();
    }

    public void stopLoaction(){
        mLocationManager.removeUpdates(this);
        mLocationListeners.clear();
        mIsLocating = false;
    }


    /***************** Location Listener *************************/
    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String reason) {
        if(mIsSinglerequest)
            stopLoaction();
        if(ERROR_OK == error) {
            double x = tencentLocation.getLongitude();
            double y = tencentLocation.getLatitude();
            double z = tencentLocation.getAltitude();
            if (mLocationListeners != null) {
                for(LocationListener listener : mLocationListeners) {
                    listener.onLocationChanged(x, y, z);
                }
            }

        }else {
            ShowMessage.showError("LocationTencent", "Location failure: " + reason);
        }


    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }


    @Override
    protected void finalize() throws Throwable {
        mLocationListeners.clear();
        mLocationManager.removeUpdates(this);

        super.finalize();
    }
}
