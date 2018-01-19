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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.manual_activity_fragment, container, false);
        initView(rootView);
        initListeners();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
        actionController.registerEventBus(eventBus);
    }

    @Override
    public void onResume() {
        super.onResume();
        String action = actionController.getCurrentActionType();
        Date time = actionController.getCurrentActionStartTime();
        refreshActionStatus(action, time);
    }

    @Override
    public void onStop() {
        actionController.unregisterEventBus(eventBus);
        eventBus.unregister(this);
        super.onStop();
    }

    @Subscribe()
    public void onReceiveStatusEvent(Events.ActionStatus event) {
        refreshActionStatus(event.getActionStatus(), event.getActionTime());
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        actionController = new ActionController(getContext(), eventBus);
    }

    private void refreshActionStatus(String action, Date startTime) {
        tvTitle.setText(action);
        if (startTime == null) {
            tvStartDate.setText(R.string.text_default_action_date);
        } else {
            tvStartDate.setText(charSequence(startTime));
        }
        updateButtonsState(action);
    }

    private void updateButtonsState(String currentAction) {
        switch (currentAction) {
            case Constants.EVENT_WALK_ACTION:
                setActiveButton(bWalk);
                break;
            case Constants.EVENT_WORK_ACTION:
                setActiveButton(bWork);
                break;
            case Constants.EVENT_REST_ACTION:
                setActiveButton(bRest);
                break;
            case Constants.EVENT_CALL_ACTION:
                setActiveButton(bCall);
                break;
            default:
                setActiveButton(null);
                break;
        }
    }

    private void setActiveButton(ToggleButton button) {
        if (button == null) {
            button = new ToggleButton(getContext());
        }
        bWork.setChecked(bWork.getId() == button.getId());
        bWalk.setChecked(bWalk.getId() == button.getId());
        bRest.setChecked(bRest.getId() == button.getId());
        bCall.setChecked(bCall.getId() == button.getId());
    }

    private void initListeners() {
        bCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveButton((ToggleButton) v);
                actionController.callActionEvent();
            }
        });
        bWork.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setActiveButton((ToggleButton) v);
                actionController.workActionEvent();
            }
        });
        bRest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setActiveButton((ToggleButton) v);
                actionController.restActionEvent();
            }
        });
        bWalk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setActiveButton((ToggleButton) v);
                actionController.walkActionEvent();
            }
        });
    }

    private void initView(View view) {
        bCall = view.findViewById(R.id.b_call);
        bWork = view.findViewById(R.id.b_work);
        bRest = view.findViewById(R.id.b_rest);
        bWalk = view.findViewById(R.id.b_walk);
        tvStartDate = view.findViewById(R.id.tv_start_date);
        tvTitle = view.findViewById(R.id.tv_title);
    }

    private String charSequence(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
        return "Start at:" + sdf.format(date);
    }

}

