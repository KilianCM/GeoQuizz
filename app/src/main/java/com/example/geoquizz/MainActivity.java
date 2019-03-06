package com.example.geoquizz;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        final Button buttonPlay = (Button) findViewById(R.id.button_play_text);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchQuizz(view);
            }
        });

        final Button buttonScore = (Button) findViewById(R.id.button_trophy_text);
        buttonScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayScore(view);
            }
        });
    }

    public void launchQuizz(View view) {
        Intent intent = new Intent(this, QuizzGeolocalisation.class);
        startActivity(intent);
    }

    public void displayScore(View view) {
        /*Intent intent = new Intent(this, QuizzGeolocalisation.class);
        startActivity(intent);*/
    }
}
