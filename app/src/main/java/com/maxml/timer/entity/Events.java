package com.maxml.timer.entity;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

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
        private String actionId;

        public DbResult(String resultStatus) {
            this.resultStatus = resultStatus;
        }

        public DbResult(String resultStatus, String actionId) {
            this.resultStatus = resultStatus;
            this.actionId = actionId;
        }

        public String getActionId() {
            return actionId;
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
        public int wifiType;

        public WifiEvent(String wifiState, int wifiType) {
            this.wifiState = wifiState;
            this.wifiType = wifiType;
        }

        public String getWifiState() {
            return wifiState;
        }

        public int getWifiType() {
            return wifiType;
        }
    }

    public static class Info {
        private String eventMessage;

        public Info(String eventMessage) {
            this.eventMessage = eventMessage;
        }

        public String getEventMessage() {
            return eventMessage;
        }
    }

    public static class TurnOnGeolocation {
        private String message;
        private int request;

        public TurnOnGeolocation(String message, int request) {
            this.message = message;
            this.request = request;
        }

        public String getMessage() {
            return message;
        }

        public int getRequest() {
            return request;
        }
    }
}
