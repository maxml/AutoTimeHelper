package com.maxml.timer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkStatus {
	private static final String LOG_TAG = "NetworkStatus";
	private static final String INTERNET_AVAILABLE = "Internet available!";
	private static final String INTERNET_NOT_AVAILABLE = "Internet NOT availablle!";
	private static boolean isConnected = false;

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						if (!isConnected) {
							isConnected = true;
						}
						Log.v(LOG_TAG, "Now you are connected to Internet!");
						Toast.makeText(context, INTERNET_AVAILABLE,
								Toast.LENGTH_SHORT).show();
						return true;
					}
				}
			}
		}
		Log.v(LOG_TAG, "You are not connected to Internet!");
		Toast.makeText(context, INTERNET_NOT_AVAILABLE, Toast.LENGTH_SHORT)
				.show();
		isConnected = false;
		return false;
	}

}
