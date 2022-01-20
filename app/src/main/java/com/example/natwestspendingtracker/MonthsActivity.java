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

import com.example.natwestspendingtracker.database.DateStringTotalTuple;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MonthsActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private SimpleDateFormat format;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        actionBar = getSupportActionBar();
        actionBar.setTitle("Months");

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
                if(total >= 1000.00){
                    tv.setBackgroundColor(Color.parseColor("#FF0000"));
                } else if(total >= 800.00) {
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

        format = new SimpleDateFormat("MMMM yyyy");

        mPurchasedItemViewModel.getPurchasedItemTotalByMonth().observe(this, dayTotalTuples -> {
            itemsAdapter.clear();
            for(DateStringTotalTuple dateStringTotalTuple : dayTotalTuples) {
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM");
                try {
                    Date date = dt.parse(dateStringTotalTuple.day);
                    String strDate = format.format(date.getTime());
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
                Intent intent = new Intent(getApplicationContext(), MonthDaysActivity.class);
                String monthYear = (String) lvItems.getItemAtPosition(position);
                String month = monthYear.split(" ")[0];
                String year = monthYear.split(" ")[1];
                intent.putExtra("month", month);
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