package com.maxml.timer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.maxml.timer.controllers.TableControllerService;
import com.maxml.timer.util.NetworkStatus;

public class NetworkReceiver extends BroadcastReceiver {
	private static final String LOG_TAG = "NetworkReceiver";
	private TableControllerService tbContr;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(LOG_TAG, "Receieved notification about network status");
		NetworkStatus netStat = new NetworkStatus();
		netStat.isNetworkOnline(context);
		// if (netStat.isConnected) {
		// Slice slice = new Slice();
		// slice.setType(SliceType.WORK);
		// slice.setDescription("WorkAction");
		// tbContr.addSlice(slice);
		// }
	}
	
}
