package com.example.geoquizz;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "score_table")
public class Score {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "city")
    private String mCity;

    @NonNull
    @ColumnInfo(name = "value")
    private int mValue;

    public Score(@NonNull String city, @NonNull int value) {
        this.mCity = city;
        this.mValue = value;
    }
    public String getCity(){
        return this.mCity;
    }

    public int getValue(){
        return this.mValue;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }
}
