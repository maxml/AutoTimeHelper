package com.maxml.timer.entity;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.maxml.timer.util.Constants;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class WalkEvent extends BaseEvent{
    private Line path;

    public WalkEvent(Date startDate, Date endDate, String description, Line path) {
        super();
        this.path = path;
        setStartDate(startDate);
        setEndDate(endDate);
        setDescription(description);
    }

    public WalkEvent() {

    }

    public Line getPath() {
        return path;
    }

    public void setPath(Line path) {
        this.path = path;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.COLUMN_ID, getId());
        result.put(Constants.COLUMN_START_DATE, getStartDate());
        result.put(Constants.COLUMN_END_DATE, getEndDate());
        result.put(Constants.COLUMN_DESCRIPTION, getDescription());
        result.put(Constants.COLUMN_IS_DELETED, isDeleted());
        result.put(Constants.COLUMN_PATH, path);
        return result;
    }
}
