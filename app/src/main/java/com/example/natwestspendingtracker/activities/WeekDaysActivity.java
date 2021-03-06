package com.example.natwestspendingtracker.activities;

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

import com.example.natwestspendingtracker.R;
import com.example.natwestspendingtracker.activities.DayActivity;
import com.example.natwestspendingtracker.database.DateStringTotalTuple;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;
import com.example.natwestspendingtracker.enums.AppColor;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeekDaysActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private int week;
    private int year;
    private SimpleDateFormat format;
    private Intent intent;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_days);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        intent = getIntent();
        week = (int) intent.getSerializableExtra("week");
        year = (int) intent.getSerializableExtra("year");

        actionBar = getSupportActionBar();
        actionBar.setTitle("Week " + week + " " + year);

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                String fullDate = (String) lvItems.getItemAtPosition(position);
                Double total = Double.parseDouble(fullDate.split("??")[1]);
                tv.setTextColor(Color.BLACK);
                if(total >= 50.00){
                    tv.setBackgroundColor(Color.parseColor(AppColor.RED.getHex()));
                } else if(total >= 20.00) {
                    tv.setBackgroundColor(Color.parseColor(AppColor.YELLOW.getHex()));
                } else {
                    tv.setBackgroundColor(Color.parseColor(AppColor.GREEN.getHex()));
                }
                return view;
            }
        };

        lvItems.setAdapter(itemsAdapter);

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

        format = new SimpleDateFormat("dd MMMM yyyy");

        mPurchasedItemViewModel.getPurchasedItemTotalByDay(week, year).observe(this, dayTotalTuples -> {
            itemsAdapter.clear();
            for(DateStringTotalTuple dateStringTotalTuple : dayTotalTuples) {
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dt.parse(dateStringTotalTuple.day);
                    String strDate = format.format(date.getTime());
                    itemsAdapter.add(
                            strDate
                                    + " - ??"
                                    + String.format("%.2f", dateStringTotalTuple.total)
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), DayActivity.class);
                String fullDate = (String) lvItems.getItemAtPosition(position);

                Integer date = Integer.parseInt(fullDate.split(" ")[0]);
                String monthName = fullDate.split(" ")[1];

                Date monthDate = null;
                try {
                    monthDate = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthName);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(monthDate);
                int monthNum = cal.get(Calendar.MONTH);

                Calendar dayStart= Calendar.getInstance();
                dayStart.set(Calendar.YEAR, year);
                dayStart.set(Calendar.MONTH, monthNum);
                dayStart.set(Calendar.DATE, date);
                dayStart.set(Calendar.HOUR_OF_DAY,0);
                dayStart.set(Calendar.MINUTE,0);
                dayStart.set(Calendar.SECOND,0);
                intent.putExtra("dayStart", dayStart);

                Calendar dayEnd = Calendar.getInstance();
                dayEnd.set(Calendar.YEAR, year);
                dayEnd.set(Calendar.MONTH, monthNum);
                dayEnd.set(Calendar.DATE, date);
                dayEnd.set(Calendar.HOUR_OF_DAY, 23);
                dayEnd.set(Calendar.MINUTE, 59);
                dayEnd.set(Calendar.SECOND, 59);
                intent.putExtra("dayEnd", dayEnd);
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