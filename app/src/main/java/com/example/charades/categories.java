package com.example.charades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class categories extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories);

        SharedPreferences sh = getSharedPreferences("UniqueId", MODE_PRIVATE);
        Long id = sh.getLong("id",0L);



        ImageButton Animal = findViewById(R.id.animal);
        ImageButton Celeb = findViewById(R.id.celeb);
        ImageButton Food = findViewById(R.id.food);
        ImageButton Festival = findViewById(R.id.festivals);
        ImageButton Movies = findViewById(R.id.Movies);
        ImageButton Song = findViewById(R.id.songs);
        ImageButton Hobbie = findViewById(R.id.hobbie);
        ImageButton Sports = findViewById(R.id.sports);
        ImageButton Games = findViewById(R.id.game);
        ImageButton Books = findViewById(R.id.books);
        ImageButton Places = findViewById(R.id.places);
        ImageButton TvShow = findViewById(R.id.tvshow);

        ImageButton custom = findViewById(R.id.custombtn);

        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, customview.class);

                startActivity(intent);
            }
        });

        Animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","Animals");
                startActivity(intent);
            }
        });
        Celeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","celebrities");
                startActivity(intent);
            }
        });
        Movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","Movies");
                startActivity(intent);
            }
        });
        Song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","Song");
                startActivity(intent);
            }
        });
        Books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","Books");
                startActivity(intent);
            }
        });
        Sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","Sports");
                startActivity(intent);
            }
        });
        Games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","Video Games");
                startActivity(intent);
            }
        });
        Hobbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","Hobbies");
                startActivity(intent);
            }
        });
        Festival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","Holidays and Festivals");
                startActivity(intent);
            }
        });
        TvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","TV Shows");
                startActivity(intent);
            }
        });
        Food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","Food");
                startActivity(intent);
            }
        });
        Places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(categories.this, pregame.class);
                intent.putExtra("cat","Places");
                startActivity(intent);
            }
        });


    }

}