package com.example.feetflee.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.feetflee.R;
import com.example.feetflee.game_logic.DIRECTION;
import com.example.feetflee.game_logic.GameManager;
import com.example.feetflee.game_logic.TIMER_STATUS;
import com.example.feetflee.helpers.Gyro;
import com.example.feetflee.helpers.LocationClass;
import com.example.feetflee.helpers.MSPV3;
import com.example.feetflee.helpers.Winner;
import com.example.feetflee.helpers.WinnersManager;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int DELAY = 500;
    public static final String NAME = "NAME";
    public static final String GAME_TYPE = "GAME_TYPE"; // 0 for sensors , 1 for buttons
    private MediaPlayer cokeSound;
    private MediaPlayer pukeSound;
    private final WinnersManager winnersManager = WinnersManager.initWinnersManager();
    private final Handler handler = new Handler();
    private Intent activity_top_ten;
    private LinearLayout hearts;
    private TextView txtScore;
    private GameManager gameManager;
    private DIRECTION manDir;
    private ArrayList<LinearLayout> grid;
    private Runnable r;
    private Vibrator vib;
    private Gyro gyro;
    private boolean done;
    private LocationClass locationClass;
    private TIMER_STATUS timerStatus;
    private String playerName;
    private String gameType;
    private ImageButton imgBtnUp;
    private ImageButton imgBtnDown;
    private ImageButton imgBtnRight;
    private ImageButton imgBtnLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDataFromWelcome();

        cokeSound = MediaPlayer.create(this, R.raw.food_drink_can_tab_pull_open);
        pukeSound = MediaPlayer.create(this , R.raw.cartoon_character_retch_vomit);

        activity_top_ten = new Intent(this, activity_top_ten.class);
        done = false;
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        txtScore= findViewById(R.id.txt_score);
        hearts = findViewById(R.id.linear_hearts);

        LinearLayout linearGrid;
        if(gameType.equals("1")) {
            linearGrid = findViewById(R.id.linear_grid_buttons);
            initButtons();
            findViewById(R.id.linear_grid_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.linear_grid_senors).setVisibility(View.INVISIBLE);
        }
        else {
            linearGrid = findViewById(R.id.linear_grid_senors);
            initGyro();
            findViewById(R.id.linear_grid_senors).setVisibility(View.VISIBLE);
            findViewById(R.id.linear_grid_buttons).setVisibility(View.INVISIBLE);
        }
        gameManager = new GameManager(gameType);
        grid = new ArrayList<LinearLayout>();
        manDir = DIRECTION.UP;
        timerStatus = TIMER_STATUS.OFF;

        //load the grid.
        for (int i = 0; i<linearGrid.getChildCount(); i++){
            grid.add((LinearLayout)linearGrid.getChildAt(i));
        }

        setManVisibleByPosition();
        setFeetVisibleByPosition();

        r = new Runnable() {
            public void run() {
                int k = manDir.ordinal();
                gameStep();
                if(!done)
                    handler.postDelayed(r, DELAY);
            }
        };

        //----- Get Location use permission and check -----
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationClass = new LocationClass(this);

        startTimer();
    }

    public void gameStep(){
        if(done)
            return;
        if(gameManager.isStrike()){
            setInvisibleByPosition(gameManager.getMan().getRow(),gameManager.getMan().getColumn());
            setInvisibleByPosition(gameManager.getFeet().getRow(),gameManager.getFeet().getColumn());
            gameManager.initPlayers();
            gameManager.setStrike(false);
            directionChange(DIRECTION.UP);
            setManVisibleByPosition();
            setFeetVisibleByPosition();
        }
        else if(gameManager.isDead()){
            gameOver();
        }
        else {
            setInvisibleByPosition(gameManager.getMan().getRow(), gameManager.getMan().getColumn());
            setInvisibleByPosition(gameManager.getFeet().getRow(), gameManager.getFeet().getColumn());
            gameManager.stepGame(manDir.ordinal());
            if (gameManager.isStrike()) {
                strike();
                initGyro();
            } else {
                setManVisibleByPosition();
                setFeetVisibleByPosition();
            }
            if(gameManager.isGotCoin()){
                cokeSound.start();
                vibrate();
                setInvisibleByPosition(gameManager.getCoin().getRow(), gameManager.getCoin().getColumn());
                setManVisibleByPosition();
            }
            if(gameManager.isShowCoin()){
                setCoinVisibleByPosition();
            }

        }
        txtScore.setText("" + gameManager.getScore());
        initGyro();
    }

    private void gameOver() {
        done = true;
        Winner winner = new Winner().setName(playerName).setScore(gameManager.getScore()).setLon(locationClass.getLon()).setLat(locationClass.getLat());
        winnersManager.addWinner(winner);

        String json = new Gson().toJson(winnersManager);
        MSPV3.getInstance().putString("DB", json);

        findViewById(R.id.linear_grid_buttons).setVisibility(View.INVISIBLE);
        findViewById(R.id.linear_grid_senors).setVisibility(View.INVISIBLE);

        startActivity(activity_top_ten);
        finish();

    }

    private void strike(){
        //setInvisibleByPosition(gameManager.getMan().getRow() ,gameManager.getMan().getColumn());
        pukeSound.start();
        ImageView imgViewMan =(ImageView) grid.get(gameManager.getMan().getRow()).getChildAt(gameManager.getMan().getColumn());
        imgViewMan.setImageResource(R.drawable.ic_puking);
        imgViewMan.setVisibility(View.VISIBLE);
        hearts.getChildAt(gameManager.getLives()).setVisibility(View.INVISIBLE);
        vibrate();

        initGyro();
        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // do something after 1s
            }

            @Override
            public void onFinish() {
                // do something end times 5s
            }

        }.start();

    }

    public void directionChange(@NonNull DIRECTION dir){
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

    private void startTimer() {
        timerStatus = TIMER_STATUS.RUNNING;
        handler.postDelayed(r, DELAY);
    }

    public void vibrate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.EFFECT_TICK));
        } else {
            vib.vibrate(100);
        }
    }

    public void initGyro() {
        gyro = new Gyro(this);
        gyro.setListener(new Gyro.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                if (ry > 1f) {
                    directionChange(DIRECTION.DOWN);
                }
                if (ry < -1f) {
                    directionChange(DIRECTION.UP);
                }
                if (rx > 1f) {
                    directionChange(DIRECTION.LEFT);
                }
                if (rx < -1f) {
                    directionChange(DIRECTION.RIGHT);
                }
            }
        });
    }

    public void setCoinVisibleByPosition(){
        int coinRow=gameManager.getCoin().getRow();
        int coinColumn=gameManager.getCoin().getColumn();

        ImageView imgView =(ImageView) grid.get(coinRow).getChildAt(coinColumn);
        imgView.setImageResource(R.drawable.ic_coke_zero);

        grid.get(coinRow).getChildAt(coinColumn).setVisibility(View.VISIBLE);
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

    private void getDataFromWelcome() {
        Bundle bundle = getIntent().getExtras();
        playerName = bundle.getString(NAME);
        gameType = String.valueOf(bundle.getInt(GAME_TYPE));

    }
    private void initButtons(){
        imgBtnUp = findViewById(R.id.img_btn_up);
        imgBtnDown = findViewById(R.id.img_btn_down);
        imgBtnRight = findViewById(R.id.img_btn_right);
        imgBtnLeft = findViewById(R.id.img_btn_left);
        imgBtnUp.setVisibility(View.VISIBLE);
        imgBtnDown.setVisibility(View.VISIBLE);
        imgBtnRight.setVisibility(View.VISIBLE);
        imgBtnLeft.setVisibility(View.VISIBLE);
        imgBtnUp.setOnClickListener(view -> { directionChange(DIRECTION.UP); });
        imgBtnDown.setOnClickListener(view -> { directionChange(DIRECTION.DOWN); });
        imgBtnRight.setOnClickListener(view -> { directionChange(DIRECTION.RIGHT); });
        imgBtnLeft.setOnClickListener(view -> { directionChange(DIRECTION.LEFT); });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //startTimer();
        if(gameType.equals("0"))
            gyro.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        done = true;
        if(gameType.equals("0"))
            gyro.unregister();
    }
}