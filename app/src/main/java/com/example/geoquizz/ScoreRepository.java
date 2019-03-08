package com.example.geoquizz;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ScoreRepository {

    private ScoreDao mScoreDao;
    private LiveData<List<Score>> mAllScores;

    ScoreRepository(Application application) {
        ScoreRoomDatabase db = ScoreRoomDatabase.getDatabase(application);
        mScoreDao = db.scoreDao();
        mAllScores = mScoreDao.getAllScores();
    }

    LiveData<List<Score>> getAllScores() {
        return mAllScores;
    }

    public void insert (Score score) {
        new insertAsyncTask(mScoreDao).execute(score);
    }

    private static class insertAsyncTask extends AsyncTask<Score, Void, Void> {

        private ScoreDao mAsyncTaskDao;

        insertAsyncTask(ScoreDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Score... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
