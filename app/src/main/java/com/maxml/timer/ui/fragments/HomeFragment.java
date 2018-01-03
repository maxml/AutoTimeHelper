package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.maxml.timer.R;
import com.maxml.timer.controllers.ActionController;
import com.maxml.timer.entity.Events;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    private TextView tvStartDate;
    private TextView tvTitle;
    private ToggleButton bCall;
    private ToggleButton bWork;
    private ToggleButton bRest;
    private ToggleButton bWalk;

    private EventBus eventBus;
    private ActionController actionController;

    public HomeFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.manual_activity_fragment, container, false);

        initView(rootView);
        registerEventBus();

        initListeners();

        return rootView;
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        actionController = new ActionController(getContext(), eventBus);
    }

    private void initView(View view) {
        bCall = (ToggleButton) view.findViewById(R.id.b_call);
        bWork = (ToggleButton) view.findViewById(R.id.b_work);
        bRest = (ToggleButton) view.findViewById(R.id.b_rest);
        bWalk = (ToggleButton) view.findViewById(R.id.b_walk);

        tvStartDate = (TextView) view.findViewById(R.id.tv_start_date);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
    }

    @Subscribe()
    public void onReceiveStatusEvent(Events.ActionStatus event) {
        tvTitle.setText(event.getActionStatus());
        Date time = event.getActionTime();
        if (time == null) {
            tvStartDate.setText(getString(R.string.widget_default_text));
        } else {
            tvStartDate.setText(charSequence(time));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshActionStatus();
        eventBus.register(this);
        actionController.registerEventBus(eventBus);
    }

    @Override
    public void onStop() {
        actionController.unregisterEventBus(eventBus);
        eventBus.unregister(this);
        super.onStop();
    }

    private void refreshActionStatus() {
        String action = actionController.getActionStatus();
        Date time = actionController.getActionTime();
        tvTitle.setText(action);
        if (time == null) {
            tvStartDate.setText(getString(R.string.widget_default_text));
        } else {
            tvStartDate.setText(charSequence(time));
        }
    }

    private void initListeners() {
        bCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                bWork.setChecked(false);
                bWalk.setChecked(false);
                bRest.setChecked(false);
                actionController.callActionEvent();
            }
        });
        bWork.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                bCall.setChecked(false);
                bWalk.setChecked(false);
                bRest.setChecked(false);
                actionController.workActionEvent();
            }
        });
        bRest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                bCall.setChecked(false);
                bWalk.setChecked(false);
                bWork.setChecked(false);
                actionController.restActionEvent();
            }
        });
        bWalk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                bCall.setChecked(false);
                bRest.setChecked(false);
                bWork.setChecked(false);
                actionController.walkActionEvent();
            }
        });

    }

    private String charSequence(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
        String currentDateAndTime = "Start at:" + sdf.format(date);
        return currentDateAndTime;
    }

}

