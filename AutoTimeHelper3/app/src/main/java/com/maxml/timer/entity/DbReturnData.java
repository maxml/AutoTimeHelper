package com.maxml.timer.entity;

import com.maxml.timer.util.EventBusType;

/**
 * Created by nazar on 16.12.17.
 */

public class DbReturnData {
    private String resultRequest;
    private EventBusType eventBusType;

    public DbReturnData(String resultRequest, EventBusType eventBusType) {
        this.resultRequest = resultRequest;
        this.eventBusType = eventBusType;
    }

    public String getResultRequest() {
        return resultRequest;
    }

    public EventBusType getEventBusType() {
        return eventBusType;
    }
}
