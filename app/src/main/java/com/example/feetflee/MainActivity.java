package com.example.feetflee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int DELAY = 500;
    private final Handler handler = new Handler();
    private LinearLayout hearts;
    private TextView txtScore;
    private ImageView imgBtnUp;
    private ImageView imgBtnDown;
    private ImageView imgBtnRight;
    private ImageView imgBtnLeft;
    private GameManager gameManager;
    private DIRECTION manDir;
    private ArrayList<LinearLayout> grid;
    private Runnable r;
    private TIMER_STATUS timerStatus;
    private int score;
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        score = 0;
        txtScore= findViewById(R.id.txt_score);
        hearts = findViewById(R.id.linear_hearts);
        imgBtnUp = findViewById(R.id.img_btn_up);
        imgBtnDown = findViewById(R.id.img_btn_down);
        imgBtnLeft = findViewById(R.id.img_btn_left);
        imgBtnRight = findViewById(R.id.img_btn_right);
        LinearLayout linearGrid = findViewById(R.id.linear_grid);

        gameManager = new GameManager();
        grid = new ArrayList<LinearLayout>();
        manDir = DIRECTION.UP;
        timerStatus = TIMER_STATUS.OFF;

        //load the grid.
        for (int i = 0; i<linearGrid.getChildCount(); i++){
            grid.add((LinearLayout)linearGrid.getChildAt(i));
        }

        setManVisibleByPosition();
        setFeetVisibleByPosition();

        imgBtnUp.setOnClickListener(view -> { directionButtonClicked(DIRECTION.UP); });
        imgBtnDown.setOnClickListener(view -> { directionButtonClicked(DIRECTION.DOWN); });
        imgBtnRight.setOnClickListener(view -> { directionButtonClicked(DIRECTION.RIGHT); });
        imgBtnLeft.setOnClickListener(view -> { directionButtonClicked(DIRECTION.LEFT); });


        r = new Runnable() {
            public void run() {
                gameStep();
                handler.postDelayed(r, DELAY);
            }
        };

        startTimer();
    }

    public void gameStep(){

        if(gameManager.isStrike()){
            setInvisibleByPosition(gameManager.getMan().getRow(),gameManager.getMan().getColumn());
            setInvisibleByPosition(gameManager.getFeet().getRow(),gameManager.getFeet().getColumn());
            gameManager.initPlayers();
            gameManager.setStrike(false);
            setManVisibleByPosition();
            setFeetVisibleByPosition();
        }
        else if(gameManager.isDead()){
            finish();
        }
        else {
            setInvisibleByPosition(gameManager.getMan().getRow(), gameManager.getMan().getColumn());
            setInvisibleByPosition(gameManager.getFeet().getRow(), gameManager.getFeet().getColumn());
            gameManager.stepGame(manDir.ordinal());
            if (gameManager.isStrike()) {
                strike();
            } else {
                setManVisibleByPosition();
                setFeetVisibleByPosition();
            }
        }
        txtScore.setText("" + gameManager.getScore());
    }


    public void directionButtonClicked(DIRECTION dir){
        ImageView imgView =(ImageView) grid.get(gameManager.getMan().getRow()).getChildAt(gameManager.getMan().getColumn());
        switch (dir) {
            case UP:
                imgView.setImageResource(R.drawable.ic_beard_man_up);
                manDir = DIRECTION.UP;
                break;
            case DOWN:
                imgView.setImageResource(R.drawable.ic_beard_man_down);
                manDir = DIRECTION.DOWN;
                break;
            case LEFT:
                imgView.setImageResource(R.drawable.ic_beard_man_left);
                manDir = DIRECTION.LEFT;
                break;
            case RIGHT:
                imgView.setImageResource(R.drawable.ic_beard_man_right);
                manDir = DIRECTION.RIGHT;
                break;
        }
    }

    public void setManVisibleByPosition(){
        int manRow=gameManager.getMan().getRow();
        int manColumn=gameManager.getMan().getColumn();

        ImageView imgView =(ImageView) grid.get(manRow).getChildAt(manColumn);
        switch (manDir) {
            case UP:
                imgView.setImageResource(R.drawable.ic_beard_man_up);
                break;
            case DOWN:
                imgView.setImageResource(R.drawable.ic_beard_man_down);
                break;
            case LEFT:
                imgView.setImageResource(R.drawable.ic_beard_man_left);
                break;
            case RIGHT:
                imgView.setImageResource(R.drawable.ic_beard_man_right);
                break;
        }
        grid.get(manRow).getChildAt(manColumn).setVisibility(View.VISIBLE);
    }

    public void setFeetVisibleByPosition(){
        int feetRow=gameManager.getFeet().getRow();
        int feetColumn=gameManager.getFeet().getColumn();

        ImageView imgView =(ImageView) grid.get(feetRow).getChildAt(feetColumn);
        switch (gameManager.getFeet().getDir()) {
            case UP:
                imgView.setImageResource(R.drawable.ic_feet_up);
                break;
            case DOWN:
                imgView.setImageResource(R.drawable.ic_feet_down);
                break;
            case LEFT:
                imgView.setImageResource(R.drawable.ic_feet_left);
                break;
            case RIGHT:
                imgView.setImageResource(R.drawable.ic_feet_right);
                break;
        }
        grid.get(feetRow).getChildAt(feetColumn).setVisibility(View.VISIBLE);
    }


    public void setInvisibleByPosition(int row, int column){
        grid.get(row).getChildAt(column).setVisibility(View.INVISIBLE);
    }

    private void startTimer() {
        timerStatus = TIMER_STATUS.RUNNING;
        handler.postDelayed(r, DELAY);
    }

    private void stopTimer() {
        handler.removeCallbacks(r);
    }

    private void strike(){
        ImageView imgViewMan =(ImageView) grid.get(gameManager.getMan().getRow()).getChildAt(gameManager.getMan().getColumn());
        ImageView imgViewFeet =(ImageView) grid.get(gameManager.getFeet().getRow()).getChildAt(gameManager.getFeet().getColumn());

        imgViewMan.setImageResource(R.drawable.ic_puking);
        imgViewMan.setVisibility(View.VISIBLE);

        hearts.getChildAt(gameManager.getLives()).setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.EFFECT_TICK));
        } else {
            vib.vibrate(100);
        }


    }
}