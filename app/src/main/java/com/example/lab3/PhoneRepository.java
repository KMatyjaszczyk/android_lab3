package com.example.lab3;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PhoneRepository {
    private PhoneDao mPhoneDao;
    private LiveData<List<Phone>> mAllPhones;

    PhoneRepository(Application application) {
        PhoneRoomDatabase phoneRoomDatabase = PhoneRoomDatabase.getDatabase(application);

        mPhoneDao = phoneRoomDatabase.phoneDao();
        mAllPhones = mPhoneDao.getWordsOrderByProducer();
    }

    LiveData<List<Phone>> getAllPhones() {
        return mAllPhones;
    }

    void insert(Phone phone) {
        PhoneRoomDatabase.databaseWriteExecutor.execute(() -> mPhoneDao.insert(phone));
    }

    void deleteAll() {
        PhoneRoomDatabase.databaseWriteExecutor.execute(() -> mPhoneDao.deleteAll());
    }

    void delete(Phone phone) {
        PhoneRoomDatabase.databaseWriteExecutor.execute(() -> mPhoneDao.delete(phone));
    }

    void update(Phone phone) {
        PhoneRoomDatabase.databaseWriteExecutor.execute(() -> mPhoneDao.update(phone));
    }
}
