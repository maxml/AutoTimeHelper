package com.maxml.timer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.maxml.timer.MyLog;
import com.maxml.timer.controllers.GeneralService;
import com.maxml.timer.util.NetworkStatus;

import java.util.List;

public class NetworkReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "NetworkReceiver";
    private GeneralService tbContr;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Receieved notification about network status");
        NetworkStatus networkStatus = new NetworkStatus(context);

        if (networkStatus.isWifiAvailable()) {
            Toast.makeText(context, "WifiEnable", Toast.LENGTH_SHORT).show();
            networkStatus.getNameCurrentWifi();
        }
    }
}
