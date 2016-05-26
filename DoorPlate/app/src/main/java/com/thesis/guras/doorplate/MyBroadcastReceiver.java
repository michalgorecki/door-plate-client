package com.thesis.guras.doorplate;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by guras on 26.05.16.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    private String DEBUG_TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(DEBUG_TAG,"onReceive()");
        String message = intent.getStringExtra("Message");
        String title = intent.getStringExtra("Title");
        Log.d(DEBUG_TAG,"Starting to build notification");
        Notification.Builder notificationBuilder  = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(new long [] {1000, 1000, 1000,1000, 1000})
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d(DEBUG_TAG,"Building notification");
        notificationManager.notify(001, notificationBuilder.build());
        Log.d(DEBUG_TAG,"onReceive()");
    }
}
