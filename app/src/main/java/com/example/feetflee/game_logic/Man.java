package com.example.feetflee.game_logic;

public class Man extends GridItem{

    public Man(){
    }

    public Man(String gameType){
        init(gameType);
    }

    public void init(String gameType){
        if(gameType.equals("1"))
            setRow(GameManager.MAX_ROW_BUTTONS);
        else
            setRow(GameManager.MAX_ROW_SENSORS);

        setColumn(1);
        setDir(0);
    }




}
