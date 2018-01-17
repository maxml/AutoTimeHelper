package com.maxml.timer;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.maxml.timer.controllers.ReceiverService;
import com.maxml.timer.database.DBFactory;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;

public class App extends MultiDexApplication {

//    private ActionController controller;

    @Override
    public void onCreate() {
        super.onCreate();
        DBFactory.setHelper(this);

//		WifiReceiver networkReceiver = new WifiReceiver();
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