package com.maxml.timer;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;
import com.maxml.timer.database.DBFactory;
import com.maxml.timer.util.NetworkUtil;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        DBFactory.setHelper(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    public void onTerminate() {
        DBFactory.releaseHelper();
        super.onTerminate();
    }
}