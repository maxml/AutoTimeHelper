package com.maxml.timer.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.maxml.timer.R;

public class OptionActionDialog extends DialogFragment {
    private static String idEvent;
    private static OnDialogItemClickListener clickListener;

    public interface OnDialogItemClickListener {
        void onDialogItemClick(int position, String idEvent);
    }

    public static OptionActionDialog getInstance(OnDialogItemClickListener listener, String id) {
        idEvent = id;
        clickListener = listener;
        return new OptionActionDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.options_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clickListener.onDialogItemClick(i, idEvent);
            }
        });

        return builder.create();
    }
}
