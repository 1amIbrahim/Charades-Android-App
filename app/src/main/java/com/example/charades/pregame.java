package com.example.charades;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class pregame extends AppCompatActivity {
    private String minute;
    private String second;
    private Map<Integer,String> items;
    private String name;
    private String[] itemsArray;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pregame);

        SharedPreferences sh = getSharedPreferences("UniqueId", MODE_PRIVATE);
        Long id = sh.getLong("id",0L);

        LinearLayout layout = findViewById(R.id.prelinear);
        ImageView img = findViewById(R.id.preimage);
        ImageButton back = findViewById(R.id.backbtn);
        Button min = findViewById(R.id.minus);
        Button sum = findViewById(R.id.plus);
        Button start = findViewById(R.id.startbtn);
        TextView timeview = findViewById(R.id.timetv);
        String time = timeview.getText().toString();
        String[] splitTime = time.split(":");
        minute = splitTime[0];
        second = splitTime[1];

        Intent i = getIntent();
        name = i.getStringExtra("cat");

        progressBar = findViewById(R.id.progressBar);
        boolean custom = i.getBooleanExtra("custom",false);

        if(custom){
            img.setVisibility(View.GONE);

            Typeface font = ResourcesCompat.getFont(this,R.font.adlamfont);
            TextView tv =new TextView(getApplicationContext());
            tv.setText(name);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(54);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTypeface(font);

            layout.addView(tv);

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference myref = db.getReference("charades");


            List<Category> categoryList = new ArrayList<>();

            DatabaseReference catref = db.getReference("charades");
            progressBar.setVisibility(View.VISIBLE);
            start.setEnabled(false);
            catref.child("Users").child(String.valueOf(id)).child("categories").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    categoryList.clear();
                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {

                        String categoryName = categorySnapshot.getKey();

                        Log.d("***************", "Category: " + categoryName + " has " + categorySnapshot.getChildrenCount() + " items");

                        List<String> items = (List<String>) categorySnapshot.getValue();

                        if (items != null) {
                            // Convert the List to a Map<Integer, String> if needed
                            Map<Integer, String> itemsMap = new HashMap<>();
                            for (int i = 0; i < items.size(); i++) {
                                itemsMap.put(i, items.get(i));
                            }

                            Category category = new Category(categoryName, itemsMap);
                            categoryList.add(category);

                        }
                        else {
                            Log.d("***************", "No items found for category: " + categoryName);
                        }
                    }

                    for (Category c : categoryList) {
                        if (c.getName().equals(name)) {
                            items = c.getItems();

                            // Initialize a list to store the strings
                            List<String> itemList = new ArrayList<>();

                            for (Map.Entry<Integer, String> entry : items.entrySet()) {
                                // Check if the value is an ArrayList
                                Object value = entry.getValue();
                                if (value instanceof ArrayList) {
                                    // Cast the value to ArrayList<String> and add its elements
                                    ArrayList<String> listValue = (ArrayList<String>) value;
                                    itemList.addAll(listValue);
                                } else if (value instanceof String) {
                                    // Add the string directly to the list
                                    itemList.add((String) value);
                                }
                            }

                            // Convert the list to a String array
                            itemsArray = itemList.toArray(new String[0]);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    start.setEnabled(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Firebase", "loadPost:onCancelled", error.toException());
                    progressBar.setVisibility(View.GONE);
                    start.setEnabled(true);
                }
            });


        }
        else{
            String imgname = name.toLowerCase();

            if(imgname.equals("festivals and holidays")){
                imgname = "festivals";
            }
            if(imgname.equals("tv shows")){
                imgname = "tvshow";
            }
            if(imgname.equals("video games")){
                imgname = "games";
            }
            @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier(imgname, "drawable", getPackageName());

            // Check if the resource ID is valid (it should not be 0)
            if (resId != 0) {
                img.setImageResource(resId);
            } else {
                // Handle the case where the image resource doesn't exist
                Log.e("Error", "Resource not found");
            }

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference myref = db.getReference("charades");


            List<Category> categoryList = new ArrayList<>();

            DatabaseReference catref = db.getReference("charades");
            progressBar.setVisibility(View.VISIBLE);
            start.setEnabled(false);
            catref.child("categories").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("onDataChange","onDataChange is Called");
                    categoryList.clear();
                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {

                        String categoryName = categorySnapshot.getKey();

                        Log.d("***************", "Category: " + categoryName + " has " + categorySnapshot.getChildrenCount() + " items");

                        Map<Integer,String> items = (Map<Integer, String>) categorySnapshot.getValue();

                        if (items != null) {
                            Category category = new Category(categoryName, items);
                            categoryList.add(category);

                        }
                        else {
                            Log.d("***************", "No items found for category: " + categoryName);
                        }
                    }

                    for (Category c : categoryList) {
                        if (c.getName().equals(name)) {
                            items = c.getItems();

                            // Initialize a list to store the strings
                            List<String> itemList = new ArrayList<>();

                            for (Map.Entry<Integer, String> entry : items.entrySet()) {
                                // Check if the value is an ArrayList
                                Object value = entry.getValue();
                                if (value instanceof ArrayList) {
                                    // Cast the value to ArrayList<String> and add its elements
                                    ArrayList<String> listValue = (ArrayList<String>) value;
                                    itemList.addAll(listValue);
                                } else if (value instanceof String) {
                                    // Add the string directly to the list
                                    itemList.add((String) value);
                                }
                            }

                            // Convert the list to a String array
                            itemsArray = itemList.toArray(new String[0]);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    start.setEnabled(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Firebase", "loadPost:onCancelled", error.toException());
                    progressBar.setVisibility(View.GONE);
                    start.setEnabled(true);
                }
            });
        }





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(pregame.this, categories.class);
                startActivity(i);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(pregame.this, game.class);
                i.putExtra("min",minute);
                i.putExtra("sec",second);
                i.putExtra("name",name);
                i.putExtra("items",itemsArray);
                startActivity(i);
            }
        });
        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(second.equals("00") && !minute.equals("1")){
                    int Intminute = Integer.parseInt(minute);
                    Intminute=Intminute-1;
                    minute = String.valueOf(Intminute);

                    second = "30";

                } else if (second.equals("30")) {
                    second = "00";
                }
                timeview.setText(minute+":"+second);
            }
        });
        sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(second.equals("00") && !minute.equals("3")){
                    second = "30";

                } else if (second.equals("30") && !minute.equals("3")) {
                    second = "00";
                    int Intminute = Integer.parseInt(minute);
                    Intminute=Intminute+1;
                    minute = String.valueOf(Intminute);
                }
                timeview.setText(minute+":"+second);
            }
        });

    }
}