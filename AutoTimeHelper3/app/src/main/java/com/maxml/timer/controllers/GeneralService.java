package com.maxml.timer.controllers;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.maxml.timer.MyLog;
import com.maxml.timer.entity.eventBus.Events;
import com.maxml.timer.googlemap.GPSTracker;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import permissions.dispatcher.NeedsPermission;

public class GeneralService extends Service {

    private Controller controller;
    private EventBus serviceEventBus;
    private EventBus widgetEventBus;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.d("Service: onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.d("Service: onCreate");
        serviceEventBus = new EventBus();
        controller = Controller.build(this, serviceEventBus);
        // start service as foreground
        Notification notification = NotificationHelper.getDefaultNotification(this);
        startForeground(Constants.NOTIFICATION_ID, notification);
        // start tracker service
        startGpsTracker();
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void startGpsTracker() {
        startService(new Intent(this, GPSTracker.class));
    }


    @Subscribe()
    public void onEventBusControll(Events.EventBusControl event) {
        String message = event.getMessage();
        switch (message) {
            case Constants.EVENT_REGISTER_EVENT_BUS:
                registerEventBus(event.getEventBus());
                break;
            case Constants.EVENT_UNREGISTER_EVENT_BUS:
                unregisterEventBus(event.getEventBus());
                break;
            case Constants.EVENT_SET_WIDGET_EVENT_BUS:
                widgetEventBus = event.getEventBus();
                break;
        }
    }

    @Subscribe()
    public void onReceiveWigetEvent(Events.WidgetEvent event) {
        controller.onReceiveWidgetEvent(event);
    }

    @Subscribe()
    public void onReceiveCallEvent(Events.CallEvent event) {
        controller.onReceiveCallEvent(event);
    }

    @Subscribe()
    public void onReceiveInfoEvent(Events.GPS event) {
//        controller.onReceiveWidgetEvent(event);
    }

    @Override
    public void onDestroy() {
        MyLog.d("Service: onDestroy");
        serviceEventBus.unregister(this);
        widgetEventBus.unregister(this);
        super.onDestroy();
    }

    private void registerEventBus(EventBus eventBus) {
        if (eventBus != null && !eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void unregisterEventBus(EventBus eventBus) {
        if (eventBus != null && eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
    }
}

