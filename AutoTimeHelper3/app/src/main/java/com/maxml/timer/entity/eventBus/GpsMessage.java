package com.maxml.timer.entity.eventBus;

import java.util.List;

/**
 * Created by nazar on 17.09.17.
 */

public class GpsMessage {
    private String message;
    private Object data;
    private List<Object> listData;

    public GpsMessage(String message){
        this.message = message;
    }

    public GpsMessage(String message, Object data){
        this.message= message;
        this.data= data;
    }

    public GpsMessage(String message, List<Object> listData){
        this.message= message;
        this.listData= listData;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }}
