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

public class EditActivity extends AppCompatActivity {

    private EditText itemDescriptionEdit;
    private EditText itemPriceEdit;
    private PurchasedItem itemToEdit;
    private Intent intent;
    private PurchasedItemViewModel mPurchasedItemViewModel;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        actionBar = getSupportActionBar();
        actionBar.setTitle("Edit or delete purchase");

        mPurchasedItemViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PurchasedItemViewModel.class);

        intent = getIntent();
        itemToEdit = (PurchasedItem) intent.getSerializableExtra("itemToEdit");
        itemDescriptionEdit = (EditText) findViewById(R.id.item_description);
        itemPriceEdit = (EditText) findViewById(R.id.item_price);

        itemDescriptionEdit.setText(itemToEdit.itemDescription);
        itemPriceEdit.setText(String.format("%.2f", itemToEdit.itemPrice));
    }

    public void onEditClicked(View button) {
        String itemDescriptionText = itemDescriptionEdit.getText().toString();
        Double itemPriceText;
        try {
            itemPriceText = Double.parseDouble(itemPriceEdit.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Item must a valid price", Toast.LENGTH_SHORT).show();
            return;
        }
        if (itemDescriptionText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Item must have description", Toast.LENGTH_SHORT).show();
            return;
        }
        itemToEdit.itemDescription = itemDescriptionText;
        itemToEdit.itemPrice = itemPriceText;
        mPurchasedItemViewModel.update(itemToEdit);
        Toast.makeText(getApplicationContext(), "Item Edited", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onDeleteClicked(View button) {
        Toast.makeText(getApplicationContext(),  "Item Deleted", Toast.LENGTH_SHORT).show();
        mPurchasedItemViewModel.deleteByPurchasedItemId(itemToEdit.uid);
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