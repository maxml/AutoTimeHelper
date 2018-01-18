package com.maxml.timer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.maxml.timer.entity.WifiState;

public class NetworkUtil {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return mWifi.isConnected();
        }
        return false;
    }

    public static boolean isWifiAvailable(Context context, int id) {
        WifiManager wifiManager = (WifiManager) context
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            return id == wifiInfo.getIpAddress();
        }
        return false;
    }

    public static WifiState getCurrentWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            return new WifiState(wifiInfo.getBSSID(), wifiInfo.getSSID());
        }
        return new WifiState();
    }
}
