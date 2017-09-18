package com.maxml.timer.entity.eventBus;

import java.util.List;

/**
 * Created by nazar on 17.09.17.
 */

public class UiMessage {
    private String message;
    private Object data;
    private List<Object> listData;

    public UiMessage(String message){
        this.message = message;
    }

    public UiMessage(String message, Object data){
        this.message= message;
        this.data= data;
    }

    public UiMessage(String message, List<Object> listData){
        this.message= message;
        this.listData= listData;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }}
