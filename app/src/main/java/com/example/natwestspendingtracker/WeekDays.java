package com.example.natwestspendingtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;

public class WeekDays extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private int currentWeek;
    private SimpleDateFormat format;
    private Set<Integer> days;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_days);

        intent = getIntent();
        currentWeek = (int) intent.getSerializableExtra("currentWeek");

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                String fullDate = (String) lvItems.getItemAtPosition(position);
                Double total = Double.parseDouble(fullDate.split("£")[1]);
                if(total >= 50.00){
                    tv.setBackgroundColor(Color.parseColor("#FF0000"));
                } else if(total >= 20.00) {
                    tv.setBackgroundColor(Color.parseColor("#FFBF00"));
                } else {
                    tv.setBackgroundColor(Color.parseColor("#00FF00"));
                }

                // Generate ListView Item using TextView
                return view;
            }
        };

        lvItems.setAdapter(itemsAdapter);

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

        format = new SimpleDateFormat("dd MMMM yyyy");

        mPurchasedItemViewModel.getPurchasedItemsCurrentWeekDates(currentWeek).observe(this, dates -> {
            itemsAdapter.clear();
            days = dates.stream().map(date -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal.get(Calendar.DATE);
            }).collect(Collectors.toSet());
            for(Integer day: days) {
                Calendar dayStart= Calendar.getInstance();
                dayStart.set(Calendar.DATE, day);
                dayStart.set(Calendar.HOUR_OF_DAY,0);
                dayStart.set(Calendar.MINUTE,0);
                dayStart.set(Calendar.SECOND,0);

                Calendar dayEnd = Calendar.getInstance();
                dayEnd.set(Calendar.DATE, day);
                dayEnd.set(Calendar.HOUR_OF_DAY, 23);
                dayEnd.set(Calendar.MINUTE, 59);
                dayEnd.set(Calendar.SECOND, 59);
                mPurchasedItemViewModel.getPurchasedItemsCurrentDayTotal(
                        dayStart.getTime().getTime(),
                        dayEnd.getTime().getTime()
                ).observe(this, total -> {
                    String strDate = format.format(dayStart.getTime());
                    itemsAdapter.add(
                            strDate
                                    + " £"
                                    + String.format("%.2f", total)
                    );
                });
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), CurrentDay.class);
                String fullDate = (String) lvItems.getItemAtPosition(position);

                Integer date = Integer.parseInt(fullDate.split(" ")[0]);
                Calendar dayStart= Calendar.getInstance();
                dayStart.set(Calendar.DATE, date);
                dayStart.set(Calendar.HOUR_OF_DAY,0);
                dayStart.set(Calendar.MINUTE,0);
                dayStart.set(Calendar.SECOND,0);
                intent.putExtra("dayStart", dayStart);

                Calendar dayEnd = Calendar.getInstance();
                dayEnd.set(Calendar.DATE, date);
                dayEnd.set(Calendar.HOUR_OF_DAY, 23);
                dayEnd.set(Calendar.MINUTE, 59);
                dayEnd.set(Calendar.SECOND, 59);
                intent.putExtra("dayEnd", dayEnd);
                startActivity(intent);
            }
        });
    }
}