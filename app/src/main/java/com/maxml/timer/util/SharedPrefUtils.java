package com.maxml.timer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maxml.timer.R;
import com.maxml.timer.entity.User;

public class SharedPrefUtils {

    public static void saveCurrentUser(Context context, User user) {
        if (user == null) return;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String userJson = gson.toJson(user);
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_USER, Context.MODE_PRIVATE);
        sp.edit().putString(Constants.SHARED_USER, userJson).apply();
    }

    public static User getCurrentUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_USER, Context.MODE_PRIVATE);
        String userFromPref = sp.getString(Constants.SHARED_USER, "");
        if (TextUtils.isEmpty(userFromPref)) return new User();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(userFromPref, User.class);
    }

    public static int getColor(Context context, String actionName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        int defaultColor = 0;
        Resources resources = context.getResources();

        if (actionName.equalsIgnoreCase(Constants.EVENT_WALK_ACTION)) {
            defaultColor = resources.getColor(R.color.event_color_blue);
        } else if (actionName.equalsIgnoreCase(Constants.EVENT_CALL_ACTION)) {
            defaultColor = resources.getColor(R.color.event_color_yellow);
        } else if (actionName.equalsIgnoreCase(Constants.EVENT_WORK_ACTION)) {
            defaultColor = resources.getColor(R.color.event_color_red);
        } else if (actionName.equalsIgnoreCase(Constants.EVENT_REST_ACTION)) {
            defaultColor = resources.getColor(R.color.event_color_green);
        }

        return sp.getInt(actionName, defaultColor);
    }

}
