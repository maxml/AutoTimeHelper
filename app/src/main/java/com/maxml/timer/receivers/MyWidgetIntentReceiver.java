package com.maxml.timer.receivers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maxml.timer.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyWidgetIntentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Constants.WIDGET_UPDATE_ACTION)) {
			updateWidgetPictureAndButtonListener(context);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
//		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
//				R.layout.widget_layout);
//
//		// updating view
//		remoteViews.setTextViewText(R.id.title, getTitle());
//
//		// re-registering for click listener
//		remoteViews.setOnClickPendingIntent(R.id.butCall, AutoTimeWidget
//				.buildButtonPendingIntent(context, SliceType.CALL));
//
//		remoteViews.setOnClickPendingIntent(R.id.butRest, AutoTimeWidget
//				.buildButtonPendingIntent(context, SliceType.REST));
//
//		remoteViews.setOnClickPendingIntent(R.id.butWalk, AutoTimeWidget
//				.buildButtonPendingIntent(context, SliceType.WALK));
//
//		remoteViews.setOnClickPendingIntent(R.id.butWork, AutoTimeWidget
//				.buildButtonPendingIntent(context, SliceType.WORK));
//
//		AutoTimeWidget.pushWidgetUpdate(context.getApplicationContext(),
//				remoteViews);
	}

	@SuppressLint("SimpleDateFormat")
	private String getTitle() {
		// return "";
		SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
		return sdf.format(new Date());

	}
}
