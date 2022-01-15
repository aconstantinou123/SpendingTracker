package com.example.natwestspendingtracker.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "purchased_items")
public class PurchasedItem {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "item_description")
    public String itemDescription;

    @ColumnInfo(name = "item_price")
    public double itemPrice;

    @ColumnInfo(name = "date")
    public Date date;

    public PurchasedItem(@NonNull int uid, @NonNull String itemDescription, double itemPrice, Date date) {
        this.uid = uid;
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
