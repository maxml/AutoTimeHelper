package com.maxml.timer.api;


public class NetworkAvailiabilityHelper {

	private static boolean networkDataBaseAvailable = true;
	
	
//	public void init(Context context){
//		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);        
//		context.registerReceiver(networkStateReceiver, filter);
//	}
	
	public static boolean getIsNetworkAvailable() {
		return networkDataBaseAvailable;
	}

	public static void setIsNetworkAvailable(boolean s) {
		networkDataBaseAvailable = s;
	}
	
//	private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
//	    @Override
//	    public void onReceive(Context context, Intent intent) {
//	        Log.w("Network Listener", "Network Type Changed");
//	        
//	        
//	    }
//	};
}
