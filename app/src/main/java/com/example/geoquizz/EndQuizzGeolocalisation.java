package com.example.geoquizz;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EndQuizzGeolocalisation extends AppCompatActivity {

    private TextView mCityName;
    private TextView mScoreText;
    public static final String EXTRA_REPLY_STRING =
            "com.example.android.roomwordssample.REPLY_STRING";
    public static final String EXTRA_REPLY_INT =
            "com.example.android.roomwordssample.REPLY_INT";

    private ScoreViewModel mScoreViewModel;
    private ImageView mBackground;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_quizz_geolocalisation);

        mCityName = findViewById(R.id.text_city);
        mScoreText = findViewById(R.id.text_score);
        mBackground = findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            mCityName.setText("Erreur");
        } else {
            mCityName.setText(extras.getString("CITY_NAME"));
            mScoreText.setText(extras.getString("SCORE_TEXT"));

            //set background gradient according to the quizz type (geolocalisation or not)
            if (extras.getInt("QUIZZ_TYPE") == 0) {
                mBackground.setImageResource(R.drawable.gradient_bg_red);
            }
            else{
                mBackground.setImageResource(R.drawable.gradient_bg_green);
            }
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

        mScoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        Score score = new Score(extras.getString("CITY_NAME"),extras.getInt("SCORE_VALUE"));
        mScoreViewModel.insert(score);

    }

    public void displayScore(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
