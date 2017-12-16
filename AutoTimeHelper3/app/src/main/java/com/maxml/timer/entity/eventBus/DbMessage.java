package com.maxml.timer.entity.eventBus;

import com.maxml.timer.entity.DbReturnData;
import com.maxml.timer.util.EventType;

/**
 * Created by nazar on 04.12.17.
 */

public class DbMessage {
    private String message;
    private DbReturnData dbReturnData;
    private Object data;

    public DbMessage(String message, EventType resultEventType) {
        this.message = message;
        this.dbReturnData = new DbReturnData(message, resultEventType);
    }

    public DbMessage(String message, EventType resultEventType, Object data) {
        this.message = message;
        this.dbReturnData = new DbReturnData(message, resultEventType);
        this.data = data;
    }
    public DbMessage(String message, DbReturnData dbReturnData) {
        this.message = message;
        this.dbReturnData = dbReturnData;
    }

    public DbMessage(String message, DbReturnData dbReturnData, Object data) {
        this.message = message;
        this.dbReturnData = dbReturnData;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DbReturnData getDbReturnData() {
        return dbReturnData;
    }

    public void setDbReturnData(DbReturnData dbReturnData) {
        this.dbReturnData = dbReturnData;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

