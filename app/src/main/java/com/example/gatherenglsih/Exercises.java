package com.example.gatherenglsih;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Exercises extends AppCompatActivity {
    DBHelper db = new DBHelper(Exercises.this);
    private AlertDialog dialog;

    //widgets
    Button readingBtn, listeningBtn, spellingBtn;
    ImageView backBtn,cardDiagramView;
    TextView closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        readingBtn = findViewById(R.id.reading_btn);
        spellingBtn = findViewById(R.id.spelling_btn);
        listeningBtn = findViewById(R.id.listening_btn);
        backBtn = findViewById(R.id.exercise_exitBtn);

        backBtn.setOnClickListener(view -> startActivity(new Intent(Exercises.this, Homepage.class)));

        spellingBtn.setOnClickListener(view -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Exercises.this);
            final View cardPopupView = getLayoutInflater().inflate(R.layout.gather_card_popup, null);
            int cardId = 1;
            final MediaPlayer cardAudio = MediaPlayer.create(Exercises.this, db.getCardAudio(cardId));

            cardDiagramView = cardPopupView.findViewById(R.id.popup_obtained_card);
            closeBtn = cardPopupView.findViewById(R.id.popup_close_btn);

            dialogBuilder.setView(cardPopupView);
            dialog = dialogBuilder.create();
            dialog.show();

            cardAudio.start();

            int diagram = db.getCardDiagram("Sofa");
            cardDiagramView.setImageResource(diagram);

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
            dialog.getWindow().setLayout(550, 1200);


            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        });

    }
}