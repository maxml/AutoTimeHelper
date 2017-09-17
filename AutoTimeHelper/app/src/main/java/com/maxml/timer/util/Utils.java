package com.maxml.timer.util;

import com.google.api.client.util.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by nazar on 13.09.17.
 */

public class Utils {
    public static Date getDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static long getDayCount(Date date) {
        if (date != null) {
            long millis = date.getTime();
            return millis / 1000 / 60 / 60 / 24;
        } else return -1;
    }
}
