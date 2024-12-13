package com.example.charades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.GravityInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class customview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customview);

        SharedPreferences sh = getSharedPreferences("UniqueId", MODE_PRIVATE);
        Long id = sh.getLong("id",0L);

        ImageButton back = findViewById(R.id.backimgbtn);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(customview.this, categories.class);

            startActivity(intent);
        });

        FloatingActionButton add = findViewById(R.id.floatingActionButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(customview.this, customcreate.class);
                i.putExtra("uid",id);
                startActivity(i);
            }
        });


        LinearLayout customCards = findViewById(R.id.customlay);
        ProgressBar progressBar = findViewById(R.id.progressBar2);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        List<Category> categoryList = new ArrayList<>();
        DatabaseReference usersRef = db.getReference("charades").child("Users").child(String.valueOf(id));

        progressBar.setVisibility(View.VISIBLE);

        // Check if the user exists
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // User exists
                    Log.d("Firebase", "User with ID " + id + " exists.");
                    categoryList.clear();
                    DatabaseReference customref = db.getReference("charades").child("Users").child(String.valueOf(id)).child("categories");
                    customref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                        String categoryName = categorySnapshot.getKey();

                        Log.d("Firebase", "Category: " + categoryName + " has " + categorySnapshot.getChildrenCount() + " items");

                        // Safely retrieve category items
                        List<String> items = (List<String>) categorySnapshot.getValue();

                        if (items != null) {
                            // Convert the List to a Map<Integer, String> if needed
                            Map<Integer, String> itemsMap = new HashMap<>();
                            for (int i = 0; i < items.size(); i++) {
                                itemsMap.put(i, items.get(i));
                            }

                            Category category = new Category(categoryName, itemsMap);
                            categoryList.add(category);
                            Log.d("Firebase", "Loaded category: " + categoryName + " with items: " + items);
                        } else {
                            Log.d("Firebase", "No items found for category: " + categoryName);
                        }
                    }

                    // Create buttons dynamically for each category
                    createCategoryButtons(categoryList, customCards);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    // User does not exist, create the user
                    Log.d("Firebase", "User with ID " + id + " does not exist. Creating user...");
                    Map<String, Object> newUser = new HashMap<>();
                    newUser.put("name", String.valueOf(id)); // Optional: Add default fields
                    newUser.put("categories", new HashMap<>());
                    usersRef.setValue(newUser).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "User with ID " + id + " created successfully.");
                        } else {
                            Log.e("Firebase", "Failed to create user: " + task.getException());
                        }
                    });
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Error checking user existence: ", error.toException());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void createCategoryButtons(List<Category> categoryList, LinearLayout customCards) {
        LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                300
        );
        lm.setMargins(16, 16, 16, 16);
        Typeface font = ResourcesCompat.getFont(this,R.font.adlamfont);

        for (Category category : categoryList) {
            Button button = new Button(this);
            button.setText(category.getName());
            button.setTextSize(24);
            button.setBackgroundColor(Color.parseColor("#F6A446"));
            button.setTextColor(Color.WHITE);
            button.setTypeface(font);

            button.setOnClickListener(view -> {
                Intent intent = new Intent(customview.this, pregame.class);
                intent.putExtra("cat", category.getName());
                startActivity(intent);
            });

            CardView cardView = new CardView(this);
            cardView.setCardElevation(16);
            cardView.setRadius(24);
            cardView.setLayoutParams(lm);
            cardView.setCardBackgroundColor(Color.parseColor("#F6A446"));

            cardView.addView(button);
            customCards.addView(cardView);
        }
    }
}
