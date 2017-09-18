package com.maxml.timer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.maxml.timer.R;
import com.maxml.timer.controllers.TableControllerService;
import com.maxml.timer.util.SliceType;
import com.maxml.timer.util.TimerConstatnts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyWidgetProvider extends AppWidgetProvider {

    private static TableControllerService controller;
//    private static TableControllerService controller = new TableControllerService();

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // initializing widget layout
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),
                R.layout.widget_layout);

        controller = new TableControllerService();

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

    public static PendingIntent buildButtonPendingIntent(Context context, SliceType type) {

/*
        if (controller.getTable().getList().isEmpty()) {
            Slice start = new Slice();
            start.setStartDate(new Date());
            start.setType(type);

            controller.getTable().addSlice(start);
            return updateWidget(context);
        }

        ArrayList<Slice> slices = controller.getTable().getList();
        slices.get(slices.size() - 1).setEndDate(new Date());

        Slice widgetstart = new Slice();
        widgetstart.setStartDate(new Date());
        widgetstart.setType(type);

        slices.add(widgetstart);
*/
        return updateWidget(context);
    }

    private static PendingIntent updateWidget(Context context) {
        Intent intent = new Intent();
        intent.setAction(TimerConstatnts.WIDGET_UPDATE_ACTION);
        return PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static CharSequence getTitle() {

        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context,
                MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}