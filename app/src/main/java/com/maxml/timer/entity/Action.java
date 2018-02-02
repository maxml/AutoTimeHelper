package com.maxml.timer.entity;

import android.support.annotation.NonNull;

import java.util.Date;

public class Action implements Comparable<Action> {
    private String id;
    private String type;
    private long dayCount;
    private String dayCount_type;
    private Date startDate;
    private Date endDate;
    private String description;
    private boolean deleted = false;

    public Action() {
    }

    public Action(String id, String type, long dayCount, String dayCount_type, Date startDate, Date endDate, String description, boolean deleted) {
        this.id = id;
        this.type = type;
        this.dayCount = dayCount;
        this.dayCount_type = dayCount_type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.deleted = deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDayCount() {
        return dayCount;
    }

    public void setDayCount(long dayCount) {
        this.dayCount = dayCount;

    }

    public String getDayCount_type() {
        return dayCount_type;
    }

    public void setDayCount_type(String dayCount_type) {
        this.dayCount_type = dayCount_type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int compareTo(@NonNull Action a) {
        if (a.getStartDate().before(startDate)) {
            return 1;
        } else if (a.getStartDate().after(startDate)) {
            return -1;
        } else {
            return 0;
        }
    }
}
