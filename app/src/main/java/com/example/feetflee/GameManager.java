package com.example.feetflee;

import static com.example.feetflee.MainActivity.DELAY;

import java.util.Random;

public class GameManager {
    private Man man;
    private Feet feet;
    private Random rnd;
    private int stepsCounter;
    private int score;
    private int lives;
    private boolean strike;
    private boolean isDead;
    static final int MAX_ROW = 4;
    static final int MAX_COLUMN = 2;

    public GameManager(){
        init();
    }

    public void init(){
        man = new Man();
        feet = new Feet();
        rnd = new Random();
        stepsCounter = 0;
        score = 0;
        lives = 3;
        strike = false;
    }

    public void initPlayers(){
        man.init();
        feet.init();
    }

    public Man getMan() {
        return man;
    }

    public Feet getFeet() {
        return feet;
    }

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
        int currentColumn = feet.getColumn();
        int currentRow = feet.getRow();

        //Every 3 steps change feet's direction.
        if(stepsCounter % 3 == 0){
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
                    feet.decreaseRow();
                }
                else{
                    newFeetDirection(currentDir.ordinal());
                }
                break;
            case DOWN:
                if(currentRow < MAX_ROW) {
                    feet.increaseRow();
                }
                else{
                    newFeetDirection(currentDir.ordinal());
                }
                break;
            case LEFT:
                if(currentColumn > 0) {
                    feet.decreaseColumn();
                }
                else{
                    newFeetDirection(currentDir.ordinal());
                }
                break;
            case RIGHT:
                if(currentColumn < MAX_COLUMN) {
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
                    if(currentRow < MAX_ROW) {
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
            if(stepsCounter%stepsPerSec == 0 )
                score += 10;
        }
        stepsCounter++;

    }




}
