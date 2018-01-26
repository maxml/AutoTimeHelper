package com.maxml.timer.entity;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;
import java.util.Random;

public class ActionWeek extends WeekViewEvent{
    private String id;
    private String type;

    public ActionWeek(String id, String name, String type, Calendar startTime, Calendar endTime) {
        super(new Random().nextInt(1000), name, startTime, endTime);
        this.type = type;
        this.id = id;
    }

    public String getActionId() {
        return this.id;
    }

    public void setActionId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
