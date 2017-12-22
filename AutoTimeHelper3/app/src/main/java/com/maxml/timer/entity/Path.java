package com.maxml.timer.entity;

import java.util.List;

/**
 * Created by nazar on 22.12.17.
 */

public class Path {
    private String walkActionId;
    private List<Coordinates> coordinates;

    public Path(String walkActionId, List<Coordinates> coordinates) {
        this.walkActionId = walkActionId;
        this.coordinates = coordinates;
    }

    public String getWalkActionId() {
        return walkActionId;
    }

    public void setWalkActionId(String walkActionId) {
        this.walkActionId = walkActionId;
    }

    public List<Coordinates> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }
}
