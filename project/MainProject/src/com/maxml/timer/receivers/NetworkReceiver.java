package com.maxml.timer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {

	private static final String LOG_TAG = "NetworkReceiver";
	private static final String CONNECTED = "android.net.NetworkInfo.State.CONNECTED";
	private static final String CONNECTIVITY_SERVICE = "android.content.Context.CONNECTIVITY_SERVICE = connectivity";
	private boolean isConnected = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(LOG_TAG, "Receieved notification about network status");
		isNetworkAvailable(context);
	}

	// TODO:strings to resources
	private boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						if (!isConnected) {
							Log.v(LOG_TAG, "Now you are connected to Internet!");
							Toast.makeText(context, "Internet available", Toast.LENGTH_SHORT).show();
							isConnected = true;
						}
						return true;
					}
				}
			}
		}
		Log.v(LOG_TAG, "You are not connected to Internet!");
		Toast.makeText(context, "Internet NOT availablle", Toast.LENGTH_SHORT).show();
		isConnected = false;
		return false;
	}
}
