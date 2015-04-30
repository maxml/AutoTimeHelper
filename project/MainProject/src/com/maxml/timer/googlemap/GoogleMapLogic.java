package com.maxml.timer.googlemap;

import java.util.ArrayList;
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
import com.maxml.timer.googlemap.GPSTracker.OnLocationChangedListener;

public class GoogleMapLogic extends ActionBarActivity implements
		OnMapReadyCallback, OnLocationChangedListener {

	public static final String LOG = "MyShit";
	public static final String NORM_LOG = "NormLog";

	private GPSTracker gps;
	private GoogleMap map;

	private List<Object> list = new ArrayList<Object>();

	public boolean heIsStandingNow = false;

	private Coordinates point;
	private Coordinates pointRadius;

	private Polyline line;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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

		if (pointRadius != null) {
			if (pointRadius.getDistance(point) > 5
					&& pointRadius.getDistance(point) < -5) {
				map.addMarker(new MarkerOptions().position(new LatLng(point
						.getLat(), point.getLong())));
				map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(point
						.getLat(), point.getLong())));
				map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

				line = map.addPolyline(new PolylineOptions()
						.add(new LatLng(point.getLat(), point.getLong()),
								new LatLng(pointRadius.getLat(), pointRadius
										.getLong())).width(5).color(Color.RED));
				list.add(line);
			} else {
				Log.d(LOG, "he is standing now = true");
				heIsStandingNow = true;
			}
		}
		pointRadius = point;
	}
}
