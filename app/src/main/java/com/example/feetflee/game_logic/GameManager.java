package com.example.feetflee.game_logic;

import static com.example.feetflee.activities.MainActivity.DELAY;

import java.util.Random;

public class GameManager {
    public static final int MAX_ROW_SENSORS = 8;
    public static final int MAX_COLUMN = 2;
    public static final int MAX_ROW_BUTTONS = 4;
    private Man man;
    private Feet feet;
    private GridItem coin;
    private Random rnd;
    private String gameType;
    private int stepsCounter;
    private int score;
    private int lives;
    private boolean strike;
    private boolean isDead;
    private boolean gotCoin;
    private boolean showCoin;
    private int rowCount;



    public GameManager(){
    }

    public GameManager(String gameType){
        this.gameType = gameType;

        if(gameType.equals("1"))
            this.rowCount = MAX_ROW_BUTTONS;
        else
            this.rowCount = MAX_ROW_SENSORS;

        init();
    }

    public void init(){
        man = new Man(gameType);
        feet = new Feet(gameType);
        rnd = new Random();
        initCoin();
        stepsCounter = 0;
        score = 0;
        lives = 3;
        strike = false;
        gotCoin = false;
        isDead = false;
        showCoin = false;
    }

    public void initPlayers(){
        man.init(gameType);
        feet.init(gameType);
    }

    public Man getMan() {
        return man;
    }

    public Feet getFeet() {
        return feet;
    }

    public GridItem getCoin() {return coin;}

    public int getStepsCounter() {
        return stepsCounter;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean isStrike() {
        return strike;
    }

    public void setStrike(boolean status){
        this.strike = status;
    }

    public boolean isDead() {
        return isDead;
    }

    public void newFeetDirection(int noGo){
        int nextDir = 0;
        do {
            nextDir = rnd.nextInt(5);
        } while (nextDir == noGo);
        feet.setDir(nextDir);
    }

    public void feetStep(){
        int coinRow = coin.getRow();
        int coinColumn = coin.getColumn();
        int currentColumn = feet.getColumn();
        int currentRow = feet.getRow();

        //Every 5 steps change feet's direction.
        if(stepsCounter % 5 == 0){
            DIRECTION current = feet.getDir();
            newFeetDirection(feet.getDir().ordinal());
            if (current != feet.getDir()) {
                return;
            }
        }

        DIRECTION currentDir = feet.getDir();
        switch (currentDir) {
            case UP:
                if(currentRow > 0) {
                    if(feet.getColumn() == coinColumn  && coin.getRow() == feet.getRow() - 1){
                        newFeetDirection(feet.getDir().ordinal());
                        feetStep();
                        break;
                    }
                    feet.decreaseRow();
                }
                else{
                    newFeetDirection(currentDir.ordinal());
                }
                break;
            case DOWN:
                if(currentRow < rowCount) {
                    if(feet.getColumn() == coinColumn  && coin.getRow() == feet.getRow() + 1){
                        newFeetDirection(feet.getDir().ordinal());
                        feetStep();
                        break;
                    }
                    feet.increaseRow();
                }
                else{
                    newFeetDirection(currentDir.ordinal());
                }
                break;
            case LEFT:
                if(currentColumn > 0) {
                    if(feet.getColumn() - 1 == coinColumn  && coin.getRow() == feet.getRow()){
                        newFeetDirection(feet.getDir().ordinal());
                        feetStep();
                        break;
                    }
                    feet.decreaseColumn();
                }
                else{
                    newFeetDirection(currentDir.ordinal());
                }
                break;
            case RIGHT:
                if(currentColumn < MAX_COLUMN) {
                    if(feet.getColumn() + 1 == coinColumn  && coin.getRow() == feet.getRow()){
                        newFeetDirection(feet.getDir().ordinal());
                        feetStep();
                        break;
                    }
                    feet.increaseColumn();
                }
                else {
                    newFeetDirection(currentDir.ordinal());
                }
                break;
        }
    }

    public void manStep(int manDir){
        DIRECTION currentDir = man.getDir();
        if (manDir != -1 && manDir != currentDir.ordinal()){
            man.setDir(manDir);
        }
        else {
            int currentColumn = man.getColumn();
            int currentRow = man.getRow();
            switch (currentDir) {
                case UP:
                    if(currentRow > 0) {
                        man.decreaseRow();
                    }
                    break;
                case DOWN:
                    if(currentRow < rowCount) {
                        man.increaseRow();
                    }
                    break;
                case LEFT:
                    if(currentColumn > 0) {
                        man.decreaseColumn();
                    }
                    break;
                case RIGHT:
                    if(currentColumn < MAX_COLUMN) {
                        man.increaseColumn();
                    }
                    break;
            }
        }
    }

    public void stepGame(int manDir){
        gotCoin = false;
        int stepsPerSec =(int) 1000 / DELAY;
        int manRowBefore = man.getRow();
        int manColumnBefore = man.getColumn();
        int feetRowBefore = feet.getRow();
        int feetColumnBefore = feet.getColumn();
        manStep(manDir);
        feetStep();
        
        if(( man.getColumn() == feet.getColumn() && man.getRow() == feet.getRow() )||
                (manRowBefore == feet.getRow() && manColumnBefore == feet.getColumn() &&
                        feetColumnBefore == man.getColumn() && feetRowBefore == man.getRow())){
            strike = true;
            lives --;
            if(lives == 0) {
                isDead = true;
            }
        }
        else{
            strike = false;
            if(stepsCounter % stepsPerSec == 0 )
                score += 10;
        }

        if(man.getRow() == coin.getRow() && man.getColumn() == coin.getColumn() && showCoin){
            gotCoin = true;
            score += 50;
            showCoin = false;
        }

        //change coin position every 7 seconds
        if(stepsCounter / stepsPerSec == 7 && stepsCounter % stepsPerSec == 0){
            initCoin();
            showCoin = true;
        }

        stepsCounter++;

    }

    public boolean isShowCoin(){
        return showCoin;
    }
    public boolean isGotCoin(){
        return gotCoin;
    }

    private void initCoin(){
        coin = new GridItem();
        coin.setColumn(rnd.nextInt(MAX_COLUMN + 1 ))
                .setRow(rnd.nextInt(rowCount + 1));

    }



}
