package com.maxml.timer.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.maxml.timer.R;
import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;
import com.maxml.timer.util.TimerConstatnts;

public class MyWidgetProvider extends AppWidgetProvider {

	private static TableController controller = new TableController();

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// initializing widget layout
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		controller = new TableController();

		// register for button event

		remoteViews.setOnClickPendingIntent(R.id.butCall,
				buildButtonPendingIntent(context, SliceType.CALL));

		remoteViews.setOnClickPendingIntent(R.id.butRest,
				buildButtonPendingIntent(context, SliceType.REST));

		remoteViews.setOnClickPendingIntent(R.id.butWalk,
				buildButtonPendingIntent(context, SliceType.WALK));

		remoteViews.setOnClickPendingIntent(R.id.butWork,
				buildButtonPendingIntent(context, SliceType.WORK));

		// updating view with initial data

		remoteViews.setTextViewText(R.id.title, getTitle());

		// request for widget update
		pushWidgetUpdate(context, remoteViews);
	}

	public static PendingIntent buildButtonPendingIntent(Context context,
			SliceType type) {

		if (controller.getTable().getList().isEmpty()) {
			Slice start = new Slice();
			start.setStartDate(new Date());
			start.setType(type);

			controller.getTable().addSlise(start);
			return updateWidget(context);
		}

		ArrayList<Slice> slices = controller.getTable().getList();
		slices.get(slices.size() - 1).setEndDate(new Date());

		Slice widgetstart = new Slice();
		widgetstart.setStartDate(new Date());
		widgetstart.setType(type);

		slices.add(widgetstart);
		return updateWidget(context);
	}

	private static PendingIntent updateWidget(Context context) {
		Intent intent = new Intent();
		intent.setAction(TimerConstatnts.WIDGET_UPDATE_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	@SuppressLint("SimpleDateFormat")
	private static CharSequence getTitle() {

		SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		return currentDateandTime;
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context,
				MyWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}
}
