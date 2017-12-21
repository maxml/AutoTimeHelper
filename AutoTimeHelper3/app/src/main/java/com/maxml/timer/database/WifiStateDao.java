package com.maxml.timer.database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.maxml.timer.entity.WifiState;

import java.sql.SQLException;
import java.util.List;

public class WifiStateDao extends BaseDaoImpl<WifiState, Integer> {

    public WifiStateDao(ConnectionSource connectionSource, Class<WifiState> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<WifiState> getAllRoles() throws SQLException {
        return this.queryForAll();
    }
}
