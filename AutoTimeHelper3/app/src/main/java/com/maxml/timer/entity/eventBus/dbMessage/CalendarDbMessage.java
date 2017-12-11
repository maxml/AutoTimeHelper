package com.maxml.timer.entity.eventBus.dbMessage;

import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by nazar on 04.12.17.
 */

public class CalendarDbMessage extends DbMessage{
    public CalendarDbMessage(String message) {
        super(message);
    }

    public CalendarDbMessage(String message, Object data) {
        super(message, data);
    }

    public CalendarDbMessage(String message, List<Object> listData) {
        super(message, listData);
    }

    @Override
    public void sendMessage() {
        EventBus.getDefault().post(this);
    }

    @Override
    public void sendErrorMessage() {
        setMessage(Constants.EVENT_DB_RESULT_ERROR);
        EventBus.getDefault().post(this);
    }
}


