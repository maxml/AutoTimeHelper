package com.maxml.timer.controllers;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.maxml.timer.R;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Events;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.DialogFactory;
import com.maxml.timer.util.NotificationHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.maxml.timer.util.Constants.MIN_DISTANCE_UPDATES;
import static com.maxml.timer.util.Constants.MIN_TIME;

public class ReceiverService extends Service implements LocationListener {

    private ActionController actionController;
    private DbController dbController;
    private EventBus serviceEventBus;
    private EventBus widgetEventBus;
    private EventBus callEventBus;

    private Handler handler;
    private int dontMoveTimer = 0;/*in min*/
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private LocationManager locationManager;
    private List<Coordinates> wayCoordinates = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.TAG, "Service: onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceEventBus = new EventBus();
        actionController = ActionController.build(this, serviceEventBus);
        dbController = DbController.build(this, serviceEventBus);
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
            case Constants.EVENT_SET_WIFI_EVENT_BUS:

        }
    }

    @Subscribe()
    public void onReceiveDbEvent(Events.DbResult event) {
        if (event.getResultStatus().equals(Constants.EVENT_WALK_ACTION_SAVED)) {
            actionController.walkActionSaved(event.getActionId());
        }
    }

    @Subscribe()
    public void onReceiveWigetEvent(Events.WidgetEvent event) {
        actionController.onReceiveWidgetEvent(event);
    }

    @Subscribe()
    public void onReceiveCallEvent(Events.CallEvent event) {
        actionController.onReceiveCallEvent(event);
    }


    @Subscribe()
    public void onGpsEvent(Events.GPS event) {
        switch (event.getMessage()) {
            case Constants.EVENT_GPS_START:
                initLocationListener();
                wayCoordinates = new ArrayList<>();
                startTimer();
                break;

            case Constants.EVENT_GPS_STOP:
                actionController.gpsStopEvent(wayCoordinates);
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
        Log.d(Constants.TAG, "Service: onDestroy");
        unregisterEventBus(serviceEventBus);
        unregisterEventBus(widgetEventBus);
        unregisterEventBus(callEventBus);
        stopTimer();
        super.onDestroy();
    }

    private void startTimer() {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                dontMoveTimer++;
                if (dontMoveTimer >= Constants.WAY_DONT_MOVE_TIME) {
                    actionController.dontMoveTimerOff();
                } else {
                    handler.postDelayed(this, 60000);
                }
            }
        });
    }

    private void stopTimer() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(ReceiverService.this);
        }
    }

    void initLocationListener() {
        Log.d("TAG", "initLocationListener() CALLED");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted
            Toast.makeText(this, R.string.location_permissinons_not_garanted, Toast.LENGTH_LONG).show();
            return;
        }

        if (!isGPSEnabled && !isNetworkEnabled) {
            DialogFactory.showGpsSwitchAlert(this);
            return;
        }
        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE_UPDATES, this);
        } else {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE_UPDATES, this);
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

