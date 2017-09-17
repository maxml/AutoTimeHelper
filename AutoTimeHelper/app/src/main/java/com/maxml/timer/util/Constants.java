package com.maxml.timer.util;

/**
 * Created by nazar on 08.09.17.
 */

public class Constants {
    public static final int RESULT_OK = 1;
    public static final int RESULT_FALSE = 0;
    public static final String SHARED_USER = "SHARED_USER";




    // handler
    public static final int DB_RESULT_OK = 1;
    public static final int DB_RESULT_ENTITY = 2;
    public static final int DB_RESULT_LIST = 3;
    public static final int DB_RESULT_FALSE = 0;

    // firebase database
    public static final String USER_DATABASE_PATH = "users";
    public static final String CALL_DATABASE_PATH  = "calls";
    public static final String SORT_BY_DATE_PATH = "sortByDate";
    public static final String LINE_DATABASE_PATH = "LINE";
    //  columns
    public static final String COLUMN_USER_ID = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_START_DATE = "startDate";
    public static final String COLUMN_END_DATE = "endDate";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String SLICE_LINE_ID = "lineUUID";
    public static final String SLICE_UPDATE_DATE = "updateDate";
    public static final String COLUMN_IS_DELETED = "isDeleted";
    public static final String LINE_USER = "lineUser";
}
