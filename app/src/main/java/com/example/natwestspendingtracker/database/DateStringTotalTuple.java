package com.example.natwestspendingtracker.database;


import androidx.room.ColumnInfo;

public class DateStringTotalTuple {
    @ColumnInfo(name = "day")
    public String day;

    @ColumnInfo(name = "total")
    public Double total;
}
