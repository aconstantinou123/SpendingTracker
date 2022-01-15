package com.example.natwestspendingtracker;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationListener extends NotificationListenerService {

    private String TAG = "Notification Received";
    private String NATWEST_NOTIFICATION = "natwest";
    private String PUSH_BULLET_NOTIFICATION = "pushbullet";
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
        if(
                sbn.getPackageName().contains(NATWEST_NOTIFICATION) ||
                sbn.getPackageName().contains(PUSH_BULLET_NOTIFICATION)
        ) {
            int id = sbn.getId();
            String name = sbn.getPackageName();
            long time = sbn.getPostTime();
            String text = sbn.getNotification().extras.get(Notification.EXTRA_BIG_TEXT).toString();
            String price = extract_cost_dot(text).toString();
            String notification = TAG + "\n" +
                    "id: " + id + "\n" +
                    "name: " + name + "\n" +
                    "text: " + text + "\n" +
                    "price: " + price + "\n" +
                    "time: " + time;
            Intent intent = new  Intent("com.example.natwestspendingtracker");
            intent.putExtra("Notification", notification);
            sendBroadcast(intent);
        }

    }

    public   Double extract_cost_dot(String cost) {
        Pattern compile = Pattern.compile("(\\d+\\.\\d+)|(\\d+)");
        Matcher matcher = compile.matcher(cost);
        matcher.find();
        return Double.valueOf(matcher.group());
    }
}