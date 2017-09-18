package com.maxml.timer.util;

/**
 * Created by nazar on 08.09.17.
 */

public class Constants {
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
    public static final String CALL_DATABASE_PATH  = "calls";
    public static final String SORT_BY_DATE_PATH = "sortByDate";
    public static final String REST_DATABASE_PATH = "rests";
    public static final String WALK_DATABASE_PATH = "walks";
    public static final String WORK_DATABASE_PATH = "works";

    //  columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_START_DATE = "startDate";
    public static final String COLUMN_END_DATE = "endDate";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IS_DELETED = "isDeleted";

    // event bus
    public static final String EVENT_RESULT_OK = "resultOk";
    public static final String EVENT_RESULT_ERROR = "resultError";
    public static final String EVENT_RESULT_LIST = "resultList";

    public static final int NOTIFICATION_ID = 100;



    public static final String EVENT_CALL_ACTION = "call";
    public static final String EVENT_CALL_MISSING = "callMissing";
    public static final String EVENT_CALL_INCOMING_ENDED = "incomingCallEnded";
    public static final String EVENT_CALL_ONGOING_ENDED = "ongoingCallEnded";
    public static final String EVENT_CALL_RINGING = "incomingCallRinging";
    public static final String EVENT_CALL_ONGOING_ANSWERED = "ongoingCallAnswered";
    public static final String EVENT_CALL_INCOMING_ANSWERED = "incomingCallAnswered";
    public static final String EVENT_UI_TABLE_DATE = "tableDate";
    public static final String EVENT_UI_GET_TABLE = "getTable";
}
