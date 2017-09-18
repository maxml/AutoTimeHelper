package com.maxml.timer.entity.eventBus;

import java.util.List;

/**
 * Created by nazar on 17.09.17.
 */

public class CallMessage {
    private String message;
    private String phoneNumber;

    public CallMessage(String message){
        this.message = message;
    }

    public CallMessage(String message, String phoneNumber){
        this.message= message;
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }}
