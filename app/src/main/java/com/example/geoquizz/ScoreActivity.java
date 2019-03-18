package com.example.geoquizz;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private static final String LOG_TAG = ScoreActivity.class.getSimpleName() ;
    private ScoreViewModel mScoreViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);

        RecyclerView recyclerView = findViewById(R.id.score_list);
        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mScoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        mScoreViewModel.getAllScores().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(@Nullable final List<Score> scores) {
                // Update the cached copy of the words in the adapter.
                adapter.setScores(scores);
            }
        });


        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Score myScore = adapter.getScoreAtPosition(position);
                        Toast.makeText(ScoreActivity.this, "Suppression du score", Toast.LENGTH_SHORT).show();

                        // Delete the word
                        mScoreViewModel.deleteScore(myScore);
                    }
                });

        helper.attachToRecyclerView(recyclerView);

    }
}
