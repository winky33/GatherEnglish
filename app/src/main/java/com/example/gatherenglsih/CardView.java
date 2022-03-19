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
    public int currentCardId;

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

        //Set Card Title
        String cardTitle = db.getCardTitle(cardID);
        cardTitleTxt.setText(cardTitle);

        //Play Audio Function
        playAudio.setOnClickListener(view -> {
            final MediaPlayer cardAudio = MediaPlayer.create(this, db.getCardAudio(cardID));
            cardAudio.start();
        });

        //Upgrade Card Function
        if(cardID % 2 != 0){//if it is a LV1 card
            boolean availability = db.checkAvailability(cardID+1);

            if(!availability){//if the LV2 card info is not stored in usercard table
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
            } else {
                upgradeCard.setVisibility(View.GONE);
            }
        }else{
            upgradeCard.setVisibility(View.GONE);
        }

        //Swap Card Diagrams Function
        currentCardId = cardID;

        swapDiagram.setOnClickListener(view -> {

            boolean swap = db.swapCardDiagram(currentCardId);
            if (swap){
                if (currentCardId % 2 == 0 ){// if current card is LV2
                    currentCardId = currentCardId-1;
                }else{
                    currentCardId = currentCardId+1;
                }
                cardDiagram.setImageResource(db.getDiagram(currentCardId));
            }
        });

    }
}