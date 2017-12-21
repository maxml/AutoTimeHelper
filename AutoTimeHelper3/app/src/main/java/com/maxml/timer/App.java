package com.maxml.timer;

import android.support.multidex.MultiDexApplication;

import com.maxml.timer.controllers.Controller;
import com.maxml.timer.database.HandlerFactory;

public class App extends MultiDexApplication {

//    private Controller controller;

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerFactory.setHelper(this);

//        controller = Controller.build();

//        Parse.enableLocalDatastore(this);
//
//        Parse.initialize(this, PSFKeys.PARSE_APPLICATION_ID, PSFKeys.PARSE_CLIENT_KEY);
//
//        ParseInstallation.getCurrentInstallation().saveInBackground();

//		NetworkReceiver networkReceiver = new NetworkReceiver();
//		networkReceiver.onReceive(this, new Intent());

//		NetworkStatus.isNetworkAvailable(this);

        //	GeneralService table = new GeneralService();

        //	table.getListSlice();
    }

    @Override
    public void onTerminate() {
        HandlerFactory.releaseHelper();
        super.onTerminate();
    }
}