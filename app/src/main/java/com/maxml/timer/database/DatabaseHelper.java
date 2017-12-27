package com.maxml.timer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Path;
import com.maxml.timer.entity.WifiState;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "auto_time_helper.db";
    private static final int DATABASE_VERSION = 7;

    private WifiStateDAO wifiStateDAO;
    private PathDAO pathDAO;
    private CoordinateDAO coordinateDAO;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, WifiState.class);
            TableUtils.createTable(connectionSource, Path.class);
            TableUtils.createTable(connectionSource, Coordinates.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, WifiState.class, true);
            TableUtils.dropTable(connectionSource, Path.class, true);
            TableUtils.dropTable(connectionSource, Coordinates.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public WifiStateDAO getWifiStateDAO() {
        if (wifiStateDAO == null) {
            try {
                wifiStateDAO = new WifiStateDAO(getConnectionSource(), WifiState.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return wifiStateDAO;
    }

    public PathDAO getPathDAO() {
        if (pathDAO == null) {
            try {
                pathDAO = new PathDAO(getConnectionSource(), Path.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return pathDAO;
    }

    public CoordinateDAO getCoordinateDAO() {
        if (coordinateDAO == null) {
            try {
                coordinateDAO = new CoordinateDAO(getConnectionSource(), Coordinates.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return coordinateDAO;
    }

    @Override
    public void close() {
        super.close();
        wifiStateDAO = null;
        pathDAO = null;
        coordinateDAO = null;
    }
}
