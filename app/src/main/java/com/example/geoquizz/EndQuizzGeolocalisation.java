package com.example.geoquizz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndQuizzGeolocalisation extends AppCompatActivity {

    private TextView mCityName;
    private TextView mScoreText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_quizz_geolocalisation);

        mCityName = findViewById(R.id.text_city);
        mScoreText = findViewById(R.id.text_score);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            mCityName.setText("Erreur");
        } else {
            mCityName.setText(extras.getString("CITY_NAME"));
            mScoreText.setText(extras.getString("SCORE_TEXT"));
        }

        final Button buttonHome = (Button) findViewById(R.id.button_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goHome(view);
            }
        });

        final Button buttonScore = (Button) findViewById(R.id.button_score);
        buttonScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayScore(view);
            }
        });
    }

    public void displayScore(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}