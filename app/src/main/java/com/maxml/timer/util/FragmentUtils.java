package com.maxml.timer.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.maxml.timer.R;

/**
 * Created by nazar on 08.09.17.
 */

public class FragmentUtils {

    public static Fragment getCurrentFragment(FragmentActivity activity) {
        return activity.getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    public static void setFragment(FragmentActivity activity, Fragment fragment, String tag) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(tag)
                .commit();
    }
    public static void setFragment(FragmentActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
}
