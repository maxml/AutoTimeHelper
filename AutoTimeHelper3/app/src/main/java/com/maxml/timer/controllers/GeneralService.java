package com.maxml.timer.controllers;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.maxml.timer.MyLog;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.DbReturnData;
import com.maxml.timer.entity.eventBus.EventMessage;
import com.maxml.timer.entity.eventBus.DbMessage;
import com.maxml.timer.googlemap.GPSTracker;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.EventType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
public class GeneralService extends Service {

    private Controller controller;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.d("Service: onCreate");
        controller = new Controller(this);
        // register EventBus
        registerEventBus();
        // start service as foreground
        Notification notification = NotificationHelper.getDefaultNotification(this);
        startForeground(Constants.NOTIFICATION_ID, notification);
        // start tracker service
        startGpsTracker();
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

    @Subscribe()
    public void onReceiveEvent(DbMessage message) {
        switch (message.getMessage()) {
            /**
         * get result from Db and send it to receivers
         */
            case Constants.EVENT_DB_RESULT_OK:
                // get result data
                Object data = message.getData();
                // get data for return request
                DbReturnData returnData = message.getDbReturnData();
                // get EventBus for result
                EventBus resultEventBus = controller.getEventBus(returnData.getEventType());
                // send result
                resultEventBus.post(new EventMessage(returnData.getResultRequest(), data));
                break;

            case Constants.EVENT_DB_RESULT_ERROR:
                // get data for return request
                DbReturnData dbReturnData = message.getDbReturnData();
                // get EventBus for result
                EventBus resEventBus = controller.getEventBus(dbReturnData.getEventType());
                // send result with message name such as request message name
                resEventBus.post(new EventMessage(Constants.EVENT_DB_RESULT_ERROR));
                break;

            /**
             * incoming Db request
             */
            default:
                controller.onReceiveDbRequest(message);
                break;
        }

    }

    @Subscribe()
    public void onReciveEvent(EventMessage event) {
        switch (event.getMessage()) {
            /** start actions*/
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
            /** end actions*/

            case Constants.EVENT_WAY_COORDINATES:
                List<Coordinates> coordinates = (ArrayList<Coordinates>) event.getData();
                controller.endWalkEvent(coordinates);
                break;
        }
    }

    private void registerEventBus() {
        controller.getEventBus(EventType.DB_EVENT_BUS).register(this);
        controller.getEventBus(EventType.UI_EVENT_BUS).register(this);
        controller.getEventBus(EventType.ACTION_EVENT_BUS).register(this);
        controller.getEventBus(EventType.GPS_EVENT_BUS).register(this);
    }

    @Override
    public void onDestroy() {
        MyLog.d("Service: onDestroy");
        controller.getEventBus(EventType.DB_EVENT_BUS).unregister(this);
        controller.getEventBus(EventType.UI_EVENT_BUS).unregister(this);
        controller.getEventBus(EventType.ACTION_EVENT_BUS).unregister(this);
        controller.getEventBus(EventType.GPS_EVENT_BUS).unregister(this);
        super.onDestroy();
    }
}
