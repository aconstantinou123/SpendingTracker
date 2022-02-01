package com.example.natwestspendingtracker.activities;

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

import com.example.natwestspendingtracker.R;
import com.example.natwestspendingtracker.database.PurchasedItem;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;
import com.example.natwestspendingtracker.enums.AppColor;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private NotificationReceiver notificationReceiver;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private Calendar todayStart;
    private Calendar todayEnd;
//    private Calendar timerStart;
    private int currentWeek;
    private int currentYear;
    private Button currentDayButton;
    private Button currentWeekButton;
    private Timer timer;
    private TimerTask dayTotalTask;
    private TimerTask weekTotalTask;
    private long hourInMilliseconds;

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

//        timerStart = Calendar.getInstance();
//        timerStart.set(Calendar.MINUTE, 0);
//        timerStart.set(Calendar.SECOND, 1);
//        timerStart.set(Calendar.MILLISECOND, 0);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/London"));
        ZonedDateTime nextRun = now.toLocalDate().atStartOfDay(now.getZone());
        if(now.compareTo(nextRun) > 0)
            nextRun = nextRun.plusDays(1);

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds() * 1000;
        System.out.println("initial delay: " + initialDelay);


        currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        notificationReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.natwestspendingtracker");
        registerReceiver(notificationReceiver,intentFilter);

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

        hourInMilliseconds = 1000 * 60 * 60;

        timer = new Timer();
        dayTotalTask = new TimerTask() {
                    @Override
                    public void run() {
                        Double total =  mPurchasedItemViewModel.getPurchasedItemsCurrentDayTotalStatic(
                                todayStart.getTime().getTime(),
                                todayEnd.getTime().getTime()
                        );
                        System.out.println("Called get day");
                        setCurrentDayButton(total);
                    }
                };

        weekTotalTask = new TimerTask() {
            @Override
            public void run() {
                Double total = mPurchasedItemViewModel.getPurchasedItemsCurrentWeekTotalStatic(
                        currentWeek
                );
                System.out.println("Called get week");
                setCurrentWeekButton(total);
            }
        };

        timer.scheduleAtFixedRate(dayTotalTask, initialDelay, hourInMilliseconds);
        timer.scheduleAtFixedRate(weekTotalTask, initialDelay, hourInMilliseconds);

        mPurchasedItemViewModel.getPurchasedItemsCurrentDayTotal(
                todayStart.getTime().getTime(),
                todayEnd.getTime().getTime()
        ).observe(this, total -> {
            setCurrentDayButton(total);
        });

        mPurchasedItemViewModel.getPurchasedItemsCurrentWeekTotal(
                currentWeek
        ).observe(this, total -> {
            setCurrentWeekButton(total);
        });
    }

    public void onCurrentDayButtonClicked(View button){
        Intent intent = new Intent(this, DayActivity.class);
        intent.putExtra("dayStart", todayStart);
        intent.putExtra("dayEnd", todayEnd);
        startActivity(intent);
    }

    public void onCurrentWeekButtonClicked(View button){
        Intent intent = new Intent(this, WeekDaysActivity.class);
        intent.putExtra("week", currentWeek);
        intent.putExtra("year", currentYear);
        startActivity(intent);
    }

    public void onWeeklyButtonClicked(View button){
        Intent intent = new Intent(this, WeeksActivity.class);
        startActivity(intent);
    }

    public void onMonthlyButtonClicked(View button){
        Intent intent = new Intent(this, MonthsActivity.class);
        startActivity(intent);
    }

    public void setCurrentDayButton(Double total) {
        Double currentDayTotal = 0.00;
        if(total != null) {
            currentDayTotal = total;
        }
        String buttonText = "Current Day"
                + System.getProperty("line.separator")
                + "£"
                +  String.format("%.2f", currentDayTotal);
        currentDayButton.setText(buttonText);
        if(currentDayTotal >= 50.00){
            currentDayButton.setBackgroundColor(Color.parseColor(AppColor.RED.getHex()));
        } else if(currentDayTotal >= 20.00) {
            currentDayButton.setBackgroundColor(Color.parseColor(AppColor.YELLOW.getHex()));
        } else {
            currentDayButton.setBackgroundColor(Color.parseColor(AppColor.GREEN.getHex()));
        }
    }

    public void setCurrentWeekButton(Double total) {
        Double currentWeekTotal = 0.00;
        if(total != null) {
            currentWeekTotal = total;
        }
        String buttonText = "Current Week"
                + System.getProperty("line.separator")
                + "£"
                + String.format("%.2f", currentWeekTotal);
        currentWeekButton.setText(buttonText);
        if(currentWeekTotal >= 200.00){
            currentWeekButton.setBackgroundColor(Color.parseColor(AppColor.RED.getHex()));
        } else if(currentWeekTotal >= 100.00) {
            currentWeekButton.setBackgroundColor(Color.parseColor(AppColor.YELLOW.getHex()));
        } else {
            currentWeekButton.setBackgroundColor(Color.parseColor(AppColor.GREEN.getHex()));
        }
    }

    public class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String receivedNotification = intent.getStringExtra("Notification");
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
            } catch (Exception e) {
                System.out.println("Unable to save notification: " + e);
            }
        }

    }
}