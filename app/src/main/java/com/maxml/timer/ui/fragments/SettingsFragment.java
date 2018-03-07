package com.maxml.timer.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.maxml.timer.R;
import com.maxml.timer.entity.ShowFragmentListener;
import com.maxml.timer.ui.activity.LoginActivity;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.SharedPreferencesUtils;
import com.u1aryz.android.colorpicker.ColorPreference;
import com.u1aryz.android.colorpicker.ColorPreferenceFragmentCompat;

public class SettingsFragment extends ColorPreferenceFragmentCompat implements
        Preference.OnPreferenceClickListener, PreferenceFragmentCompat.OnPreferenceStartScreenCallback, Preference.OnPreferenceChangeListener {
    private Toolbar toolbar;

    private ShowFragmentListener fragmentListener;
    private int color;
    private Preference restPreference;
    private Preference callPreference;
    private Preference workPreference;
    private Preference walkPreference;
    private Preference actionJoinedPreference;
    private Preference actionSelectedPreference;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowFragmentListener) {
            fragmentListener = (ShowFragmentListener) context;
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);

        initView();

        Preference wifiPreference = getPreferenceManager().findPreference(Constants.KEY_SETTING_WIFI);
        Preference singInPreference = getPreferenceManager().findPreference(Constants.KEY_MANAGE_ACCOUNT);
        Preference defaultPreference = getPreferenceManager().findPreference(Constants.KEY_DEFAULT_COLOR);
        restPreference = getPreferenceManager().findPreference(Constants.EVENT_REST_ACTION);
        callPreference = getPreferenceManager().findPreference(Constants.EVENT_CALL_ACTION);
        workPreference = getPreferenceManager().findPreference(Constants.EVENT_WORK_ACTION);
        walkPreference = getPreferenceManager().findPreference(Constants.EVENT_WALK_ACTION);
        actionJoinedPreference = getPreferenceManager().findPreference(Constants.ACTION_JOINED);
        actionSelectedPreference = getPreferenceManager().findPreference(Constants.ACTION_SELECTED);

        wifiPreference.setOnPreferenceClickListener(this);
        singInPreference.setOnPreferenceClickListener(this);
        defaultPreference.setOnPreferenceClickListener(this);
        restPreference.setOnPreferenceClickListener(this);
        callPreference.setOnPreferenceClickListener(this);
        workPreference.setOnPreferenceClickListener(this);
        walkPreference.setOnPreferenceClickListener(this);
        actionJoinedPreference.setOnPreferenceClickListener(this);
        actionSelectedPreference.setOnPreferenceClickListener(this);

        restPreference.setOnPreferenceChangeListener(this);
        callPreference.setOnPreferenceChangeListener(this);
        workPreference.setOnPreferenceChangeListener(this);
        walkPreference.setOnPreferenceChangeListener(this);
        actionJoinedPreference.setOnPreferenceChangeListener(this);
        actionSelectedPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equalsIgnoreCase(Constants.KEY_SETTING_WIFI)) {
            SettingWifiFragment fragment = new SettingWifiFragment();
            fragmentListener.showFragment(fragment);
        } else if (preference.getKey().equalsIgnoreCase(Constants.KEY_MANAGE_ACCOUNT)) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        } else if (preference.getKey().equalsIgnoreCase(Constants.KEY_DEFAULT_COLOR)) {
            SharedPreferencesUtils.makeDefaultColors(getContext());
            setDefaultColors();
        } else if (preference.getKey().equalsIgnoreCase(Constants.EVENT_REST_ACTION)) {
            color = SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_REST_ACTION);
        } else if (preference.getKey().equalsIgnoreCase(Constants.EVENT_CALL_ACTION)) {
            color = SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_CALL_ACTION);
        } else if (preference.getKey().equalsIgnoreCase(Constants.EVENT_WORK_ACTION)) {
            color = SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_WORK_ACTION);
        } else if (preference.getKey().equalsIgnoreCase(Constants.EVENT_WALK_ACTION)) {
            color = SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_WALK_ACTION);
        } else if (preference.getKey().equalsIgnoreCase(Constants.ACTION_JOINED)) {
            color = SharedPreferencesUtils.getColor(getContext(), Constants.ACTION_JOINED);
        } else if (preference.getKey().equalsIgnoreCase(Constants.ACTION_SELECTED)) {
            color = SharedPreferencesUtils.getColor(getContext(), Constants.ACTION_SELECTED);
        }
        return true;
    }

    @Override
    public Fragment getCallbackFragment() {
        return this;
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat, PreferenceScreen preferenceScreen) {
        preferenceFragmentCompat.setPreferenceScreen(preferenceScreen);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Integer newColor = (int) newValue;
        if (changeOnRightData(preference.getKey(), newColor)) {
            SharedPreferencesUtils.setColor(getContext(), preference.getKey(), color);
            Toast.makeText(getContext(), R.string.select_different_colors, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void initView() {
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
    }

    private void setDefaultColors() {
        ((ColorPreference) restPreference).setColor(SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_REST_ACTION));
        ((ColorPreference) callPreference).setColor(SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_CALL_ACTION));
        ((ColorPreference) workPreference).setColor(SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_WORK_ACTION));
        ((ColorPreference) walkPreference).setColor(SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_WALK_ACTION));
        ((ColorPreference) actionJoinedPreference).setColor(SharedPreferencesUtils.getColor(getContext(), Constants.ACTION_JOINED));
        ((ColorPreference) actionSelectedPreference).setColor(SharedPreferencesUtils.getColor(getContext(), Constants.ACTION_SELECTED));
    }

    private boolean changeOnRightData(String key, int newColor) {
        int callColor = SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_CALL_ACTION);
        int walkColor = SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_WALK_ACTION);
        int workColor = SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_WORK_ACTION);
        int restColor = SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_REST_ACTION);
        int androidJoined = SharedPreferencesUtils.getColor(getContext(), Constants.ACTION_JOINED);
        int actionSelected = SharedPreferencesUtils.getColor(getContext(), Constants.EVENT_REST_ACTION);
        if (key.equalsIgnoreCase(Constants.EVENT_WALK_ACTION)) {
            if (newColor != callColor && newColor != workColor && newColor != restColor && newColor != actionSelected && newColor != androidJoined) {
                return true;
            }
        } else if (key.equalsIgnoreCase(Constants.EVENT_REST_ACTION)) {
            if (newColor != callColor && newColor != workColor && newColor != walkColor && newColor != actionSelected && newColor != androidJoined) {
                return true;
            }
        } else if (key.equalsIgnoreCase(Constants.EVENT_CALL_ACTION)) {
            if (newColor != restColor && newColor != workColor && newColor != walkColor && newColor != actionSelected && newColor != androidJoined) {
                return true;
            }
        } else if (key.equalsIgnoreCase(Constants.EVENT_WORK_ACTION)) {
            if (newColor != callColor && newColor != restColor && newColor != walkColor && newColor != actionSelected && newColor != androidJoined) {
                return true;
            }
        } else if (key.equalsIgnoreCase(Constants.ACTION_JOINED)) {
            if (newColor != workColor && newColor != callColor && newColor != restColor && newColor != walkColor && newColor != actionSelected) {
                return true;
            }
        } else if (key.equalsIgnoreCase(Constants.ACTION_SELECTED)) {
            if (newColor != workColor && newColor != callColor && newColor != restColor && newColor != walkColor && newColor != androidJoined) {
                return true;
            }
        }
        return false;
    }
}