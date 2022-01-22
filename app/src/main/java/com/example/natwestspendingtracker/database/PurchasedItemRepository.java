package com.example.natwestspendingtracker.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

class PurchasedItemRepository {

    private PurchasedItemDao mPurchasedItemDao;
    private LiveData<List<PurchasedItem>> mAllPurchasedItems;
    private LiveData<List<Date>> mAllPurchasedItemDates;
    private LiveData<List<DateStringTotalTuple>> mPurchasedItemTotalByMonth;
    private LiveData<List<WeekYearTotalTuple>> mPurchasedItemTotalByWeekYear;

    PurchasedItemRepository(Application application) {
        PurchasedItemsRoomDatabase db = PurchasedItemsRoomDatabase.getDatabase(application);
        mPurchasedItemDao = db.purchasedItemDao();
        mAllPurchasedItems = mPurchasedItemDao.getPurchasedItems();
        mAllPurchasedItemDates = mPurchasedItemDao.getPurchasedItemDates();
        mPurchasedItemTotalByMonth = mPurchasedItemDao.getPurchasedItemTotalByMonths();
        mPurchasedItemTotalByWeekYear = mPurchasedItemDao.getPurchasedItemTotalByWeekYear();
    }

    public LiveData<List<PurchasedItem>> getAllPurchasedItems() {
        return mAllPurchasedItems;
    }

    public LiveData<List<Date>> getAllPurchasedItemDates() { return mAllPurchasedItemDates; }

    public  LiveData<List<PurchasedItem>> getPurchasedItemsCurrentDay(long dayst, long dayend) {
        return mPurchasedItemDao.getPurchasedItemsCurrentDay(dayst, dayend);
    }

    public LiveData<Double> getPurchasedItemsCurrentDayTotal(long dayst, long dayend) {
        return mPurchasedItemDao.getPurchasedItemsCurrentDayTotal(dayst, dayend);
    }
    public Double getPurchasedItemsCurrentDayTotalStatic(long dayst, long dayend) {
        return mPurchasedItemDao.getPurchasedItemsCurrentDayTotalStatic(dayst, dayend);
    }

    public LiveData<Double> getPurchasedItemsCurrentWeekTotal(int week) {
        return mPurchasedItemDao.getPurchasedItemsCurrentWeekTotal(week);
    }

    public Double getPurchasedItemsCurrentWeekTotalStatic(int week) {
        return mPurchasedItemDao.getPurchasedItemsCurrentWeekTotalStatic(week);
    }

    public LiveData<List<DateStringTotalTuple>> getPurchasedItemTotalByDay(int week, int year) {
        return mPurchasedItemDao.getPurchasedItemTotalByDay(week, year);
    }

    public LiveData<List<DateStringTotalTuple>> getPurchasedItemTotalByMonthDay(int week, int year) {
        return mPurchasedItemDao.getPurchasedItemTotalByMonthDay(week, year);
    }

    public LiveData<List<DateStringTotalTuple>> getPurchasedItemTotalByMonth() {
        return mPurchasedItemTotalByMonth;
    }

    public LiveData<List<WeekYearTotalTuple>> getPurchasedItemTotalByWeekYear() {
        return mPurchasedItemTotalByWeekYear;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(PurchasedItem purchasedItem) {
        PurchasedItemsRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPurchasedItemDao.insert(purchasedItem);
        });
    }

    void update(PurchasedItem purchasedItem) {
        PurchasedItemsRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPurchasedItemDao.update(purchasedItem);
        });
    }

    public void deleteByPurchasedItemId(long uid) {
        PurchasedItemsRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPurchasedItemDao.deleteByPurchasedItemId(uid);
        });
    }

    public void deleteAll () {
        PurchasedItemsRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPurchasedItemDao.deleteAll();
        });
    }
}
