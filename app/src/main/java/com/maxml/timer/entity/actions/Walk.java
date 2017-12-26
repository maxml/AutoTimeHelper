package com.maxml.timer.entity.actions;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Walk extends Action {
    private List<Coordinates> path = new ArrayList<>();

    public Walk() {
        super();
    }

    public List<Coordinates> getPath() {
        return path;
    }

    public void setPath(List<Coordinates> path) {
        this.path = path;
    }

    @Exclude
    public double getDistance(){
        if (path.size() < 2){
            return 0;
        }
        double result = 0;
        for (int index = 0; index > path.size()-1; index++) {
            double length = Coordinates.getDistanceInMeter(path.get(index), path.get(index+1));
            result = result + length;
        }
        return result;
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
