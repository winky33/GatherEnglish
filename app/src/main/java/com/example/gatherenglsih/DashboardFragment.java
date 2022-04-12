package com.example.gatherenglsih;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    //widgets
    private ProgressBar totalCardCollect, totalCardUpgrade;
    private TextView totalCardCollectTV, totalCardUpgradeTV,
            totalSpellExe, spellExeCorAns, spellTotalQues,
            totalLisExe, lisExeCorAns, lisTotalQues,
            totalReadExe, readExeCorAns, readTotalQues;

    //vars
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private int noCollectedCard, noTotalCard, noUpgradeCard, i=0, c=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        DBHelper db = new DBHelper(getActivity());

        //retrieve userID
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);

        String uuid = sharedPrefs.getString(PREF_UNIQUE_ID, null);

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

        //set spelling exercises stats
        totalSpellExe = rootView.findViewById(R.id.spelling_exercise_completed_no);
        spellExeCorAns = rootView.findViewById(R.id.spelling_correct_no);
        spellTotalQues = rootView.findViewById(R.id.spelling_total_no);

        //set listening exercises stats
        totalLisExe = rootView.findViewById(R.id.listening_exercise_completed_no);
        lisExeCorAns = rootView.findViewById(R.id.listening_correct_no);
        lisTotalQues = rootView.findViewById(R.id.listening_total_no);

        //set reading exercises stats
        totalReadExe = rootView.findViewById(R.id.reading_exercise_completed_no);
        readExeCorAns = rootView.findViewById(R.id.reading_correct_no);
        readTotalQues = rootView.findViewById(R.id.reading_total_no);

        getExeStat(db);

        return rootView;
    }

    public void flashcardProgresses(){
        final Handler collectedHandler = new Handler();
        collectedHandler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (i <= noCollectedCard) {
                    totalCardCollectTV.setText(i+"/"+noTotalCard);
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
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (c <= noUpgradeCard) {
                    totalCardUpgradeTV.setText(c+"/"+noTotalCard);
                    totalCardUpgrade.setProgress(c);
                    totalCardUpgrade.setMax(noTotalCard);
                    c++;
                    upgradedHandler.postDelayed(this, 100);
                } else {
                    upgradedHandler.removeCallbacks(this);
                }
            }
        }, 100);
    }

    public void getExeStat(DBHelper db){
        //Spelling
        ArrayList<Integer> totalSpellingExe;
        totalSpellingExe = db.getExerciseStat("Spelling");

        totalSpellExe.setText(String.valueOf(totalSpellingExe.get(0)));
        spellExeCorAns.setText(String.valueOf(totalSpellingExe.get(1)));
        spellTotalQues.setText(String.valueOf(totalSpellingExe.get(2)));

        //Listening
        ArrayList<Integer> totalListeningExe;
        totalListeningExe = db.getExerciseStat("Listening");

        totalLisExe.setText(String.valueOf(totalListeningExe.get(0)));
        lisExeCorAns.setText(String.valueOf(totalListeningExe.get(1)));
        lisTotalQues.setText(String.valueOf(totalListeningExe.get(2)));

        //Reading
        ArrayList<Integer> totalReadingExe;
        totalReadingExe = db.getExerciseStat("Reading");

        totalReadExe.setText(String.valueOf(totalReadingExe.get(0)));
        readExeCorAns.setText(String.valueOf(totalReadingExe.get(1)));
        readTotalQues.setText(String.valueOf(totalReadingExe.get(2)));
    }

}