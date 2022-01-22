package com.example.natwestspendingtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.natwestspendingtracker.database.PurchasedItem;
import com.example.natwestspendingtracker.database.PurchasedItemViewModel;

import java.util.Calendar;

public class NewActivity extends AppCompatActivity {

    private EditText itemDescriptionNew;
    private EditText itemPriceNew;
    private Calendar date;
    private Intent intent;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add a new purchase");

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

        intent = getIntent();
        date = (Calendar) intent.getSerializableExtra("date");
        itemDescriptionNew = (EditText) findViewById(R.id.item_description);
        itemPriceNew = (EditText) findViewById(R.id.item_price);
    }

    public void onSubmitClicked(View button) {
        String itemDescriptionText = itemDescriptionNew.getText().toString();
        Double itemPriceText;
        try {
            itemPriceText = Double.parseDouble(itemPriceNew.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Item must a valid price", Toast.LENGTH_SHORT).show();
            return;
        }
        if (itemDescriptionText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Item must have description", Toast.LENGTH_SHORT).show();
            return;
        }
        PurchasedItem newPurchasedItem = new PurchasedItem(
                date.getTime().getTime(),
                itemDescriptionText,
                itemPriceText,
                date.getTime(),
                date.get(Calendar.MONTH),
                date.get(Calendar.YEAR),
                date.get(Calendar.WEEK_OF_YEAR)
        );
        mPurchasedItemViewModel.insert(newPurchasedItem);
        Toast.makeText(getApplicationContext(), "Item Created", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onCancelClicked(View button) {
        finish();
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