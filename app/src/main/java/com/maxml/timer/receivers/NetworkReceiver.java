package com.maxml.timer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maxml.timer.controllers.ActionController;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.entity.Events;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;

public class NetworkReceiver extends BroadcastReceiver {
    private DbController dbController;
    private EventBus eventBus;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (NetworkUtil.isWifiAvailable(context)) {
            initController(context);

            WifiState wifiState  = NetworkUtil.getCurrentWifi(context);

            dbController.wifiActivated(wifiState);
        }
    }

    private void initController(Context context) {
        if (dbController == null) {
            eventBus = new EventBus();
            dbController = new DbController(context, eventBus);

            dbController.registerEventBus(eventBus);
            eventBus.post(new Events.WifiEvent(Constants.EVENT_SET_WIFI_EVENT_BUS));
        }
    }
}
