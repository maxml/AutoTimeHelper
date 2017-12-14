package com.maxml.timer.controllers;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.maxml.timer.MyLog;
import com.maxml.timer.R;
import com.maxml.timer.api.ActionCRUD;
import com.maxml.timer.api.PathCRUD;
import com.maxml.timer.api.TableCRUD;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.actions.Action;
import com.maxml.timer.entity.eventBus.CallMessage;
import com.maxml.timer.entity.eventBus.UiMessage;
import com.maxml.timer.entity.eventBus.dbMessage.DbMessage;
import com.maxml.timer.entity.eventBus.dbMessage.DbResultMessage;
import com.maxml.timer.entity.eventBus.GpsMessage;
import com.maxml.timer.entity.eventBus.ActionMessage;
import com.maxml.timer.googlemap.GPSTracker;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;
import com.maxml.timer.widget.MyWidgetProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;

public class GeneralService extends Service {

    private ActionController controller;

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

        controller = new ActionController(this);
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
        controller.onReceiveDbRequest(message);
    }

    @Subscribe()
    public void onCallEvent(CallMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_CALL_MISSING:
                break;
            case Constants.EVENT_CALL_RINGING:
                break;

            case Constants.EVENT_CALL_ONGOING_ANSWERED:
                controller.startCallEvent();
                break;

            case Constants.EVENT_CALL_INCOMING_ANSWERED:
                controller.startCallEvent();
                break;

            // simple logic for both events
            case Constants.EVENT_CALL_INCOMING_ENDED:
            case Constants.EVENT_CALL_ONGOING_ENDED:
                controller.endCallEvent();
                break;
        }
    }

    @Subscribe()
    public void onGpsEvent(GpsMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_WAY_COORDINATES:
                List<Coordinates> coordinates = (List<Coordinates>) event.getData();
                controller.endWalkEvent(coordinates);
                break;
        }
    }

    @Subscribe()
    public void onWidgetEvent(ActionMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_WALK_ACTION:
                controller.walkActionEvent();
                break;

            case Constants.EVENT_WORK_ACTION:
                controller.workActionEvent();
                break;

            case Constants.EVENT_REST_ACTION:
                controller.restActionEvent();
                break;

            case Constants.EVENT_CALL_ACTION:
                controller.callActionEvent();
                break;
        }
    }

    @Override
    public void onDestroy() {
        MyLog.d("Service: onDestroy");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
