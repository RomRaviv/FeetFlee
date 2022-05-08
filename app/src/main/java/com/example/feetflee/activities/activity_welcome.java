package com.example.feetflee.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.feetflee.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class activity_welcome extends AppCompatActivity {
    private MaterialButton btn_play;
    private MaterialButton btn_topten;
    private TextInputEditText text_name;
    private CheckBox checkBoxGameType;
    private Intent mainActivity;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findView();
        initView();
        changeViewByBTN();

    }

    private void initView() {
        bundle = new Bundle();
        mainActivity = new Intent(this, MainActivity.class);
    }

    void changeViewByBTN(){
        btn_topten.setOnClickListener(v -> startActivity(new Intent(this, activity_top_ten.class)));

        btn_play.setOnClickListener(v -> {
            if(sendDataToActivity()) {
                startActivity(mainActivity);
            }
        });
    }


    public boolean sendDataToActivity () {
        String namePlayer;
        String gameType;
        if(checkBoxGameType.isChecked()) {
            gameType = "1";
        }
        else {
            gameType = "0";
        }
        if (!text_name.getText().toString().equals("")  && !text_name.getText().toString().equals("Enter your name")){
            namePlayer = text_name.getText().toString();

            bundle.putString(MainActivity.NAME, namePlayer);
            bundle.putInt(MainActivity.GAME_TYPE, Integer.parseInt(gameType));
            mainActivity.putExtras(bundle);

            return true;

        }
        else {
            Toast.makeText(this, "Must enter a name! ", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void findView() {
        checkBoxGameType = findViewById(R.id.checkBox_gameType);
        btn_play =findViewById(R.id.start_fab);
        btn_topten =findViewById(R.id.top_ten_fab);
        text_name = findViewById(R.id.edt_name);
    }


    }