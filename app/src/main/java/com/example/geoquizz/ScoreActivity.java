package com.example.geoquizz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private static final String LOG_TAG = ScoreActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);
    }
}
