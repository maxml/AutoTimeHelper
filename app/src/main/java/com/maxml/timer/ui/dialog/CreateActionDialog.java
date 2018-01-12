package com.maxml.timer.ui.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.maxml.timer.R;
import com.maxml.timer.entity.Action;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;

import java.util.Calendar;

public class CreateActionDialog extends DialogFragment implements View.OnClickListener {
    private BootstrapEditText etEndTime;
    private BootstrapEditText etDate;
    private BootstrapEditText etStartTime;
    private BootstrapEditText etDescription;
    private Spinner sAction;

    private Calendar calendarStartDate = Calendar.getInstance();
    private Calendar calendarEndDate = Calendar.getInstance();

    private ArrayAdapter<String> sAdapter;

    private static CreateActionDialog createActionDialog;
    private static OnActionCreatedListener listener;

    public interface OnActionCreatedListener {
        void onActionCreated(Action action);
    }

    public static CreateActionDialog getInstance(OnActionCreatedListener actionCreatedListener) {
            listener = actionCreatedListener;

        if (createActionDialog == null) {
            createActionDialog = new CreateActionDialog();
        }
        return createActionDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_action, null);

        initUI(rootView);

        setListeners();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onActionCreated(createAction());
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final Button button = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
                if (!isSuccessfulDate()) {
                    button.setEnabled(false);
                }

                TextWatcher textChangedListener = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!isSuccessfulDate()) {
                            button.setEnabled(false);
                            Toast.makeText(getContext(), R.string.message_wrong_date, Toast.LENGTH_SHORT).show();
                        } else {
                            button.setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                };

                etStartTime.addTextChangedListener(textChangedListener);
                etEndTime.addTextChangedListener(textChangedListener);
                etDate.addTextChangedListener(textChangedListener);
            }
        });

        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bet_start_time:
                CheckTimeDialog checkStartTimeDialog = CheckTimeDialog.getInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendarStartDate.set(Calendar.HOUR_OF_DAY, i);
                        calendarStartDate.set(Calendar.MINUTE, i1);
                        etStartTime.setText(Utils.parseToTime(calendarStartDate.getTimeInMillis()));
                    }
                });
                checkStartTimeDialog.show(getFragmentManager(), "checkStartTimeDialog");
                break;
            case R.id.bet_end_time:
                CheckTimeDialog checkEndTimeDialog = CheckTimeDialog.getInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendarEndDate.set(Calendar.HOUR_OF_DAY, i);
                        calendarEndDate.set(Calendar.MINUTE, i1);
                        etEndTime.setText(Utils.parseToTime(calendarEndDate.getTimeInMillis()));
                    }
                });
                checkEndTimeDialog.show(getFragmentManager(), "checkEndTimeDialog");
                break;
            case R.id.bet_date:
                CheckDateDialog checkDateDialog = CheckDateDialog.getInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendarStartDate.set(Calendar.YEAR, i);
                        calendarStartDate.set(Calendar.MONTH, i1);
                        calendarStartDate.set(Calendar.DAY_OF_MONTH, i2);
                        calendarEndDate.set(Calendar.YEAR, i);
                        calendarEndDate.set(Calendar.MONTH, i1);
                        calendarEndDate.set(Calendar.DAY_OF_MONTH, i2);
                        etDate.setText(Utils.parseToDate(calendarStartDate.getTimeInMillis()));
                    }
                });
                checkDateDialog.show(getFragmentManager(), "checkDateDialog");
        }
    }

    private void initUI(View rootView) {
        etStartTime = (BootstrapEditText) rootView.findViewById(R.id.bet_start_time);
        etEndTime = (BootstrapEditText) rootView.findViewById(R.id.bet_end_time);
        etDate = (BootstrapEditText) rootView.findViewById(R.id.bet_date);
        etDescription = (BootstrapEditText) rootView.findViewById(R.id.bet_description);
        sAction = (Spinner) rootView.findViewById(R.id.s_action_type);

        sAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                new String[]{Constants.EVENT_CALL_ACTION, Constants.EVENT_REST_ACTION,
                        Constants.EVENT_WALK_ACTION, Constants.EVENT_WORK_ACTION});

        sAction.setAdapter(sAdapter);

        etStartTime.setText(Utils.parseToTime(System.currentTimeMillis()));
        etEndTime.setText(Utils.parseToTime(System.currentTimeMillis()));
        etDate.setText(Utils.parseToDate(System.currentTimeMillis()));
    }

    private Action createAction() {
        Action action = new Action();

        if (sAction.getSelectedItemPosition() == 0) {
            action.setType(Constants.EVENT_CALL_ACTION);
        } else if (sAction.getSelectedItemPosition() == 1) {
            action.setType(Constants.EVENT_REST_ACTION);
        } else if (sAction.getSelectedItemPosition() == 2) {
            action.setType(Constants.EVENT_WALK_ACTION);
        } else if (sAction.getSelectedItemPosition() == 3) {
            action.setType(Constants.EVENT_WORK_ACTION);
        }
        action.setDayCount(Utils.getDayCount(calendarStartDate.getTime()));
        action.setDescription(etDescription.getText().toString());
        action.setStartDate(calendarStartDate.getTime());
        action.setEndDate(calendarEndDate.getTime());

        return action;
    }

    private void setListeners() {
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
        etDate.setOnClickListener(this);
    }

    private boolean isSuccessfulDate() {
        return calendarStartDate.getTimeInMillis() < calendarEndDate.getTimeInMillis();
    }
}
