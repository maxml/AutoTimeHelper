package com.maxml.timer.util;

public class Constants {

    public static final String TAG = "LOG";

    public static final float MIN_DISTANCE_START_WALK_ACTION = 25f;
    public static final float MIN_DISTANCE_UPDATES = 5f;
    public static final long MIN_TIME = 1000*20;

    public static final int RESULT_OK = 1;
    public static final int RESULT_FALSE = 0;
    public static final String SHARED_USER = "SHARED_USER";
    public static final String SHARED_COLOR = "SHARED_COLOR";

    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    public static final int REQUEST_LOCATION_PERMISSIONS = 2;

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

    public static final int WIFI_TYPE_NONE = 0;
    public static final int WIFI_TYPE_HOME = 1;
    public static final int WIFI_TYPE_WORK = 2;
    public static final String EVENT_WIFI_ENABLE = "eventWifiEnable";
    public static final String EVENT_WIFI_DISABLE = "eventWifiDisable";

    public static final String EVENT_CALL_ACTION = "call";
    public static final String EVENT_CALL_MISSING = "callMissing";
    public static final String EVENT_CALL_INCOMING_ENDED = "incomingCallEnded";
    public static final String EVENT_CALL_ONGOING_ENDED = "ongoingCallEnded";
    public static final String EVENT_CALL_RINGING = "incomingCallRinging";
    public static final String EVENT_CALL_ONGOING_ANSWERED = "ongoingCallAnswered";
    public static final String EVENT_CALL_INCOMING_ANSWERED = "incomingCallAnswered";
    public static final String REQUEST_TABLE_DATE = "tableDate";
    public static final String EVENT_TABLE_DATE_RESULT = "getTable";
    public static final String EVENT_SAVE_COORDINATES = "saveCoordinates";
    public static final String ACTION_WIDGET_RECEIVER = "clickWidgetButton";
    public static final String WIDGET_EXTRA = "widgetExtra";

    public static final String EVENT_WORK_ACTION = "work";
    public static final String EVENT_REST_ACTION = "rest";
    public static final String EVENT_WALK_ACTION = "walk";
    public static final String WIDGET_UPDATE_ACTION_STATUS = "updateActionStatus";
    public final static String WIDGET_UPDATE_ACTION = "com.javatechig.intent.action.UPDATE_WIDGET";
    public static final String EVENT_LOCATION_CHANGE = "locationChange";
    public static final String EVENT_GPS_START = "startGpsEvent";
    public static final String EVENT_GPS_STOP = "stopGpsEvent";
    public static final String EVENT_WAY_COORDINATES = "wayCoordinates";
    public static final String EVENT_ACTION_STATUS = "newActionStatus";
    public static final String COLUMN_DAY_COUNT = "dayCount";
    public static final String EVENT_WALK_ACTION_SAVED = "walkActionSaved";
    public static final String EVENT_REGISTER_EVENT_BUS = "registerEventBus";
    public static final String EVENT_UNREGISTER_EVENT_BUS = "unregisterEventBus";
    public static final String EVENT_SET_WIDGET_EVENT_BUS = "setCurrentEventBusAsWidget";
    public static final String EVENT_SET_CALL_EVENT_BUS = "setCallEventBus";
    public static final String EVENT_SET_WIFI_EVENT_BUS = "setWifiEventBus";
    public static final int WAY_DONT_MOVE_TIME = 5;
    public static final String EXTRA_ID_PATH = "extraIdPath";
    public static final String EXTRA_ID_ACTION = "extraIdAction";
    public static final String EXTRA_TIME_ACTION = "extraTimeAction";
    public static final String EXTRA_LIST_ID_PATH = "extraListIdPath";
    public static final String LOG = "appLog";

    public static final String TABLE_PATH = "tablePath";
    public static final String TABLE_COORDINATES = "tableCoordinates";
    public static final String TABLE_WIFI_STATE = "wifiStates";

    public static final int ID_BUTTON_EDIT = 0;
    public static final int ID_BUTTON_DELETE = 1;

    public static final int WEEK_COUNT_DAY = 7;
    public static final String EVENT_CLOSE_APP = "closeApp";
}
