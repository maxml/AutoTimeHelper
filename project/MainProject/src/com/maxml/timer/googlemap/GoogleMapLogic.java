package com.maxml.timer.googlemap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.maxml.timer.R;
import com.maxml.timer.SliceControllers.Controller;
import com.maxml.timer.api.SliceCRUD;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.entity.Table;
import com.maxml.timer.googlemap.GPSTracker.OnLocationChangedListener;
import com.parse.ParseUser;

public class GoogleMapLogic extends ActionBarActivity implements OnMapReadyCallback,
		OnLocationChangedListener {
	
	public static final String LOG = "MyShit";
	public static final String NORM_LOG = "NormLog";
	
	private GPSTracker gps;
	private GoogleMap map;
	private Controller controller = new Controller();
	
	private List<Line> list = new ArrayList<Line>();
	
	public boolean heIsStandingNow = false;
	
	private Coordinates point;
	private Coordinates pointRadius;
	
	private Polyline lineAdd;
	private Calendar c = Calendar.getInstance();
	private Date starttime = c.getTime();
	
	private static final double MIN_DISTANS_TO_ADD_NEW_POINT_ON_MAP = 5;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_map);
		
		gps = new GPSTracker(GoogleMapLogic.this);
		gps.setLocationChangeListener(this);
		
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
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
		
		Log.d(LOG, "current time = " + c);
		Log.d(LOG, "user " + ParseUser.getCurrentUser().getObjectId());
		if (pointRadius != null) {
			Log.d(LOG, "point radius != null");
			Log.d(LOG, "distance:" + Coordinates.getDistanceInMeter(pointRadius, point));
			if (Coordinates.getDistanceInMeter(pointRadius, point) > MIN_DISTANS_TO_ADD_NEW_POINT_ON_MAP) {
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
				Point start = new Point(point.getLat(), point.getLong());
				Point finish = new Point(pointRadius.getLat(), pointRadius.getLong());
				Line line = new Line(start, finish, ParseUser.getCurrentUser().getObjectId());
				Slice slice = new Slice(ParseUser.getCurrentUser().getObjectId(), line, starttime,
						finishtime, "walk time", SliceType.WALK);
				controller.addSlise(slice);
				Log.d(LOG, "slice = " + slice.toString());
				list.add(line);
				
				starttime = finishtime;
			} else {
				Log.d(LOG, "he is standing now = true");
				heIsStandingNow = true;
			}
		}
		pointRadius = point;
	}
}
