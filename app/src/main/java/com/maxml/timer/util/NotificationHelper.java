package com.maxml.timer.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.maxml.timer.R;
import com.maxml.timer.ui.activity.LoginActivity;
import com.maxml.timer.ui.activity.SplashActivity;

public class NotificationHelper {

    public static Notification getDefaultNotification(Context context) {
        NotificationCompat.Builder nb = getNotificationBuilder(context, context.getString(R.string.notification_text));
        return nb.build();
    }

    public static void showMessageNotification(Context context, String message) {
        // intent that is started when the notification is clicked
        Intent notificationIntent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nb = getNotificationBuilder(context,message)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1, 1, 1});

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Constants.NOTIFICATION_MESSAGE_ID, nb.build());
    }

    public static void updateNotification(Context context, String message) {
        Context appContext = context.getApplicationContext();
        NotificationCompat.Builder nb = getNotificationBuilder(appContext,message);
        NotificationManager manager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Constants.NOTIFICATION_APP_ID, nb.build());
    }

    private static NotificationCompat.Builder getNotificationBuilder(Context context, String text) {
        // intent that is started when the notification is clicked
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        NotificationCompat.Builder nb;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nb = new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID);
        } else {
            nb = new NotificationCompat.Builder(context);
        }
        nb.setContentIntent(pendingIntent)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        return nb;
    }
}
