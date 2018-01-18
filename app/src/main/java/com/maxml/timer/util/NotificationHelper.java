package com.maxml.timer.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.maxml.timer.R;
import com.maxml.timer.ui.activity.LoginActivity;

public class NotificationHelper {

    private static NotificationCompat.Builder getNotificationBuilder(Context context) {
        return new NotificationCompat.Builder(context)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon);
    }

    public static Notification getDefaultNotification(Context context) {
        // intent that is started when the notification is clicked
        Intent notificationIntent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        NotificationCompat.Builder nb = getNotificationBuilder(context)
                .setContentIntent(pendingIntent)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        android.R.mipmap.sym_def_app_icon));

        return nb.build();
    }

    public static void showMessageNotification(Context context, String message) {
        // intent that is started when the notification is clicked
        Intent notificationIntent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nb = getNotificationBuilder(context)
                .setContentIntent(pendingIntent)
                .setContentTitle(context.getString(R.string.notification_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] {1, 1, 1})
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        android.R.mipmap.sym_def_app_icon));

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Constants.NOTIFICATION_MESSAGE_ID, nb.build());
    }

    public static void updateNotification(Context context, String message) {
        Context appContext = context.getApplicationContext();

        Intent notificationIntent = new Intent(appContext, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 0,
                notificationIntent, 0);

        NotificationCompat.Builder nb = getNotificationBuilder(appContext)
                .setContentIntent(pendingIntent)
                .setContentTitle(appContext.getString(R.string.notification_title))
                .setContentText(message)
                .setLargeIcon(BitmapFactory.decodeResource(appContext.getResources(),
                        android.R.mipmap.sym_def_app_icon));

        NotificationManager manager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Constants.NOTIFICATION_APP_ID, nb.build());
    }
}
