package com.example.gatherenglsih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class Exercises extends AppCompatActivity {

    //widgets
    Button readingBtn, listeningBtn, spellingBtn;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        readingBtn = findViewById(R.id.reading_btn);
        spellingBtn = findViewById(R.id.spelling_btn);
        listeningBtn = findViewById(R.id.listening_btn);
        backBtn = findViewById(R.id.exercise_exitBtn);

        backBtn.setOnClickListener(view -> startActivity(new Intent(Exercises.this, Homepage.class)));

        spellingBtn.setOnClickListener(view -> startActivity(new Intent(Exercises.this, SpellingExercise.class)));

        listeningBtn.setOnClickListener(view -> startActivity(new Intent(Exercises.this, ListeningExercise.class)));

        readingBtn.setOnClickListener(view -> startActivity(new Intent(Exercises.this, ReadingExercise.class)));

    }
}