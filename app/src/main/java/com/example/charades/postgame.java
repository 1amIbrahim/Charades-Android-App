package com.example.charades;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import kotlin.Unit;

public class postgame extends AppCompatActivity {
    private String min;
    private String sec;
    private String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_postgame);
        Button back = findViewById(R.id.backbtn4);
        Button restart = findViewById(R.id.restart);

        Intent i = getIntent();
        ArrayList<String> visited = i.getStringArrayListExtra("visited");
        boolean[] isCorrect = i.getBooleanArrayExtra("isCorrect");
        int score = i.getIntExtra("score",0);
        min = i.getStringExtra("min");
        sec = i.getStringExtra("sec");
        items = i.getStringArrayExtra("items");

        TextView scoretv = findViewById(R.id.scoretext);
        scoretv.setText("You Got "+score+" Cards");

        Typeface font = ResourcesCompat.getFont(this,R.font.adlamfont);

        LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout history = findViewById(R.id.history);
        lm.setMargins(2,4,2,4);
        if(visited != null && isCorrect != null){
            int idx = 0;
            for(String item : visited){
                TextView tv = new TextView(getApplicationContext());
                tv.setText(item);
                tv.setTypeface(font);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                tv.setTextColor(Color.WHITE);
                tv.setLayoutParams(lm);
                if(!isCorrect[idx]){
                    tv.setAlpha(0.5f);
                }
                history.addView(tv);
                idx++;
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(postgame.this, categories.class);
                startActivity(i);
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(postgame.this, game.class);
                i.putExtra("min",min);
                i.putExtra("sec",sec);
                i.putExtra("items",items);
                startActivity(i);
            }
        });

    }
}