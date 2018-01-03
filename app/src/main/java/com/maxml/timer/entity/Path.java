package com.maxml.timer.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.maxml.timer.database.PathDAO;
import com.maxml.timer.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@DatabaseTable(tableName = Constants.TABLE_PATH, daoClass = PathDAO.class)
public class Path {
    @DatabaseField(dataType = DataType.STRING, id = true)
    private String walkActionId;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<Coordinates> coordinates;

    public Path() {
    }

    public Path(String walkActionId) {
        this.walkActionId = walkActionId;
    }

    public ArrayList<Coordinates> getCoordinates() {
        ArrayList<Coordinates> result = new ArrayList<>();
        result.addAll(coordinates);
        return result;
    }

    public String getWalkActionId() {
        return walkActionId;
    }

    public void setWalkActionId(String walkActionId) {
        this.walkActionId = walkActionId;
    }

}
