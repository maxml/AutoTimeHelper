package com.maxml.timer.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.maxml.timer.R;

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

    public static boolean backFragment(FragmentActivity activity) {
        activity.getSupportFragmentManager()
                .popBackStack();

        return activity.getSupportFragmentManager().getBackStackEntryCount() != 0;
    }
}
