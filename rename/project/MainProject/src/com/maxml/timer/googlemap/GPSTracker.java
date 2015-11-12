package com.maxml.timer.googlemap;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {
	
	private final Context context;
	
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	
	private Location location;
	
	double latitude;
	double longitude;
	
	private static final long MIN_DISTANCE_UPDATES = 5;
	private static final long MIN_TIME = 1000 * 20;
	public boolean isLastChengedOfLocationWas = false;
	
	protected LocationManager locationManager;
	
	private OnLocationChangedListener listener;
	
	public interface OnLocationChangedListener {
		void onLocationChanged(Location location);
	}
	
	public GPSTracker(Context context) {
		this.context = context;
		getLocation();
	}
	
	public void setLocationChangeListener(OnLocationChangedListener listener) {
		this.listener = listener;
	}
	
	public Location getLocation() {
		Log.d("TAG", "getLocation() CALLED");
		try {
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
			
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
		
		return location;
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
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		
		alertDialog.setTitle("GPS is setting");
		
		alertDialog.setMessage("GPS is not enabled. Do you want to go to setting menu?");
		
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
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
	public void onLocationChanged(Location location) {
		listener.onLocationChanged(location);
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		getLocation();
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		getLocation();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
