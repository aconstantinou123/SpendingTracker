package com.example.natwestspendingtracker.database;

import androidx.room.ColumnInfo;

public class WeekYearTotalTuple {
    @ColumnInfo(name = "week")
    public Integer week;

    @ColumnInfo(name = "year")
    public Integer year;

    @ColumnInfo(name = "total")
    public Double total;
}
