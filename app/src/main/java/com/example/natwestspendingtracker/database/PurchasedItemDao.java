package com.example.natwestspendingtracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface PurchasedItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PurchasedItem purchasedItem);

    @Query("DELETE FROM purchased_items")
    void deleteAll();

    @Query("SELECT * FROM purchased_items ORDER BY date ASC")
    LiveData<List<PurchasedItem>> getPurchasedItems();

    @Query("SELECT strftime('%m', date) FROM purchased_items ORDER BY date ASC")
    LiveData<List<Integer>> getMonths();

    @Query("SELECT date FROM purchased_items ORDER BY date ASC")
    LiveData<List<Date>> getPurchasedItemDates();

    @Query("SELECT * FROM purchased_items WHERE month = :month ORDER BY date ASC")
    LiveData<List<PurchasedItem>> getPurchasedItemsByMonth(int month);
}
