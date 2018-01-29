package com.maxml.timer.database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.maxml.timer.entity.Coordinates;

import java.sql.SQLException;
import java.util.List;

public class CoordinateDAO extends BaseDaoImpl<Coordinates, Integer> {

    public CoordinateDAO(ConnectionSource connectionSource, Class<Coordinates> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public void save(List<Coordinates> data) {
        try {
            create(data);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
