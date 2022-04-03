package com.example.gatherenglsih;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

public class CardView extends AppCompatActivity {
    DBHelper db = new DBHelper(CardView.this);
    TextToSpeech textToSpeech;

    //widgets
    ImageView cardDiagram, exitBtn;
    ImageButton playAudio, upgradeCard, swapDiagram;
    TextView coinTxt, cardTitleTxt;

    //vars
    public int coinAmount;
    public int selectedImage;
    public int currentCardId;
    public String uuid;

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
            coinAmount = intent.getIntExtra("coinAmount",0);
            uuid = intent.getStringExtra("userID");

            coinTxt.setText(String.valueOf(coinAmount));
            cardDiagram.setImageResource(selectedImage);
        }
        int cardID = db.getCardIDbyDiagram(String.valueOf(selectedImage));

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){//if no error found
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        //Set Card Title
        String cardTitle = db.getCardTitle(cardID);
        cardTitleTxt.setText(cardTitle);

        //Play Audio Function
        playAudio.setOnClickListener(view -> {
            textToSpeech.speak(cardTitle,TextToSpeech.QUEUE_FLUSH,null);
        });

        //Upgrade Card Function
        if(cardID % 2 != 0){//if it is a LV1 card
            boolean availability = db.checkAvailability(cardID+1);

            if(!availability){//if the LV2 card info is not stored in usercard table
                upgradeCard.setVisibility(View.VISIBLE);

                upgradeCard.setOnClickListener(view -> {
                    if (coinAmount >= 5){
                        String currentTime = Calendar.getInstance().getTime().toString();
                        coinAmount = coinAmount- 5;

                        db.updateCoinAmount(uuid, coinAmount);

                        boolean upgrade = db.storeUpgradeCard(cardID, currentTime);
                        if (upgrade){
                            upgradeCard.setVisibility(View.GONE);
                            cardDiagram.setImageResource(db.getDiagram(cardID+1));
                            coinTxt.setText(String.valueOf(coinAmount));
                        }else{
                            Toast errorToast = Toast.makeText(CardView.this, "Error", Toast.LENGTH_SHORT);
                            errorToast.show();
                        }
                    }else{
                        new AlertDialog.Builder(CardView.this)
                                .setTitle("Alert")
                                .setMessage("Coin Amount Insufficient, Please collect it by completing Exercises.")
                                .setCancelable(true).setPositiveButton("Go to Exercise", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(CardView.this,Exercises.class));
                            }
                        }).show();
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