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

import java.util.HashSet;
import java.util.Set;

public class SharedPreferencesUtils {

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

    public static void saveTags(Context context, Set<String> tags) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_TAGS, Context.MODE_PRIVATE);
        sp.edit().putStringSet(Constants.SHARED_TAGS, tags).apply();
    }

    public static Set<String> getTags(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_TAGS, Context.MODE_PRIVATE);
        return sp.getStringSet(Constants.SHARED_TAGS, new HashSet<String>());
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
        } else if (actionName.equalsIgnoreCase(Constants.ACTION_SELECTED)) {
            defaultColor = resources.getColor(R.color.select_action);
        } else if (actionName.equalsIgnoreCase(Constants.ACTION_JOINED)) {
            defaultColor = resources.getColor(R.color.joined_action);
        }
        return sp.getInt(actionName, defaultColor);
    }

    public static void setColor(Context context, String actionName, int color) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        if (actionName.equalsIgnoreCase(Constants.EVENT_WALK_ACTION)) {
            editor.putInt(actionName, color);
        } else if (actionName.equalsIgnoreCase(Constants.EVENT_CALL_ACTION)) {
            editor.putInt(actionName, color);
        } else if (actionName.equalsIgnoreCase(Constants.EVENT_WORK_ACTION)) {
            editor.putInt(actionName, color);
        } else if (actionName.equalsIgnoreCase(Constants.EVENT_REST_ACTION)) {
            editor.putInt(actionName, color);
        } else if (actionName.equalsIgnoreCase(Constants.ACTION_SELECTED)) {
            editor.putInt(actionName, color);
        } else if (actionName.equalsIgnoreCase(Constants.ACTION_JOINED)) {
            editor.putInt(actionName, color);
        }
        editor.apply();
        editor.commit();
    }
}
