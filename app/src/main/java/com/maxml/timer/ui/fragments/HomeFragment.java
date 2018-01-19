package com.maxml.timer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.maxml.timer.util.Constants;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.manual_activity_fragment, container, false);

        initView(rootView);
        setStartUI();

        initListeners();

        return rootView;
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

    private void setStartUI() {
        String activeStatus = actionController.getPreviousActionType();
        Date actionDate = actionController.getPreviousActionStartTime();
        if (activeStatus != null && actionDate != null) {
            tvTitle.setText(activeStatus);
            tvStartDate.setText(charSequence(actionDate));
            if (activeStatus.equalsIgnoreCase(Constants.EVENT_CALL_ACTION)) {
                bCall.setChecked(true);
            } else if (activeStatus.equalsIgnoreCase(Constants.EVENT_WORK_ACTION)) {
                bWork.setChecked(true);
            } else if (activeStatus.equalsIgnoreCase(Constants.EVENT_REST_ACTION)) {
                bRest.setChecked(true);
            } else if (activeStatus.equalsIgnoreCase(Constants.EVENT_WALK_ACTION)) {
                bWalk.setChecked(true);
            }
        }
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        actionController = new ActionController(getContext(), eventBus);
    }

    private void initView(View view) {
        bCall = view.findViewById(R.id.b_call);
        bWork = view.findViewById(R.id.b_work);
        bRest = view.findViewById(R.id.b_rest);
        bWalk = view.findViewById(R.id.b_walk);

        tvStartDate = view.findViewById(R.id.tv_start_date);
        tvTitle = view.findViewById(R.id.tv_title);
    }

    private void refreshActionStatus() {
        String action = actionController.getPreviousActionType();
        Date time = actionController.getPreviousActionStartTime();
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
        return "Start at:" + sdf.format(date);
    }

}

