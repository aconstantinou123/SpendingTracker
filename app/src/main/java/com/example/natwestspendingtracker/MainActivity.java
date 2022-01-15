package com.example.natwestspendingtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.natwestspendingtracker.database.PurchasedItem;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private NotificationReceiver notificationReceiver;
    private PurchasedItemViewModel mPurchasedItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName())) {        //ask for permission
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }

        notificationReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.natwestspendingtracker");
        registerReceiver(notificationReceiver,intentFilter);

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

//        mPurchasedItemViewModel.deleteAll();

        mPurchasedItemViewModel.getAllPurchasedItems().observe(this, purchasedItems -> {
            itemsAdapter.clear();
            // Update the cached copy of the purchased items in the adapter.
            for(PurchasedItem purchasedItem : purchasedItems) {
                itemsAdapter.add(purchasedItem.toString());
            }
        });
    }

    public class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedNotification = intent.getStringExtra("Notification");
            System.out.println("Notification received " + receivedNotification);
            String[] notificationArray = receivedNotification.split(":");
            int uid = Integer.parseInt(notificationArray[0]);
            String text = notificationArray[1];
            double price = Double.parseDouble(notificationArray[2]);
            long time = Long.parseLong(notificationArray[3]);
            PurchasedItem purchasedItem = new PurchasedItem(uid, text, price, time);
            mPurchasedItemViewModel.insert(purchasedItem);
        }

    }
}