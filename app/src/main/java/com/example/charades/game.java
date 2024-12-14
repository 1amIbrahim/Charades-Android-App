package com.example.charades;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class game extends AppCompatActivity {

    int score = 0;
    int idx = 0;
    boolean reset = false;
    String[] items;
    ArrayList<String> visited = new ArrayList<>();
    ArrayList<Boolean> isCorrect = new ArrayList<>();
    private SensorManager sm;
    private SensorEventListener accelEvent;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        TextView item = findViewById(R.id.itembox);
        CardView Tick = findViewById(R.id.tick);
        CardView Cross = findViewById(R.id.cross);
        TextView time = findViewById(R.id.timebox);
        TextView scoreview = findViewById(R.id.scoretv);
        String min;
        String sec;

        Intent i = getIntent();
        min = i.getStringExtra("min");
        sec = i.getStringExtra("sec");
        items = i.getStringArrayExtra("items");

        if (items == null || items.length == 0) {
            Log.e("game", "Items array is null or empty.");
            return;  // Exit or handle error
        }

        List<String> stringList = Arrays.asList(items);
        Collections.shuffle(stringList);
        items = stringList.toArray(new String[0]);  // Update items array with shuffled list
        item.setText(items[idx]);

        int minutes = Integer.parseInt(min);
        int seconds = Integer.parseInt(sec);
        int milliseconds = (minutes * 60 * 1000) + (seconds * 1000);
        new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                time.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
                time.setText("00:00");
                Intent i = new Intent(game.this, postgame.class);
                i.putExtra("min",min);
                i.putExtra("sec",sec);
                i.putExtra("items",items);
                i.putExtra("score",score);
                i.putExtra("visited",visited);
                boolean[] correct = new boolean[isCorrect.size()];
                for (int j = 0; j < isCorrect.size(); j++) {
                    correct[j] = isCorrect.get(j);
                }
                i.putExtra("isCorrect",correct);
                startActivity(i);
            }
        }.start();

        ImageButton back = findViewById(R.id.backbtn2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(game.this, postgame.class);
                i.putExtra("min",min);
                i.putExtra("sec",sec);
                i.putExtra("items",items);
                i.putExtra("score",score);
                i.putExtra("visited",visited);
                boolean[] correct = new boolean[isCorrect.size()];
                for (int j = 0; j < isCorrect.size(); j++) {
                    correct[j] = isCorrect.get(j);
                }
                i.putExtra("isCorrect",correct);
                startActivity(i);
            }
        });

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        visited.add(items[idx]);
        isCorrect.add(false);
        accelEvent = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float acceleration_z = sensorEvent.values[2];

                    if (reset && acceleration_z > 9.0f) {
                        reset = false;
                        Cross.setVisibility(View.VISIBLE);

                        // Play "pass" sound
                        MediaPlayer mediaPlayer = MediaPlayer.create(game.this, R.raw.pass);
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(mp -> mp.release());
                        } else {
                            Log.e("MediaPlayer", "Failed to play 'pass' sound.");
                        }

                        // Delay logic for incorrect input
                        new android.os.Handler().postDelayed(() -> {
                            isCorrect.add(false);
                            idx++;
                            if (idx == items.length) {
                                back.performClick();
                                idx = 0;
                            }
                            visited.add(items[idx]);
                            item.setText(items[idx]);
                            Cross.setVisibility(View.GONE);
                        }, 1000); // 1 second delay

                        if (vibrator != null) {
                            vibrator.vibrate(200);
                        }

                    } else if (reset && acceleration_z < -9.0f) {
                        reset = false;
                        Tick.setVisibility(View.VISIBLE);

                        // Play "correct" sound
                        MediaPlayer mediaPlayer = MediaPlayer.create(game.this, R.raw.correct);
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(mp -> mp.release());
                        } else {
                            Log.e("MediaPlayer", "Failed to play 'correct' sound.");
                        }

                        // Delay logic for correct input
                        new android.os.Handler().postDelayed(() -> {
                            visited.add(items[idx]);
                            isCorrect.add(true);
                            score++;
                            scoreview.setText("SCORE: " + score);
                            idx++;
                            if (idx == items.length) {
                                back.performClick();
                                idx = 0;
                            }
                            item.setText(items[idx]);
                            Tick.setVisibility(View.GONE);
                        }, 1000); // 1 second delay

                        if (vibrator != null) {
                            vibrator.vibrate(200);
                        }
                    }

                    if (acceleration_z > -2.0f && acceleration_z < 2.0f) {
                        reset = true;
                    }
                }
            }



            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        sm.registerListener(accelEvent, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (sm != null && accelEvent != null) {
            sm.unregisterListener(accelEvent);
        }
    }

}