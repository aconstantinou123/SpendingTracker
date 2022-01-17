package com.example.natwestspendingtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.natwestspendingtracker.database.PurchasedItem;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private NotificationReceiver notificationReceiver;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private Calendar todayStart;
    private Calendar todayEnd;
    private int currentWeek;
    private Double currentDayTotal;
    private Double currentWeekTotal;
    private Button currentDayButton;
    private Button currentWeekButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName())) {        //ask for permission
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }

        currentDayButton = (Button) findViewById(R.id.currentDayButton);
        currentWeekButton = (Button) findViewById(R.id.currentWeekButton);

        todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY,0);
        todayStart.set(Calendar.MINUTE,0);
        todayStart.set(Calendar.SECOND,0);

        todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);

        currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

        notificationReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.natwestspendingtracker");
        registerReceiver(notificationReceiver,intentFilter);

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

//        mPurchasedItemViewModel.deleteAll();

        mPurchasedItemViewModel.getPurchasedItemsCurrentDayTotal(
                todayStart.getTime().getTime(),
                todayEnd.getTime().getTime()
        ).observe(this, total -> {
            currentDayTotal = 0.00;
            if(total != null) {
                currentDayTotal = total;
            }
            String buttonText = "Current Day"
                    + System.getProperty("line.separator")
                    + "£"
                    +  String.format("%.2f", currentDayTotal);
            currentDayButton.setText(buttonText);
            if(currentDayTotal >= 50.00){
                currentDayButton.setBackgroundColor(Color.parseColor("#FF0000"));
            } else if(currentDayTotal >= 20.00) {
                currentDayButton.setBackgroundColor(Color.parseColor("#FFBF00"));
            } else {
                currentDayButton.setBackgroundColor(Color.parseColor("#00FF00"));
            }
        });

        mPurchasedItemViewModel.getPurchasedItemsCurrentWeekTotal(
                currentWeek
        ).observe(this, total -> {
            System.out.println("total: " + total);
            currentWeekTotal = total;
            String buttonText = "Current Week"
                    + System.getProperty("line.separator")
                    + "£"
                    + String.format("%.2f", currentWeekTotal);
            currentWeekButton.setText(buttonText);
            if(currentWeekTotal >= 200.00){
                currentWeekButton.setBackgroundColor(Color.parseColor("#FF0000"));
            } else if(currentWeekTotal >= 100.00) {
                currentWeekButton.setBackgroundColor(Color.parseColor("#FFBF00"));
            } else {
                currentWeekButton.setBackgroundColor(Color.parseColor("#00FF00"));
            }
        });
    }

    public void onCurrentDayButtonClicked(View button){
        Intent intent = new Intent(this, CurrentDay.class);
        intent.putExtra("dayStart", todayStart);
        intent.putExtra("dayEnd", todayEnd);
        startActivity(intent);
    }

    public void onCurrentWeekButtonClicked(View button){
        Intent intent = new Intent(this, WeekDays.class);
        intent.putExtra("currentWeek", currentWeek);
        startActivity(intent);
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