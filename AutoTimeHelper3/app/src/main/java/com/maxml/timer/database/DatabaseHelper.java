package com.maxml.timer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.maxml.timer.MyLog;
import com.maxml.timer.entity.WifiState;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "auto_time_helper.db";
    private static final int DATABASE_VERSION = 12;

    private WifiStateDao wifiStateDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, WifiState.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, WifiState.class, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }

    public WifiStateDao getWifiStateDao() throws SQLException {
        if (wifiStateDao == null) {
            wifiStateDao = new WifiStateDao(getConnectionSource(), WifiState.class);
        }
        return wifiStateDao;
    }

    @Override
    public void close() {
        super.close();
        wifiStateDao = null;
    }
}
