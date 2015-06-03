package com.maxml.timer.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkStatus {
	public static final String LOG_TAG = "NetworkStatus";
	public static final String INTERNET_AVAILABLE = "Internet available!";
	public static final String INTERNET_NOT_AVAILABLE = "Internet NOT availablle!";
	public static final String SETTINGS = "Settings";
	public static boolean isConnected = false;

	public static boolean isNetworkAvailable(Context context) {
		final Context contextThis = context;
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
		// AlertDialog.Builder notAvailablle = new AlertDialog.Builder(context);
		// notAvailablle.setMessage(INTERNET_NOT_AVAILABLE);
		// notAvailablle.setCancelable(true);
		// notAvailablle.setPositiveButton(SETTINGS,
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// Intent intent = new
		// Intent(android.provider.Settings.ACTION_SETTINGS);
		// contextThis.startActivity(intent);
		// }
		//
		// });
		// notAvailablle.show();
		isConnected = false;
		return false;
	}
}
