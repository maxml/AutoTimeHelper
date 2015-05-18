package com.maxml.timer.receivers;

import com.maxml.timer.util.NetworkStatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver {
	private static final String LOG_TAG = "NetworkReceiver";
	NetworkStatus networkStatus;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(LOG_TAG, "Receieved notification about network status");
		networkStatus.isNetworkAvailable(context);
	}

}
