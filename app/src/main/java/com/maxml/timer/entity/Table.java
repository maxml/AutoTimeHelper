package com.maxml.timer.entity;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private long day;
    private List<Action> callList;
    private List<Action> workList;
    private List<Action> walkList;
    private List<Action> restList;

    public Table() {
        callList = new ArrayList<>();
        walkList = new ArrayList<>();
        workList = new ArrayList<>();
        restList = new ArrayList<>();
    }

    public void addCall(Action call){
        callList.add(call);
    }
    public void addWork(Action work){
        workList.add(work);
    }
    public void addRest(Action rest){
        restList.add(rest);
    }
    public void addWalk(Action walk){
        walkList.add(walk);
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public List<Action> getCallList() {
        return callList;
    }

    public void setCallList(List<Action> callList) {
        this.callList = callList;
    }

    public List<Action> getWorkList() {
        return workList;
    }

    public void setWorkList(List<Action> workList) {
        this.workList = workList;
    }

    public List<Action> getWalkList() {
        return walkList;
    }

    public void setWalkList(List<Action> walkList) {
        this.walkList = walkList;
    }

    public List<Action> getRestList() {
        return restList;
    }

    public void setRestList(List<Action> restList) {
        this.restList = restList;
    }
}
