package com.maxml.timer.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.maxml.timer.util.Constants;

@DatabaseTable(tableName = Constants.TABLE_WIFI_STATE)
public class WifiState {
    @DatabaseField(dataType = DataType.STRING, id = true)
    private String id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name;

    @DatabaseField(dataType = DataType.INTEGER)
    private int type;

    public WifiState() {
    }

    public WifiState(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
