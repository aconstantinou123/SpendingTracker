package com.example.natwestspendingtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.natwestspendingtracker.database.PurchasedItem;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class DayActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<PurchasedItem> purchasedItemsCopy;
    private ListView lvItems;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private Calendar dayStart;
    private Calendar dayEnd;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        purchasedItemsCopy = new ArrayList<>();

        Intent intent = getIntent();
        dayStart = (Calendar) intent.getSerializableExtra("dayStart");
        dayEnd = (Calendar) intent.getSerializableExtra( "dayEnd");

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

        mPurchasedItemViewModel.getPurchasedItemsCurrentDay(
                dayStart.getTime().getTime(),
                dayEnd.getTime().getTime()
        ).observe(this, purchasedItems -> {
            purchasedItemsCopy.clear();
            itemsAdapter.clear();
            // Update the cached copy of the purchased items in the adapter.
            for(PurchasedItem purchasedItem : purchasedItems) {
                purchasedItemsCopy.add(purchasedItem);
                itemsAdapter.add(purchasedItem.itemDescription);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newItemIntent = new Intent(getApplicationContext(), NewActivity.class);
                Calendar now = Calendar.getInstance();
                dayStart.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
                dayStart.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
                newItemIntent.putExtra("date", dayStart);
                startActivity(newItemIntent);
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Intent intentDay = new Intent(getApplicationContext(), EditActivity.class);
                intentDay.putExtra("itemToEdit",  purchasedItemsCopy.get(pos));
                startActivity(intentDay);
            }
        });
    }
}