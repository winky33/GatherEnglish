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

        //set total collected/upgraded flashcard progress bar
        totalCardCollect = rootView.findViewById(R.id.total_collect_progress);
        totalCardCollectTV = rootView.findViewById(R.id.total_collect_tv);
        totalCardUpgrade = rootView.findViewById(R.id.total_upgrade_progress);
        totalCardUpgradeTV = rootView.findViewById(R.id.total_upgrade_tv);
        noTotalCard = db.getTotalCardAmount();
        noUpgradeCard = db.getUpgradedCardQty();
        noCollectedCard = db.getCollectedCardQty();

        flashcardProgresses();



        return rootView;
    }

    public void flashcardProgresses(){
        final Handler collectedHandler = new Handler();
        collectedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (i <= noCollectedCard) {
                    totalCardCollectTV.setText(i+"/"+noTotalCard);;
                    totalCardCollect.setProgress(i);
                    totalCardCollect.setMax(noTotalCard);
                    i++;
                    collectedHandler.postDelayed(this, 100);
                } else {
                    collectedHandler.removeCallbacks(this);
                }
            }
        }, 100);

        final Handler upgradedHandler = new Handler();
        upgradedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (i <= noUpgradeCard) {
                    totalCardUpgradeTV.setText(i+"/"+noTotalCard);;
                    totalCardUpgrade.setProgress(i);
                    totalCardUpgrade.setMax(noTotalCard);
                    i++;
                    upgradedHandler.postDelayed(this, 100);
                } else {
                    upgradedHandler.removeCallbacks(this);
                }
            }
        }, 100);
    }

}