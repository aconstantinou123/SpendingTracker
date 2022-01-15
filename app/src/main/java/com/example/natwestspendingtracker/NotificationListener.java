package com.example.natwestspendingtracker;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.ArrayList;

public class NotificationListener extends NotificationListenerService {

    private String TAG = "Notification Received";
    private ArrayList<String> notifications = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Called On Create");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        System.out.println("onNotificationPosted");
        prepareNotification(sbn);

//        StatusBarNotification[] array = getActiveNotifications();
//        for (int i = 0; i < array.length; i++) {
//            showLog(array[i]);
//        }
    }

    @Override
    public void onListenerConnected() {
        System.out.println("Connected to listener");
    }

    private void prepareNotification(StatusBarNotification sbn) {
        int id = sbn.getId();
        String name = sbn.getPackageName();
//        long time = sbn.getPostTime();
//        boolean clearable = sbn.isClearable();
//        boolean playing = sbn.isOngoing();
//        CharSequence text = sbn.getNotification().tickerText;
        String text = sbn.getNotification().extras.get(Notification.EXTRA_BIG_TEXT).toString();

        String notification = TAG + "\n" +
                "id: " + id + "\n" +
                "name: " + name + "\n" +
                "text: " + text;
        Intent intent = new  Intent("com.example.natwestspendingtracker");
        intent.putExtra("Notification", notification);
        sendBroadcast(intent);
    }
}