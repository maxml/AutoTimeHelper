package com.maxml.timer.controllers;

import android.content.Context;
import android.content.Intent;

import com.maxml.timer.R;
import com.maxml.timer.api.ActionCRUD;
import com.maxml.timer.api.CoordinatesCRUD;
import com.maxml.timer.api.TableCRUD;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.DbReturnData;
import com.maxml.timer.entity.actions.Action;
import com.maxml.timer.entity.eventBus.EventMessage;
import com.maxml.timer.entity.eventBus.DbMessage;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.EventBusType;
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

public class Controller {

    private static Map<EventBusType, EventBus> eventBuses = new HashMap<>();
    private Context context;

    // db
    private ActionCRUD actionCRUD;
    private TableCRUD tableCRUD;
    private CoordinatesCRUD coordinatesCRUD;

    private Map<String, Action> actions = new HashMap<>();
    private List<String> stateStack = new ArrayList<>();


    public Controller(Context context) {
        this.context = context;

        initCRUD();
    }

    public static Controller build(Context context) {
        Controller entity = new Controller(context);
        entity.addEventBus(EventBusType.DB, EventBus.builder().build());
        entity.addEventBus(EventBusType.CONTROLLER, EventBus.builder().build());
        entity.addEventBus(EventBusType.HOME_FRAGMENT, EventBus.builder().build());
        entity.addEventBus(EventBusType.ACTION_EVENT, EventBus.builder().build());
        entity.addEventBus(EventBusType.GPS, EventBus.builder().build());
        return entity;
    }

    public void onReceiveDbMessage(DbMessage message) {
        switch (message.getMessage()) {
            /**
             * get result from Db and send it to receivers
             */
            case Constants.EVENT_DB_RESULT_OK:
                // get data for return request
                DbReturnData returnData = message.getDbReturnData();
                if (returnData == null) {
                    return;
                }
                // get result data
                Object data = message.getData();
                // get EventBus for result
                EventBus resultEventBus = getEventBus(returnData.getEventBusType());
                // send result
                resultEventBus.post(new EventMessage(returnData.getResultRequest(), data));
                break;

            case Constants.EVENT_DB_RESULT_ERROR:
                // get data for return request
                DbReturnData dbReturnData = message.getDbReturnData();
                if (dbReturnData == null) {
                    return;
                }
                // get EventBus for result
                EventBus resEventBus = getEventBus(dbReturnData.getEventBusType());
                // send result with message name such as request message name
                resEventBus.post(new EventMessage(Constants.EVENT_DB_RESULT_ERROR));
                break;


            case Constants.EVENT_TABLE_DATE_REQUEST:
                Date date = (Date) message.getData();
                tableCRUD.read(date, message.getDbReturnData());
                break;
        }
    }

    public void onReceiveEventMessage(EventMessage message) {
        switch (message.getMessage()) {
            /** start actions*/
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
            /** end actions*/

//            case Constants.EVENT_WALK_ACTION_SAVED:
//                // stop gps tracking
//                getEventBus(EventBusType.GPS).post(new EventMessage(Constants.EVENT_STOP));
//                break;
//
//            case Constants.EVENT_WAY_COORDINATES:
//                List<Coordinates> coordinates = (ArrayList<Coordinates>) message.getData();
//                coordinatesCRUD.(coordinates);
//                break;
        }

    }


    public EventBus getEventBus(EventBusType type) {
        return eventBuses.get(type);
    }

    private void restActionEvent() {
        Action rest = actions.get(Constants.EVENT_REST_ACTION);
        if (rest == null) {
            startRestEvent();
        } else {
            endRestEvent();
        }
    }

    private void callActionEvent() {
        Action call = actions.get(Constants.EVENT_CALL_ACTION);
        if (call == null) {
            startCallEvent();
        } else {
            endCallEvent();
        }
    }


    private void workActionEvent() {
        Action work = actions.get(Constants.EVENT_WORK_ACTION);
        if (work == null) {
            startWorkEvent();
        } else {
            endWorkEvent();
        }
    }

    private void walkActionEvent() {
        Action walk = actions.get(Constants.EVENT_WALK_ACTION);
        if (walk == null) {
            // start gps tracking
            getEventBus(EventBusType.GPS).post(new EventMessage(Constants.EVENT_START));
            // create walk action
            startWalkEvent();
        } else {
            // save walt action
            endWalkEvent();
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
        updateStatus(context.getString(R.string.widget_call_text));
    }

    private void endCallEvent() {
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

    private void endWalkEvent() {
        Action walk = actions.get(Constants.EVENT_WALK_ACTION);
        if (walk == null) {
            startWorkEvent();
            return;
        }
        walk.setEndDate(new Date());
        // add return data, after complete CRUD create method result will sent to
        // EventBus "CONTROLLER" with message "EVENT_SAVE_COORDINATES"
        DbReturnData returnData = new DbReturnData(Constants.EVENT_WALK_ACTION_SAVED, EventBusType.CONTROLLER);
        actionCRUD.create(walk, returnData);
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
        eventBuses.get(EventBusType.HOME_FRAGMENT).post(new EventMessage(Constants.EVENT_NEW_ACTION_STATUS, status));
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
        tableCRUD = new TableCRUD(context);
        actionCRUD = new ActionCRUD(context);
        coordinatesCRUD = new CoordinatesCRUD(context);
    }


    private void addEventBus(EventBusType type, EventBus eventBus) {
        eventBuses.put(type, eventBus);
    }

}
