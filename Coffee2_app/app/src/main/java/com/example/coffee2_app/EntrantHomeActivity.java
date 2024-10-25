package com.example.coffee2_app;

import android.os.Bundle;
import android.util.Log;

import com.example.coffee2_app.databinding.EntrantMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coffee2_app.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EntrantHomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EntrantMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = EntrantMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        Log.d("FirestoreCheck", "Firestore is Initialized");
        writeTestDocument();
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.entrant_navigation_home, R.id.entrant_navigation_waitlist, R.id.entrant_navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
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