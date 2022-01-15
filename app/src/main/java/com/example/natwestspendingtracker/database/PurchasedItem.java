package com.example.natwestspendingtracker.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PurchasedItem {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "item_description")
    public String itemDescription;

    @ColumnInfo(name = "item_price")
    public double itemPrice;

    @ColumnInfo(name = "date")
    public long  date;
}
