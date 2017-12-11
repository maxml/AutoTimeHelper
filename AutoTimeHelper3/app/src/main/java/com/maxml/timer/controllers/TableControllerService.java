package com.maxml.timer.controllers;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.maxml.timer.MyLog;
import com.maxml.timer.R;
import com.maxml.timer.api.CallEventCRUD;
import com.maxml.timer.api.RestEventCRUD;
import com.maxml.timer.api.TableCRUD;
import com.maxml.timer.api.WalkEventCRUD;
import com.maxml.timer.api.WorkEventCRUD;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.actions.CallAction;
import com.maxml.timer.entity.actions.RestAction;
import com.maxml.timer.entity.actions.WalkAction;
import com.maxml.timer.entity.actions.WorkAction;
import com.maxml.timer.entity.eventBus.CallMessage;
import com.maxml.timer.entity.eventBus.UiMessage;
import com.maxml.timer.entity.eventBus.dbMessage.DbMessage;
import com.maxml.timer.entity.eventBus.dbMessage.DbResultMessage;
import com.maxml.timer.entity.eventBus.GpsMessage;
import com.maxml.timer.entity.eventBus.ActionMessage;
import com.maxml.timer.googlemap.GPSTracker;
import com.maxml.timer.util.Constants;
import com.maxml.timer.widget.MyWidgetProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import permissions.dispatcher.NeedsPermission;

public class TableControllerService extends Service {

    // db
    private WalkEventCRUD walkCRUD;
    private WorkEventCRUD workCRUD;
    private RestEventCRUD restCRUD;
    private CallEventCRUD callCRUD;
    private TableCRUD tableCRUD;
    // entity
    private static CallAction call;
    private static RestAction rest;
    private static WorkAction work;
    private static WalkAction walk;
//    private String actionStatus;
//    private String lastStatus;

    private List<String> stateStack = new ArrayList<>();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.d("Service: onCreate");

        // register EventBus
        EventBus.getDefault().register(this);

        // start service as foreground
        Notification notification = NotificationHelper.getDefaultNotification(this);
        startForeground(Constants.NOTIFICATION_ID, notification);

        // start tracker service
        startGpsTracker();

        initCRUD();
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void startGpsTracker() {
        startService(new Intent(this, GPSTracker.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.d("Service: onStartCommand");
        return START_STICKY;
    }

    /**
     * get result from Db and send it to receivers
     */
    @Subscribe()
    public void onDbResult(DbResultMessage message) {
        switch (message.getMessage()) {
            case Constants.EVENT_DB_RESULT_OK:
                // get result message
                DbMessage messageWithResult = message.getMessageForResult();
                // send message
                messageWithResult.sendMessage();
                break;

            case Constants.EVENT_DB_RESULT_ERROR:
                // get result message
                DbMessage messageWithError = message.getMessageForResult();
                // send error message
                messageWithError.sendErrorMessage();
                break;

            case Constants.EVENT_DB_RESULT_LIST:
                // get result message
                DbMessage messageWithListResult = message.getMessageForResult();
                // send list message
                messageWithListResult.sendMessage();
                break;
        }
    }

    /**
     * incoming Db request
     */
    @Subscribe()
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

    @Subscribe()
    public void onCallEvent(CallMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_CALL_MISSING:
                break;
            case Constants.EVENT_CALL_RINGING:
                break;

            case Constants.EVENT_CALL_ONGOING_ANSWERED:
                startCallEvent(event.getPhoneNumber());
                break;

            case Constants.EVENT_CALL_INCOMING_ANSWERED:
                startCallEvent(event.getPhoneNumber());
                break;

            // simple logic for both events
            case Constants.EVENT_CALL_INCOMING_ENDED:
            case Constants.EVENT_CALL_ONGOING_ENDED:
                endCallEvent();
                break;
        }
    }

    @Subscribe()
    public void onGpsEvent(GpsMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_WAY_COORDINATES:
                List<Coordinates> coordinates = (List<Coordinates>) event.getData();
                endWalkEvent(coordinates);
                break;
        }
    }

