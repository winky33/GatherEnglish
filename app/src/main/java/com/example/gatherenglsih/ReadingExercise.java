package com.example.gatherenglsih;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class ReadingExercise extends AppCompatActivity {
    DBHelper db = new DBHelper(ReadingExercise.this);
    SpeechRecognizer speechRecognizer;

    //widgets
    TextView quesNoTxt, quesWord,testing;
    ImageView exitBtn, quesDiagram, micBtn;
    Button submitBtn;

    //vars
    protected static final int RESULT_SPEECH = 1;
    private int currentPos = 1, currentScore = 0;
    private String inputAnswer = "";
    ArrayList<String> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_exercise);

        exitBtn = findViewById(R.id.reading_ExitBtn);
        quesNoTxt = findViewById(R.id.readingQuestionNo);
        quesWord = findViewById(R.id.reading_ques_word);
        quesDiagram = findViewById(R.id.reading_ques_diagram);
        micBtn = findViewById(R.id.reading_mic_button);
        testing = findViewById(R.id.testing);
        submitBtn = findViewById(R.id.reading_submit_button);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReadingExercise.this, Homepage.class));
            }
        });

        questions = db.getSpellingReadingQuestion();
        generateQuestion(currentPos-1);

        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAudioPermission();
                micBtn.setColorFilter(ContextCompat.getColor(ReadingExercise.this, R.color.mic_enabled_color));
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");


                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    testing.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doValidate();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    inputAnswer = text.get(0);
                    testing.setText(inputAnswer);
                }
                break;
        }
    }

    private void generateQuestion (int quesNo){
        String question = questions.get(quesNo);
        String questionText = "Question "+currentPos;
        int diagram = db.getCardDiagram(question);

        quesNoTxt.setText(questionText);
        quesDiagram.setImageResource(diagram);
        quesWord.setText(question.toUpperCase());

    }

    private void doValidate() {

        if(quesWord.getText().toString().equals(inputAnswer.toUpperCase())) {
//            Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
            currentScore ++;
            currentPos ++;
            if (currentPos <= questions.size()){
                generateQuestion(currentPos - 1);

            }else{
                Intent intent = new Intent(ReadingExercise.this,ResultPage.class);
                intent.putExtra("ExerciseType","Reading");
                intent.putExtra("CorrectAnswer", currentScore);
                intent.putExtra("Total Question", questions.size());
                startActivity(intent);
            }

        } else {
            currentPos ++;
            if (currentPos <= questions.size()){
                generateQuestion(currentPos - 1);

            }else{
                Intent intent = new Intent(ReadingExercise.this,ResultPage.class);
                intent.putExtra("ExerciseType","Reading");
                intent.putExtra("CorrectAnswer", currentScore);
                intent.putExtra("Total Question", questions.size());
                startActivity(intent);
            }
        }
        inputAnswer = "";
    }

    private void checkAudioPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // M = 23
            if (ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
                Intent audioIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.example.gatherenglsih"));
                startActivity(audioIntent);
                Toast.makeText(this, "Allow Microphone Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}