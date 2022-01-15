package com.example.natwestspendingtracker.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "purchased_items")
public class PurchasedItem {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "item_description")
    public String itemDescription;

    @ColumnInfo(name = "item_price")
    public double itemPrice;

    @ColumnInfo(name = "date")
    public long  date;

    public PurchasedItem(@NonNull String itemDescription, double itemPrice, long date) {
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.date = date;
    }

    @Override
    public String toString() {
        return "PurchasedItem{" +
                "uid=" + uid +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemPrice=" + itemPrice +
                ", date=" + date +
                '}';
    }
}