    @Subscribe()
    public void onWidgetEvent(ActionMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_WALK_ACTION:
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
                break;

            case Constants.EVENT_WORK_ACTION:
                if (work == null) {
                    startWorkEvent();
                } else {
                    endWorkEvent();
                }
                break;

            case Constants.EVENT_REST_ACTION:
                if (rest == null) {
                    startRestEvent();
                } else {
                    endRestEvent();
                }
                break;

            case Constants.EVENT_CALL_ACTION:
                if (call == null) {
                    startCallEvent(getString(R.string.call_number_widget));
                } else {
                    endCallEvent();
                }
                break;
        }
    }

    private void startRestEvent() {
        rest = new RestAction();
        rest.setStartDate(new Date());
        // add to stack trace
        stateStack.add(Constants.EVENT_REST_ACTION);
        // update status
        updateStatus(getString(R.string.widget_rest_text));
    }

    private void endRestEvent() {
        rest.setEndDate(new Date());
        restCRUD.create(rest);
        // clear temp entity
        rest = null;
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_REST_ACTION);
        // set previous action status
        String lastStatus = getLastStatus();
        updateStatus(lastStatus);
    }

    private void startWorkEvent() {
        work = new WorkAction();
        work.setStartDate(new Date());
        // add to stack trace
        stateStack.add(Constants.EVENT_WORK_ACTION);
        // update status
        updateStatus(getString(R.string.widget_work_text));
    }

    private void endWorkEvent() {
        work.setEndDate(new Date());
        workCRUD.create(work);
        // clear temp entity
        work = null;
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_WORK_ACTION);
        // set previous action status
        String lastStatus = getLastStatus();
        updateStatus(lastStatus);
    }

    private void startWalkEvent() {
        walk = new WalkAction();
        walk.setStartDate(new Date());
        // add to stack trace
        stateStack.add(Constants.EVENT_WALK_ACTION);
        // update status
        updateStatus(getString(R.string.widget_walk_text));
    }

    private void endWalkEvent(List<Coordinates> coordinates) {
        walk.setEndDate(new Date());
        walk.setPath(coordinates);
        walkCRUD.create(walk);
        // clear temp entity
        walk = null;
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_WALK_ACTION);
        // set previous action status
        String lastStatus = getLastStatus();
        updateStatus(lastStatus);
    }

    private void startCallEvent(String callNumber) {
        call = new CallAction();
        call.setStartDate(new Date());
        call.setDescription(getString(R.string.ongoing_call_description) + " " + callNumber);
        // add to stack trace
        stateStack.add(Constants.EVENT_CALL_ACTION);
        // update status
        updateStatus(getString(R.string.widget_call_text));
    }

    private void endCallEvent() {
        call.setEndDate(new Date());
        callCRUD.create(call, null);
        // clear temp entity
        call = null;
        // delete from stacktrace
        stateStack.remove(Constants.EVENT_CALL_ACTION);
        // set previous action status
        String lastStatus = getLastStatus();
        updateStatus(lastStatus);
    }

    private String getLastStatus() {
        if (stateStack.size() == 0) {
            return getString(R.string.widget_default_text);
        } else {
            return stateStack.get(stateStack.size() - 1);
        }
    }

    private void updateStatus(String status) {
        EventBus.getDefault().post(new UiMessage(Constants.EVENT_NEW_ACTION_STATUS, status));
        updateWidgetStatus(status);
        NotificationHelper.updateNotification(this, status);
    }

    private void updateWidgetStatus(String status) {
        Intent intent = new Intent(this, MyWidgetProvider.class);
        intent.setAction(Constants.WIDGET_UPDATE_ACTION_STATUS);
        intent.putExtra(Constants.WIDGET_EXTRA, status);
        sendBroadcast(intent);
    }


    private void initCRUD() {
        walkCRUD = new WalkEventCRUD();
        workCRUD = new WorkEventCRUD();
        callCRUD = new CallEventCRUD();
        restCRUD = new RestEventCRUD();
        tableCRUD = new TableCRUD();
    }

    @Override
    public void onDestroy() {
        MyLog.d("Service: onDestroy");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
