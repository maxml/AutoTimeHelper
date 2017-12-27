package com.maxml.timer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.maxml.timer.R;
import com.maxml.timer.controllers.Controller;
import com.maxml.timer.controllers.ReceiverService;
import com.maxml.timer.entity.Events;
import com.maxml.timer.ui.activity.LoginActivity;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.Utils;

import org.greenrobot.eventbus.EventBus;

public class AutoTimeWidget extends AppWidgetProvider {
    private EventBus eventBus;
    private Controller controller;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (!Utils.isServiceRunning(context, ReceiverService.class)) {
            if (controller == null) {
                eventBus = new EventBus();
                controller = new Controller(context,eventBus);
                controller.registerEventBus(eventBus);
                eventBus.post(new Events.WidgetEvent(Constants.EVENT_SET_WIDGET_EVENT_BUS));
            }
        }
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();

        // if action null return
        if (action == null) {
            return;
        }

        // if action widget
        if (action.equals(Constants.ACTION_WIDGET_RECEIVER)) {
            String message = intent.getStringExtra(Constants.WIDGET_EXTRA);
            if (message == null) {
                context.startActivity(new Intent(context, LoginActivity.class));
                return;
            }
            switch (message) {
                case Constants.EVENT_WORK_ACTION:
                    eventBus.post(new Events.WidgetEvent(Constants.EVENT_WORK_ACTION));
                    break;

                case Constants.EVENT_WALK_ACTION:
                    eventBus.post(new Events.WidgetEvent(Constants.EVENT_WALK_ACTION));
                    break;

                case Constants.EVENT_REST_ACTION:
                    eventBus.post(new Events.WidgetEvent(Constants.EVENT_REST_ACTION));
                    break;

                case Constants.EVENT_CALL_ACTION:
                    eventBus.post(new Events.WidgetEvent(Constants.EVENT_CALL_ACTION));
                    break;
            }
        }

        // update action status
        if (action.equals(Constants.WIDGET_UPDATE_ACTION_STATUS)) {
            updateActionStatus(context, intent);
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = getRemoteView(context);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void updateActionStatus(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, AutoTimeWidget.class);
        int[] appWidgetsIds = appWidgetManager.getAppWidgetIds(thisWidget);
        RemoteViews views = getRemoteView(context);
        String title = intent.getStringExtra(Constants.WIDGET_EXTRA);
        if (title == null || title.equals("")) {
            title = context.getString(R.string.widget_default_text);
        }
        views.setTextViewText(R.id.title, title);
        appWidgetManager.updateAppWidget(appWidgetsIds, views);
    }

    private RemoteViews getRemoteView(Context context) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // create intent for button WORK
        Intent activeWork = new Intent(context, AutoTimeWidget.class);
        activeWork.setAction(Constants.ACTION_WIDGET_RECEIVER);
        activeWork.putExtra(Constants.WIDGET_EXTRA, Constants.EVENT_WORK_ACTION);
        PendingIntent pendingWork = PendingIntent.getBroadcast(context, 1, activeWork,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // create intent for button REST
        Intent activeRest = new Intent(context, AutoTimeWidget.class);
        activeRest.setAction(Constants.ACTION_WIDGET_RECEIVER);
        activeRest.putExtra(Constants.WIDGET_EXTRA, Constants.EVENT_REST_ACTION);
        PendingIntent pendingRest = PendingIntent.getBroadcast(context, 2, activeRest,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // create intent for button WALK
        Intent activeWalk = new Intent(context, AutoTimeWidget.class);
        activeWalk.setAction(Constants.ACTION_WIDGET_RECEIVER);
        activeWalk.putExtra(Constants.WIDGET_EXTRA, Constants.EVENT_WALK_ACTION);
        PendingIntent pendingWalk = PendingIntent.getBroadcast(context, 3, activeWalk,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // create intent for button CALL
        Intent activeCall = new Intent(context, AutoTimeWidget.class);
        activeCall.setAction(Constants.ACTION_WIDGET_RECEIVER);
        activeCall.putExtra(Constants.WIDGET_EXTRA, Constants.EVENT_CALL_ACTION);
        PendingIntent pendingCall = PendingIntent.getBroadcast(context, 4, activeCall,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // start MainActivity
        Intent activeActivity = new Intent(context, LoginActivity.class);
        PendingIntent pendingActivity = PendingIntent.getActivity(context, 0, activeActivity, 0);

        // register actions
        views.setOnClickPendingIntent(R.id.butWork, pendingWork);
        views.setOnClickPendingIntent(R.id.butWalk, pendingWalk);
        views.setOnClickPendingIntent(R.id.butRest, pendingRest);
        views.setOnClickPendingIntent(R.id.butCall, pendingCall);
        views.setOnClickPendingIntent(R.id.root, pendingActivity);
        return views;
    }
}