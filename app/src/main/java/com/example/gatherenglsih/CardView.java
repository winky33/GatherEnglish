package com.example.gatherenglsih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class CardView extends AppCompatActivity {
    DBHelper db = new DBHelper(CardView.this);

    //widgets
    ImageView cardDiagram, exitBtn;
    ImageButton playAudio, upgradeCard, swapDiagram;
    TextView coinTxt, cardTitleTxt;

    //vars
    public int selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        cardDiagram = findViewById(R.id.card_view_diagram);
        cardTitleTxt = findViewById(R.id.card_view_title);
        playAudio = findViewById(R.id.audio_btn);
        upgradeCard = findViewById(R.id.upgrade_btn);
        exitBtn = findViewById(R.id.cardViewExitBtn);
        coinTxt = findViewById(R.id.cardView_coinText);
        swapDiagram = findViewById(R.id.swap_btn);

        exitBtn.setOnClickListener(view -> startActivity(new Intent(CardView.this, CardGallery.class)));

        Intent intent = getIntent();

        if (intent.getExtras() != null){
            selectedImage = intent.getIntExtra("diagram",0);
            String coinAmount = intent.getStringExtra("coinAmount");

            coinTxt.setText(coinAmount);
            cardDiagram.setImageResource(selectedImage);
        }

        int cardID = db.getCardIDbyDiagram(String.valueOf(selectedImage));
        String cardTitle = db.getCardTitle(cardID);

        cardTitleTxt.setText(cardTitle);

        playAudio.setOnClickListener(view -> {
            final MediaPlayer cardAudio = MediaPlayer.create(this, db.getCardAudio(cardID));
            cardAudio.start();
        });


        if(cardID % 2 != 0){
            boolean availability = db.checkAvailability(cardID+1);

            if(!availability){//if the update card info is not stored in usercard table
                upgradeCard.setVisibility(View.VISIBLE);

                upgradeCard.setOnClickListener(view -> {
                    String currentTime = Calendar.getInstance().getTime().toString();

                    boolean upgrade = db.storeUpgradeCard(cardID, currentTime);
                    if (upgrade){
                        upgradeCard.setVisibility(View.GONE);
                        cardDiagram.setImageResource(db.getDiagram(cardID+1));
                    }else{
                        Toast errorToast = Toast.makeText(CardView.this, "Error", Toast.LENGTH_SHORT);
                        errorToast.show();
                    }
                });
            }else {
                upgradeCard.setVisibility(View.GONE);
            }
        }else{
            upgradeCard.setVisibility(View.GONE);
        }


        swapDiagram.setOnClickListener(view -> {

            boolean swap = db.swapCardDiagram(cardID);
            if (swap){
                if (cardID % 2 == 0 ){
                    cardDiagram.setImageResource(db.getDiagram(cardID-1));
                }else{
                    cardDiagram.setImageResource(db.getDiagram(cardID+1));
                }

            }
        });

    }
}