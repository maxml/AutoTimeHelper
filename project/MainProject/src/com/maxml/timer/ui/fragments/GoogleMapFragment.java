package com.maxml.timer.ui.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.maxml.timer.R;
import com.maxml.timer.controllers.ControllerGoogleMap;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.googlemap.GPSTracker;
import com.maxml.timer.googlemap.GPSTracker.OnLocationChangedListener;
import com.parse.ParseUser;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback,
		OnLocationChangedListener {
	
	public static final String LOG = "MyShit";
	public static final String NORM_LOG = "NormLog";
	
	private GPSTracker gps;
	private GoogleMap map;
	private ControllerGoogleMap controllerGoogleMap = new ControllerGoogleMap();
	
	public List<Object> list = new ArrayList<Object>();
	public List<Line> listLine = new ArrayList<Line>();
	public static List<Slice> listSlice = new ArrayList<Slice>();
	
	private Coordinates point;
	private Coordinates pointRadius;
	
	private Polyline lineAdd;
	private Calendar c = Calendar.getInstance();
	private Date starttime = c.getTime();
	
	private static final double MIN_DISTANS_TO_ADD_NEW_POINT_ON_MAP = 5;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_google_map, container, false);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gps = new GPSTracker(getActivity());
		gps.setLocationChangeListener(this);
		
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		
	}
	
	@Override
	public void onMapReady(GoogleMap map) {
		Log.d(LOG, "start method onMapReady");
		this.map = map;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Log.d(LOG, "start method onLocationChanged");
		point = new Coordinates();
		point.setLat(location.getLatitude());
		point.setLong(location.getLongitude());
		
		// Log.d(LOG, "current time = " + c);
		Log.d(LOG, "user " + ParseUser.getCurrentUser().getObjectId());
		if (pointRadius != null) {
			Log.d(LOG, "point radius != null");
			Log.d(LOG, "distance:" + Coordinates.getDistanceInMeter(pointRadius, point));
			
			if (Coordinates.getDistanceInMeter(pointRadius, point) > MIN_DISTANS_TO_ADD_NEW_POINT_ON_MAP) {
				inRadius();
			} else {
				outTheRadius();
			}
		}
		pointRadius = point;
	}
	
	private void inRadius() {
		// map.addMarker(new MarkerOptions().position(new LatLng(point.getLat(),
		// point.getLong())));
		// map.moveCamera(CameraUpdateFactory.newLatLng(new
		// LatLng(point.getLat(), point.getLong())));
		// map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
		
		// lineAdd = map.addPolyline(new PolylineOptions()
		// .add(new LatLng(point.getLat(), point.getLong()),
		// new LatLng(pointRadius.getLat(),
		// pointRadius.getLong())).width(5).color(Color.RED));
		
		c = Calendar.getInstance();
		Date finishtime = c.getTime();
		Point finish = new Point(point.getLat(), point.getLong());
		Point start = new Point(pointRadius.getLat(), pointRadius.getLong());
		Line line = new Line(start, finish, ParseUser.getCurrentUser().getObjectId());
		listLine.add(line);
		Slice slice = new Slice(ParseUser.getCurrentUser().getObjectId(), line, starttime, finishtime,
				"walk time", SliceType.WALK);
		controllerGoogleMap.addSlise(slice);
		Log.d(LOG, "start = " + start.toString());
		Log.d(LOG, "finish = " + finish.toString());
		Log.d(LOG, "slice = " + slice.toString());
		Log.d(LOG, " ---------------------------------------- ");
		listSlice.add(slice);
		starttime = finishtime;
		list.add(lineAdd);
	}
	
	private void outTheRadius() {
		c = Calendar.getInstance();
		Date finishtime = c.getTime();
		
		Log.d(LOG, "he is standing now = true");
		
		Point finish = new Point(point.getLat(), point.getLong());
		Point start = new Point(pointRadius.getLat(), pointRadius.getLong());
		Line line = new Line(start, finish, ParseUser.getCurrentUser().getObjectId());
		Slice slice = new Slice(ParseUser.getCurrentUser().getObjectId(), line, starttime, finishtime,
				"rest time", SliceType.REST);
		controllerGoogleMap.addSlise(slice);
		Log.d(LOG, "start = " + start.toString());
		Log.d(LOG, "finish = " + finish.toString());
		Log.d(LOG, "slice = " + slice.toString());
		Log.d(LOG, " ---------------------------------------- ");
		listSlice.add(slice);
		starttime = finishtime;
	}
}
