package com.example.natwestspendingtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.example.natwestspendingtracker.database.PurchasedItem;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private NotificationReceiver notificationReceiver;
    private PurchasedItemViewModel mPurchasedItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.natwestspendingtracker");
        registerReceiver(notificationReceiver,intentFilter);

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

//        mPurchasedItemViewModel.deleteAll();
    }

    public void onMonthlyButtonClicked(View button){
        Intent intent = new Intent(this, MonthsActivity.class);
        startActivity(intent);
    }

    public class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedNotification = intent.getStringExtra("Notification");
            System.out.println("Notification received " + receivedNotification);
            String[] notificationArray = receivedNotification.split(":");
            long uid = Long.parseLong(notificationArray[0]);
            String text = notificationArray[1];
            double price = Double.parseDouble(notificationArray[2]);
            Date time = new Date(Long.parseLong(notificationArray[3]));
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            PurchasedItem purchasedItem = new PurchasedItem(uid, text, price, time, month, year, week);
            mPurchasedItemViewModel.insert(purchasedItem);
        }

    }
}