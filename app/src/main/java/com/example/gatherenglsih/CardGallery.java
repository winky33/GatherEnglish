package com.example.gatherenglsih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class CardGallery extends AppCompatActivity {
    //widgets
    GridView gridView;
    ImageView exitBtn;
    TextView coinAmount;

    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_gallery);

        DBHelper db = new DBHelper(CardGallery.this);
        ArrayList<Integer> images = db.getFlashcardDiagrams();

        exitBtn = findViewById(R.id.exitBtn);
        coinAmount = findViewById(R.id.coinText);
        gridView = findViewById(R.id.cards_gridView);

        //retrieve userID
        SharedPreferences sharedPrefs = this.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        //vars
        String uuid = sharedPrefs.getString(PREF_UNIQUE_ID, null);

        //set coin amount
        int coin = db.getCoinAmount(uuid);
        coinAmount.setText(String.valueOf(coin));

        exitBtn.setOnClickListener(view -> startActivity(new Intent(CardGallery.this, Homepage.class)));

        //set flashcards gridview
        CustomAdapter customAdapter = new CustomAdapter(images, this);
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            int selectedImage = images.get(i);
            Intent intent = new Intent(CardGallery.this, CardView.class);
            intent.putExtra("diagram", selectedImage);
            intent.putExtra("coinAmount", coin);
            intent.putExtra("userID", uuid);

            startActivity(intent);
        });
    }

    public static class CustomAdapter extends BaseAdapter {
        private final ArrayList<Integer> imagesPhoto;
        private final Context context;
        private final LayoutInflater layoutInflater;

        public CustomAdapter(ArrayList<Integer> imagesPhoto, Context context){
            this.imagesPhoto = imagesPhoto;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return imagesPhoto.size();
        }

        @Override
        public Object getItem(int i){
            return null;
        }

        @Override
        public long getItemId(int i){
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup){

            if(view == null){
                view = layoutInflater.inflate(R.layout.row_items, viewGroup, false);

            }

            ImageView imageView = view.findViewById((R.id.card_view));

            imageView.setImageResource(imagesPhoto.get(i));

            return view;
        }

    }

}