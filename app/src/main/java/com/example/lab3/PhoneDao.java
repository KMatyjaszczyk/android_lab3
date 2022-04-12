package com.example.lab3;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhoneDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Phone phone);

    @Delete
    void delete(Phone phone);

    @Query("DELETE FROM phone")
    void deleteAll();

    @Query("SELECT * FROM phone ORDER BY producer ASC")
    LiveData<List<Phone>> getWordsOrderByProducer();
}
