package com.maxml.timer.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;

import com.maxml.timer.R;
import com.maxml.timer.entity.ShowFragmentListener;
import com.maxml.timer.ui.activity.LoginActivity;
import com.maxml.timer.util.Constants;
import com.u1aryz.android.colorpicker.ColorPreferenceFragmentCompat;

public class SettingsFragment extends ColorPreferenceFragmentCompat implements
        Preference.OnPreferenceClickListener{
    private Toolbar toolbar;

    private ShowFragmentListener fragmentListener;

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

        Preference colorsPreference = getPreferenceManager().findPreference(Constants.KEY_COLORS);
        Preference wifiPreference = getPreferenceManager().findPreference(Constants.KEY_SETTING_WIFI);
        Preference singInPreference = getPreferenceManager().findPreference(Constants.KEY_MANAGE_ACCOUNT);

        colorsPreference.setOnPreferenceClickListener(this);
        wifiPreference.setOnPreferenceClickListener(this);
        singInPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equalsIgnoreCase(Constants.KEY_COLORS)) {
            SettingColorsFragment fragment = new SettingColorsFragment();
            fragmentListener.showFragment(fragment);
        } else if (preference.getKey().equalsIgnoreCase(Constants.KEY_SETTING_WIFI)) {
            SettingWifiFragment fragment = new SettingWifiFragment();
            fragmentListener.showFragment(fragment);
        } else if (preference.getKey().equalsIgnoreCase(Constants.KEY_MANAGE_ACCOUNT)) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.setTitle(R.string.app_name);
    }

    private void initView() {
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
    }
}