package com.maxml.timer.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.maxml.timer.R;

public class DialogFactory {

	public static void showErrorDialog(Context context, String title, String description){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
				.setMessage(description)
				.setIcon(R.drawable.ic_launcher)
				.setPositiveButton("��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.create().show();
	}
	
}
