package com.maxml.timer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maxml.timer.entity.User;

/**
 * Created by nazar on 08.09.17.
 */

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

}
