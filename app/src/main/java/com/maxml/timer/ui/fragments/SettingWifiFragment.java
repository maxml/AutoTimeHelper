package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.ui.adapter.WifiAdapter;
import com.maxml.timer.ui.dialog.OptionDialog;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class SettingWifiFragment extends Fragment implements WifiAdapter.OnItemClickListener, OptionDialog.OnDialogItemClickListener {
    private EventBus eventBus;
    private DbController controller;

    private WifiAdapter adapter;
    private WifiState lastWifiState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting_wifi, container, false);
        
        initView(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        controller.registerEventBus(eventBus);

        loadWifi();
    }

    @Override
    public void onStop() {
        controller.unregisterEventBus(eventBus);
        eventBus.unregister(this);
        super.onStop();
    }
    @Override
    public void onItemClick(WifiState wifiState) {
        this.lastWifiState = wifiState;
        OptionDialog dialog = OptionDialog.getInstance(this, R.array.options_wifi_type);
        dialog.show(getFragmentManager(), "OptionDialog");
    }

    @Override
    public void onDialogItemClick(int position) {
        if (position == 0) {
            lastWifiState.setType(Constants.WIFI_TYPE_HOME);
        } else if (position == 1){
            lastWifiState.setType(Constants.WIFI_TYPE_WORK);
        } else {
            lastWifiState.setType(0);
        }
        controller.updateWifi(lastWifiState);
        controller.getAllWifi();
    }

    @Subscribe
    public void receiveWifiFromDB(List<WifiState> list) {
        adapter.swapData(list);
    }

    private void initView(View rootView) {
        RecyclerView rvWifi = rootView.findViewById(R.id.rv_wifi);
        rvWifi.setHasFixedSize(true);
        rvWifi.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new WifiAdapter(new ArrayList<WifiState>(), this);
        rvWifi.setAdapter(adapter);
    }

    private void loadWifi() {
        controller.getAllWifi();
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        controller = new DbController(getContext(), eventBus);
    }
}
