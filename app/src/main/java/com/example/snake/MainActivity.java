package com.example.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String PREFS_NAME = "PREFS_NAME";
    private static final String MUSIC_STATE = "MUSIC_STATE";
    private SurfaceView surfaceView;
    private TextView scoreTv , recordTv,speedTv;
    private String movingPosition = "right";
    private final List<SnakePoints> snakePointsList = new ArrayList<>();
    private SurfaceHolder surfaceHolder;
    public int score = 0;
    private static int pointSize = 28;
    private static final int defaultTalePoints = 3;
    private static final int snakeColor = Color.GREEN;
    private static int snakeMovingSpeed = 500;
    private int positionX,positionY;
    private Timer timer;
    private Canvas canvas = null;
    private Paint pointcolor = null;
    private SharedPreferences pref;
    private final String key = "KEY";
    int size;
    MediaPlayer m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);
        scoreTv = findViewById(R.id.scoreTv);
        speedTv = findViewById(R.id.speedTv);

        final AppCompatImageButton topBtn = findViewById(R.id.topBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.leftBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.rightBtn);
        final AppCompatImageButton bottomBtn = findViewById(R.id.bottomBtn);

        surfaceView.getHolder().addCallback(this);

        SharedPreferences sharedPreferences2 = getSharedPreferences("myPrefs", MODE_PRIVATE);
        pointSize = sharedPreferences2.getInt("KEY_SEEK", 28);

        topBtn.setOnClickListener(view -> {
            if(!movingPosition.equals("bottom")) {
                movingPosition = "top";
            }
        });
        leftBtn.setOnClickListener(view -> {
            if(!movingPosition.equals("right")) {
                movingPosition = "left";
            }
        });
        rightBtn.setOnClickListener(view -> {
            if(!movingPosition.equals("left")) {
                movingPosition = "right";
            }
        });
        bottomBtn.setOnClickListener(view -> {
            if(!movingPosition.equals("top")) {
                movingPosition = "bottom";
            }
        });
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
    private void init(){
        m = MediaPlayer.create(this, R.raw.ggg);
        m.start();
        m.setLooping(true);
        snakePointsList.clear();
        scoreTv.setText("0");
        snakeMovingSpeed = 500;
        score = 0;
        movingPosition = "right";

        int startPositionX = (pointSize) * defaultTalePoints;

        for (int i = 0; i < defaultTalePoints; i++){
            SnakePoints snakePoints = new SnakePoints(startPositionX,pointSize);
            snakePointsList.add(snakePoints);

            startPositionX = startPositionX - (pointSize * 2);
        }

        addPoint();
        moveSnake();
    }
    private void addPoint(){
        int surfaceWidth = surfaceView.getWidth() - (pointSize * 2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize * 2);

        int randomXPosition = new Random().nextInt(surfaceWidth / pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight / pointSize);

        if (randomXPosition % 2 == 1){
            randomXPosition++;
        }

        if (randomYPosition % 2 == 1){
            randomYPosition++;
        }
        positionX = (pointSize * randomXPosition) + pointSize;
        positionY = (pointSize * randomYPosition) + pointSize;
    }
    private void moveSnake(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int headPositionX = snakePointsList.get(0).getPositionX();
                int headPositionY = snakePointsList.get(0).getPositionY();

                if (headPositionX == positionX && positionY == headPositionY){
                    growSnake();
                    addPoint();
                    if (snakeMovingSpeed<900){
                        snakeMovingSpeed+=40;
                    }

                    timer.purge();
                    timer.cancel();
                    moveSnake();
                }
                switch (movingPosition){
                    case "right":
                        snakePointsList.get(0).setPositionX(headPositionX + (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                        break;
                    case "left":
                        snakePointsList.get(0).setPositionX(headPositionX - (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                        break;
                    case "top":
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY - ((pointSize * 2)));
                        break;
                    case "bottom":
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY + (pointSize * 2));
                        break;
                }
                if (checkGameOver(headPositionX,headPositionY)){
                    timer.purge();
                    timer.cancel();

                    Context context;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Игра окончена");
                    builder.setMessage("Ваши очки:" + score);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Играть снова", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in = new Intent(MainActivity.this, MenuActivity.class);
                            in.putExtra("key", score);
                            init();
                        }
                    });
                    builder.setNegativeButton("В меню", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m.stop();
                            m.release();
                            Intent intent10 = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent10);
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                }

                else {
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                    canvas.drawCircle(snakePointsList.get(0).getPositionX(), snakePointsList.get(0).getPositionY(),pointSize,createPointColor());
                    canvas.drawCircle(positionX,positionY,pointSize,createPointColor());

                    for (int i = 1; i < snakePointsList.size(); i++) {
                        int getTempPositionX = snakePointsList.get(i).getPositionX();
                        int getTempPositionY = snakePointsList.get(i).getPositionY();

                        snakePointsList.get(i).setPositionX(headPositionX);
                        snakePointsList.get(i).setPositionY(headPositionY);
                        canvas.drawCircle(snakePointsList.get(i).getPositionX(), snakePointsList.get(i).getPositionY(),pointSize,createPointColor());

                        headPositionX = getTempPositionX;
                        headPositionY = getTempPositionY;
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        },1000-snakeMovingSpeed, 1000-snakeMovingSpeed);
    }
    private void growSnake(){
        SnakePoints snakePoints = new SnakePoints(0,0);
        snakePointsList.add(snakePoints);
        score++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreTv.setText(String.valueOf(score));
                speedTv.setText(String.valueOf("Скорость:" + snakeMovingSpeed+" "+"Размер:"+pointSize));
            }
        });
    }
    private boolean checkGameOver( int headPositionX, int headPositionY){
        boolean gameOver = false;
        if (snakePointsList.get(0).getPositionX() < 0 || snakePointsList.get(0).getPositionY() < 0 || snakePointsList.get(0).getPositionX() >= surfaceView.getWidth() || snakePointsList.get(0).getPositionY() >= surfaceView.getHeight()) {
            m.stop();
            gameOver = true;
            SharedPreferences sharedPreferences = getSharedPreferences("SCORE", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("SCORE",score);
            editor.apply();
        }
        else {
            for (int i = 0; i < snakePointsList.size(); i++) {
                if (headPositionX == snakePointsList.get(i).getPositionX() && headPositionY == snakePointsList.get(i).getPositionY()){
                    gameOver = true;
//                    Intent intent = new Intent(this, MenuActivity.class);
//                    intent.putExtra("score", score);  // Используйте "score" как ключ
//                    startActivity(intent);
                    SharedPreferences sharedPreferences = getSharedPreferences("SCORE", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("SCORE",score);
                    editor.apply();
                    finish();
                    break;
                }
            }
        }
        return gameOver;
    }
    private Paint createPointColor(){
        if (pointcolor == null){
            pointcolor = new Paint();
            pointcolor.setColor(snakeColor);
            pointcolor.setStyle(Paint.Style.FILL);
            pointcolor.setAntiAlias(true);

        }
        return pointcolor;
    }
}