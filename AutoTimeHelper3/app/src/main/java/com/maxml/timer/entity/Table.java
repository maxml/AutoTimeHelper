package com.maxml.timer.entity;

import com.maxml.timer.entity.actions.CallAction;
import com.maxml.timer.entity.actions.RestAction;
import com.maxml.timer.entity.actions.WalkAction;
import com.maxml.timer.entity.actions.WorkAction;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private long day;
    private List<CallAction> callList;
    private List<WorkAction> workList;
    private List<WalkAction> walkList;
    private List<RestAction> restList;

    public Table() {
        callList = new ArrayList<>();
        walkList = new ArrayList<>();
        workList = new ArrayList<>();
        restList = new ArrayList<>();
    }

    public Table(long day, List<CallAction> callList,
                 List<WorkAction> workList, List<WalkAction> walkList, List<RestAction> restList) {
        this.day = day;
        this.callList = callList;
        this.workList = workList;
        this.walkList = walkList;
        this.restList = restList;
    }

    public void addCall(CallAction call){
        callList.add(call);
    }
    public void addWork(WorkAction work){
        workList.add(work);
    }
    public void addRest(RestAction rest){
        restList.add(rest);
    }
    public void addWalk(WalkAction walk){
        walkList.add(walk);
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public List<CallAction> getCallList() {
        return callList;
    }

    public void setCallList(List<CallAction> callList) {
        this.callList = callList;
    }

    public List<WorkAction> getWorkList() {
        return workList;
    }

    public void setWorkList(List<WorkAction> workList) {
        this.workList = workList;
    }

    public List<WalkAction> getWalkList() {
        return walkList;
    }

    public void setWalkList(List<WalkAction> walkList) {
        this.walkList = walkList;
    }

    public List<RestAction> getRestList() {
        return restList;
    }

    public void setRestList(List<RestAction> restList) {
        this.restList = restList;
    }
}
