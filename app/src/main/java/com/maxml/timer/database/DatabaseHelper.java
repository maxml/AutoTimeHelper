package com.maxml.timer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.maxml.timer.MyLog;
import com.maxml.timer.api.CoordinateCRUD;
import com.maxml.timer.api.PathCRUD;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Path;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.util.Constants;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "auto_time_helper.db";
    private static final int DATABASE_VERSION = 12;

    private WifiStateDao wifiStateDao;
    private PathCRUD pathCRUD;
    private CoordinateCRUD coordinateCRUD;

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

    public WifiStateDao getWifiStateDao() throws SQLException {
        if (wifiStateDao == null) {
            wifiStateDao = new WifiStateDao(getConnectionSource(), WifiState.class);
        }
        return wifiStateDao;
    }

    public PathCRUD getPathCRUD() throws SQLException {
        if (pathCRUD == null) {
            pathCRUD = new PathCRUD(getConnectionSource(), Path.class);
        }
        return pathCRUD;
    }

    public CoordinateCRUD getCoordinateCRUD() throws SQLException {
        if (coordinateCRUD == null) {
            coordinateCRUD = new CoordinateCRUD(getConnectionSource(), Coordinates.class);
        }
        return coordinateCRUD;
    }

    @Override
    public void close() {
        super.close();
        wifiStateDao = null;
        pathCRUD = null;
        coordinateCRUD = null;
    }
}
