package com.maxml.timer.entity.actions;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.maxml.timer.util.Constants;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class RestAction extends BaseAction {


    public RestAction(Date startDate, Date endDate, String description) {
        super();
        setStartDate(startDate);
        setEndDate(endDate);
        setDescription(description);
    }

    public RestAction() {

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.COLUMN_ID, getId());
        result.put(Constants.COLUMN_START_DATE, getStartDate());
        result.put(Constants.COLUMN_END_DATE, getEndDate());
        result.put(Constants.COLUMN_DESCRIPTION, getDescription());
        result.put(Constants.COLUMN_IS_DELETED, isDeleted());
        return result;
    }
}


