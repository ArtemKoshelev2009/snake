package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
public class MenuActivity extends AppCompatActivity {
    private TextView recordTv, recordTv2;
    private final String KEY = "KEY";
    private final String KEY2 = "KEY2";
    MediaPlayer menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        int records = loadScore();
        int records1 = loadScore1();
        menu = MediaPlayer.create(this, R.raw.menu1);
        menu.start();
        menu.setLooping(true);

        recordTv = findViewById(R.id.recordTv);
        recordTv.setText("Ваш максимальный результат в «Классика»:" + records);
        recordTv2 = findViewById(R.id.recordTv2);
        recordTv2.setText("Ваш максимальный результат в «Рандом»: " + records1);

        int score = 0;
        int score1 = 0;

        SharedPreferences sharedPreferences = getSharedPreferences("SCORE", MODE_PRIVATE);
        score = sharedPreferences.getInt("SCORE", 0);
        if (score > records) {
            records = score;
            saveScore(records);
        }
        recordTv.setText("Ваш максимальный результат в «Классика»: " + records);
        SharedPreferences sharedPreferences2 = getSharedPreferences("SCORE1", MODE_PRIVATE);
        score1 = sharedPreferences2.getInt("SCORE1", 0);
        if (score1 > records1) {
            records1 = score1;
            saveScore1(records1);
        }
        recordTv2.setText("Ваш максимальный результат в «Рандом»: " + records1);



        Intent intent1 = new Intent(this, MainActivity2.class);
        intent1.putExtra("sound", R.raw.sound);
    }
    public void solveLinear(View view) {
        menu.stop();
        menu.release();
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }
    public void solveLinear2(View view) {
        menu.stop();
        menu.release();
        Intent intent3 = new Intent(this, MainActivity2.class);
        startActivity(intent3);
    }
    public void saveScore(int records) {
        SharedPreferences prefs = this.getSharedPreferences("game_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY, records);
        editor.apply();
    }
    public int loadScore() {
        SharedPreferences prefs = this.getSharedPreferences("game_preferences", Context.MODE_PRIVATE);
        return prefs.getInt(KEY, 0);
    }
    public void saveScore1(int records1) {
        SharedPreferences prefs = this.getSharedPreferences("game_preferences2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY2, records1);
        editor.apply();
    }
    public int loadScore1() {
        SharedPreferences prefs = this.getSharedPreferences("game_preferences2", Context.MODE_PRIVATE);
        return prefs.getInt(KEY2, 0);
    }

    public void solveLinear3(View view) {
        menu.stop();
        menu.release();
        Intent ii = new Intent(this, MainActivity3.class);
        startActivity(ii);
    }
}