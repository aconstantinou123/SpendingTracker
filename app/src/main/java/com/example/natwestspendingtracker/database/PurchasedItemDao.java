package com.example.natwestspendingtracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface PurchasedItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PurchasedItem purchasedItem);

    @Update
    void update(PurchasedItem purchasedItem);

    @Query("DELETE FROM purchased_items")
    void deleteAll();

    @Query("SELECT * FROM purchased_items ORDER BY date ASC")
    LiveData<List<PurchasedItem>> getPurchasedItems();

    @Query("SELECT date FROM purchased_items ORDER BY date ASC")
    LiveData<List<Date>> getPurchasedItemDates();

    @Query("SELECT * FROM purchased_items WHERE date  BETWEEN :dayst AND :dayend")
    LiveData<List<PurchasedItem>> getPurchasedItemsCurrentDay(long dayst, long dayend);

    @Query("SELECT SUM(item_price) FROM purchased_items WHERE week = :week")
    LiveData<Double> getPurchasedItemsCurrentWeekTotal(int week);

    @Query("SELECT SUM(item_price) FROM purchased_items WHERE week = :week")
    Double getPurchasedItemsCurrentWeekTotalStatic(int week);

    @Query("SELECT SUM(item_price) FROM purchased_items WHERE date  BETWEEN :dayst AND :dayend")
    LiveData<Double> getPurchasedItemsCurrentDayTotal(long dayst, long dayend);

    @Query("SELECT SUM(item_price) FROM purchased_items WHERE date  BETWEEN :dayst AND :dayend")
    Double getPurchasedItemsCurrentDayTotalStatic(long dayst, long dayend);


    @Query("DELETE FROM purchased_items WHERE uid = :uid")
    void deleteByPurchasedItemId(long uid);

    @Query("SELECT strftime('%Y-%m-%d', datetime(date/1000, 'unixepoch')) AS day, " +
            "SUM(item_price) AS total " +
            "FROM purchased_items " +
            "WHERE (week = :week AND year = :year)" +
            "GROUP BY strftime('%d', datetime(date/1000, 'unixepoch'))" +
            "ORDER BY date DESC")
    LiveData<List<DateStringTotalTuple>> getPurchasedItemTotalByDay(int week, int year);

    @Query("SELECT strftime('%Y-%m', datetime(date/1000, 'unixepoch')) AS day, " +
            "SUM(item_price) AS total " +
            "FROM purchased_items " +
            "GROUP BY strftime('%m', datetime(date/1000, 'unixepoch')), " +
            "strftime('%Y', datetime(date/1000, 'unixepoch'))")
    LiveData<List<DateStringTotalTuple>> getPurchasedItemTotalByMonths();

    @Query("SELECT strftime('%Y-%m-%d', datetime(date/1000, 'unixepoch')) AS day, " +
            "SUM(item_price) AS total " +
            "FROM purchased_items " +
            "WHERE (month = :month AND year = :year)" +
            "GROUP BY strftime('%d', datetime(date/1000, 'unixepoch'))" +
            "ORDER BY date DESC")
    LiveData<List<DateStringTotalTuple>> getPurchasedItemTotalByMonthDay(int month, int year);

    @Query("SELECT week, year,  SUM(item_price) AS total " +
            "FROM purchased_items " +
            "GROUP BY week, year " +
            "ORDER BY date DESC")
    LiveData<List<WeekYearTotalTuple>> getPurchasedItemTotalByWeekYear();
}
