package com.maxml.timer;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, PSFKeys.PARSE_APPLICATION_ID, PSFKeys.PARSE_CLIENT_KEY);

        ParseInstallation.getCurrentInstallation().saveInBackground();

//		NetworkReceiver networkReceiver = new NetworkReceiver();
//		networkReceiver.onReceive(this, new Intent());

//		NetworkStatus.isNetworkAvailable(this);

        //	TableController table = new TableController();

        //	table.getListSlice();
    }
}