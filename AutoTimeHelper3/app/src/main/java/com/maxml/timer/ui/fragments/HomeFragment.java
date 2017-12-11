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
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.eventBus.ActionMessage;
import com.maxml.timer.entity.eventBus.UiMessage;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
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

        return view;
    }

    @Subscribe()
    public void onUiEvent(UiMessage event) {
        switch (event.getMessage()) {
            case Constants.EVENT_NEW_ACTION_STATUS:
                title.setText(сharSequence());
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
                EventBus.getDefault().post(new ActionMessage(Constants.EVENT_CALL_ACTION));
            }
        });

        butWork.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                butCall.setChecked(false);
                butWalk.setChecked(false);
                butRest.setChecked(false);
                EventBus.getDefault().post(new ActionMessage(Constants.EVENT_WORK_ACTION));
            }
        });
        butRest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                butCall.setChecked(false);
                butWalk.setChecked(false);
                butWork.setChecked(false);
                EventBus.getDefault().post(new ActionMessage(Constants.EVENT_REST_ACTION));
            }
        });
        butWalk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                butCall.setChecked(false);
                butRest.setChecked(false);
                butWork.setChecked(false);
                EventBus.getDefault().post(new ActionMessage(Constants.EVENT_WALK_ACTION));
            }
        });

	}
	
	public String сharSequence() {
		SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
		String currentDateandTime = "Start at:" + sdf.format(new Date());
		return currentDateandTime;
	}

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}

