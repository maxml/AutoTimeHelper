package com.maxml.timer.entity.eventBus;

/**
 * Created by nazar on 17.09.17.
 */

public class ActionMessage {
    private String message;
    private String event;

    public ActionMessage(String message){
        this.message = message;
    }

    public ActionMessage(String message, String event){
        this.message= message;
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public String getEvent() {
        return event;
    }}
