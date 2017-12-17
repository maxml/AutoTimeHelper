package com.maxml.timer.controllers;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.maxml.timer.MyLog;
import com.maxml.timer.entity.eventBus.EventMessage;
import com.maxml.timer.entity.eventBus.DbMessage;
import com.maxml.timer.googlemap.GPSTracker;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.EventBusType;

import org.greenrobot.eventbus.Subscribe;

import permissions.dispatcher.NeedsPermission;

public class GeneralService extends Service {

    private Controller controller;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.d("Service: onCreate");
        controller = new Controller(this);
        // register EventBus
        registerEventBus();
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.d("Service: onStartCommand");
        return START_STICKY;
    }

    @Subscribe()
    public void onReceiveEvent(DbMessage message) {
        controller.onReceiveDbMessage(message);
    }

    @Subscribe()
    public void onReceiveEvent(EventMessage message) {
        controller.onReceiveEventMessage(message);
    }

    private void registerEventBus() {
        controller.getEventBus(EventBusType.DB).register(this);
        controller.getEventBus(EventBusType.HOME_FRAGMENT).register(this);
        controller.getEventBus(EventBusType.ACTION_EVENT).register(this);
        controller.getEventBus(EventBusType.GPS).register(this);
        controller.getEventBus(EventBusType.CONTROLLER).register(this);
    }

    @Override
    public void onDestroy() {
        MyLog.d("Service: onDestroy");
        controller.getEventBus(EventBusType.DB).unregister(this);
        controller.getEventBus(EventBusType.HOME_FRAGMENT).unregister(this);
        controller.getEventBus(EventBusType.ACTION_EVENT).unregister(this);
        controller.getEventBus(EventBusType.GPS).unregister(this);
        controller.getEventBus(EventBusType.CONTROLLER).unregister(this);
        super.onDestroy();
    }
}
