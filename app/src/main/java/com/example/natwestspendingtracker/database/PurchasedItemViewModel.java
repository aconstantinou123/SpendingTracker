package com.example.natwestspendingtracker.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PurchasedItemViewModel extends AndroidViewModel {

    private PurchasedItemRepository mRepository;

    private final LiveData<List<PurchasedItem>> mAllPurchasedItems;

    public PurchasedItemViewModel (Application application) {
        super(application);
        mRepository = new PurchasedItemRepository(application);
        mAllPurchasedItems = mRepository.getAllPurchasedItems();
    }

    public LiveData<List<PurchasedItem>> getAllPurchasedItems() { return mAllPurchasedItems; }

    public void insert(PurchasedItem purchasedItem) { mRepository.insert(purchasedItem); }

    public void deleteAll() { mRepository.deleteAll(); }
}
