package com.example.natwestspendingtracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PurchasedItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PurchasedItem purchasedItem);

    @Query("DELETE FROM purchased_items")
    void deleteAll();

    @Query("SELECT * FROM purchased_items")
    LiveData<List<PurchasedItem>> getPurchasedItems();
}
