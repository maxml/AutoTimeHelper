package com.maxml.timer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.maxml.timer.controllers.ActionController;
import com.maxml.timer.entity.Events;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;

public class CallReceiver extends BroadcastReceiver {

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static boolean isIncoming = false;
    private ActionController actionController;
    private EventBus eventBus;

    public void onReceive(Context context, Intent intent) {
        initController(context);
        int state = 0;
        String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        if (stateStr != null) {
            if (stateStr.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }
        }
        onCallStateChanged(state);
    }

    private void initController(Context context) {
        if (actionController == null) {
            eventBus = new EventBus();
            actionController = new ActionController(context, eventBus);
            actionController.registerEventBus(eventBus);
            eventBus.post(new Events.CallEvent(Constants.EVENT_SET_CALL_EVENT_BUS));
        }
    }

    private void onCallStateChanged(int state) {
        if (lastState == state) {
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    // ring but not pick up, MISSING CALL
                    eventBus.post(new Events.CallEvent(Constants.EVENT_CALL_MISSING));
                } else {
                    if (isIncoming) {
                        //INCOMING CALL ENDED
                        eventBus.post(new Events.CallEvent(Constants.EVENT_CALL_INCOMING_ENDED));
                    } else {
                        //ONGOING CALL ENDED
                        eventBus.post(new Events.CallEvent(Constants.EVENT_CALL_ONGOING_ENDED));
                    }
                }
                break;

            case TelephonyManager.CALL_STATE_RINGING:
                // RINGING INCOMING CALL
                isIncoming = true;
                eventBus.post(new Events.CallEvent(Constants.EVENT_CALL_RINGING));
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    // outgoing call answered
                    isIncoming = false;
                    eventBus.post(new Events.CallEvent(Constants.EVENT_CALL_ONGOING_ANSWERED));
                } else {
                    // incoming call answered
                    isIncoming = true;
                    eventBus.post(new Events.CallEvent(Constants.EVENT_CALL_INCOMING_ANSWERED));
                }
                break;
        }
        lastState = state;
    }
}
