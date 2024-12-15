package com.example.gametrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        TextView titleView = findViewById(R.id.titleTextView);
        TextView genreView = findViewById(R.id.genreTextView);
        TextView platformView = findViewById(R.id.platformTextView);
        TextView developerView = findViewById(R.id.developerTextView);
        TextView releaseYearView = findViewById(R.id.releaseYearTextView);
        TextView ownedView = findViewById(R.id.ownedTextView);
        TextView commentView = findViewById(R.id.commentTextView);

        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        String genre = intent.getExtras().getString("genre");
        String platform = intent.getExtras().getString("platform");
        String developer = intent.getExtras().getString("developer");
        int releaseYear = intent.getExtras().getInt("release_year", -1);
        boolean owned = intent.getExtras().getBoolean("owned", false);
        String comment = intent.getExtras().getString("comment");

        titleView.setText(title);
        genreView.setText(genre);
        platformView.setText(platform);
        developerView.setText(developer);
        releaseYearView.setText(String.valueOf(releaseYear));
        ownedView.setText(owned ? "Owned" : "Not Owned");
        commentView.setText(comment != null ? comment : "No comments available");

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
