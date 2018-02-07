package com.maxml.timer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.maxml.timer.controllers.ActionController;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;

public class WifiReceiver extends BroadcastReceiver {
    private DbController dbController;
    private ActionController actionController;
    private EventBus eventBus;

    public static boolean isActiveWifi;
    public static int wifiType;

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) && networkInfo != null
                && networkInfo.getTypeName().equalsIgnoreCase("WIFI")) {
            if (networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                connectedWifi(context);
            } else if (networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                if (isActiveWifi) {
                    disconnectedWifi(context);
                }
            }
        }
    }

    private void connectedWifi(Context context) {
        initController(context);

        WifiState wifiState = NetworkUtil.getCurrentWifi(context);

        dbController.wifiActivated(wifiState);
        int type = dbController.getWifiTypeFromDB(wifiState);

        actionController.onReceiveWifiEvent(new Events.WifiEvent(Constants.EVENT_WIFI_ENABLE, type));
        unregisterController();
        isActiveWifi = true;
        wifiType = type;
    }

    private void disconnectedWifi(Context context) {
        initController(context);
        actionController.onReceiveWifiEvent(new Events.WifiEvent(Constants.EVENT_WIFI_DISABLE, wifiType));
        unregisterController();
        isActiveWifi = false;
    }

    private void initController(Context context) {
        if (dbController == null) {
            eventBus = new EventBus();
            dbController = new DbController(context, eventBus);
            actionController = new ActionController(context, eventBus);

            dbController.registerEventBus(eventBus);
            actionController.registerEventBus(eventBus);
            actionController.setWifiEventBus();
        }
    }

    private void unregisterController() {
        dbController.unregisterEventBus(eventBus);
        actionController.unregisterEventBus(eventBus);
    }
}