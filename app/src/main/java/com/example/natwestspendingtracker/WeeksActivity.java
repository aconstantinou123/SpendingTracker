package com.example.natwestspendingtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.natwestspendingtracker.database.PurchasedItemViewModel;
import com.example.natwestspendingtracker.database.WeekYearTotalTuple;

import java.util.ArrayList;

public class WeeksActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeks);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        actionBar = getSupportActionBar();
        actionBar.setTitle("Weeks");

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                String fullDate = (String) lvItems.getItemAtPosition(position);
                Double total = Double.parseDouble(fullDate.split("£")[1]);
                tv.setTextColor(Color.BLACK);
                if(total >= 200.00){
                    tv.setBackgroundColor(Color.parseColor("#FF0000"));
                } else if(total >= 100.00) {
                    tv.setBackgroundColor(Color.parseColor("#FFBF00"));
                } else {
                    tv.setBackgroundColor(Color.parseColor("#00FF00"));
                }
                return view;
            }
        };;
        lvItems.setAdapter(itemsAdapter);
        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

        mPurchasedItemViewModel.getPurchasedItemTotalByWeekYear().observe(this, weekYearTotalTuples -> {
            itemsAdapter.clear();
            for(WeekYearTotalTuple weekYearTotalTuple : weekYearTotalTuples) {
                itemsAdapter.add("Week " +
                        weekYearTotalTuple.week +
                        " " +
                        weekYearTotalTuple.year +
                        " - £" +
                        String.format("%.2f", weekYearTotalTuple.total)
                    );
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), WeekDaysActivity.class);
                String monthYear = (String) lvItems.getItemAtPosition(position);
                Integer week = Integer.parseInt(monthYear.split(" ")[1]);
                Integer year = Integer.parseInt(monthYear.split(" ")[2]);
                intent.putExtra("week", week);
                intent.putExtra("year", year);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}