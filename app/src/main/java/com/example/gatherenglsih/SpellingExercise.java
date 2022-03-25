package com.example.gatherenglsih;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class SpellingExercise extends AppCompatActivity {
    DBHelper db = new DBHelper(SpellingExercise.this);
    Random random = new Random();

    //widgets
    ImageView exitBtn, questionDiagram;
    TextView questionTxt;
    Animation smallbigforth;

    //vars
    private int presCounter = 0, maxPresCounter = 0, currentPos = 1, currentScore = 0;
    ArrayList<String> questions = new ArrayList<>();
    private char[] keys;
    private String textAnswer;
    private String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spelling_exercise);

        smallbigforth = AnimationUtils.loadAnimation(this, R.anim.smallbigforth);
        exitBtn = findViewById(R.id.spelling_ExitBtn);
        questionDiagram = findViewById(R.id.spelling_ques_diagram);
        questionTxt = findViewById(R.id.spellingTextQuestion);

        questions = db.getSpellingQuestion();

        exitBtn.setOnClickListener(view -> startActivity(new Intent(SpellingExercise.this, Homepage.class)));

        generateQuestion(currentPos-1);

    }

    private void generateQuestion (int quesNo){
        String question = questions.get(quesNo);
        String questionText = "Question "+currentPos;
        int diagram = db.getCardDiagram(question);

        questionTxt.setText(questionText);
        questionDiagram.setImageResource(diagram);
        char randomLetter1 = abc.charAt(random.nextInt(abc.length()));
        char randomLetter2 = abc.charAt(random.nextInt(abc.length()));

        textAnswer = question.toUpperCase();
        int i = 0;
        while (i < textAnswer.length()){
            keys[i] = textAnswer.charAt(i);
            i++;
        }
        keys[i + 1] = randomLetter1;
        keys[i + 1] = randomLetter2;

        //keys = textAnswer.toCharArray();

        shuffleArray(keys);

        for (char key : keys) {
            addView(((LinearLayout) findViewById(R.id.spellingLayoutParent)), String.valueOf(key), ((EditText) findViewById(R.id.spellingEditText)));
        }

        maxPresCounter = keys.length;
        Log.i("Submit Answer", "Ans Length: "+maxPresCounter);
    }

    private char[] shuffleArray(char[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            char a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }


    private void addView(LinearLayout viewParent, final String text, final EditText editText) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        linearLayoutParams.rightMargin = 30;

        final TextView textView = new TextView(this);

        textView.setLayoutParams(linearLayoutParams);
        textView.setBackground(getDrawable(R.drawable.bgpink));
        textView.setTextColor(this.getColor(R.color.colorPurple));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(32);

        textView.setOnClickListener(v -> {
            if(presCounter < maxPresCounter) {
                if (presCounter == 0)
                    editText.setText("");

                    editText.setText(editText.getText().toString() + text);
                    textView.startAnimation(smallbigforth);
                    textView.animate().alpha(0).setDuration(300);
                    textView.setClickable(false);

                    presCounter++;

                if (presCounter == maxPresCounter)
                    doValidate();
            }
        });

        viewParent.addView(textView);
    }


    private void doValidate() {
        presCounter = 0;

        EditText editText = findViewById(R.id.spellingEditText);
        LinearLayout linearLayout = findViewById(R.id.spellingLayoutParent);

        if(editText.getText().toString().equals(textAnswer)) {
//            Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
            currentScore ++;
            currentPos ++;
            if (currentPos <= questions.size()){
                generateQuestion(currentPos - 1);

            }else{
                Intent intent = new Intent(SpellingExercise.this,ResultPage.class);
                intent.putExtra("ExerciseType","Spelling");
                intent.putExtra("CorrectAnswer", currentScore);
                intent.putExtra("Total Question", questions.size());
                startActivity(intent);
            }

        } else {
            Toast.makeText(SpellingExercise.this, "Wrong", Toast.LENGTH_SHORT).show();
        }
        editText.setText("");

        shuffleArray(keys);
        linearLayout.removeAllViews();
        for (char key : keys) {
            addView(linearLayout, String.valueOf(key), editText);
        }

    }
}