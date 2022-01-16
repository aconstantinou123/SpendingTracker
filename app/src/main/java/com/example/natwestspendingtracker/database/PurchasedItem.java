package com.example.natwestspendingtracker.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "purchased_items")
public class PurchasedItem {
    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "item_description")
    public String itemDescription;

    @ColumnInfo(name = "item_price")
    public double itemPrice;

    @ColumnInfo(name = "date")
    public Date date;

    @ColumnInfo(name = "month")
    public int month;

    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "week")
    public int week;

    public PurchasedItem(@NonNull long uid, @NonNull String itemDescription, double itemPrice, Date date, int month, int year, int week) {
        this.uid = uid;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.date = date;
        this.month = month;
        this.year = year;
        this.week = week;
    }

    @Override
    public String toString() {
        return "PurchasedItem{" +
                "uid=" + uid +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemPrice=" + itemPrice +
                ", date=" + date +
                ", month=" + month +
                ", year=" + year +
                ", week=" + week +
                '}';
    }
}
