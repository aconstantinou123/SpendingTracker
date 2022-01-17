package com.example.natwestspendingtracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class MonthsActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private PurchasedItemViewModel mPurchasedItemViewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months);

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

//        mPurchasedItemViewModel.deleteAll();

        mPurchasedItemViewModel.getAllPurchasedItemDates().observe(this, dates -> {
            itemsAdapter.clear();
            // Update the cached copy of the purchased items in the adapter.
            Set<Integer> months = dates.stream().map(date -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal.get(Calendar.MONTH);
            }).collect(Collectors.toSet());
            for(Integer month : months) {
                String monthName = Month.of(month + 1).getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
                itemsAdapter.add(monthName);
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), MonthDayActivity.class);
                String month = (String) lvItems.getItemAtPosition(position);
                intent.putExtra("month", month);
                startActivity(intent);
            }
        });
    }
}