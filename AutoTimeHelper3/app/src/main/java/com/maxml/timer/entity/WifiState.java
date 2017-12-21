package com.maxml.timer.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "wifiStates")
public class WifiState {
    @DatabaseField(dataType = DataType.INTEGER)
    private int id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name;

    public WifiState() {
        id = 0;
        name = "";
    }

    public WifiState(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
