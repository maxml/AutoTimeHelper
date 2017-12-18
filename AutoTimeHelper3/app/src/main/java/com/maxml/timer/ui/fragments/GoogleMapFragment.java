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

    public static final String LOG = "MyShit";

    private GoogleMap map;

    public List<Object> list = new ArrayList<>();
    public List<Line> listLine = new ArrayList<>();
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
        mapFragment.getMapAsync(this);

        return inflater.inflate(R.layout.activity_google_map, container, false);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(LOG, "start method onMapReady");
        this.map = map;
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


    public void onLocationChanged(Location location) {
        Log.d(LOG, "start method onLocationChanged");
        point = new Coordinates();
        point.setLatitude(location.getLatitude());
        point.setLongitude(location.getLongitude());
        point.setDate(new Date());

        // Log.d(LOG, "current time = " + c);
//		Log.d(LOG, "user " + ParseUser.getCurrentUser().getId());
        if (pointRadius != null) {
            Log.d(LOG, "point radius != null");
            Log.d(LOG, "distance:" + Coordinates.getDistanceInMeter(pointRadius, point));

            if (Coordinates.getDistanceInMeter(pointRadius, point) > Constants.MIN_DISTANCE_UPDATES) {
                inRadius();
            } else {
                outTheRadius();
            }
        }
        pointRadius = point;
    }

    private void inRadius() {
        map.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(),
                point.getLongitude())));
        map.moveCamera(CameraUpdateFactory.newLatLng(new
                LatLng(point.getLatitude(), point.getLongitude())));
        map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

        lineAdd = map.addPolyline(new PolylineOptions()
                .add(new LatLng(point.getLatitude(), point.getLongitude()),
                        new LatLng(pointRadius.getLatitude(),
                                pointRadius.getLongitude())).width(5).color(Color.RED));

//        c = Calendar.getInstance();
//        Date finishTime = c.getTime();
//        Point finish = new Point(point.getLatitude(), point.getLongitude());
//        Point start = new Point(pointRadius.getLatitude(), pointRadius.getLongitude());
//        Line line = new Line(start, finish, UserAPI.getCurrentUser().getId());
//        listLine.add(line);
//        Slice slice = new Slice(ParseUser.getCurrentUser().getId(), line, starttime, finishTime,
//                "walk time", SliceType.WALK);
//        controllerGoogleMap.addSlice(slice);
//        Log.d(LOG, "start = " + start.toString());
//        Log.d(LOG, "finish = " + finish.toString());
//        Log.d(LOG, " ---------------------------------------- ");
//        walkActions.add(slice);
//        starttime = finishTime;
//        list.add(lineAdd);
    }

    private void outTheRadius() {
        c = Calendar.getInstance();
        Date finishtime = c.getTime();

        Log.d(LOG, "he is standing now = true");

//        Point finish = new Point(point.getLatitude(), point.getLongitude());
//        Point start = new Point(pointRadius.getLatitude(), pointRadius.getLongitude());
//        Line line = new Line(start, finish, ParseUser.getCurrentUser().getId());
//        Slice slice = new Slice(ParseUser.getCurrentUser().getId(), line, starttime, finishtime,
//                "rest time", SliceType.REST);
//        controllerGoogleMap.addSlice(slice);
//        Log.d(LOG, "start = " + start.toString());
//        Log.d(LOG, "finish = " + finish.toString());
//        Log.d(LOG, "slice = " + slice.toString());
//        Log.d(LOG, " ---------------------------------------- ");
//        walkActions.add(slice);
//        starttime = finishtime;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
