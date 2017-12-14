package com.maxml.timer.controllers;

import android.content.Context;
import android.content.Intent;

import com.maxml.timer.R;
import com.maxml.timer.api.ActionCRUD;
import com.maxml.timer.api.PathCRUD;
import com.maxml.timer.api.TableCRUD;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.actions.Action;
import com.maxml.timer.entity.eventBus.GpsMessage;
import com.maxml.timer.entity.eventBus.UiMessage;
import com.maxml.timer.entity.eventBus.dbMessage.DbMessage;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;
import com.maxml.timer.widget.MyWidgetProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nazar on 12.12.17.
 */

public class ActionController {

    private static Map<EventType, EventBus> eventBuses = new HashMap<>();
    private Context context;

    // db
    private ActionCRUD actionCRUD;
    private TableCRUD tableCRUD;
    private PathCRUD pathCRUD;

    private Map<String, Action> actions = new HashMap<>();
    private List<String> stateStack = new ArrayList<>();

    public void saveWalkedPath(List<Coordinates> coordinates) {
        endWalkEvent(coordinates);
    }

    public enum EventType {
        DB_EVENT_BUS, UI_EVENT_BUS
    }


    public ActionController(Context context) {
        this.context = context;

        initCRUD();
    }

    public static ActionController build(Context context) {
        ActionController entity = new ActionController(context);
        entity.addEventBus(EventType.DB_EVENT_BUS, EventBus.builder().build());
        entity.addEventBus(EventType.UI_EVENT_BUS, EventBus.builder().build());
        return entity;
    }

    public EventBus getEventBus(EventType type) {
        return eventBuses.get(type);
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
            EventBus.getDefault().post(new GpsMessage(Constants.EVENT_START));
            // create walk action
            startWalkEvent();
        } else {
            // stop gps tracking
            EventBus.getDefault().post(new GpsMessage(Constants.EVENT_STOP));
            // get result in onGpsEvent method
        }
    }


    public void onReceiveDbRequest(DbMessage message) {
        switch (message.getMessage()) {
            case Constants.EVENT_TABLE_DATE_REQUEST:
                Date date = (Date) message.getData();
                // change message msg name to receiver constant
                message.setMessage(Constants.EVENT_TABLE_DATE_RESULT);
                tableCRUD.read(date, message);
                break;
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
        // add it to temp map
        actions.put(Constants.EVENT_REST_ACTION, rest);
        // add to stack trace
        stateStack.add(Constants.EVENT_REST_ACTION);
        // update status
        updateStatus(context.getString(R.string.widget_rest_text));
    }

    private void endRestEvent() {
        Action rest = actions.get(Constants.EVENT_REST_ACTION);
        if (rest == null) {
            startRestEvent();
            return;
        }
        rest.setEndDate(new Date());
        actionCRUD.create(rest, null);
        // clear temp entity
        actions.remove(Constants.EVENT_REST_ACTION);
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_REST_ACTION);
        // set previous action status
        String lastStatus = getLastStatus();
        updateStatus(lastStatus);
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
        updateStatus(context.getString(R.string.widget_work_text));
    }

    private void endWorkEvent() {
        Action work = actions.get(Constants.EVENT_WORK_ACTION);
        if (work == null) {
            startWorkEvent();
            return;
        }
        work.setEndDate(new Date());
        actionCRUD.create(work, null);
        // clear temp entity
        actions.remove(Constants.EVENT_WORK_ACTION);
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_WORK_ACTION);
        // set previous action status
        String lastStatus = getLastStatus();
        updateStatus(lastStatus);
    }

    public void startCallEvent() {
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
        updateStatus(context.getString(R.string.widget_call_text));
    }

    public void endCallEvent() {
        Action call = actions.get(Constants.EVENT_CALL_ACTION);
        if (call == null) {
            startCallEvent();
            return;
        }
        call.setEndDate(new Date());
        actionCRUD.create(call, null);
        // clear temp entity
        actions.remove(Constants.EVENT_CALL_ACTION);
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_CALL_ACTION);
        // set previous action status
        String lastStatus = getLastStatus();
        updateStatus(lastStatus);
    }

    private void startWalkEvent() {
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
        updateStatus(context.getString(R.string.widget_call_text));
    }

    public void endWalkEvent(List<Coordinates> coordinates) {
        Action walk = actions.get(Constants.EVENT_WALK_ACTION);
        if (walk == null) {
            startWorkEvent();
            return;
        }
        walk.setEndDate(new Date());
        actionCRUD.create(walk, null);
//        pathCRUD.create(coordinates);
        // clear temp entity
        actions.remove(Constants.EVENT_CALL_ACTION);
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_CALL_ACTION);
        // set previous action status
        String lastStatus = getLastStatus();
        updateStatus(lastStatus);
    }


    private String getLastStatus() {
        if (stateStack.size() == 0) {
            return context.getString(R.string.widget_default_text);
        } else {
            return stateStack.get(stateStack.size() - 1);
        }
    }

    private void updateStatus(String status) {
        EventBus.getDefault().post(new UiMessage(Constants.EVENT_NEW_ACTION_STATUS, status));
        updateWidgetStatus(status);
        NotificationHelper.updateNotification(context, status);
    }

    private void updateWidgetStatus(String status) {
        Intent intent = new Intent(context, MyWidgetProvider.class);
        intent.setAction(Constants.WIDGET_UPDATE_ACTION_STATUS);
        intent.putExtra(Constants.WIDGET_EXTRA, status);
        context.sendBroadcast(intent);
    }


    private void initCRUD() {
        tableCRUD = new TableCRUD();
        actionCRUD = new ActionCRUD();
        pathCRUD = new PathCRUD();
    }


    private void addEventBus(EventType type, EventBus eventBus) {
        eventBuses.put(type, eventBus);
    }

}
