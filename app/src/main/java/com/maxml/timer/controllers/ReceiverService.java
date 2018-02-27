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
    private EventBus wifiEventBus;
    private EventBus mainActivityEventBus;

    private Handler handler;
    private int dontMoveTimer = 0;/*in min*/
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean isFirstRunMineActivity = true;
    private Location startAutoWalkPoint;
    private LocationManager locationManager;
    private LocationListener autoWalkActionListener;
    private List<Coordinates> wayCoordinates = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.LOG_TAG, "Service: onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.LOG, "Start service");
        serviceEventBus = new EventBus();
        actionController = ActionController.build(this, serviceEventBus);
        dbController = DbController.build(this, serviceEventBus);
        serviceEventBus.register(this);
        // start service as foreground
        Notification notification = NotificationHelper.getDefaultNotification(this);
        startForeground(Constants.NOTIFICATION_APP_ID, notification);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Constants.LOG, "catch new location" + location.getLatitude() + " : " + location.getLongitude());
        if (location.getAccuracy()>40){
            Log.d(Constants.LOG, "Location accuracy is too huge: " + location.getAccuracy());
            return;
        }
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
                wifiEventBus = event.getEventBus();
                break;
            case Constants.EVENT_SET_MAIN_ACTIVITY_EVENT_BUS:
                mainActivityEventBus = event.getEventBus();
                if (isFirstRunMineActivity) {
                    isFirstRunMineActivity = false;
                    initAutoStartWalkAction();
                }
                break;
        }
    }

    @Subscribe()
    public void onReceiveDbEvent(Events.DbResult event) {
        if (event.getResultStatus().equals(Constants.EVENT_WALK_ACTION_SAVED)) {
            actionController.walkActionSaved(event.getActionId());
        }
    }

    @Subscribe()
    public void onInfoEvent(Events.Info event) {
        switch (event.getEventMessage()) {
            case Constants.EVENT_CLOSE_APP:
                stopForeground(true);
                stopSelf();
                break;
        }
    }

    @Subscribe()
    public void onTurnOnGeolocationEvent(Events.TurnOnGeolocation event) {
        switch (event.getMessage()) {
            case Constants.EVENT_TURN_ON_SUCCESSFUL:
                if (event.getRequest() == Constants.REQUEST_WALK_TRACKER) {
                    initWalkLocationListener();
                }
                if (event.getRequest() == Constants.REQUEST_AUTO_WALK_STARTER) {
                    initAutoStartWalkAction();
                }
                break;
            case Constants.EVENT_TURN_ON_DENY:
                NotificationHelper.showMessageNotification(this, getString(R.string.message_deny_location_tracker));
                break;
        }
    }

    @Subscribe()
    public void onReceiveWifiEvent(Events.WifiEvent event) {
        actionController.onReceiveWifiEvent(event);
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
                Log.d(Constants.LOG, "EVENT_GPS_START");
                // check location permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    initWalkLocationListener();
                    stopAutoStartWalkAction();
                } else {
                    // permission not granted
                    Toast.makeText(this, R.string.toast_deny_location_permission, Toast.LENGTH_LONG).show();
                    return;
                }
                wayCoordinates = new ArrayList<>();
                startTimer();
                break;

            case Constants.EVENT_GPS_STOP:
                Log.d(Constants.LOG, "EVENT_GPS_STOP");
                actionController.gpsStopEvent(wayCoordinates);
                stopUsingGPS();
                initAutoStartWalkAction();
                break;
        }
    }

    // need location permission
    private void initWalkLocationListener() {
        Log.d(Constants.LOG, "initWalkLocationListener() CALLED");
        // check location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted
            Toast.makeText(this, R.string.toast_deny_location_permission, Toast.LENGTH_LONG).show();
            Log.d(Constants.LOG, "location permission not granted");
            return;
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            if (mainActivityEventBus != null) {
                mainActivityEventBus.post(new Events.TurnOnGeolocation(Constants.EVENT_TURN_ON_GEOLOCATION, Constants.REQUEST_WALK_TRACKER));
            } else {
                Log.d(Constants.LOG, "mainActivityEventBus == null");
            }
        }

        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE_UPDATES, this);
        }
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE_UPDATES, this);
        }
    }

    private void stopAutoStartWalkAction() {
        Log.d(Constants.LOG, "Autostart stopAutoStartWalkAction()");
        if (locationManager != null) {
            locationManager.removeUpdates(autoWalkActionListener);
        }
        autoWalkActionListener = null;
        startAutoWalkPoint = null;
    }


    private void initAutoStartWalkAction() {
        Log.d(Constants.LOG, "Autostart initAutoStartWalkAction()");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted
            Log.d(Constants.LOG, "Autostart cancel, location permission not granted");
            return;
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            if (mainActivityEventBus != null) {
                mainActivityEventBus.post(new Events.TurnOnGeolocation(Constants.EVENT_TURN_ON_GEOLOCATION, Constants.REQUEST_AUTO_WALK_STARTER));
            } else {
                Log.d(Constants.LOG, "EventBus mainActivityEventBus == null");
            }
            return;
        }
        Log.d(Constants.LOG, "Autostart WalkAction: turn on and permission granted");
        autoWalkActionListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(Constants.LOG, location.getProvider() + " " + location.getAccuracy());
                if (startAutoWalkPoint == null) {
                    startAutoWalkPoint = location;
                    return;
                }

                if (location.getAccuracy() < 40 && startAutoWalkPoint.distanceTo(location) >= Constants.MIN_DISTANCE_START_WALK_ACTION) {
                    Log.d(Constants.LOG, "Autostart WalkAction: is start WalkAction");
                    actionController.autoWalkAction();
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
        };

        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME,
                    Constants.MIN_DISTANCE_START_WALK_ACTION, autoWalkActionListener);
        }
        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME,
                    Constants.MIN_DISTANCE_START_WALK_ACTION, autoWalkActionListener);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, "Service: onDestroy");
        unregisterEventBus(serviceEventBus);
        unregisterEventBus(widgetEventBus);
        unregisterEventBus(callEventBus);
        unregisterEventBus(wifiEventBus);
        unregisterEventBus(mainActivityEventBus);
        stopTimer();
        stopUsingGPS();
        stopAutoStartWalkAction();
        super.onDestroy();
    }

    private void startTimer() {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                dontMoveTimer++;
                if (dontMoveTimer >= Constants.WAY_DONT_MOVE_TIME) {
                    Log.d(Constants.LOG_TAG, "Don't move timer activated");
                    actionController.dontMoveTimerOff();
                    stopTimer();
                } else {
                    handler.postDelayed(this, 60000);
                }
            }
        });
    }

    private void stopTimer() {
        dontMoveTimer = 0;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(ReceiverService.this);
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

