package com.maxml.timer.controllers;

import android.content.Context;
import android.content.Intent;

import com.maxml.timer.R;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Path;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.Events;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NotificationHelper;
import com.maxml.timer.util.Utils;
import com.maxml.timer.widget.AutoTimeWidget;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionController {

    private static EventBus serviceEventBus;
    private EventBus entityEventBus;
    private Context context;
    private DbController dbController;

    private String walkActionId;
    private static Map<String, Action> actions = new HashMap<>();
    private List<String> stateStack = new ArrayList<>();

    public ActionController(Context context, EventBus entityEventBus) {
        this.context = context;
        this.entityEventBus = entityEventBus;
        dbController = new DbController(context, entityEventBus);
    }

    public static ActionController build(Context context, EventBus eventBus) {
        serviceEventBus = eventBus;
        return new ActionController(context, eventBus);
    }

    public void onReceiveCallEvent(Events.CallEvent event) {
        switch (event.getCallState()) {
            case Constants.EVENT_SET_CALL_EVENT_BUS:
                serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_SET_CALL_EVENT_BUS, entityEventBus));
                break;

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
            case Constants.EVENT_SET_WIFI_EVENT_BUS:
                serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_SET_WIFI_EVENT_BUS, entityEventBus));
                break;
            case Constants.EVENT_WIFI_ENABLE:
                startWifiEvent();
                break;
            case Constants.EVENT_WIFI_DISABLE:
                endWifiEvent();
                break;
        }
    }

    public void onReceiveWidgetEvent(Events.WidgetEvent event) {
        switch (event.getMessage()) {
            case Constants.EVENT_SET_WIDGET_EVENT_BUS:
                serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_SET_WIDGET_EVENT_BUS, entityEventBus));
                break;

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

    public String getActionStatus() {
        if (stateStack.size() == 0) {
            return context.getString(R.string.widget_default_text);
        } else {
            return stateStack.get(stateStack.size() - 1);
        }
    }

    public void dontMoveTimerOff() {
        serviceEventBus.post(new Events.GPS(Constants.EVENT_GPS_STOP));
        // todo event after dont move timer off
        restActionEvent();
    }


    public Date getActionTime() {
        if (stateStack.size() == 0) {
            return null;
        }
        String status = getActionStatus();
        Action action = actions.get(status);
        if (action == null) {
            return null;
        } else {
            return action.getStartDate();
        }
    }


    public void restActionEvent() {
        Action rest = actions.get(Constants.EVENT_REST_ACTION);
        if (rest == null) {
            startRestEvent();
        } else {
            endRestEvent();
        }
    }

    public void callActionEvent() {
        Action call = actions.get(Constants.EVENT_CALL_ACTION);
        if (call == null) {
            startCallEvent();
        } else {
            endCallEvent();
        }
    }


    public void workActionEvent() {
        Action work = actions.get(Constants.EVENT_WORK_ACTION);
        if (work == null) {
            startWorkEvent();
        } else {
            endWorkEvent();
        }
    }

    public void walkActionEvent() {
        Action walk = actions.get(Constants.EVENT_WALK_ACTION);
        if (walk == null) {
            // start gps tracking
            serviceEventBus.post(new Events.GPS(Constants.EVENT_GPS_START));
            // create walk action
            startWalkEvent();
        } else {
            // save walt action
            endWalkEvent();
        }
    }


    private void startWifiEvent() {
        Action work = new Action();
        work.setType(Constants.EVENT_WORK_ACTION);
        work.setStartDate(new Date(System.currentTimeMillis()));
        work.setDayCount(Utils.getDayCount(new Date()));
        String dayCountType = work.getDayCount() + "_" + work.getType();
        work.setDayCount_type(dayCountType);
        // add it to temp map
        actions.put(Constants.EVENT_WORK_ACTION, work);
        // add to stack trace
        stateStack.add(Constants.EVENT_WORK_ACTION);
        // update status
        updateStatus(Constants.EVENT_WORK_ACTION, work.getStartDate());
    }

    private void endWifiEvent() {
        Action work = actions.get(Constants.EVENT_WORK_ACTION);
        if (work == null) {
            startWifiEvent();
            return;
        }
        work.setEndDate(new Date(System.currentTimeMillis()));
        dbController.createAction(work);
        // clear temp entity
        actions.remove(Constants.EVENT_WORK_ACTION);
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_WORK_ACTION);
        // set previous action status
        updateStatus(getActionStatus(), getActionTime());
    }

    private void startRestEvent() {
        // create action entity
        Action rest = new Action();
        rest.setType(Constants.EVENT_REST_ACTION);
        rest.setStartDate(new Date());
        rest.setDayCount(Utils.getDayCount(new Date()));
        String dayCount_type = rest.getDayCount() + "_" + rest.getType();
        rest.setDayCount_type(dayCount_type);
        // add it to temp map
        actions.put(Constants.EVENT_REST_ACTION, rest);
        // add to stack trace
        stateStack.add(Constants.EVENT_REST_ACTION);
        // update status
        updateStatus(Constants.EVENT_REST_ACTION, rest.getStartDate());
    }

    private void endRestEvent() {
        Action rest = actions.get(Constants.EVENT_REST_ACTION);
        if (rest == null) {
            startRestEvent();
            return;
        }
        rest.setEndDate(new Date());
        dbController.createAction(rest);
        // clear temp entity
        actions.remove(Constants.EVENT_REST_ACTION);
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_REST_ACTION);
        // set previous action status
        updateStatus(getActionStatus(), getActionTime());
    }

    private void startWorkEvent() {
        // create action entity
        Action work = new Action();
        work.setType(Constants.EVENT_WORK_ACTION);
        work.setStartDate(new Date());
        work.setDayCount(Utils.getDayCount(new Date()));
        String dayCount_type = work.getDayCount() + "_" + work.getType();
        work.setDayCount_type(dayCount_type);
        // add it to temp map
        actions.put(Constants.EVENT_WORK_ACTION, work);
        // add to stack trace
        stateStack.add(Constants.EVENT_WORK_ACTION);
        // update status
        updateStatus(Constants.EVENT_WORK_ACTION, work.getStartDate());
    }

    private void endWorkEvent() {
        Action work = actions.get(Constants.EVENT_WORK_ACTION);
        if (work == null) {
            startWorkEvent();
            return;
        }
        work.setEndDate(new Date());
        dbController.createAction(work);
        // clear temp entity
        actions.remove(Constants.EVENT_WORK_ACTION);
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_WORK_ACTION);
        // set previous action status
        updateStatus(getActionStatus(), getActionTime());
    }

    private void startCallEvent() {
        // create action entity
        Action call = new Action();
        call.setType(Constants.EVENT_CALL_ACTION);
        call.setStartDate(new Date());
        call.setDayCount(Utils.getDayCount(new Date()));
        String dayCount_type = call.getDayCount() + "_" + call.getType();
        call.setDayCount_type(dayCount_type);
        // add it to temp map
        actions.put(Constants.EVENT_CALL_ACTION, call);
        // add to stack trace
        stateStack.add(Constants.EVENT_CALL_ACTION);
        // update status
        updateStatus(Constants.EVENT_CALL_ACTION, call.getStartDate());
    }

    private void endCallEvent() {
        Action call = actions.get(Constants.EVENT_CALL_ACTION);
        if (call == null) {
            startCallEvent();
            return;
        }
        call.setEndDate(new Date());
        dbController.createAction(call);
        // clear temp entity
        actions.remove(Constants.EVENT_CALL_ACTION);
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_CALL_ACTION);
        // set previous action status
        updateStatus(getActionStatus(), getActionTime());
    }

    private void startWalkEvent() {
        // create action entity
        Action walk = new Action();
        walk.setType(Constants.EVENT_WALK_ACTION);
        walk.setStartDate(new Date());
        walk.setDayCount(Utils.getDayCount(new Date()));
        String dayCount_type = walk.getDayCount() + "_" + walk.getType();
        walk.setDayCount_type(dayCount_type);
        // add it to temp map
        actions.put(Constants.EVENT_WALK_ACTION, walk);
        // add to stack trace
        stateStack.add(Constants.EVENT_WALK_ACTION);
        // update status
        updateStatus(Constants.EVENT_WALK_ACTION, walk.getStartDate());
    }

    private void endWalkEvent() {
        Action walk = actions.get(Constants.EVENT_WALK_ACTION);
        if (walk == null) {
            startWalkEvent();
            return;
        }
        walk.setEndDate(new Date());
        dbController.createWalkAction(walk);
        // clear temp entity
        actions.remove(Constants.EVENT_WALK_ACTION);
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_WALK_ACTION);
        // set previous action status
        updateStatus(getActionStatus(), getActionTime());
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
