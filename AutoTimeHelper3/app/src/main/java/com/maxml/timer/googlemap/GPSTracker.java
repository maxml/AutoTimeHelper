package com.maxml.timer.googlemap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.eventBus.EventMessage;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.EventBusType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.maxml.timer.util.Constants.MIN_DISTANCE_UPDATES;
import static com.maxml.timer.util.Constants.MIN_TIME;

public class GPSTracker extends Service implements LocationListener {

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    private List<Coordinates> wayCoordinates = new ArrayList<>();

    private Location location;
    private LocationManager locationManager;
    private Controller controller;
    private EventBus eventBus;

    private double latitude;
    private double longitude;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        controller = new Controller(this);
        eventBus = controller.getEventBus(EventBusType.GPS);
        eventBus.register(this);
        initLocationListener();
    }

    @Subscribe()
    public void onGpsEvent(EventMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_START:
                if (wayCoordinates.size() == 0) {
                    return;
                }
                Coordinates lastCoordinate = wayCoordinates.get(wayCoordinates.size() - 1);
                wayCoordinates = new ArrayList<>();
                wayCoordinates.add(lastCoordinate);
                break;

            case Constants.EVENT_STOP:
                eventBus.post(new EventMessage(Constants.EVENT_WAY_COORDINATES, wayCoordinates));
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        eventBus.post(new EventMessage(Constants.EVENT_LOCATION_CHANGE, location));

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


    @SuppressLint("MissingPermission")
    private void initLocationListener() {
        Log.d("TAG", "initLocationListener() CALLED");
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME,
                            MIN_DISTANCE_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME,
                            MIN_DISTANCE_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    //TODO: get it in DialogFactory
    public void showSettingAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS is setting");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to setting menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
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
        super.onDestroy();
        if (eventBus != null && eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
    }
}
