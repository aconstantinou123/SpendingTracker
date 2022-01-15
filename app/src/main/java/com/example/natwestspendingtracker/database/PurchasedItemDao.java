package com.example.natwestspendingtracker.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PurchasedItemDao {
    @Query("SELECT * FROM purchaseditem")
    List<PurchasedItem> getAll();

    @Query("SELECT * FROM purchaseditem WHERE uid IN (:userIds)")
    List<PurchasedItem> loadAllByIds(int[] userIds);

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert
    void insertAll(PurchasedItem... purchasedItems);

    @Delete
    void delete(PurchasedItem purchasedItem);
}
