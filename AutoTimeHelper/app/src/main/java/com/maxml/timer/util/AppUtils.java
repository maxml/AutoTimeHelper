package com.maxml.timer.util;

/**
 * Created by nazar on 10.09.17.
 */

public class AppUtils {
    public static double degToRad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double radToDeg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static double toRadians(double a) {
        return Math.toRadians(a);
    }

}
