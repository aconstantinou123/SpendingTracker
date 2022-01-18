package com.example.natwestspendingtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.natwestspendingtracker.database.PurchasedItem;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.util.ArrayList;
import java.util.Calendar;

public class CurrentDay extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<PurchasedItem> purchasedItemsCopy;
    private ListView lvItems;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private Calendar todayStart;
    private Calendar todayEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_day);

        purchasedItemsCopy = new ArrayList<>();

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
                purchasedItemsCopy.add(purchasedItem);
                itemsAdapter.add(purchasedItem.itemDescription);
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                Toast.makeText(getApplicationContext(), purchasedItemsCopy.get(pos).itemDescription, Toast.LENGTH_SHORT).show();
                mPurchasedItemViewModel.deleteByPurchasedItemId(purchasedItemsCopy.get(pos).uid);
                purchasedItemsCopy.remove(purchasedItemsCopy.get(pos));
                return true;
            }
        });
    }
}