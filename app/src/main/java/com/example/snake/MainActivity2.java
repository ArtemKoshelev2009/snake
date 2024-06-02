package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    private static final String PREFS_NAME = "game_preferences";
    private static final String GG = "GG";
    private static final String KEY_SEEK = "SeekState";
    SeekBar seekControl;
    TextView ggTv;
    private int progress1;

    MediaPlayer menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        menu = MediaPlayer.create(this, R.raw.menu1);
        menu.start();
        initializeViews();
        SeekBarDo();
    }

    private void SeekBarDo() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedProgress = sharedPreferences.getInt(KEY_SEEK, 50);
        seekControl.setProgress(savedProgress);
        updateVolume(savedProgress);

        seekControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateVolume(progress);
                    ggTv.setText("Размер карты: " + progress);
                    SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("KEY_SEEK",progress);
                    editor.apply();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateVolume(int progress) {
        float volume = (progress / 100f);
    }

    private void initializeViews() {
        seekControl = findViewById(R.id.switchMusic);
        ggTv = findViewById(R.id.ggTv);
            seekControl.setMax(80); // Устанавливаем максимальное значение на 79 (от 0 до 79 = 80 значений)
            seekControl.setMin(1); // Устанавливаем минимальное значение на 1
            seekControl.setProgress(40); // Устанавливаем начальное значение в середину диапазона (например, 40)
    }


    private void manageMediaPlayers(int progress) {
        SharedPreferences sharedPreferences = getSharedPreferences(GG, MODE_PRIVATE);
        progress1 = sharedPreferences.getInt(GG, 10);
    }

    public void solveLinear3(View view) {
        menu.stop();
        menu.release();
        Intent i = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(i);
    }
}