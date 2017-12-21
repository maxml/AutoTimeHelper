package com.maxml.timer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.maxml.timer.MyLog;

public class NetworkStatus {
    private Context context;

    public NetworkStatus(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        try {
            return mWifi.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    public String getNameCurrentWifi() {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        MyLog.i(wifiInfo.getSSID()); //name
        MyLog.i(wifiInfo.getBSSID()); //id
        MyLog.i(wifiInfo.getIpAddress() + ""); //id
        return "";
    }

    public boolean isCurrentWifi(String name) {
        return name.equalsIgnoreCase(getNameCurrentWifi());
    }
}
