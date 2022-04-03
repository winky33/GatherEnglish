package com.example.gatherenglsih;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class ListeningExercise extends AppCompatActivity {
    DBHelper db = new DBHelper(ListeningExercise.this);
    TextToSpeech textToSpeech;

    //widgets
    private TextView questionNumber, opt1Txt, opt2Txt, opt3Txt, opt4Txt;
    private ImageButton audioBtn;
    private CardView opt1Btn, opt2Btn, opt3Btn, opt4Btn;
    private ImageView exitBtn, opt1Diagram, opt2Diagram, opt3Diagram, opt4Diagram;
    private Button submitBtn;

    //vars
    private ArrayList<ListeningQuizModel> quizModelArrayList;
    int currentScore = 0, currentPos = 1, mSelectedOptionPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_exercise);

        questionNumber = findViewById(R.id.listening_ques_no);
        audioBtn = findViewById(R.id.listening_audio_btn);
        exitBtn = findViewById(R.id.listening_ExitBtn);
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
        submitBtn = findViewById(R.id.listening_submit_button);
        quizModelArrayList = new ArrayList<>();

        exitBtn.setOnClickListener(view -> startActivity(new Intent(ListeningExercise.this, Homepage.class)));

        //generate 5 questions
        while (quizModelArrayList.size()< 5){
            db.getListeningQuestions(quizModelArrayList);
        }

        setDataToViews(currentPos-1);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){//if no error found
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        audioBtn.setOnClickListener(view -> {
            String audio = quizModelArrayList.get(currentPos-1).getQuestionTitle();
            textToSpeech.speak(audio, TextToSpeech.QUEUE_FLUSH,null);
        });
        audioBtn.performClick();

        opt1Btn.setOnClickListener(view -> selectedOptionView(opt1Btn, 1));

        opt2Btn.setOnClickListener(view -> selectedOptionView(opt2Btn, 2));

        opt3Btn.setOnClickListener(view -> selectedOptionView(opt3Btn, 3));

        opt4Btn.setOnClickListener(view -> selectedOptionView(opt4Btn, 4));

        submitBtn.setOnClickListener(view -> {
            if(mSelectedOptionPosition == 0){
                currentPos ++;

                if (currentPos <= quizModelArrayList.size()){
                    setDataToViews(currentPos-1);
                }else{
                    Intent intent = new Intent(ListeningExercise.this, ResultPage.class);
                    intent.putExtra("ExerciseType", "Listening");
                    intent.putExtra("CorrectAnswer", currentScore);
                    intent.putExtra("Total Question", quizModelArrayList.size());
                    startActivity(intent);
                }
            }else{
                if(quizModelArrayList.get(currentPos-1).getAnswer() != mSelectedOptionPosition){
                    answerView(mSelectedOptionPosition, Color.RED);
                    answerView(quizModelArrayList.get(currentPos-1).getAnswer(), Color.GREEN);
                }else{
                    answerView(mSelectedOptionPosition, Color.GREEN);
                    currentScore++;
                }

                if (currentPos == quizModelArrayList.size()){
                    submitBtn.setText("FINISH");
                }else{
                    submitBtn.setText("CONTINUE");
                }
                mSelectedOptionPosition = 0;
            }
        });

    }

    private void setDataToViews(int currentPos){
        int questionNum = currentPos+1;
        questionNumber.setText("Question " + questionNum);

        defaultOptionView();
        audioBtn.performClick();

        if (currentPos == quizModelArrayList.size()){
            submitBtn.setText("FINISH");
        }else{
            submitBtn.setText("SUBMIT");
        }

        opt1Txt.setText(quizModelArrayList.get(currentPos).getOption1());
        opt2Txt.setText(quizModelArrayList.get(currentPos).getOption2());
        opt3Txt.setText(quizModelArrayList.get(currentPos).getOption3());
        opt4Txt.setText(quizModelArrayList.get(currentPos).getOption4());
        opt1Diagram.setImageResource(quizModelArrayList.get(currentPos).getOption1Diagram());
        opt2Diagram.setImageResource(quizModelArrayList.get(currentPos).getOption2Diagram());
        opt3Diagram.setImageResource(quizModelArrayList.get(currentPos).getOption3Diagram());
        opt4Diagram.setImageResource(quizModelArrayList.get(currentPos).getOption4Diagram());

    }

    private void answerView(int answer, int backgroundColor){
        switch (answer){
            case 1:
                opt1Btn.setCardBackgroundColor(backgroundColor);
                break;
            case 2:
                opt2Btn.setCardBackgroundColor(backgroundColor);
                break;
            case 3:
                opt3Btn.setCardBackgroundColor(backgroundColor);
                break;
            case 4:
                opt4Btn.setCardBackgroundColor(backgroundColor);
                break;
        }
    }

    private void defaultOptionView(){
        ArrayList<CardView> options = new ArrayList<>();
        options.add(0, opt1Btn);
        options.add(1, opt2Btn);
        options.add(2, opt3Btn);
        options.add(3, opt4Btn);

        for (CardView option : options){
            option.setCardBackgroundColor(Color.WHITE);
        }
    }

    private void selectedOptionView(CardView optionBtn, int selectedOptionNum){
        mSelectedOptionPosition = selectedOptionNum;

        defaultOptionView();
        optionBtn.setCardBackgroundColor(Color.parseColor("#FEE741"));

    }
}