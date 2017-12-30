package com.maxml.timer.database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.maxml.timer.entity.WifiState;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class WifiStateDAO extends BaseDaoImpl<WifiState, String> {

    public WifiStateDAO(ConnectionSource connectionSource, Class<WifiState> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<WifiState> getAllRoles() {
        try {
            return this.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Nullable
    public WifiState getWifiStatesById(String id) {
        try {
            return queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(WifiState wifiState) {
        try {
            create(wifiState);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
