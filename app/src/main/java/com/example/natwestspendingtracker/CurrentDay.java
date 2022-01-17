package com.example.natwestspendingtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.natwestspendingtracker.database.PurchasedItem;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.util.ArrayList;
import java.util.Calendar;

public class CurrentDay extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private Calendar todayStart;
    private Calendar todayEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_day);

        Intent intent = getIntent();
        todayStart = (Calendar) intent.getSerializableExtra("dayStart");
        todayEnd = (Calendar) intent.getSerializableExtra( "dayEnd");

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

        mPurchasedItemViewModel.getPurchasedItemsCurrentDay(
                todayStart.getTime().getTime(),
                todayEnd.getTime().getTime()
        ).observe(this, purchasedItems -> {
            itemsAdapter.clear();
            // Update the cached copy of the purchased items in the adapter.
            for(PurchasedItem purchasedItem : purchasedItems) {
                itemsAdapter.add(purchasedItem.itemDescription);
            }
        });
    }
}