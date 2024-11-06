package com.example.coffee2_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.coffee2_app.databinding.AdminMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminHomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private AdminMainBinding binding;
    private Admin admin;
    private String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        Log.d("FirestoreCheck", "Firestore is Initialized");

        BottomNavigationView navView = findViewById(R.id.admin_nav_view);
        Intent intent = getIntent();

        // Retrieve Admin and Device ID from Intent
        deviceID = intent.getStringExtra("deviceID");

        // Set up navigation for Admin sections
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.admin_navigation_events,
                R.id.admin_navigation_profiles,
                R.id.admin_navigation_images,
                R.id.admin_navigation_facilities)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.adminNavView, navController);

    }
}
