package com.maxml.timer.database;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Path;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoordinateDAO extends BaseDaoImpl<Coordinates, Integer> {

    public CoordinateDAO(ConnectionSource connectionSource, Class<Coordinates> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
