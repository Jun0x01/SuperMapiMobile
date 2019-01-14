package com.jun.tools.fileutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;
import java.util.List;

public class NetworkManager {

	
	private Context mContext;
	
	private WifiManager mWifiManager;

	private boolean mWifiState;

	private boolean mNetDataState;
	
	public NetworkManager (Context context) {
		mContext = context;
		mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
	}
	
	public void scanWifi(){
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		int wifiState = mWifiManager.getWifiState();
		boolean isWifiEnabed = mWifiManager.isWifiEnabled();
		
		DhcpInfo dhcpInfo = mWifiManager.getDhcpInfo();
		List<WifiConfiguration> wifiConfigs = mWifiManager.getConfiguredNetworks();
		List<ScanResult> scanWifis = mWifiManager.getScanResults();
		if(wifiConfigs != null){
			int size = wifiConfigs.size();
			WifiConfiguration config;
			for(int i=0; i<size; i++){
				config = wifiConfigs.get(i);
			}
		}
		int count = scanWifis.size();
		System.out.println("Wifi count: " + count);
	}
	
	public void checkNetworkStatus(){
		ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<? extends ConnectivityManager> class1 = connectivityManager.getClass();
		Class<?>[] classes = null;
		Class<?> arg = null;
		try {
			Method method = class1.getMethod("getMobileDataEnabled", classes);
			boolean isEnable = (Boolean)method.invoke(connectivityManager, arg);
			mNetDataState = isEnable;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		boolean isEnable = wifiManager.isWifiEnabled();
		mWifiState = isEnable;
	}
	public void closeNetwork(){
		ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<? extends ConnectivityManager> class1 = connectivityManager.getClass();
		Class<?>[] classes = null;
		Class<?> arg = null;
		try {
			Method method = class1.getMethod("getMobileDataEnabled", classes);
			boolean isEnable = (Boolean)method.invoke(connectivityManager, arg);
			if (isEnable) {
				classes = new Class[1];
				classes[0] = boolean.class;
				method = class1.getMethod("setMobileDataEnabled", classes);
				method.invoke(connectivityManager, false);
				mNetDataState = isEnable;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		boolean isEnable = wifiManager.isWifiEnabled();
		if(isEnable){
			wifiManager.setWifiEnabled(false);
			mWifiState = isEnable;
		}
	}
	
	public void restoreNetwork(){
		if (mNetDataState) {
			ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			Class<? extends ConnectivityManager> class1 = connectivityManager.getClass();
			Class[] classes = new Class[1];
			classes[0] = boolean.class;
			try {
				Method method = class1.getMethod("setMobileDataEnabled", classes);
				method.invoke(connectivityManager, mNetDataState);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		if (mWifiState) {
			WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(mWifiState);
		}
	}
	
	public void disconnectNetwork(){
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		wifiManager.disconnect();
	}
}
