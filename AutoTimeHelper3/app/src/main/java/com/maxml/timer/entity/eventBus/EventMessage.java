package com.maxml.timer.entity.eventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazar on 17.09.17.
 */

public class EventMessage {
    private String message;
    private Object data;
    private ArrayList<Object> listData;

    public EventMessage(String message){
        this.message = message;
    }

    public EventMessage(String message, Object data){
        this.message= message;
        this.data= data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }}
