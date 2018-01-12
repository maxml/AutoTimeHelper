package com.maxml.timer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Path;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private EventBus eventBus;
    private MapView mapView;
    private DbController dbController;

    // single path
    private String idPath;
    private Polyline polyline;

    // multi path
    private List<String> listIdPath;
    private List<Polyline> polylines = new ArrayList<>();

    private ShowProgressListener progressListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        progressListener = (ShowProgressListener) context;
    }

    public GoogleMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_google_map, container, false);
        Log.d(Constants.LOG, "GoogleMapFragment method onCreateView");
//        if (mapView == null) {
//            mapView = (SupportMapFragment) getChildFragmentManager()
//                    .findFragmentById(R.id.map);
//        }
        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        // init feedback bridge
        eventBus = new EventBus();
        dbController = new DbController(getContext(), eventBus);
        registerEventBus();
        // get input data
        Bundle argument = getArguments();
        if (argument != null) {
            idPath = argument.getString(Constants.EXTRA_ID_PATH);
            listIdPath = argument.getStringArrayList(Constants.EXTRA_LIST_ID_PATH);
        } else {
            // todo data for test fragment
            idPath = "-L1x9-4twC3flcTw9FX8";
        }
        // init map
        mapView.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(Constants.LOG, "start method onMapReady");
        this.map = map;
        if (polyline == null && polylines.size() == 0) {
            getDataFromDb();
        }
    }

    @Subscribe()
    public void onReceiveSinglePath(Path path) {
        Log.d(Constants.LOG, "start method onReceiveSinglePath");
        if (path == null || path.getCoordinates() == null) {
            Toast.makeText(getActivity(), R.string.toast_walk_without_path, Toast.LENGTH_LONG).show();
            return;
        }
        PolylineOptions polylineOptions = getPolylineOptions(path);
        polyline = map.addPolyline(polylineOptions);
        polyline.setClickable(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(polyline.getPoints().get(0), 15.0f));
        progressListener.hideProgressBar();
    }

    @Subscribe()
    public void onReceiveMultiPath(ArrayList<Path> paths) {
        Log.d(Constants.LOG, "start method onReceiveMultiPath");
        if (paths == null) {
            Toast.makeText(getActivity(), R.string.toast_walk_without_path, Toast.LENGTH_LONG).show();
            return;
        }
        for (Path path : paths) {
            if (path == null || path.getCoordinates() == null || path.getCoordinates().size() > 0) {
                continue;
            }
            PolylineOptions polylineOptions = getPolylineOptions(path);
            Polyline polyline = map.addPolyline(polylineOptions);
            polyline.setClickable(true);
            polylines.add(polyline);
        }
        if (polylines.size() > 0) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(polylines.get(0).getPoints().get(0), 15.0f));
        } else {
            Toast.makeText(getActivity(), R.string.toast_walk_without_path, Toast.LENGTH_LONG).show();
        }
        progressListener.hideProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Constants.LOG, "GoogleMapFragment method onStart");
        registerEventBus();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constants.LOG, "GoogleMapFragment method onResume");
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        Log.d(Constants.LOG, "GoogleMapFragment method onPause");
        if (mapView != null) {
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(Constants.LOG, "GoogleMapFragment method onStop");
        unregisterEventBus();
        if (map != null) {
            mapView.onStop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.LOG, "GoogleMapFragment method onDestroy");
        map.clear();
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    private PolylineOptions getPolylineOptions(Path path) {
        List<Coordinates> coordinates = path.getCoordinates();
        PolylineOptions polylineOptions = new PolylineOptions();
        for (Coordinates coordinate : coordinates) {
            LatLng point = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
            polylineOptions.add(point);
            map.addMarker(new MarkerOptions().position(point).title(coordinate.getDate().toString()));
        }
        return polylineOptions;
    }

    private void getDataFromDb() {
        Log.d(Constants.LOG, "start method getDataFromDb");
        if (listIdPath != null) {
            progressListener.showProgressBar();
            dbController.getPathFromDb(listIdPath);
            return;
        }
        if (idPath != null) {
            progressListener.showProgressBar();
            dbController.getPathFromDb(idPath);
            return;
        }
    }

    private void registerEventBus() {
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
            dbController.registerEventBus(eventBus);
        }
    }

    private void unregisterEventBus() {
        if (eventBus.isRegistered(this)) {
            dbController.unregisterEventBus(eventBus);
            eventBus.unregister(this);
        }
    }
}
