package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxml.timer.R;
import com.maxml.timer.controllers.ActionController;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.receivers.WifiReceiver;
import com.maxml.timer.ui.adapter.WifiAdapter;
import com.maxml.timer.ui.dialog.OptionDialog;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class SettingWifiFragment extends Fragment implements WifiAdapter.OnItemClickListener,
        OptionDialog.OnDialogItemClickListener {
    private Toolbar toolbar;
    private TextView tvTextEmpty;

    private EventBus eventBus;
    private DbController dbController;
    private ActionController actionController;

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
        View rootView = inflater.inflate(R.layout.fragment_setting_wifi, container,
                false);
        
        initView(rootView);
        showMessageIfListIsEmpty();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        dbController.registerEventBus(eventBus);
        actionController.registerEventBus(eventBus);

        loadWifi();
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        dbController.unregisterEventBus(eventBus);
        actionController.unregisterEventBus(eventBus);
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
        dbController.updateWifi(lastWifiState);
        dbController.getAllWifi();

        announceAboutConnecting();
        showMessageIfListIsEmpty();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.setTitle(R.string.app_name);
    }

    private void announceAboutConnecting() {
        WifiState wifiState = NetworkUtil.getCurrentWifi(getContext());

        if (wifiState.getId() != null && !wifiState.getId().equalsIgnoreCase("")) {
            actionController.onReceiveWifiEvent(new Events.WifiEvent(Constants.EVENT_WIFI_ENABLE,
                    lastWifiState.getType()));
            WifiReceiver.isActiveWifi = true;
            WifiReceiver.wifiType = lastWifiState.getType();
        }
    }

    @Subscribe
    public void receiveWifiFromDB(List<WifiState> list) {
        adapter.swapData(list);
        showMessageIfListIsEmpty();
    }

    private void initView(View rootView) {
        RecyclerView rvWifi = rootView.findViewById(R.id.rv_wifi);
        toolbar = getActivity().findViewById(R.id.toolbar);
        tvTextEmpty = rootView.findViewById(R.id.tv_text_empty);

        rvWifi.setHasFixedSize(true);
        rvWifi.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new WifiAdapter(new ArrayList<WifiState>(), this);
        rvWifi.setAdapter(adapter);

        toolbar.setTitle(R.string.settings);
    }

    private void loadWifi() {
        dbController.getAllWifi();
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        dbController = new DbController(getContext(), eventBus);
        actionController = new ActionController(getContext(), eventBus);
    }

    public void showMessageIfListIsEmpty() {
        if (adapter.getItemCount() == 0) {
            tvTextEmpty.setVisibility(View.VISIBLE);
        } else {
            tvTextEmpty.setVisibility(View.GONE);
        }
    }
}
