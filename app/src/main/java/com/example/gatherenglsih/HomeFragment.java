package com.example.gatherenglsih;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gatherenglsih.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static String uuid;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        DBHelper db = new DBHelper(getActivity());

        //retrieve userID
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        uuid = sharedPrefs.getString(PREF_UNIQUE_ID, null);

        //set homepage welcome text
        TextView homeText = (TextView) rootView.findViewById(R.id.homeText);
        homeText.setText("Welcome, "+db.getUserName(uuid));

        Button gather_card_btn = (Button) rootView.findViewById(R.id.gathercard_btn);
        Button gallery_btn = (Button) rootView.findViewById(R.id.gallery_btn);
        Button exercise_btn = (Button) rootView.findViewById(R.id.exercise_btn);

        gather_card_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GatherCard.class);
                startActivity(intent);
            }
        });

        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CardGallery.class);
                startActivity(intent);
            }
        });

        exercise_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Exercises.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}