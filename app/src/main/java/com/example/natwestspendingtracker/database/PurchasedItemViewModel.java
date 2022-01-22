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
    private final LiveData<List<DateStringTotalTuple>> mPurchasedItemTotalByMonth;
    private final LiveData<List<WeekYearTotalTuple>> mPurchasedItemTotalByWeekYear;

    public PurchasedItemViewModel (Application application) {
        super(application);
        mRepository = new PurchasedItemRepository(application);
        mAllPurchasedItems = mRepository.getAllPurchasedItems();
        mAllPurchasedItemDates = mRepository.getAllPurchasedItemDates();
        mPurchasedItemTotalByMonth = mRepository.getPurchasedItemTotalByMonth();
        mPurchasedItemTotalByWeekYear = mRepository.getPurchasedItemTotalByWeekYear();
    }

    public LiveData<List<PurchasedItem>> getAllPurchasedItems() { return mAllPurchasedItems; }

    public LiveData<List<PurchasedItem>> getPurchasedItemsCurrentDay(long dayst, long dayend) {
        return mRepository.getPurchasedItemsCurrentDay(dayst, dayend);
    }

    public LiveData<Double> getPurchasedItemsCurrentDayTotal(long dayst, long dayend) {
        return mRepository.getPurchasedItemsCurrentDayTotal(dayst, dayend);
    }

    public Double getPurchasedItemsCurrentDayTotalStatic(long dayst, long dayend) {
        return mRepository.getPurchasedItemsCurrentDayTotalStatic(dayst, dayend);
    }

    public LiveData<Double> getPurchasedItemsCurrentWeekTotal(int week) {
        return mRepository.getPurchasedItemsCurrentWeekTotal(week);
    }

    public Double getPurchasedItemsCurrentWeekTotalStatic(int week) {
        return mRepository.getPurchasedItemsCurrentWeekTotalStatic(week);
    }

    public LiveData<List<DateStringTotalTuple>> getPurchasedItemTotalByDay(int week, int year) {
        return mRepository.getPurchasedItemTotalByDay(week, year);
    }

    public LiveData<List<DateStringTotalTuple>> getPurchasedItemTotalByMonthDay(int week, int year) {
        return mRepository.getPurchasedItemTotalByMonthDay(week, year);
    }

    public LiveData<List<DateStringTotalTuple>> getPurchasedItemTotalByMonth() {
        return mPurchasedItemTotalByMonth;
    }

    public LiveData<List<WeekYearTotalTuple>> getPurchasedItemTotalByWeekYear() {
        return mPurchasedItemTotalByWeekYear;
    }

    public void insert(PurchasedItem purchasedItem) { mRepository.insert(purchasedItem); }

    public void update(PurchasedItem purchasedItem) { mRepository.update(purchasedItem); }

    public void deleteByPurchasedItemId(long uid) { mRepository.deleteByPurchasedItemId(uid); }

    public void deleteAll() { mRepository.deleteAll(); }
}
