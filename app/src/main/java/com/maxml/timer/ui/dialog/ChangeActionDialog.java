package com.maxml.timer.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.maxml.timer.ui.fragments.GoogleMapFragment;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

public class ChangeActionDialog extends DialogFragment implements View.OnClickListener {
    private BootstrapButton bbShowPathInMap;
    private Button bPositive;
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
    private static OnActionChangedListener actionChangedListener;

    private static ChangeActionDialog dialog;

    public interface OnActionChangedListener {
        void actionChanged(Action action);
    }

    public static ChangeActionDialog getInstance(OnActionChangedListener listener) {
        if (dialog == null) {
            dialog = new ChangeActionDialog();
        }
        actionChangedListener = listener;
        return dialog;
    }

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_details_action, null);

        initUI(rootView);
        setListeners();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressListener.showProgressBar();

                        updateAction();
                        actionChangedListener.actionChanged(action);
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                bPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
            }
        });

        return dialog;
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

                dismiss();
                break;
        }
    }

    @Subscribe
    public void receiveActionFromDb(final Action action) {
        this.oldAction = new Action(action.getId(), action.getType(), action.getDayCount(), action.getDayCount_type(),
                action.getStartDate(), action.getEndDate(), action.getDescription(), action.isDeleted());
        this.action = action;
        updateUI(action);
        initUIbShow();
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
                    bPositive.setEnabled(false);
                } else {
                    bPositive.setEnabled(true);
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
                    bPositive.setEnabled(false);
                } else {
                    bPositive.setEnabled(true);
                }
            }
        });
        checkEndTimeDialog.show(getFragmentManager(), "checkEndTimeDialog");
    }

    private void updateAction() {
        setDataToAction();

        dbController.updateActionInDb(action, oldAction.getDescription());
    }


    private void setDataToAction() {
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
    }

    private void registerEventBus() {
        eventBus = new EventBus();
        dbController = new DbController(getContext(), eventBus);
    }

    private void initUI(View view) {
        bbShowPathInMap = view.findViewById(R.id.bb_show_path_in_map);

        sAction = view.findViewById(R.id.s_action_type);

        etDescription = view.findViewById(R.id.bet_description);
        etStartDate = view.findViewById(R.id.bet_start_time);
        etEndDate = view.findViewById(R.id.bet_end_time);

        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                new String[]{Constants.EVENT_CALL_ACTION, Constants.EVENT_REST_ACTION,
                        Constants.EVENT_WALK_ACTION, Constants.EVENT_WORK_ACTION});

        sAction.setAdapter(sAdapter);
    }

    private void initUIbShow() {
        if (action.getType().equalsIgnoreCase(Constants.EVENT_WALK_ACTION)) {
            bbShowPathInMap.setVisibility(View.VISIBLE);
        } else {
            bbShowPathInMap.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        bbShowPathInMap.setOnClickListener(this);

        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);

        sAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDataToAction();
                initUIbShow();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
