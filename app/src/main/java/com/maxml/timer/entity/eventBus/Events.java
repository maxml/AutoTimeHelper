package com.maxml.timer.entity.eventBus;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

/**
 * Created by nazar on 18.12.17.
 */

public class Events {

    public static class EventBusControl {
        private String message;
        private EventBus eventBus;

        public EventBusControl(String message, EventBus eventBus) {
            this.message = message;
            this.eventBus = eventBus;
        }

        public String getMessage() {
            return message;
        }

        public EventBus getEventBus() {
            return eventBus;
        }
    }

    public static class ActionStatus {
        private String actionStatus;
        private Date actionTime;

        public ActionStatus(String actionStatus, Date actionTime) {
            this.actionStatus = actionStatus;
            this.actionTime = actionTime;
        }

        public String getActionStatus() {
            return actionStatus;
        }

        public Date getActionTime() {
            return actionTime;
        }
    }

    public static class GPS {
        private String message;

        public GPS(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class WidgetEvent {
        private String message;

        public WidgetEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class DbResult {
        private String resultStatus;

        public DbResult(String resultStatus) {
            this.resultStatus = resultStatus;
        }

        public String getResultStatus() {
            return resultStatus;
        }
    }

    public static class CallEvent {
        private String callState;

        public CallEvent(String callState) {
            this.callState = callState;
        }

        public String getCallState() {
            return callState;
        }
    }

    public static class WifiEvent {
        public String wifiState;

        public WifiEvent(String wifiState) {
            this.wifiState = wifiState;
        }

        public String getWifiState() {
            return wifiState;
        }
    }
}
