package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.maxml.timer.R;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Path;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private EventBus eventBus;
    private Controller controller;

    // single path
    private String idPath;
    private Polyline polyline;

    // multi path
    private List<String> listIdPath;
    private List<Polyline> polylines = new ArrayList<>();


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
        if (listIdPath != null) {
            controller.getPathFromDb(listIdPath);
            return;
        }
        if (idPath != null) {
            controller.getPathFromDb(idPath);
            return;
        }
    }

    @Subscribe()
    public void onReceiveSinglePath(Path path) {
        if (path == null || path.getCoordinates() == null) {
            return;
        }
        PolylineOptions polylineOptions = getPolylineOptions(path);
        polyline = map.addPolyline(polylineOptions);
        polyline.setClickable(true);
    }

    @Subscribe()
    public void onReceiveMultyPath(ArrayList<Path> paths) {
        if (paths == null) {
            return;
        }
        for (Path path : paths) {
            if (path == null || path.getCoordinates() == null) {
                continue;
            }
            PolylineOptions polylineOptions = getPolylineOptions(path);
            Polyline polyline = map.addPolyline(polylineOptions);
            polyline.setClickable(true);
            polylines.add(polyline);
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

    private PolylineOptions getPolylineOptions(Path path) {
        List<Coordinates> coordinates = path.getCoordinates();
        PolylineOptions polylineOptions = new PolylineOptions();
        for (Coordinates coordinate : coordinates) {
            polylineOptions.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()));
        }
        return polylineOptions;
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
