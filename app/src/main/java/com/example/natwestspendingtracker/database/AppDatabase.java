package com.example.natwestspendingtracker.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PurchasedItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PurchasedItemDao purchasedItemDao();
}
