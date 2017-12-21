package com.maxml.timer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.maxml.timer.MyLog;
import com.maxml.timer.controllers.GeneralService;
import com.maxml.timer.database.HandlerFactory;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.util.NetworkStatus;

import java.sql.SQLException;

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

        try {
            HandlerFactory.getHelper().getWifiStateDao().create(new WifiState(1, "1"));
        } catch (SQLException e) {
            e.printStackTrace();

            MyLog.i("------------------1");
        }

        try {
            WifiState wifiState = HandlerFactory.getHelper().getWifiStateDao().getAllRoles().get(0);
            MyLog.i(wifiState.getName());
        } catch (SQLException e) {
            e.printStackTrace();

            MyLog.i("------------------2");
        }

    }
}
