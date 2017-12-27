package com.maxml.timer.database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.maxml.timer.entity.WifiState;

import java.sql.SQLException;
import java.util.List;

public class WifiStateDAO extends BaseDaoImpl<WifiState, Integer> {

    public WifiStateDAO(ConnectionSource connectionSource, Class<WifiState> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<WifiState> getAllRoles() throws SQLException {
        return this.queryForAll();
    }

    public List<WifiState> getWifiStateById(int id) throws SQLException {
        QueryBuilder<WifiState, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("id", id);
        PreparedQuery<WifiState> preparedQuery = queryBuilder.prepare();
        List<WifiState> list = query(preparedQuery);
        return list;
    }
}
