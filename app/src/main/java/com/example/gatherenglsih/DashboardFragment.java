package com.example.gatherenglsih;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DashboardFragment extends Fragment {

    //widgets
    private ProgressBar totalCardCollect, totalCardUpgrade;
    private TextView totalCardCollectTV, totalCardUpgradeTV;

    //vars
    private static String uuid;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private int noCollectedCard, noTotalCard, noUpgradeCard, i=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        DBHelper db = new DBHelper(getActivity());

        //retrieve userID
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        uuid = sharedPrefs.getString(PREF_UNIQUE_ID, null);

        //set dashboard username
        TextView homeText = (TextView) rootView.findViewById(R.id.dashboardText);
        homeText.setText(db.getUserName(uuid));

        //set total collected flashcard progress bar
        totalCardCollect = rootView.findViewById(R.id.total_collect_progress);
        totalCardCollectTV = rootView.findViewById(R.id.total_collect_tv);

        noTotalCard = db.getTotalCardAmount();
        noCollectedCard = db.getCollectedCardQty();


        totalCardCollectTV.setText("Flashcard \nCollected \n"+noCollectedCard+"/"+noTotalCard);

        while (i <= noCollectedCard) {
            int currentProgress = 0;
            currentProgress = currentProgress + i;
            totalCardCollect.setProgress(currentProgress);
            totalCardCollect.setMax(noTotalCard);
            i++;
        }


        return rootView;
    }
}