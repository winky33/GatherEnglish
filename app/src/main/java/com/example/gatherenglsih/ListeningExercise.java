package com.example.gatherenglsih;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class ListeningExercise extends AppCompatActivity {
    DBHelper db = new DBHelper(ListeningExercise.this);
    Random random;

    //widgets
    private TextView questionNumber, opt1Txt, opt2Txt, opt3Txt, opt4Txt;
    private ImageButton audioBtn;
    private CardView opt1Btn, opt2Btn, opt3Btn, opt4Btn;
    private ImageView opt1Diagram, opt2Diagram, opt3Diagram, opt4Diagram;

    //vars
    private ArrayList<ListeningQuizModel> quizModelArrayList;
    int currentScore = 0, questionAttempted = 0, currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_exercise);

        questionNumber = findViewById(R.id.listening_ques_no);
        audioBtn = findViewById(R.id.listening_audio_btn);
        opt1Btn = findViewById(R.id.listening_opt1);
        opt2Btn = findViewById(R.id.listening_opt2);
        opt3Btn = findViewById(R.id.listening_opt3);
        opt4Btn = findViewById(R.id.listening_opt4);
        opt1Diagram = findViewById(R.id.listening_opt1_diagram);
        opt2Diagram = findViewById(R.id.listening_opt2_diagram);
        opt3Diagram = findViewById(R.id.listening_opt3_diagram);
        opt4Diagram = findViewById(R.id.listening_opt4_diagram);
        opt1Txt = findViewById(R.id.listening_opt1_title);
        opt2Txt = findViewById(R.id.listening_opt2_title);
        opt3Txt = findViewById(R.id.listening_opt3_title);
        opt4Txt = findViewById(R.id.listening_opt4_title);
        quizModelArrayList = new ArrayList<>();
        random = new Random();

        //generate 5 questions
        db.getListeningQuestions(quizModelArrayList);
        db.getListeningQuestions(quizModelArrayList);
        db.getListeningQuestions(quizModelArrayList);
        db.getListeningQuestions(quizModelArrayList);
        db.getListeningQuestions(quizModelArrayList);
        for (int x = 0; x < quizModelArrayList.size(); x++){
            for (int y = 1; y < quizModelArrayList.size(); y++){
                if (quizModelArrayList.get(y).getQuestionAudio() == quizModelArrayList.get(x).getQuestionAudio()){
                    quizModelArrayList.remove(y);
                    db.getListeningQuestions(quizModelArrayList);
                }
            }
        }

        currentPos = random.nextInt(quizModelArrayList.size());
        setDataToViews(currentPos);
        audioBtn.performClick();

        audioBtn.setOnClickListener(view -> {
            int audio = quizModelArrayList.get(currentPos).getQuestionAudio();
            final MediaPlayer cardAudio = MediaPlayer.create(ListeningExercise.this, audio);
            cardAudio.start();
        });

        opt1Btn.setOnClickListener(view -> {
            if(quizModelArrayList.get(currentPos).getAnswer().trim().equalsIgnoreCase(
                    opt1Txt.getText().toString().trim())){
                currentScore++;
            }
            questionAttempted++;
            currentPos = random.nextInt(quizModelArrayList.size());
            setDataToViews(currentPos);
        });

        opt2Btn.setOnClickListener(view -> {
            if(quizModelArrayList.get(currentPos).getAnswer().trim().equalsIgnoreCase(
                    opt2Txt.getText().toString().trim())){
                currentScore++;
            }
            questionAttempted++;
            currentPos = random.nextInt(quizModelArrayList.size());
            setDataToViews(currentPos);
        });

        opt3Btn.setOnClickListener(view -> {
            if(quizModelArrayList.get(currentPos).getAnswer().trim().equalsIgnoreCase(
                    opt3Txt.getText().toString().trim())){
                currentScore++;
            }
            questionAttempted++;
            currentPos = random.nextInt(quizModelArrayList.size());
            setDataToViews(currentPos);
        });

        opt4Btn.setOnClickListener(view -> {
            if(quizModelArrayList.get(currentPos).getAnswer().trim().equalsIgnoreCase(
                    opt4Txt.getText().toString().trim())){
                currentScore++;
            }
            questionAttempted++;
            currentPos = random.nextInt(quizModelArrayList.size());
            setDataToViews(currentPos);
        });

    }

    private void setDataToViews(int currentPos){
        questionNumber.setText("Question " + questionAttempted);

        if(questionAttempted == 5){
            new AlertDialog.Builder(ListeningExercise.this)
                    .setTitle("Congratulation")
                    .setMessage("Your Score is \n"+currentScore+"/5\n You have Earned "+currentScore+" coins")
                    .setCancelable(true).setPositiveButton("Back to Home", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(ListeningExercise.this,Homepage.class));
                }
            }).show();
        }else{
            opt1Txt.setText(quizModelArrayList.get(currentPos).getOption1());
            opt2Txt.setText(quizModelArrayList.get(currentPos).getOption2());
            opt3Txt.setText(quizModelArrayList.get(currentPos).getOption3());
            opt4Txt.setText(quizModelArrayList.get(currentPos).getOption4());
            opt1Diagram.setImageResource(quizModelArrayList.get(currentPos).getOption1Diagram());
            opt2Diagram.setImageResource(quizModelArrayList.get(currentPos).getOption2Diagram());
            opt3Diagram.setImageResource(quizModelArrayList.get(currentPos).getOption3Diagram());
            opt4Diagram.setImageResource(quizModelArrayList.get(currentPos).getOption4Diagram());
        }
    }
}