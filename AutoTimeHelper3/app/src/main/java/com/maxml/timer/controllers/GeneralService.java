package com.maxml.timer.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.maxml.timer.MyLog;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.eventBus.Events;
import com.maxml.timer.googlemap.GPSTracker;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.DialogFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import permissions.dispatcher.NeedsPermission;

import static com.maxml.timer.util.Constants.MIN_DISTANCE_UPDATES;
import static com.maxml.timer.util.Constants.MIN_TIME;

public class GeneralService extends Service implements LocationListener {

    private Controller controller;
    private EventBus serviceEventBus;
    private EventBus widgetEventBus;
    private EventBus callEventBus;


    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    private LocationManager locationManager;
    private List<Coordinates> wayCoordinates = new ArrayList<>();


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
        serviceEventBus.register(this);
        // start service as foreground
        Notification notification = NotificationHelper.getDefaultNotification(this);
        startForeground(Constants.NOTIFICATION_ID, notification);
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
            case Constants.EVENT_SET_CALL_EVENT_BUS:
                callEventBus = event.getEventBus();
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
    public void onGpsEvent(Events.GPS event) {
        switch (event.getMessage()) {
            case Constants.EVENT_GPS_START:
                initLocationListener();
                wayCoordinates = new ArrayList<>();
                break;

            case Constants.EVENT_GPS_STOP:
                controller.savePath(wayCoordinates);
                stopUsingGPS();
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Coordinates point = new Coordinates();
        point.setLatitude(location.getLatitude());
        point.setLongitude(location.getLongitude());
        point.setDate(new Date());

        if (wayCoordinates.size() == 0) {
            wayCoordinates.add(point);
            return;
        }

        // if distance between previous point and current more than constant
        // add current point to wayCoordinates
        if (Coordinates.getDistanceInMeter(wayCoordinates.get(wayCoordinates.size() - 1), point) > MIN_DISTANCE_UPDATES) {
            wayCoordinates.add(point);
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onDestroy() {
        MyLog.d("Service: onDestroy");
        serviceEventBus.unregister(this);
        widgetEventBus.unregister(this);
        callEventBus.unregister(this);
        super.onDestroy();
    }

    private void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GeneralService.this);
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    @SuppressLint("MissingPermission")
    private void initLocationListener() {
        Log.d("TAG", "initLocationListener() CALLED");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            DialogFactory.showGpsSwitchAlert(this);
        } else {

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME,
                        MIN_DISTANCE_UPDATES, this);

            }

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME,
                        MIN_DISTANCE_UPDATES, this);

            }
        }
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

