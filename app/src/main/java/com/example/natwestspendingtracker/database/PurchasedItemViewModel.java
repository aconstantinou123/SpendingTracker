package com.example.natwestspendingtracker.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class PurchasedItemViewModel extends AndroidViewModel {

    private PurchasedItemRepository mRepository;

    private final LiveData<List<PurchasedItem>> mAllPurchasedItems;
    private final LiveData<List<Date>> mAllPurchasedItemDates;

    public PurchasedItemViewModel (Application application) {
        super(application);
        mRepository = new PurchasedItemRepository(application);
        mAllPurchasedItems = mRepository.getAllPurchasedItems();
        mAllPurchasedItemDates = mRepository.getAllPurchasedItemDates();
    }

    public LiveData<List<PurchasedItem>> getAllPurchasedItems() { return mAllPurchasedItems; }

    public LiveData<List<PurchasedItem>> getPurchasedItemsByMonth(int month) {
        return mRepository.getPurchasedItemsByMonth(month);
    }

    public LiveData<List<Date>> getAllPurchasedItemDates() { return mAllPurchasedItemDates; };

    public void insert(PurchasedItem purchasedItem) { mRepository.insert(purchasedItem); }

    public void deleteAll() { mRepository.deleteAll(); }
}
