package com.example.geoquizz;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {

    @Insert
    void insert(Score score);

    @Query("DELETE FROM score_table")
    void deleteAll();

    @Query("SELECT * from score_table ORDER BY id DESC")
    LiveData<List<Score>> getAllScores();
}
