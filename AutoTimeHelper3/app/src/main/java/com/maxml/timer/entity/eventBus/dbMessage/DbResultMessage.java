package com.maxml.timer.entity.eventBus.dbMessage;

/**
 * Created by nazar on 17.09.17.
 */

public class DbResultMessage{

    private String message;
    private DbMessage messageForResult;

    public DbResultMessage(String message) {
        this.message = message;
    }

    public DbResultMessage(String message, DbMessage messageForResult) {
        this.message = message;
        this.messageForResult = messageForResult;
    }

    public String getMessage() {
        return message;
    }

    public DbMessage getMessageForResult() {
        return messageForResult;
    }
}