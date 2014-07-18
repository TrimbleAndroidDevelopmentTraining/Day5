package com.trimble.servicesapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

public class SimpleStartedBoundService extends Service {

    public static final String PARAM_IN_MSG = "imsg";

    // Threads
    private void longRunningOperation() {
        SystemClock.sleep(5000); // 5 seconds
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Threads
        final String msg = intent.getStringExtra(PARAM_IN_MSG);
        new Thread(new Runnable() {
            public void run() {
                longRunningOperation();

                //sendResultsUsingActivityIntent(msg);

                // Notifications
                sendResultsUsingNotification(msg);
                sendResultsUsingBroadcastIntent(msg);

                stopSelf();
            }
        }).start();

        return START_NOT_STICKY;
    }

    private void sendResultsUsingActivityIntent(String resultMsg) {
        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra(ResultActivity.PARAM_OUT_MSG, resultMsg);

        // Because we are staring an Activity from outside an existing Activity
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(resultIntent);
    }

    // LocalBroadcast
    private void sendResultsUsingBroadcastIntent(String resultMsg) {
        Intent resultIntent = new Intent(LocalServiceActivity.BROADCAST_ACTION);
        resultIntent.putExtra(LocalServiceActivity.PARAM_OUT_MSG, resultMsg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    // Notifications
    private void sendResultsUsingNotification(String resultMsg) {
        NotificationCompat.Builder  builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Notification");
        builder.setContentText(resultMsg);
        builder.setTicker("Long Running Operation Complete!!!");
        builder.setSmallIcon(R.drawable.ic_launcher);

        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra(ResultActivity.PARAM_OUT_MSG, resultMsg);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager  notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(111, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
