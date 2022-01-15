package com.example.natwestspendingtracker.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class PurchasedItemRepository {

    private PurchasedItemDao mPurchasedItemDao;
    private LiveData<List<PurchasedItem>> mAllPurchasedItems;

    PurchasedItemRepository(Application application) {
        PurchasedItemsRoomDatabase db = PurchasedItemsRoomDatabase.getDatabase(application);
        mPurchasedItemDao = db.purchasedItemDao();
        mAllPurchasedItems = mPurchasedItemDao.getPurchasedItems();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<PurchasedItem>> getAllPurchasedItems() {
        return mAllPurchasedItems;
    }

    public void deleteAll () {
        PurchasedItemsRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPurchasedItemDao.deleteAll();
        });
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(PurchasedItem purchasedItem) {
        PurchasedItemsRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPurchasedItemDao.insert(purchasedItem);
        });
    }
}
