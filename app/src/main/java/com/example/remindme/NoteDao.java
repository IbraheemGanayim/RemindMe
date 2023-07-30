package com.example.remindme;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface NoteDao {
    @Query("SELECT * FROM alarmDetails")
    List<alarmDetails> getAllDeatils();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(alarmDetails note);

    @Query("DELETE FROM alarmDetails WHERE hour=:hour1 AND minute=:minute1")
    int delete(int hour1, int minute1);

    @Query("SELECT * FROM alarmDetails WHERE hour=:hour1 AND minute=:minute1")
    List<alarmDetails> find(int hour1, int minute1);
}
