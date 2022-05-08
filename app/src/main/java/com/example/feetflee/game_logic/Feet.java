package com.example.feetflee.game_logic;

public class Feet extends GridItem{

    public Feet(){
    }


    public Feet(String gameType){
        init(gameType);
    }

    public void init(String gameType) {

        if(gameType.equals("1"))
            setRow(GameManager.MAX_ROW_BUTTONS);
        else
            setRow(GameManager.MAX_ROW_SENSORS);
        setRow(0);
        setColumn(1);
        setDir(1);
    }










}
