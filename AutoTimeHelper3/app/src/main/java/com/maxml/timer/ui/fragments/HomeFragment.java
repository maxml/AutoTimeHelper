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
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.eventBus.EventMessage;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.EventBusType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {
    private Controller controller;
    private EventBus eventBusHomeFrg;
    private EventBus eventBusAction;
    private TextView title;
    private TextView description;
    private ToggleButton butCall;
    private ToggleButton butWork;
    private ToggleButton butRest;
    private ToggleButton butWalk;

    public HomeFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manual_activity_fragment, container, false);
        butCall = (ToggleButton) view.findViewById(R.id.butCall);
        butWork = (ToggleButton) view.findViewById(R.id.butWork);
        butRest = (ToggleButton) view.findViewById(R.id.butRest);
        butWalk = (ToggleButton) view.findViewById(R.id.butWalk);
        title = (TextView) view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);

        title.setText(getString(R.string.widget_default_text));
        description.setText(getString(R.string.widget_default_text));
        initListeners();

        controller = new Controller(getContext());
        eventBusHomeFrg = controller.getEventBus(EventBusType.HOME_FRAGMENT);
        eventBusAction = controller.getEventBus(EventBusType.ACTION_EVENT);
        return view;
    }

    @Subscribe()
    public void onReceiveEvent(EventMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_NEW_ACTION_STATUS:
                title.setText(charSequence());
                String status = (String) event.getData();
                description.setText(status);
                break;
        }
    }


    private void initListeners() {
        butCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                butWork.setChecked(false);
                butWalk.setChecked(false);
                butRest.setChecked(false);
                eventBusAction.post(new EventMessage(Constants.EVENT_CALL_ACTION));
            }
        });

        butWork.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                butCall.setChecked(false);
                butWalk.setChecked(false);
                butRest.setChecked(false);
                eventBusAction.post(new EventMessage(Constants.EVENT_WORK_ACTION));
            }
        });
        butRest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                butCall.setChecked(false);
                butWalk.setChecked(false);
                butWork.setChecked(false);
                eventBusAction.post(new EventMessage(Constants.EVENT_REST_ACTION));
            }
        });
        butWalk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                butCall.setChecked(false);
                butRest.setChecked(false);
                butWork.setChecked(false);
                eventBusAction.post(new EventMessage(Constants.EVENT_WALK_ACTION));
            }
        });

	}
	
	public String charSequence() {
		SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
		String currentDateAndTime = "Start at:" + sdf.format(new Date());
		return currentDateAndTime;
	}

    @Override
    public void onStart() {
        super.onStart();
        eventBusHomeFrg.register(this);
    }

    @Override
    public void onStop() {
        eventBusHomeFrg.unregister(this);
        super.onStop();
    }
}

