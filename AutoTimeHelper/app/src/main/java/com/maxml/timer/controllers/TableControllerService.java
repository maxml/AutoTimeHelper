package com.maxml.timer.controllers;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.maxml.timer.MyLog;
import com.maxml.timer.R;
import com.maxml.timer.api.CallEventCRUD;
import com.maxml.timer.api.RestEventCRUD;
import com.maxml.timer.api.WalkEventCRUD;
import com.maxml.timer.api.WorkEventCRUD;
import com.maxml.timer.entity.Table;
import com.maxml.timer.entity.actions.CallAction;
import com.maxml.timer.entity.actions.RestAction;
import com.maxml.timer.entity.actions.WalkAction;
import com.maxml.timer.entity.actions.WorkAction;
import com.maxml.timer.entity.eventBus.CallMessage;
import com.maxml.timer.entity.eventBus.DbMessage;
import com.maxml.timer.entity.eventBus.UiMessage;
import com.maxml.timer.ui.fragments.GoogleMapFragment;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

public class TableControllerService extends Service {

    private Notification notification;

    // // TODO: 18.09.17
    private Table table;
    private GoogleMapFragment googlemapF;
    // db
    private WalkEventCRUD walkCRUD;
    private WorkEventCRUD workCRUD;
    private RestEventCRUD restCRUD;
    private CallEventCRUD callCRUD;
    // entity
    private static CallAction call;
    private static RestAction rest;
    private static WorkAction work;
    private static WalkAction walk;


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
        notification = NotificationHelper.getDefaultNotification(this);
        startForeground(Constants.NOTIFICATION_ID, notification);

        initCRUD();
        registerReceivers();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.d("Service: onStartCommand");
/*
        // if activity restart and resend intent onStart update notification
        if (notification != null) {
            NotificationHelper.updateNotification(this, currentSong);
        }
*/
        return START_STICKY;
    }

    @Subscribe(sticky = true)
    public void onDbEvent(DbMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_RESULT_OK:
                break;
            case Constants.EVENT_RESULT_ERROR:
                break;
            case Constants.EVENT_RESULT_LIST:
                break;
        }
    }
    @Subscribe(sticky = true)
    public void onUiEvent(UiMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_UI_TABLE_DATE:
                // // TODO: 18.09.17  
                Date date = (Date) event.getData();
                break;
            case Constants.EVENT_RESULT_ERROR:
                break;
            case Constants.EVENT_RESULT_LIST:
                break;
        }
    }

    @Subscribe(sticky = true)
    public void onCallEvent(CallMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_CALL_MISSING:
                break;
            case Constants.EVENT_CALL_RINGING:
                break;

            case Constants.EVENT_CALL_ONGOING_ANSWERED:
                String outputNumber = event.getPhoneNumber();
                call.setStartDate(new Date());
                call.setDescription(getString(R.string.ongoing_call_description) + " " + outputNumber);
                break;

            case Constants.EVENT_CALL_INCOMING_ANSWERED:
                String inputNumber = event.getPhoneNumber();
                call.setStartDate(new Date());
                call.setDescription(getString(R.string.incoming_call_description) + " " + inputNumber);
                break;

            // simple logic for both events
            case Constants.EVENT_CALL_INCOMING_ENDED:
            case Constants.EVENT_CALL_ONGOING_ENDED:
                call.setEndDate(new Date());
                callCRUD.create(call);
                break;
        }
    }


    private void initCRUD() {
        walkCRUD = new WalkEventCRUD();
        workCRUD = new WorkEventCRUD();
        callCRUD = new CallEventCRUD();
        restCRUD = new RestEventCRUD();
    }

    private void registerReceivers() {

    }

    @Override
    public void onDestroy() {
        MyLog.d("Service: onDestroy");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
