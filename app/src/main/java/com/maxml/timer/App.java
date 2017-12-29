package com.maxml.timer;

import android.support.multidex.MultiDexApplication;

import com.maxml.timer.database.DBFactory;

public class App extends MultiDexApplication {

//    private ActionController controller;

    @Override
    public void onCreate() {
        super.onCreate();
        DBFactory.setHelper(this);

//        controller = ActionController.build();

//        Parse.enableLocalDatastore(this);
//
//        Parse.initialize(this, PSFKeys.PARSE_APPLICATION_ID, PSFKeys.PARSE_CLIENT_KEY);
//
//        ParseInstallation.getCurrentInstallation().saveInBackground();

//		NetworkReceiver networkReceiver = new NetworkReceiver();
//		networkReceiver.onReceive(this, new Intent());

//		NetworkStatus.isNetworkAvailable(this);

        //	ReceiverService table = new ReceiverService();

        //	table.getListSlice();
    }

    @Override
    public void onTerminate() {
        DBFactory.releaseHelper();
        super.onTerminate();
    }
}