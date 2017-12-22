package com.maxml.timer.database;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by morozione on 21.12.17.
 */

public class DBFactory {
    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getHelper() {
        return  databaseHelper;
    }

    public static void setHelper(Context context) {
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static void releaseHelper() {
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }
}
