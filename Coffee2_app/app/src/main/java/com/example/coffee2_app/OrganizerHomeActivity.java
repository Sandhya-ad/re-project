package com.example.coffee2_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coffee2_app.databinding.OrganizerMainBinding;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Activity for organizer home screen. Sets up and manages the navigation bar
 */
public class OrganizerHomeActivity extends AppCompatActivity {

    private OrganizerMainBinding binding;
    private Organizer organizer;
    private FirebaseFirestore db;
    private String deviceID;

    /**
     * Returns organizer for other Fragments
     * @return organizer
     */
    public Organizer getOrganizer() { return organizer; }

    /**
     * Returns deviceID for other Fragments
     * @return deviceID
     */
    public String getDeviceID() { return deviceID; }

    /**
     * Initializes the view binding, retrieves the Organizer and device ID, and sets up navigation
     *
     * @param savedInstanceState Contains data it most recently supplied
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = OrganizerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        Log.d("FirestoreCheck", "Firestore is Initialized");

        Intent intent = getIntent(); // Retrieve Entrant and Device ID from Intent
        organizer = (Organizer) intent.getSerializableExtra("organizer");
        deviceID = intent.getStringExtra("deviceID");

        // Confirm Organizer and Device ID are set
        if (organizer == null) {
            Log.e("FirestoreCheck", "Facility is null.");
        } else {
            Log.d("FirestoreCheck", "Facility retrieved: " + organizer.getUserID());
        }

        if (deviceID == null) {
            Log.e("FirestoreCheck", "Device ID is null.");
        } else {
            Log.d("FirestoreCheck", "Device ID: " + deviceID);
        }

        // Proceed to set up navigation only after organizer and deviceID are confirmed
        setupNavigation();
    }

    /**
     * Sets up the bottom navigation and navigation controller for the app
     */
    private void setupNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.organizer_navigation_home, R.id.organizer_navigation_addevent, R.id.organizer_navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.organizerNavView, navController);
    }
}