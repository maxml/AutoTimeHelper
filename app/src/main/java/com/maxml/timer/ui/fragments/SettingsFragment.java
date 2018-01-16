package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.maxml.timer.R;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.SharedPrefUtils;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        addPreferencesFromResource();
//        Log.i("LOG", "onCreatePreferences: " + "" + SharedPrefUtils.getColor(getContext(),
//                Constants.EVENT_REST_ACTION));
        setPreferencesFromResource(R.xml.setting, rootKey);
    }
}
