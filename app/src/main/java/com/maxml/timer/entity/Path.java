package com.maxml.timer.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.maxml.timer.database.PathDAO;
import com.maxml.timer.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by nazar on 22.12.17.
 */


@DatabaseTable(tableName = Constants.TABLE_PATH, daoClass = PathDAO.class)
public class Path {
    @DatabaseField(dataType = DataType.STRING, id = true)
    private String walkActionId;
    @ForeignCollectionField(eager = true)
    private Collection<Coordinates> coordinates = new ArrayList<>();

    public Path() {
    }

    public Path(String walkActionId, List<Coordinates> coordinates) {
        this.walkActionId = walkActionId;
        this.coordinates = coordinates;
    }

    public String getWalkActionId() {
        return walkActionId;
    }

    public void setWalkActionId(String walkActionId) {
        this.walkActionId = walkActionId;
    }

    public List<Coordinates> getCoordinates() {
        return (List<Coordinates>) coordinates;
    }

    public void setCoordinates(List<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }
}
