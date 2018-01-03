package com.maxml.timer.database;

import android.support.annotation.Nullable;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.maxml.timer.entity.Path;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PathDAO extends BaseDaoImpl<Path, String> {

    public PathDAO(ConnectionSource connectionSource, Class<Path> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public void save(Path path) {
        try {
            create(path);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public Path getPathById(String idPath) {
        try {
            return queryForId(idPath);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Path();
        }
    }

    public List<Path> getPathById(List<String> listIdPath) {
        try {
            List<Path> result = new ArrayList<>();

            for (String id : listIdPath) {
                Path path = queryForId(id);
                if (path != null) {
                    result.add(path);
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
