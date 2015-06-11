package com.maxml.timer;

import android.app.Application;
import android.content.Intent;

import com.maxml.timer.controllers.TableController;
import com.maxml.timer.receivers.NetworkReceiver;
import com.maxml.timer.util.NetworkStatus;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class App extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Parse.enableLocalDatastore(this);
		
		Parse.initialize(this, "v7xyeLy701EXeS70aggBiMDzRIlTH0q1tiYlZqFV",
				"oxzucbjTHAKP20t9o4UjBCRBoKN0gk4jL4Mm9UjK");
		
		ParseInstallation.getCurrentInstallation().saveInBackground();

//		NetworkReceiver networkReceiver = new NetworkReceiver();
//		networkReceiver.onReceive(this, new Intent());
//		
//		NetworkStatus.isNetworkAvailable(this);
		
	//	TableController table = new TableController();
		
	//	table.getListSlice(ParseUser.getCurrentUser().getObjectId());

	}
	
}
