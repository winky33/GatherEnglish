package com.example.gatherenglsih;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.UUID;
import com.example.gatherenglsih.DBHelper;

public class LaunchPage extends AppCompatActivity {
    DBHelper db = new DBHelper(LaunchPage.this);
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    Window window;

    //widgets
    Button btn_continue;
    EditText name;

    //vars
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchpage);

        String uuid = id(this);

        View lay = findViewById(R.id.view_to_listen_for_touch);

        lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(uuid);
            }
        });

    }

    public void login(String uuid){
        Boolean userID = db.getUserID(uuid);

        if (userID == false){
            dialogBuilder = new AlertDialog.Builder(this);
            final View loginPopupView = getLayoutInflater().inflate(R.layout.popup_window,null);

            btn_continue = loginPopupView.findViewById(R.id.continue_btn);
            name = loginPopupView.findViewById(R.id.name);

            dialogBuilder.setView(loginPopupView);
            dialog = dialogBuilder.create();
            dialog.show();

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

            btn_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nameTXT = name.getText().toString();
                    String currentTime = Calendar.getInstance().getTime().toString();

                    if(nameTXT.isEmpty()){
                        Toast.makeText(LaunchPage.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;

                    }else{
                        boolean addNew = db.addNewUser(uuid, nameTXT, currentTime);

                        if (addNew == true){
                            startActivity(new Intent(LaunchPage.this, Homepage.class));
                        }
                    }
                }
            });

        }else{
            startActivity(new Intent(LaunchPage.this, Homepage.class));
        }
    }

    public synchronized static String id(Context context){
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                ((SharedPreferences.Editor) editor).putString(PREF_UNIQUE_ID, uniqueID);
                editor.apply();
            }
        }
        return uniqueID;
    }

}