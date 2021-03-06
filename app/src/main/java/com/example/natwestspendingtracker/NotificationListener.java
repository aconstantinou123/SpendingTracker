package com.example.natwestspendingtracker;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationListener extends NotificationListenerService {

    private String NATWEST_NOTIFICATION = "natwest";
    private String PUSH_BULLET_NOTIFICATION = "pushbullet";

    @Override
    public void onCreate() {
        super.onCreate();
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
        prepareNotification(sbn);
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
            if (sbn.getNotification().extras.containsKey(Notification.EXTRA_BIG_TEXT)) {
                long time = sbn.getPostTime();
                String text = sbn.getNotification().extras.get(Notification.EXTRA_BIG_TEXT).toString();
                Double price = extract_cost_dot(text);
                String notification = time + ":" + text + ":" + price + ":" + time;
                Intent intent = new  Intent("com.example.natwestspendingtracker");
                intent.putExtra("Notification", notification);
                sendBroadcast(intent);
            }

        }

    }

    public Double extract_cost_dot(String cost) {
        try {
            Pattern compile = Pattern.compile("(\\d+\\.\\d+)|(\\d+)");
            Matcher matcher = compile.matcher(cost);
            matcher.find();
            return Double.valueOf(matcher.group());
        } catch (Exception e) {
            System.out.println("Unable to save notification: " + e);
            return 0.00;
        }
    }
}