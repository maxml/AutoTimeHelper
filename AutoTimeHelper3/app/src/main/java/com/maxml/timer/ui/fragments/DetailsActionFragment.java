package com.maxml.timer.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.maxml.timer.MyLog;
import com.maxml.timer.R;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.entity.actions.Action;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;

public class DetailsActionFragment extends Fragment implements View.OnClickListener {

    private BootstrapButton bbChangeAction;
    private BootstrapButton bbChangeDescription;
    private BootstrapButton bbChangeData;
    private BootstrapButton bbOk;

    private EditText etDescription;
    private EditText etStartDate;
    private EditText etEndDate;

    private Spinner sAction;

    private Action action;
    private ArrayAdapter<String> sAdapter;

    private Controller controller;
    private EventBus eventBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details_action, container, false);

        initUI(rootView);
        setListeners();

        eventBus = new EventBus();
        controller = new Controller(getContext(), eventBus);

        return rootView;
    }

    @Override
    public void onStart() {
        eventBus.register(this);
        controller.registerEventBus(eventBus);
        super.onStart();
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        controller.unregisterEventBus(eventBus);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bb_change_action:
                sAction.setEnabled(true);
                bbOk.setVisibility(View.VISIBLE);
                break;
            case R.id.bb_change_description:
                etDescription.setEnabled(true);
                bbOk.setVisibility(View.VISIBLE);
                break;
            case R.id.bb_change_date:

                break;
            case R.id.bb_ok:
                sAction.setEnabled(false);
                etDescription.setEnabled(false);
                bbOk.setVisibility(View.INVISIBLE);
                //TODO make updating data through controller
                break;
        }
    }

    private void initUI(View view) {
        bbChangeData = (BootstrapButton) view.findViewById(R.id.bb_change_date);
        bbChangeAction = (BootstrapButton) view.findViewById(R.id.bb_change_action);
        bbChangeDescription = (BootstrapButton) view.findViewById(R.id.bb_change_description);
        bbOk = (BootstrapButton) view.findViewById(R.id.bb_ok);

        sAction = (Spinner) view.findViewById(R.id.s_action);

        etStartDate = (EditText) view.findViewById(R.id.bet_start_date);
        etEndDate = (EditText) view.findViewById(R.id.bet_end_date);

        sAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                new String[] {Constants.EVENT_CALL_ACTION, Constants.EVENT_REST_ACTION,
                        Constants.EVENT_WALK_ACTION, Constants.EVENT_WORK_ACTION});

        sAction.setAdapter(sAdapter);
    }

    private void setListeners() {
        bbChangeAction.setOnClickListener(this);
        bbChangeDescription.setOnClickListener(this);
        bbChangeData.setOnClickListener(this);
        bbOk.setOnClickListener(this);
    }

    private void updateUI(Action action) {
        if (action.getType().equalsIgnoreCase(Constants.EVENT_CALL_ACTION)) {
            sAction.setSelection(0);
        } else if (action.getType().equalsIgnoreCase(Constants.EVENT_REST_ACTION)) {
            sAction.setSelection(1);
        } else if (action.getType().equalsIgnoreCase(Constants.EVENT_WALK_ACTION)) {
            sAction.setSelection(2);
        } else if (action.getType().equalsIgnoreCase(Constants.EVENT_WORK_ACTION)) {
            sAction.setSelection(3);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");

        etDescription.setText(action.getDescription());
        etStartDate.setText(sdf.format(action.getStartDate()));
        etEndDate.setText(sdf.format(action.getEndDate()));
    }
}
