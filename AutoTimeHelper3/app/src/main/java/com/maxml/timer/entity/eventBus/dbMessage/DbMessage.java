package com.maxml.timer.entity.eventBus.dbMessage;

import java.util.List;

/**
 * Created by nazar on 04.12.17.
 */

public abstract class DbMessage {
    private String message;
    private Object data;

    public DbMessage(String message) {
        this.message = message;
    }

    public DbMessage(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public abstract void  sendMessage();

    public abstract void sendErrorMessage();
}

