package com.example.gatherenglsih;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        DBHelper db = new DBHelper(getActivity());

        //retrieve userID
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        String uuid = sharedPrefs.getString(PREF_UNIQUE_ID, null);

        //set homepage welcome text
        TextView homeText = rootView.findViewById(R.id.homeText);
        homeText.setText("Welcome, "+db.getUserName(uuid));

        Button gather_card_btn = rootView.findViewById(R.id.gathercard_btn);
        Button gallery_btn = rootView.findViewById(R.id.gallery_btn);
        Button exercise_btn = rootView.findViewById(R.id.exercise_btn);

        gather_card_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), GatherCard.class);
            startActivity(intent);
        });

        gallery_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CardGallery.class);
            startActivity(intent);
        });

        exercise_btn.setOnClickListener(view -> {
            boolean exerciseRequirement = db.checkCardQty();

            if (exerciseRequirement){
                exercise_btn.setClickable(true);
                Intent intent = new Intent(getActivity(), Exercises.class);
                startActivity(intent);
            }else{
                exercise_btn.setClickable(false);
                new AlertDialog.Builder(getActivity())
                        .setTitle("Alert")
                        .setMessage("Please collect at least 5 Flashcard to unlock the Exercise")
                        .setCancelable(false).setPositiveButton("Go to Gather Card", (dialogInterface, i) -> startActivity(new Intent(getActivity(),GatherCard.class))).show();
            }
        });

        return rootView;
    }
}