package com.maxml.timer.ui.fragments;


import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.maxml.timer.R;
import com.maxml.timer.controllers.DbController;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.ShowFragmentListener;
import com.maxml.timer.entity.ShowProgressListener;
import com.maxml.timer.ui.dialog.CheckTimeDialog;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

public class DetailsActionFragment extends Fragment implements View.OnClickListener {

    private BootstrapButton bbChangeAction;
    private BootstrapButton bbChangeDescription;
    private BootstrapButton bbChangeData;
    private BootstrapButton bbShowPathInMap;
    private BootstrapButton bbOk;
    private EditText etDescription;
    private EditText etStartDate;
    private EditText etEndDate;
    private Spinner sAction;

    private EventBus eventBus;
    private DbController dbController;
    private Action oldAction;
    private Action action;

    private ShowProgressListener progressListener;
    private ShowFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowProgressListener) {
            progressListener = (ShowProgressListener) context;
        }
        if (context instanceof ShowFragmentListener) {
            fragmentListener = (ShowFragmentListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details_action, container, false);

        initUI(rootView);
        setListeners();

        return rootView;
    }

    @Override
    public void onStart() {
        eventBus.register(this);
        dbController.registerEventBus(eventBus);

        loadAction();
        super.onStart();
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
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
                etStartDate.setEnabled(true);
                etEndDate.setEnabled(true);
                bbOk.setVisibility(View.VISIBLE);
                break;
            case R.id.bet_start_time:
                showDialogStartTime();
                break;
            case R.id.bet_end_time:
                showDialogEndTime();
                break;
            case R.id.bb_show_path_in_map:
                GoogleMapFragment mapFragment = new GoogleMapFragment();

                Bundle args = new Bundle();
                args.putString(Constants.EXTRA_ID_PATH, action.getId());

                mapFragment.setArguments(args);
                fragmentListener.showFragment(mapFragment);
                break;
            case R.id.bb_ok:
                sAction.setEnabled(false);
                etDescription.setEnabled(false);
                etStartDate.setEnabled(false);
                etEndDate.setEnabled(false);
                bbOk.setVisibility(View.INVISIBLE);
                progressListener.showProgressBar();

                updateAction();
                break;
        }
    }

    @Subscribe
    public void receiveActionFromDb(final Action action) {
        this.oldAction = new Action(action.getId(), action.getType(), action.getDayCount(), action.getDayCount_type(),
                action.getStartDate(), action.getEndDate(), action.getDescription(), action.isDeleted());
        this.action = action;
        updateUI(action);
        initUIbShow(getView());
        progressListener.hideProgressBar();
    }

    @Subscribe
    public void onDatabaseEvent(Events.DbResult event) {
        switch (event.getResultStatus()) {
            case Constants.EVENT_DB_RESULT_OK:
                progressListener.hideProgressBar();
                break;
            case Constants.EVENT_DB_RESULT_ERROR:
                progressListener.hideProgressBar();

                Toast.makeText(getContext(), R.string.action_not_exist, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void loadAction() {
        if (getArguments() != null) {
            dbController.getActionFromDb(getArguments().getString(Constants.EXTRA_ID_ACTION));
            progressListener.showProgressBar();
        }
    }

    private void showDialogStartTime() {
        final Calendar calendarStartDate = Calendar.getInstance();
        calendarStartDate.setTime(action.getStartDate());
        CheckTimeDialog checkStartTimeDialog = CheckTimeDialog.getInstance(action.getStartDate(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendarStartDate.set(Calendar.HOUR_OF_DAY, i);
                calendarStartDate.set(Calendar.MINUTE, i1);
                action.setStartDate(calendarStartDate.getTime());
                etStartDate.setText(Utils.parseToTime(calendarStartDate.getTimeInMillis()));

                if (!isSuccessfulDate()) {
                    bbOk.setEnabled(false);
                } else {
                    bbOk.setEnabled(true);
                }
            }
        });
        checkStartTimeDialog.show(getFragmentManager(), "checkStartTimeDialog");
    }

    private void showDialogEndTime() {
        final Calendar calendarEndDate = Calendar.getInstance();
        calendarEndDate.setTime(action.getEndDate());
        CheckTimeDialog checkEndTimeDialog = CheckTimeDialog.getInstance(action.getEndDate(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendarEndDate.set(Calendar.HOUR_OF_DAY, i);
                calendarEndDate.set(Calendar.MINUTE, i1);
                action.setEndDate(calendarEndDate.getTime());
                etEndDate.setText(Utils.parseToTime(calendarEndDate.getTimeInMillis()));

                if (!isSuccessfulDate()) {
                    bbOk.setEnabled(false);
                } else {
                    bbOk.setEnabled(true);
                }
            }
        });
        checkEndTimeDialog.show(getFragmentManager(), "checkEndTimeDialog");
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

        dbController.updateActionInDb(action, oldAction.getDescription());
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        dbController = new DbController(getContext(), eventBus);
    }

    private void initUI(View view) {
        bbChangeData = view.findViewById(R.id.bb_change_date);
        bbChangeAction = view.findViewById(R.id.bb_change_action);
        bbChangeDescription = view.findViewById(R.id.bb_change_description);
        bbShowPathInMap = view.findViewById(R.id.bb_show_path_in_map);
        bbOk = view.findViewById(R.id.bb_ok);

        sAction = view.findViewById(R.id.s_action_type);
        sAction.setEnabled(false);

        etDescription = view.findViewById(R.id.bet_description);
        etStartDate = view.findViewById(R.id.bet_start_time);
        etEndDate = view.findViewById(R.id.bet_end_time);

        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                new String[]{Constants.EVENT_CALL_ACTION, Constants.EVENT_REST_ACTION,
                        Constants.EVENT_WALK_ACTION, Constants.EVENT_WORK_ACTION});

        sAction.setAdapter(sAdapter);
    }

    private void initUIbShow(View view) {
        if (action.getType().equalsIgnoreCase(Constants.EVENT_WALK_ACTION)) {
            bbShowPathInMap = view.findViewById(R.id.bb_show_path_in_map);
            bbShowPathInMap.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners() {
        bbChangeAction.setOnClickListener(this);
        bbChangeDescription.setOnClickListener(this);
        bbChangeData.setOnClickListener(this);
        bbShowPathInMap.setOnClickListener(this);
        bbOk.setOnClickListener(this);

        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
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

        etDescription.setText(action.getDescription());
        etStartDate.setText(Utils.parseToTime(action.getStartDate().getTime()));
        etEndDate.setText(Utils.parseToTime(action.getEndDate().getTime()));
    }

    private boolean isSuccessfulDate() {
        return action.getStartDate().getTime() < action.getEndDate().getTime();
    }
}
