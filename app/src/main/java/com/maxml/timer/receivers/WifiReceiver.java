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
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.entity.Events;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.logging.StreamHandler;

public class WifiReceiver extends BroadcastReceiver {
    private DbController dbController;
    private ActionController actionController;
    private EventBus eventBus;

    private static int counter;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    initController(context);

                    WifiState wifiState = NetworkUtil.getCurrentWifi(context);

                    dbController.wifiActivated(wifiState);
                    int type = dbController.sendWifiStateFromDB(wifiState);

                    if (type == Constants.WIFI_TYPE_HOME) {
                        actionController.onReceiveWifiEvent(new Events.WifiEvent(Constants.EVENT_WIFI_ENABLE));
                        unregisterController();
                        counter = 0;
                    }
                } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                    if (counter == 0) {
                        initController(context);
                        actionController.onReceiveWifiEvent(new Events.WifiEvent(Constants.EVENT_WIFI_DISABLE));
                        unregisterController();
                        counter++;
                    }
                }
            }
        }
    }

    private void initController(Context context) {
        if (dbController == null) {
            eventBus = new EventBus();
            dbController = new DbController(context, eventBus);
            actionController = new ActionController(context, eventBus);

            dbController.registerEventBus(eventBus);
            actionController.registerEventBus(eventBus);
            eventBus.post(new Events.WifiEvent(Constants.EVENT_SET_WIFI_EVENT_BUS));
        }
    }

    private void unregisterController() {
        dbController.unregisterEventBus(eventBus);
        actionController.unregisterEventBus(eventBus);
    }
}
