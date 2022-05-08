package com.example.feetflee.helpers;

import java.util.ArrayList;
import java.util.Collections;

public class WinnersManager {

    private ArrayList<Winner> Winners;
    private static WinnersManager WinnersManager;

    public static WinnersManager getWinnersManager() {
        return WinnersManager;
    }


    public WinnersManager() {
        this.Winners =  new ArrayList<>();
    }

    public static WinnersManager initWinnersManager() {
        if (WinnersManager == null) {
            WinnersManager = new WinnersManager();
        }
        return WinnersManager;
    }

    public void addWinner (Winner Winner) {
        Winners.add(Winner);
    }

    public Winner getSpecificWinner (int position) {
        return Winners.get(position);
    }

    public ArrayList<Winner> getWinners() {
        return Winners;
    }

    public WinnersManager setWinners(ArrayList<Winner> Winners) {
        this.Winners = Winners;
        return this;
    }

    public void  sortByScore () {
        Collections.sort(Winners);
    }
}
