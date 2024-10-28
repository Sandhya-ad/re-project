package com.example.coffee2_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffee2_app.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ActivityMainBinding binding;
    private String deviceID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Use the XML layout with the 3 buttons

        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Find the buttons
        Button buttonEntrant = findViewById(R.id.button_enter_entrant);
        Button buttonOrganizer = findViewById(R.id.button_enter_organizer);
        Button buttonAdmin = findViewById(R.id.button_enter_admin);

        // Set click listener for "Enter as Entrant" button
        buttonEntrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EntrantHomeActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, EntrantHomeActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for "Enter as Organizer" button
//        buttonOrganizer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to OrganizerHomeActivity when the button is clicked (create this activity)
//                Intent intent = new Intent(MainActivity.this, OrganizerHomeActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // Set click listener for "Enter as Admin" button
//        buttonAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to AdminHomeActivity when the button is clicked (create this activity)
//                Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
//                startActivity(intent);
//            }
//        });
    }
    private void writeTestDocument() {
        // Create a sample data map
        Map<String, Object> sampleData = new HashMap<>();
        sampleData.put("message", "Hello, Firestore!");

        // Add a new document with a generated ID
        db.collection("testCollection")
                .add(sampleData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FirestoreCheck", "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.d("FirestoreCheck", "Error adding document", e);
                });
    }

}