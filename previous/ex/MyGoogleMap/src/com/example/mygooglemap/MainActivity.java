package com.example.mygooglemap;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.mygooglemap.GPSTracker.OnLocationChangedListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity implements
		OnMapReadyCallback, OnLocationChangedListener {

	private static final String LOG = "MyShit";
	private static final int NO_TIME = -500;

	private Button btnShowLocation;

	private GPSTracker gps;
	private GoogleMap map;

	private List<Object> list = new ArrayList<Object>();

	private double latitude;
	private double longitude;

	public boolean onPause = false;

	private long lastTime = NO_TIME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gps = new GPSTracker(MainActivity.this);
		gps.setLocationChangeListener(this);

		btnShowLocation = (Button) findViewById(R.id.show_location);

		btnShowLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (gps.canGetLocation()) {
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();

					Toast.makeText(
							getApplicationContext(),
							"Your location is - \nLat: " + latitude
									+ "\nLong: " + longitude, Toast.LENGTH_LONG)
							.show();
				} else {
					gps.showSettingAlert();
				}

			}
		});

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

	}

	@Override
	public void onMapReady(GoogleMap map) {
		this.map = map;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (lastTime == NO_TIME) {
			lastTime = System.currentTimeMillis();
		} else {
			Log.d(LOG,
					"Time he was on this location:"
							+ (System.currentTimeMillis() - lastTime));
		}
		Log.d(LOG, "latitude: " + location.getLatitude() + " longitude:"
				+ location.getLongitude());
		map.addMarker(new MarkerOptions().position(new LatLng(location
				.getLatitude(), location.getLongitude())));
		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location
				.getLatitude(), location.getLongitude())));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

		Coordinates coordinate = new Coordinates();
		coordinate.setLatitude(location.getLatitude());
		coordinate.setLongitude(location.getLongitude());
		list.add(coordinate);
	}
}
