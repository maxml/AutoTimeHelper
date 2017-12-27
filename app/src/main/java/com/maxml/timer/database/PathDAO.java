package com.maxml.timer.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.Path;
import com.maxml.timer.entity.User;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.util.Constants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PathDAO extends BaseDaoImpl<Path, String> {

    public PathDAO(ConnectionSource connectionSource, Class<Path> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public void save(Path path) throws SQLException {
        create(path);
    }

    public Path read(String idPath) throws SQLException {
        return queryForId(idPath);
    }

    public List<Path> read(List<String> listIdPath) throws SQLException {
        List<Path> result = new ArrayList<>();
        for (String id : listIdPath) {
            Path path = queryForId(id);
            if (path != null) {
                result.add(path);
            }
        }
        return result;
    }
}
