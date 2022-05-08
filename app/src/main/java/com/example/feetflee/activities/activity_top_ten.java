package com.example.feetflee.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.feetflee.R;
import com.example.feetflee.fragments.CallBack_ListPlayerClicked;
import com.example.feetflee.fragments.CallBack_Map;
import com.example.feetflee.fragments.ToptenListFragment;
import com.example.feetflee.fragments.MapsFragment;
import com.example.feetflee.helpers.MSPV3;
import com.example.feetflee.helpers.WinnersManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;


public class activity_top_ten extends AppCompatActivity {

    private ToptenListFragment fragmentList;
    private MapsFragment fragmentMap;
    private WinnersManager md;
    private FloatingActionButton fab_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        fab_back = findViewById(R.id.fab_back);

        fab_back.setOnClickListener(view -> {
            finish();
        });

        fragmentMap = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.maps_frame_layout , fragmentMap)
                .commit();

        fragmentList = new ToptenListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.topten_frame_layout , fragmentList)
                .commit();

        String js = MSPV3.getInstance().getString("DB", "");
        md = new Gson().fromJson(js, WinnersManager.class);


        fragmentList.setCallBack_listPlayerClicked(new CallBack_ListPlayerClicked() {
            @Override
            public void locateOnMap(double lat, double lon, String playerName) {
                fragmentMap.pointOnMap(lat, lon, playerName);
            }
        });


    }

}