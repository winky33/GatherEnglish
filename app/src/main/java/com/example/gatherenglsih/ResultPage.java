package com.example.gatherenglsih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class ResultPage extends AppCompatActivity {
    DBHelper db = new DBHelper(this);

    //widgets
    TextView nameTV, scoreTV, coinTV;
    Button finishBtn;

    //vars
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        nameTV = findViewById(R.id.result_tv_name);
        scoreTV = findViewById(R.id.result_tv_score);
        coinTV = findViewById(R.id.result_tv_coin);
        finishBtn = findViewById(R.id.result_btn_finish);

        //retrieve userID
        SharedPreferences sharedPrefs = this.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        String uuid = sharedPrefs.getString(PREF_UNIQUE_ID, null);

        //set username
        String username = db.getUserName(uuid);
        nameTV.setText(username);

        Intent intent = getIntent();
        if (intent.getExtras() != null){
            String currentTime = Calendar.getInstance().getTime().toString();
            String exeType = intent.getStringExtra("ExerciseType");
            int totalQues = intent.getIntExtra("Total Question", 0);
            int score = intent.getIntExtra("CorrectAnswer", 0);

            scoreTV.setText("Your Score is "+score+" out of "+totalQues);
            coinTV.setText(score+" Coins Collected");

            db.updateCoinAmount(uuid, score);

            db.addNewExercise(exeType, currentTime, score, totalQues);
        }

        finishBtn.setOnClickListener(view -> startActivity(new Intent(ResultPage.this, Exercises.class)));
    }
}