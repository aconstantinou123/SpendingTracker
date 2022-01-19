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

import com.example.natwestspendingtracker.database.DateStringTotalTuple;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthDaysActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private String month;
    private Integer year;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private SimpleDateFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        month = (String) intent.getSerializableExtra("month");
        year = Integer.parseInt((String) intent.getSerializableExtra("year"));

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
                android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                String fullDate = (String) lvItems.getItemAtPosition(position);
                Double total = Double.parseDouble(fullDate.split("£")[1]);
                tv.setTextColor(Color.BLACK);
                if(total >= 50.00){
                    tv.setBackgroundColor(Color.parseColor("#FF0000"));
                } else if(total >= 20.00) {
                    tv.setBackgroundColor(Color.parseColor("#FFBF00"));
                } else {
                    tv.setBackgroundColor(Color.parseColor("#00FF00"));
                }
                return view;
            }
        };
        lvItems.setAdapter(itemsAdapter);

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

        format = new SimpleDateFormat("dd MMMM yyyy");

        mPurchasedItemViewModel.getPurchasedItemTotalByMonthDay(monthNum, year).observe(this, dayTotalTuples -> {
            itemsAdapter.clear();
            for(DateStringTotalTuple dateStringTotalTuple : dayTotalTuples) {
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date pDate = dt.parse(dateStringTotalTuple.day);
                    String strDate = format.format(pDate.getTime());
                    itemsAdapter.add(
                            strDate
                                    + " - £"
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
                Calendar dayStart= Calendar.getInstance();
                dayStart.set(Calendar.YEAR, year);
                dayStart.set(Calendar.MONTH, monthNum);
                dayStart.set(Calendar.DATE, date);
                dayStart.set(Calendar.HOUR_OF_DAY,0);
                dayStart.set(Calendar.MINUTE,0);
                dayStart.set(Calendar.SECOND,0);
                intent.putExtra("dayStart", dayStart);

                Calendar dayEnd = Calendar.getInstance();
                dayStart.set(Calendar.YEAR, year);
                dayStart.set(Calendar.MONTH, monthNum);
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