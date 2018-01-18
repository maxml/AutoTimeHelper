package com.maxml.timer.ui.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class CheckTimeDialog extends DialogFragment {
    private static CheckTimeDialog checkTimeDialog;
    private static TimePickerDialog.OnTimeSetListener listener;

    public static CheckTimeDialog getInstance(TimePickerDialog.OnTimeSetListener clickListener) {
        listener = clickListener;
        if (checkTimeDialog == null) {
            checkTimeDialog = new CheckTimeDialog();
        }
        return checkTimeDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        return new TimePickerDialog(getContext(), listener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }
}
