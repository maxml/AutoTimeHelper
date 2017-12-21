package com.maxml.timer.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.maxml.timer.R;
import com.maxml.timer.api.ActionCRUD;
import com.maxml.timer.api.CoordinatesCRUD;
import com.maxml.timer.api.TableCRUD;
import com.maxml.timer.api.UserAPI;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Table;
import com.maxml.timer.entity.actions.Action;
import com.maxml.timer.entity.eventBus.Events;
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

public class Controller {

    private static EventBus serviceEventBus;
    private EventBus entityEventBus;
    private Context context;

    // db
    private ActionCRUD actionCRUD;
    private TableCRUD tableCRUD;
    private CoordinatesCRUD coordinatesCRUD;
    private UserAPI userAPI;

    private String walkActionId;
    private Map<String, Action> actions = new HashMap<>();
    private List<String> stateStack = new ArrayList<>();

    public Controller(Context context, EventBus entityEventBus) {
        this.context = context;
        this.entityEventBus = entityEventBus;
        initCRUD();
    }

    public static Controller build(Context context, EventBus eventBus) {
        serviceEventBus = eventBus;
        return new Controller(context, eventBus);
    }

    /********************* START DB TOOLS **************************/
    public void sendDbResultOk() {
        entityEventBus.post(new Events.DbResult(Constants.EVENT_DB_RESULT_OK));
    }

    public void sendDbResultError() {
        entityEventBus.post(new Events.DbResult(Constants.EVENT_DB_RESULT_ERROR));
    }

    public void getTableFromDb(Date date) {
        tableCRUD.read(date);
    }

    public void sendTableFromDb(Table table) {
        entityEventBus.post(table);
    }

    public void updateUserEmail(String email) {
        userAPI.updateEmail(email);
    }

    public void updateUserName(String name) {
        userAPI.updateName(name);
    }

    public void updateUserPhoto(String uri) {
        userAPI.updatePhoto(Uri.parse(uri));
    }

    public void createUser(String email, String password) {
        userAPI.create(email, password);
    }

    public void sentUser() {
        entityEventBus.post(userAPI);
    }

    public void login(String email, String password) {
        userAPI.login(email, password);
    }

    public void logout() {
        userAPI.logout();
    }

    public void forgotPassword(String email) {
        userAPI.sentPassword(email);
    }

    public void saveWalkPath(String walkActionId) {
        this.walkActionId = walkActionId;
        serviceEventBus.post(new Events.GPS(Constants.EVENT_GPS_STOP));
    }

    public void savePath(List<Coordinates> coordinates) {
        if (walkActionId != null) {
            coordinatesCRUD.create(walkActionId, coordinates);
        }
        walkActionId = null;
    }


    /********************* END DB TOOLS **************************/


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

//            case Constants.EVENT_WALK_ACTION_SAVED:
//                // stop gps tracking
//                getEventBus(EventBusType.GPS).post(new EventMessage(Constants.EVENT_GPS_STOP));
//                break;
//
//            case Constants.EVENT_WAY_COORDINATES:
//                List<Coordinates> coordinates = (ArrayList<Coordinates>) message.getData();
//                coordinatesCRUD.(coordinates);
//                break;

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
// todo
        serviceEventBus.post(new Events.GPS(Constants.EVENT_GPS_STOP));
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
        updateStatus(context.getString(R.string.widget_rest_text), rest.getStartDate());
    }

    private void endRestEvent() {
        Action rest = actions.get(Constants.EVENT_REST_ACTION);
        if (rest == null) {
            startRestEvent();
            return;
        }
        rest.setEndDate(new Date());
        actionCRUD.create(rest);
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
        updateStatus(context.getString(R.string.widget_work_text), work.getStartDate());
    }

    private void endWorkEvent() {
        Action work = actions.get(Constants.EVENT_WORK_ACTION);
        if (work == null) {
            startWorkEvent();
            return;
        }
        work.setEndDate(new Date());
        actionCRUD.create(work);
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
        updateStatus(context.getString(R.string.widget_call_text), call.getStartDate());
    }

    private void endCallEvent() {
        Action call = actions.get(Constants.EVENT_CALL_ACTION);
        if (call == null) {
            startCallEvent();
            return;
        }
        call.setEndDate(new Date());
        actionCRUD.create(call);
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
        updateStatus(context.getString(R.string.widget_walk_text), walk.getStartDate());
    }

    private void endWalkEvent() {
        Action walk = actions.get(Constants.EVENT_WALK_ACTION);
        if (walk == null) {
            startWalkEvent();
            return;
        }
        walk.setEndDate(new Date());
        actionCRUD.createWalkAction(walk);
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
        Intent intent = new Intent(context, MyWidgetProvider.class);
        intent.setAction(Constants.WIDGET_UPDATE_ACTION_STATUS);
        intent.putExtra(Constants.WIDGET_EXTRA, status);
        context.sendBroadcast(intent);
    }


    private void initCRUD() {
        tableCRUD = new TableCRUD(this);
        actionCRUD = new ActionCRUD(this);
        coordinatesCRUD = new CoordinatesCRUD(this);
        userAPI = new UserAPI(context, this);
    }

}
