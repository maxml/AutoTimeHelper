package com.maxml.timer.util;

import android.app.ActivityManager;
import android.content.Context;

import com.google.api.client.util.Data;
import com.maxml.timer.R;
import com.maxml.timer.entity.Coordinates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utils {
    public static Date getDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static Date getDate(int hour, int minute, int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static String parseToTime(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(timeInMillis));
    }

    public static String parseToTimeDuration(Context context, long timeInMillis) {
        int hour = (int) ((timeInMillis / (1000 * 60 * 60)) % 24);
        int min = (int) ((timeInMillis / (1000 * 60)) % 60);
        int sec = (int) (timeInMillis / 1000) % 60;
        StringBuilder sb = new StringBuilder();
        if (hour > 0) {
            sb.append(hour);
            sb.append(context.getString(R.string.hour));
            sb.append(":");
        }
        if (hour > 0 || min > 0) {
            sb.append(min);
            sb.append(context.getString(R.string.min));
        }
        if (hour == 0 && min < 2) {
            if (min > 0) {
                sb.append(":");
            }
            sb.append(sec);
            sb.append(context.getString(R.string.sec));
        }
        return sb.toString();
    }

    public static String parseToDate(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date(timeInMillis));
    }

    public static Date getDate(String dayCount) {
        if (dayCount != null) {
            try {
                long days = Long.valueOf(dayCount);
                long millis = days * 24 * 60 * 60 * 1000;
                return new Date(millis);
            } catch (NumberFormatException e) {
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

    public static String getTimeSubscribe(Date startDate, Date endDate) {
        int hour = (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60);
        int minute = (int) (endDate.getTime() - startDate.getTime()) / 1000 / 60 % 60;

        if (hour != 0) {
            return hour + "." + minute;
        } else {
            return minute + "";
        }
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    public static double getDistance(List<Coordinates> path) {
        if (path.size() < 2) {
            return 0;
        }
        double result = 0;
        for (int index = 0; index > path.size() - 1; index++) {
            double length = Coordinates.getDistanceInMeter(path.get(index), path.get(index + 1));
            result = result + length;
        }
        return result;
    }

}
