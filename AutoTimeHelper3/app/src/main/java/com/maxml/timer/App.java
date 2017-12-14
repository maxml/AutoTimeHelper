package com.maxml.timer;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.maxml.timer.controllers.ActionController;

public class App extends MultiDexApplication {

    private ActionController controller;

    @Override
    public void onCreate() {
        super.onCreate();

        controller = ActionController.build(this);

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
}