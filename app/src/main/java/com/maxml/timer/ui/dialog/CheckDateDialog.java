package com.maxml.timer.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class CheckDateDialog extends DialogFragment {
    private static CheckDateDialog checkDateDialog;
    private static DatePickerDialog.OnDateSetListener listener;

    public static CheckDateDialog getInstance(DatePickerDialog.OnDateSetListener clickListener){
        listener = clickListener;
        if (checkDateDialog == null){
            checkDateDialog = new CheckDateDialog();
        }
        return checkDateDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        return new DatePickerDialog(getContext(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }
}
