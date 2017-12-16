package com.maxml.timer.entity;

import com.maxml.timer.util.EventType;

/**
 * Created by nazar on 16.12.17.
 */

public class DbReturnData {
    private String resultRequest;
    private EventType eventType;

    public DbReturnData(String resultRequest, EventType eventType) {
        this.resultRequest = resultRequest;
        this.eventType = eventType;
    }

    public String getResultRequest() {
        return resultRequest;
    }

    public EventType getEventType() {
        return eventType;
    }
}
