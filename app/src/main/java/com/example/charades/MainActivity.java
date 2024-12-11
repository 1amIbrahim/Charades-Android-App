package com.example.charades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ImageButton play;
    private Long uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        uid = createId();

        play = findViewById(R.id.imageButton);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, categories.class);

                startActivity(i);
            }

        });


    }
    public Long createId(){
        SharedPreferences sh = getSharedPreferences("UniqueId", MODE_PRIVATE);
        Long id = sh.getLong("id",0L);;

        if(id == 0L){
            SharedPreferences.Editor edit = sh.edit();
            Long newid = Calendar.getInstance().getTimeInMillis();
            edit.putLong("id",newid);
            id = newid;
            edit.apply();
        }
        return id;
    }

}