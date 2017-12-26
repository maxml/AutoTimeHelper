package com.maxml.timer.api;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Path;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoordinateCRUD extends BaseDaoImpl<Coordinates, Integer> {

    public CoordinateCRUD(ConnectionSource connectionSource, Class<Coordinates> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
