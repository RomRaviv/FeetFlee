package com.example.feetflee.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.feetflee.R;
import com.example.feetflee.helpers.MSPV3;
import com.example.feetflee.helpers.Winner;
import com.example.feetflee.helpers.WinnersManager;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;


public class ToptenListFragment extends Fragment {

    private MaterialButton winners[];

    private CallBack_ListPlayerClicked callBack_listPlayerClicked;

    public void setCallBack_listPlayerClicked(CallBack_ListPlayerClicked callBack_listPlayerClicked) {
        this.callBack_listPlayerClicked = callBack_listPlayerClicked;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topten_list, container, false);
        findViews(view);
        initViews();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        String js = MSPV3.getInstance().getString("DB", "");
        WinnersManager winnersManager = new Gson().fromJson(js, WinnersManager.class);
        if(winnersManager == null)
            return;
        winnersManager.sortByScore();
        for (int i = 0; i < winnersManager.getWinners().size(); i++) {
            Winner winner = winnersManager.getSpecificWinner(i);
            winners[i].setVisibility(View.VISIBLE);
            winners[i].setText(i+1+". "+winner.getName()+"\n  Score: "+winner.getScore());
            winners[i].setOnClickListener(v -> callBack_listPlayerClicked.locateOnMap(winner.getLat(),winner.getLon(), winner.getName()));

        }
    }

    private void findViews(View view) {
        winners= new MaterialButton[]{
                view.findViewById(R.id.fList_BTN_place1),
                view.findViewById(R.id.fList_BTN_place2),
                view.findViewById(R.id.fList_BTN_place3),
                view.findViewById(R.id.fList_BTN_place4),
                view.findViewById(R.id.fList_BTN_place5),
                view.findViewById(R.id.fList_BTN_place6),
                view.findViewById(R.id.fList_BTN_place7),
                view.findViewById(R.id.fList_BTN_place8),
                view.findViewById(R.id.fList_BTN_place9),
                view.findViewById(R.id.fList_BTN_place10),
        };
    }
}
