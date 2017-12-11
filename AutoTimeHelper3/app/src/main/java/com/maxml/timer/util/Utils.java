package com.maxml.timer.util;

import android.app.ActivityManager;
import android.content.Context;

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

    public static Date getDate(String dayCount) {
        if (dayCount != null) {
               try {
                   long days = Long.valueOf(dayCount);
                   long millis = days * 24*60*60* 1000;
                   return new Date(millis);
               } catch (NumberFormatException e){
                   return null;
               }

        } else {
            return null;
        }
    }

    public static long getDayCount(Date date) {
        if (date != null) {
            long millis = date.getTime();
            return millis / 1000 / 60 / 60 / 24;
        } else return -1;
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}