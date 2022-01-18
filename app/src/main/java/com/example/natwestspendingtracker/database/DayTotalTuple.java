package com.example.natwestspendingtracker.database;


import androidx.room.ColumnInfo;

public class DayTotalTuple {
    @ColumnInfo(name = "day")
    public String day;

    @ColumnInfo(name = "total")
    public Double total;

    DayTotalTuple(){};
}
