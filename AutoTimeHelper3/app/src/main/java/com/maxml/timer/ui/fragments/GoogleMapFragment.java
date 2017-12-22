package com.maxml.timer.ui.fragments;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.maxml.timer.R;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.actions.Action;
import com.maxml.timer.entity.eventBus.Events;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private EventBus eventBus;
    private Controller controller;

    private String idPath;
    private List<String> listIdPath;

    public static List<Action> walkActions = new ArrayList<>();

    private Coordinates point;
    private Coordinates pointRadius;

    private Polyline lineAdd;
    private Calendar c = Calendar.getInstance();
    private Date starttime = c.getTime();


    public GoogleMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        // init feedback bridge
        eventBus = new EventBus();
        controller = new Controller(getContext(), eventBus);
        registerEventBus();
        // get input data
        Bundle argument = getArguments();
        idPath = argument.getString(Constants.EXTRA_ID_PATH);
        listIdPath = argument.getStringArrayList(Constants.EXTRA_LIST_ID_PATH);
        // init map
        mapFragment.getMapAsync(this);
        return inflater.inflate(R.layout.activity_google_map, container, false);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(Constants.LOG, "start method onMapReady");
        this.map = map;
        if (listIdPath != null){
            controller.getPathFromDb(listIdPath);
            return;
        }
        if (idPath != null){
            controller.getPathFromDb(idPath);
            return;
        }
    }

    @Subscribe()
    public void onGpsEvent(Events.GPS event) {
        switch (event.getMessage()) {
            case Constants.EVENT_LOCATION_CHANGE:
//                Location location = (Location) event.getData();
//                onLocationChanged(location);
                break;

            case Constants.EVENT_WAY_COORDINATES:
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
    }

    @Override
    public void onStop() {
        unregisterEventBus();
        super.onStop();
    }


    private void registerEventBus() {
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
            controller.registerEventBus(eventBus);
        }
    }

    private void unregisterEventBus() {
        if (eventBus.isRegistered(this)) {
            controller.unregisterEventBus(eventBus);
            eventBus.unregister(this);
        }
    }
}
