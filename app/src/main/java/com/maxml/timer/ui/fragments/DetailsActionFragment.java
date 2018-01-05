package com.maxml.timer.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.maxml.timer.R;
import com.maxml.timer.controllers.ActionController;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.entity.Table;
import com.maxml.timer.util.ActionConverter;
import com.maxml.timer.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailsActionFragment extends Fragment implements View.OnClickListener {

    private BootstrapButton bbChangeAction;
    private BootstrapButton bbChangeDescription;
    private BootstrapButton bbChangeData;
    private BootstrapButton bbOk;
    private EditText etDescription;
    private EditText etStartDate;
    private EditText etEndDate;
    private Spinner sAction;

    private ArrayAdapter<String> sAdapter;
    private EventBus eventBus;
    private DbController dbController;
    private Action action;

    private ActionController actionController;
    private ShowProgressListener progressListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowProgressListener) {
            progressListener = (ShowProgressListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();

        dbController.getActionFromDb(getArguments().getString(Constants.EXTRA_ID_ACTION));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details_action, container, false);

        initUI(rootView);
        setListeners();

        return rootView;
    }

    @Override
    public void onStart() {
        eventBus.register(this);
        actionController.registerEventBus(eventBus);
        dbController.registerEventBus(eventBus);
        super.onStart();
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        actionController.unregisterEventBus(eventBus);
        dbController.unregisterEventBus(eventBus);
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
                progressListener.showProgressBar();

                updateAction();
                break;
        }
    }

    @Subscribe
    public void receiveActionFromDb(Action action) {
        this.action = action;
        updateUI(action);
    }

    @Subscribe
    public void onDatabaseEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                progressListener.hideProgressBar();
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                progressListener.hideProgressBar();

                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void updateAction() {
        if (sAction.getSelectedItemPosition() == 0) {
            action.setType(Constants.EVENT_CALL_ACTION);
        } else if (sAction.getSelectedItemPosition() == 1) {
            action.setType(Constants.EVENT_REST_ACTION);
        } else if (sAction.getSelectedItemPosition() == 2) {
            action.setType(Constants.EVENT_WALK_ACTION);
        } else if (sAction.getSelectedItemPosition() == 3) {
            action.setType(Constants.EVENT_WORK_ACTION);
        }
        action.setDescription(etDescription.getText().toString());

        dbController.updateActionInDb(action);
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        actionController = new ActionController(getContext(), eventBus);
        dbController = new DbController(getContext(), eventBus);
    }

    private void initUI(View view) {
        bbChangeData = (BootstrapButton) view.findViewById(R.id.bb_change_date);
        bbChangeAction = (BootstrapButton) view.findViewById(R.id.bb_change_action);
        bbChangeDescription = (BootstrapButton) view.findViewById(R.id.bb_change_description);
        bbOk = (BootstrapButton) view.findViewById(R.id.bb_ok);

        sAction = (Spinner) view.findViewById(R.id.s_action);
        sAction.setEnabled(false);

        etDescription = (EditText) view.findViewById(R.id.bet_description);
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
