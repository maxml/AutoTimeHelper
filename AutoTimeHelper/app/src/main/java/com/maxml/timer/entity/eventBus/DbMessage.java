package com.maxml.timer.entity.eventBus;

import java.util.List;

/**
 * Created by nazar on 17.09.17.
 */

public class DbMessage {
    private String message;
    private Object data;
    private List<Object> listData;

    public  DbMessage(String message){
        this.message = message;
    }

    public  DbMessage(String message, Object data){
        this.message= message;
        this.data= data;
    }

    public  DbMessage(String message, List<Object> listData){
        this.message= message;
        this.listData= listData;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }}
