package com.maxml.timer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkStatus {
	public static final String LOG_TAG = "NetworkStatus";
	public static final String INTERNET_AVAILABLE = "Internet available!";
	public static final String INTERNET_NOT_AVAILABLE = "Internet NOT availablle!";
	public static final String SETTINGS = "Settings";
	public static boolean isConnected = false;
	
	// public void isNetworkAvailable(Context context) {
	// ConnectivityManager connectivity = (ConnectivityManager) context
	// .getSystemService(Context.CONNECTIVITY_SERVICE);
	// if (connectivity != null) {
	// if
	// (connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()
	// ||
	// connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected())
	// {
	// isConnected = true;
	// Log.v(LOG_TAG, "Now you are connected to Internet!");
	// Toast.makeText(context, INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
	// } else {
	// isConnected = false;
	// Log.v(LOG_TAG, "You are not connected to Internet!");
	// Toast.makeText(context, INTERNET_NOT_AVAILABLE, Toast.LENGTH_SHORT).show();
	// }
	// }
	// }
	//
	public void isNetworkOnline(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
				isConnected = true;
				Toast.makeText(context, INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
			} else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
					isConnected = true;
					Toast.makeText(context, INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, INTERNET_NOT_AVAILABLE, Toast.LENGTH_SHORT).show();
		}
		
	}
}
