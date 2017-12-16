package com.maxml.timer.entity.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.util.NetworkStatus;

public class NetworkReceiver extends BroadcastReceiver {
	private static final String LOG_TAG = "NetworkReceiver";
	private TableController tbContr;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(LOG_TAG, "Receieved notification about network status");
		NetworkStatus netStat = new NetworkStatus();
		netStat.isNetworkOnline(context);
		// if (netStat.isConnected) {
		// Slice slice = new Slice();
		// slice.setType(SliceType.WORK);
		// slice.setDescription("Work");
		// tbContr.addSlise(slice);
		// }
	}
	
}
