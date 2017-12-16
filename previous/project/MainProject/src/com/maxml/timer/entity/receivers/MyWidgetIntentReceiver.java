package com.maxml.timer.entity.receivers;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.maxml.timer.R;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.util.TimerConstatnts;
import com.maxml.timer.widget.MyWidgetProvider;

public class MyWidgetIntentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(TimerConstatnts.WIDGET_UPDATE_ACTION)) {
			updateWidgetPictureAndButtonListener(context);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);

		// updating view
		remoteViews.setTextViewText(R.id.title, getTitle());

		// re-registering for click listener
		remoteViews.setOnClickPendingIntent(R.id.butCall, MyWidgetProvider
				.buildButtonPendingIntent(context, SliceType.CALL));

		remoteViews.setOnClickPendingIntent(R.id.butRest, MyWidgetProvider
				.buildButtonPendingIntent(context, SliceType.REST));

		remoteViews.setOnClickPendingIntent(R.id.butWalk, MyWidgetProvider
				.buildButtonPendingIntent(context, SliceType.WALK));

		remoteViews.setOnClickPendingIntent(R.id.butWork, MyWidgetProvider
				.buildButtonPendingIntent(context, SliceType.WORK));

		MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews);
	}

	@SuppressLint("SimpleDateFormat")
	private String getTitle() {
		// return "";
		SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		return currentDateandTime;

	}
}
