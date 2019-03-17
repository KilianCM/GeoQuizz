package com.example.geoquizz;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ScoreViewModel extends AndroidViewModel {

    private ScoreRepository mRepository;

    private LiveData<List<Score>> mAllScores;

    public ScoreViewModel (Application application) {
        super(application);
        mRepository = new ScoreRepository(application);
        mAllScores = mRepository.getAllScores();
    }

    LiveData<List<Score>> getAllScores() { return mAllScores; }

    public void insert(Score score) { mRepository.insert(score); }

    public void deleteScore(Score score) {mRepository.deleteScore(score);}

}