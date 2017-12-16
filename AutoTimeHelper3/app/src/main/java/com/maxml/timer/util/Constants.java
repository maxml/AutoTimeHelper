package com.maxml.timer.util;

/**
 * Created by nazar on 08.09.17.
 */

public class Constants {

    public static final long MIN_DISTANCE_UPDATES = 5;
    public static final long MIN_TIME = 1000 * 20;

    public static final int RESULT_OK = 1;
    public static final int RESULT_FALSE = 0;
    public static final String SHARED_USER = "SHARED_USER";

    // handler
//    public static final int DB_RESULT_OK = 1;
//    public static final int DB_RESULT_ENTITY = 2;
//    public static final int DB_RESULT_LIST = 3;
//    public static final int DB_RESULT_FALSE = 0;

    // firebase database
    public static final String USER_DATABASE_PATH = "users";
    public static final String WALK_DATABASE_PATH = "walks";

    //  columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_START_DATE = "startDate";
    public static final String COLUMN_END_DATE = "endDate";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IS_DELETED = "isDeleted";

    // event bus
    public static final String EVENT_DB_RESULT_OK = "resultOk";
    public static final String EVENT_DB_RESULT_ERROR = "resultError";

    public static final int NOTIFICATION_ID = 100;



    public static final String EVENT_CALL_ACTION = "call";
    public static final String EVENT_CALL_MISSING = "callMissing";
    public static final String EVENT_CALL_INCOMING_ENDED = "incomingCallEnded";
    public static final String EVENT_CALL_ONGOING_ENDED = "ongoingCallEnded";
    public static final String EVENT_CALL_RINGING = "incomingCallRinging";
    public static final String EVENT_CALL_ONGOING_ANSWERED = "ongoingCallAnswered";
    public static final String EVENT_CALL_INCOMING_ANSWERED = "incomingCallAnswered";
    public static final String EVENT_TABLE_DATE_REQUEST = "tableDate";
    public static final String EVENT_TABLE_DATE_RESULT = "getTable";
    public static final String EVENT_SAVE_COORDINATES = "saveCoordinates";
    public static final String ACTION_WIDGET_RECEIVER = "clickWidgetButton";
    public static final String WIDGET_EXTRA = "widgetExtra";

    public static final String EVENT_WORK_ACTION = "work";
    public static final String EVENT_REST_ACTION = "rest";
    public static final String EVENT_WALK_ACTION = "walk";
    public static final String WIDGET_UPDATE_ACTION_STATUS = "updateActionStatus";
    public static final String EVENT_LOCATION_CHANGE = "locationChange";
    public static final String EVENT_START = "startEvent";
    public static final String EVENT_STOP = "stopEvent";
    public static final String EVENT_WAY_COORDINATES = "wayCoordinates";
    public static final String EVENT_NEW_ACTION_STATUS = "newActionStatus";
    public static final String COLUMN_DAY_COUNT = "dayCount";
    public static final String EVENT_WALK_ACTION_SAVED = "walkActionSaved";
}
