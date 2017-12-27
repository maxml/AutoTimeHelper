package com.maxml.timer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.entity.Events;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;

public class NetworkReceiver extends BroadcastReceiver {
    private Controller controller;
    private EventBus eventBus;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (NetworkUtil.isWifiAvailable(context)) {
            initController(context);

            WifiState wifiState  = NetworkUtil.getCurrentWifi(context);

            controller.wifiActivated(wifiState);
        }
    }

    private void initController(Context context) {
        if (controller == null) {
            eventBus = new EventBus();
            controller = new Controller(context, eventBus);

            controller.registerEventBus(eventBus);
            eventBus.post(new Events.WifiEvent(Constants.EVENT_SET_WIFI_EVENT_BUS));
        }
    }
}
