package com.example.lab3;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PhoneViewModel extends AndroidViewModel {
    private final PhoneRepository mRepository;
    private final LiveData<List<Phone>> mAllPhones;

    public PhoneViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = new PhoneRepository(application);
        this.mAllPhones = mRepository.getAllPhones();
    }

    LiveData<List<Phone>> getAllPhones() {
        return mAllPhones;
    }

    public void insert(Phone phone) {
        mRepository.insert(phone);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deletePhone(Phone phone) {
        mRepository.delete(phone);
    }
}
