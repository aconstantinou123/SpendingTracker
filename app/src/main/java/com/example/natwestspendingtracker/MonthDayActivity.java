package com.example.natwestspendingtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.natwestspendingtracker.database.PurchasedItem;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthDayActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private String month;
    private PurchasedItemViewModel mPurchasedItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        month = (String) intent.getSerializableExtra("month");

        Date date = null;
        try {
            date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int monthNum = cal.get(Calendar.MONTH);

        System.out.println("Month: " + monthNum);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_day);

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

        mPurchasedItemViewModel.getPurchasedItemsByMonth(monthNum).observe(this, purchasedItems -> {
            itemsAdapter.clear();
            // Update the cached copy of the purchased items in the adapter.
            for(PurchasedItem purchasedItem : purchasedItems) {
                System.out.println("time: " + purchasedItem.date.getTime());
                itemsAdapter.add(purchasedItem.toString());
            }
        });
    }
}