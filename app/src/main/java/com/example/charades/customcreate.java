package com.example.charades;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class customcreate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customcreate);

        TextView nametxt = findViewById(R.id.textView5);

        ImageButton back = findViewById(R.id.imageButton3);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(customcreate.this, customview.class);
            startActivity(intent);
        });

        Button collectButton = findViewById(R.id.createdeck);
        Intent i = getIntent();
        Long id = i.getLongExtra("uid",0L);

        EditText inputField = findViewById(R.id.inputField);
        LinearLayout cardContainer = findViewById(R.id.cardContainer);

        inputField.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                String word = inputField.getText().toString().trim();

                if (!word.isEmpty()) {
                    createCard(word, cardContainer);
                    inputField.setText(""); // Clear the input field
                } else {
                    Toast.makeText(this, "Please enter a word", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        collectButton.setOnClickListener(v -> {

            ArrayList<String> words = new ArrayList<String>();
            String name = nametxt.getText().toString();
            if(collectWords(cardContainer,words)){
                Toast.makeText(this, "Collected words: ", Toast.LENGTH_SHORT).show();

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference userRef = db.getReference("charades").child("Users").child(String.valueOf(id)).child("categories");


                Map<String, Object> categoryData = new HashMap<>();
                categoryData.put(name, words);


                userRef.setValue(categoryData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Categories added successfully under user " + id);
                    } else {
                        Log.e("Firebase", "Failed to add categories: ", task.getException());
                    }
                });
            }
            else{
                Toast.makeText(this,"Enter At Least 10 Cards",Toast.LENGTH_SHORT).show();
            }
            // Now you have the words in the 'words' ArrayList, you can use them as needed

            Intent intent = new Intent(customcreate.this,customview.class);
            startActivity(intent);

        });
    }

    private void createCard(String word, LinearLayout cardContainer) {

        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lm.setMargins(0,10,0,10);
        cardView.setLayoutParams(lm);
        cardView.setCardElevation(16);
        cardView.setRadius(16);
        cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        cardView.setPadding(8, 8, 8, 8);


        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerLayout.setGravity(Gravity.CENTER_VERTICAL);

        TextView wordTextView = new TextView(this);
        wordTextView.setText(word);
        wordTextView.setTextSize(18);
        wordTextView.setPadding(16, 0, 16, 0);

        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        deleteButton.setContentDescription("Delete");
        deleteButton.setBackgroundResource(android.R.color.transparent);
        deleteButton.setPadding(25, 25, 25, 25);

        deleteButton.setOnClickListener(v -> {
            cardContainer.removeView(cardView);
        });

        innerLayout.addView(wordTextView);
        innerLayout.addView(deleteButton);

        cardView.addView(innerLayout);

        cardContainer.addView(cardView);
    }
    private boolean collectWords(LinearLayout cardContainer,ArrayList<String> words) {
        if(cardContainer.getChildCount() < 10) {
            return false;
        }
        // Iterate over the children of the cardContainer (which are the CardViews)
        for (int i = 0; i < cardContainer.getChildCount(); i++) {
            CardView cardView = (CardView) cardContainer.getChildAt(i);
            LinearLayout innerLayout = (LinearLayout) cardView.getChildAt(0); // The inner LinearLayout holding TextView and ImageButton
            TextView wordTextView = (TextView) innerLayout.getChildAt(0); // The TextView holding the word
            words.add(wordTextView.getText().toString()); // Add the text of the TextView to the ArrayList
        }

        return true;
    }



}