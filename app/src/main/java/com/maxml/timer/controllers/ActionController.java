package com.maxml.timer.controllers;

import android.content.Context;
import android.content.Intent;

import com.maxml.timer.R;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.BackStackEntity;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NotificationHelper;
import com.maxml.timer.util.Utils;
import com.maxml.timer.widget.AutoTimeWidget;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActionController {

    private static EventBus serviceEventBus;
    private EventBus entityEventBus;
    private Context context;
    private DbController dbController;

    private static String walkActionId;
    private static Action currentAction;
    private static List<BackStackEntity> backStack = new ArrayList<>();

    public ActionController(Context context, EventBus entityEventBus) {
        this.context = context;
        this.entityEventBus = entityEventBus;
        dbController = new DbController(context, entityEventBus);
    }

    public static ActionController build(Context context, EventBus eventBus) {
        serviceEventBus = eventBus;
        return new ActionController(context, eventBus);
    }

    public void setMineActivityEventBus() {
        serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_SET_MAIN_ACTIVITY_EVENT_BUS, entityEventBus));
    }

    public void setCallEventBus() {
        serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_SET_CALL_EVENT_BUS, entityEventBus));
    }

    public void setWidgetEventBus() {
        serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_SET_WIDGET_EVENT_BUS, entityEventBus));
    }

    public void setWifiEventBus() {
        serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_SET_WIFI_EVENT_BUS, entityEventBus));
    }

    public void onReceiveCallEvent(Events.CallEvent event) {
        switch (event.getCallState()) {
            case Constants.EVENT_CALL_ONGOING_ANSWERED:
                startCallEvent();
                break;

            case Constants.EVENT_CALL_INCOMING_ANSWERED:
                startCallEvent();
                break;

            // simple logic for both events
            case Constants.EVENT_CALL_INCOMING_ENDED:
            case Constants.EVENT_CALL_ONGOING_ENDED:
                endCallEvent();
                break;
        }
    }

    public void onReceiveWifiEvent(Events.WifiEvent event) {
        switch (event.getWifiState()) {
            case Constants.EVENT_WIFI_ENABLE:
                startWorkEvent();
                break;
            case Constants.EVENT_WIFI_DISABLE:
                endWorkEvent();
                break;
        }
    }

    public void onReceiveWidgetEvent(Events.WidgetEvent event) {
        switch (event.getMessage()) {
            case Constants.EVENT_WALK_ACTION:
                walkActionEvent();
                break;

            case Constants.EVENT_WORK_ACTION:
                workActionEvent();
                break;

            case Constants.EVENT_REST_ACTION:
                restActionEvent();
                break;

            case Constants.EVENT_CALL_ACTION:
                callActionEvent();
                break;
        }
    }

    public void gpsStopEvent(List<Coordinates> wayCoordinates) {
        if (walkActionId != null) {
            dbController.savePath(walkActionId, wayCoordinates);
        }
        walkActionId = null;
    }

    public void walkActionSaved(String walkActionId) {
        this.walkActionId = walkActionId;
        serviceEventBus.post(new Events.GPS(Constants.EVENT_GPS_STOP));
    }

    public void registerEventBus(EventBus entityEventBus) {
        serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_REGISTER_EVENT_BUS, entityEventBus));
    }

    public void unregisterEventBus(EventBus entityEventBus) {
        serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_UNREGISTER_EVENT_BUS, entityEventBus));
    }

    public String getCurrentActionType() {
        if (currentAction == null) {
            return context.getString(R.string.text_default_action_state);
        } else {
            return currentAction.getType();
        }
    }

    public Date getCurrentActionStartTime() {
        if (currentAction == null) {
            return null;
        } else {
            return currentAction.getStartDate();
        }
    }

    public void dontMoveTimerOff() {
        serviceEventBus.post(new Events.GPS(Constants.EVENT_GPS_STOP));
        // todo event after dont move timer off
        restActionEvent();
    }

    public void restActionEvent() {
        if (currentAction != null && currentAction.getType().equals(Constants.EVENT_REST_ACTION)) {
            endRestEvent();
        } else {
            startRestEvent();
        }
    }

    public void callActionEvent() {
        if (currentAction != null && currentAction.getType().equals(Constants.EVENT_CALL_ACTION)) {
            endCallEvent();
        } else {
            startCallEvent();
        }
    }

    public void workActionEvent() {
        if (currentAction != null && currentAction.getType().equals(Constants.EVENT_WORK_ACTION)) {
            endWorkEvent();
        } else {
            startWorkEvent();
        }
    }

    public void walkActionEvent() {
        if (currentAction != null && currentAction.getType().equals(Constants.EVENT_WALK_ACTION)) {
            endWalkEvent();
        } else {
            startWalkEvent();
        }
    }

    private void startRestEvent() {
        // create action entity
        Action rest = new Action();
        rest.setType(Constants.EVENT_REST_ACTION);
        rest.setStartDate(new Date());
        rest.setDayCount(Utils.getDayCount(new Date()));
        String dayCount_type = rest.getDayCount() + "_" + rest.getType();
        rest.setDayCount_type(dayCount_type);
        // add to stack trace
        boolean isBreak = false;
        if (currentAction != null) {
            // this Action break previous Action
            isBreak = true;
        }
        backStack.add(new BackStackEntity(rest, isBreak));
        // mark as current
        currentAction = rest;
        // update status
        updateStatus(Constants.EVENT_REST_ACTION, rest.getStartDate());
    }

    private void endRestEvent() {
        Action rest = currentAction;
        rest.setEndDate(new Date());
        dbController.createAction(rest);
        // clear temp entity
        currentAction = null;
        // set previous action status
        setPreviousAction();
    }

    private void startWorkEvent() {
        // create action entity
        Action work = new Action();
        work.setType(Constants.EVENT_WORK_ACTION);
        work.setStartDate(new Date());
        work.setDayCount(Utils.getDayCount(new Date()));
        String dayCount_type = work.getDayCount() + "_" + work.getType();
        work.setDayCount_type(dayCount_type);
        // add to stack trace
        boolean isBreak = false;
        if (currentAction != null) {
            // this Action break previous Action
            isBreak = true;
        }
        backStack.add(new BackStackEntity(work, isBreak));
        // mark as current
        currentAction = work;
        // update status
        updateStatus(Constants.EVENT_WORK_ACTION, work.getStartDate());
    }

    private void endWorkEvent() {
        Action work = currentAction;
        work.setEndDate(new Date());
        dbController.createAction(work);
        // clear temp entity
        currentAction = null;
        // set previous action status
        setPreviousAction();
    }

    private void startCallEvent() {
        // create action entity
        Action call = new Action();
        call.setType(Constants.EVENT_CALL_ACTION);
        call.setStartDate(new Date());
        call.setDayCount(Utils.getDayCount(new Date()));
        String dayCount_type = call.getDayCount() + "_" + call.getType();
        call.setDayCount_type(dayCount_type);
        // add to stack trace
        boolean isBreak = false;
        if (currentAction != null) {
            // this Action break previous Action
            isBreak = true;
        }
        backStack.add(new BackStackEntity(call, isBreak));
        // mark as current
        currentAction = call;
        // update status
        updateStatus(Constants.EVENT_CALL_ACTION, call.getStartDate());
    }

    private void endCallEvent() {
        Action call = currentAction;
        call.setEndDate(new Date());
        dbController.createAction(call);
        // clear temp entity
        currentAction = null;
        // set previous action status
        setPreviousAction();
    }

    private void startWalkEvent() {
        // create action entity
        Action walk = new Action();
        walk.setType(Constants.EVENT_WALK_ACTION);
        walk.setStartDate(new Date());
        walk.setDayCount(Utils.getDayCount(new Date()));
        String dayCount_type = walk.getDayCount() + "_" + walk.getType();
        walk.setDayCount_type(dayCount_type);
        // add to stack trace
        boolean isBreak = false;
        if (currentAction != null) {
            // this Action break previous Action
            isBreak = true;
        }
        backStack.add(new BackStackEntity(walk, isBreak));
        // mark as current
        currentAction = walk;
        // update status
        updateStatus(Constants.EVENT_WALK_ACTION, walk.getStartDate());
        // start gps tracking
        serviceEventBus.post(new Events.GPS(Constants.EVENT_GPS_START));
    }

    private void endWalkEvent() {
        Action walk = currentAction;
        walk.setEndDate(new Date());
        dbController.createWalkAction(walk);
        // clear temp entity
        currentAction = null;
        // set previous action status
        setPreviousAction();
    }

    private void setPreviousAction() {
        if (backStack.size() > 1) {
            // if last Action break previous continue previous action
            BackStackEntity entity = backStack.get(backStack.size()-1);
            if (entity.isBreak()){
                continuePreviousAction();
                return;
            }
        }
        updateStatus(context.getString(R.string.text_default_action_state), null);
    }

    private void continuePreviousAction() {
        String type = backStack.get(backStack.size()-2).getType();
        switch (type){
            case Constants.EVENT_REST_ACTION:
                startRestEvent();
                break;
            case Constants.EVENT_WALK_ACTION:
                startWalkEvent();
                break;
            case Constants.EVENT_WORK_ACTION:
                startWorkEvent();
                break;
            case Constants.EVENT_CALL_ACTION:
                startCallEvent();
                break;
        }
    }

    private void updateStatus(String status, Date time) {
        Events.ActionStatus statusEvent = new Events.ActionStatus(status, time);
        entityEventBus.post(statusEvent);
        updateWidgetStatus(status);
        NotificationHelper.updateNotification(context, status);
    }

    private void updateWidgetStatus(String status) {
        Intent intent = new Intent(context, AutoTimeWidget.class);
        intent.setAction(Constants.WIDGET_UPDATE_ACTION_STATUS);
        intent.putExtra(Constants.WIDGET_EXTRA, status);
        context.sendBroadcast(intent);
    }

    public void closeApp() {
        serviceEventBus.post(new Events.Info(Constants.EVENT_CLOSE_APP));
    }
}
