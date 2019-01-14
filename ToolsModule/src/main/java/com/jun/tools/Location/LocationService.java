package com.jun.tools.Location;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Jun on 2017/9/12.
 */

public class LocationService extends Service implements LocationListener {

    Location mLocation;
    LocationTencent mLocationTencent;
    ArrayList<LocationListener> mLocationListeners;
    ServiceBinder mBinder;
    public LocationService() {
        super();
        mLocationListeners = new ArrayList<>();
        mBinder = new ServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocation = new Location();
        mLocationTencent = new LocationTencent(getApplicationContext());
        mLocationTencent.addLocationListener(this);

        for (LocationListener listener : mLocationListeners){
            mLocationTencent.addLocationListener(listener);
        }
        mLocationTencent.startLocation();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    //@IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        mLocationTencent.stopLoaction();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    public class ServiceBinder extends Binder {
        public LocationService getService(){
            return LocationService.this;
        }
    }

    /************************* LocationListener *****************************/

    @Override
    public void onLocationChanged(double x, double y, double z) {
        synchronized (mLocation) {
            mLocation.setX(x);
            mLocation.setY(y);
            mLocation.setZ(z);
        }
    }

    /******************************* User Defined **************************************************/
    public Location getLocation() {
        synchronized (mLocation){
            return new Location(mLocation);
        }
    }

    public void setInterval(int interval){
        mLocationTencent.setInterval(interval);
    }

    public boolean addLocationListener(LocationListener listener){
        if(mLocationTencent != null) {
            return mLocationTencent.addLocationListener(listener);
        }else {
            return mLocationListeners.add(listener);
        }
    }

    public boolean removeLocationListener(LocationListener listener){
        return mLocationTencent.removeLocationListener(listener);
    }





}
