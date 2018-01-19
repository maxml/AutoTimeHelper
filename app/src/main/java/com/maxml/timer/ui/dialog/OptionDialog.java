package com.maxml.timer.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class OptionDialog extends DialogFragment {
    private static int resourceItems;

    private static OnDialogItemClickListener clickListener;

    public interface OnDialogItemClickListener {
        void onDialogItemClick(int position);
    }

    public static OptionDialog getInstance(OnDialogItemClickListener listener, int resource) {
        resourceItems = resource;
        clickListener = listener;
        return new OptionDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(getActivity())
                .setItems(resourceItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickListener.onDialogItemClick(i);
                    }
                });

        return builder.create();
    }
}
