package com.example.coffee2_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.coffee2_app.databinding.EntrantMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EntrantHomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EntrantMainBinding binding;
    private Entrant entrant;
    private String deviceID;

    /**
     * Getter method for entrant object, for fragments
     * @return Entrant
     */
    public Entrant getEntrant() {
        return entrant;
    }

    /**
     * Getter method for DeviceID
     * @return DeviceID
     */
    public String getDeviceID() {
        return deviceID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = EntrantMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        Log.d("FirestoreCheck", "Firestore is Initialized");
        writeTestDocument();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        Intent intent = getIntent();

        // Retrieve Entrant and Device ID from Intent
        entrant = (Entrant) intent.getSerializableExtra("entrant");
        deviceID = intent.getStringExtra("deviceID");

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.entrant_navigation_home, R.id.entrant_navigation_waitlist, R.id.entrant_navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Confirm Entrant and Device ID are set
        if (entrant == null) {
            Log.e("FirestoreCheck", "Entrant is null.");
        } else {
            Log.d("FirestoreCheck", "Entrant retrieved: " + entrant.getUserId());
        }

        if (deviceID == null) {
            Log.e("FirestoreCheck", "Device ID is null.");
        } else {
            Log.d("FirestoreCheck", "Device ID: " + deviceID);
        }
    }

    private void writeTestDocument() {
        Map<String, Object> sampleData = new HashMap<>();
        sampleData.put("message", "Hello, Firestore!");

        db.collection("testCollection")
                .add(sampleData)
                .addOnSuccessListener(documentReference -> Log.d("FirestoreCheck", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.d("FirestoreCheck", "Error adding document", e));
    }

    public void updateEntrantInFirestore( String name, String email) {
        if (deviceID != null && entrant != null) {
            db.collection("users")
                    .document(deviceID)  // Use Device ID as the document ID in "users"
                    .set(entrant)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Entrant updated successfully in users"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating entrant in users", e));
        } else {
            Log.w("Firestore", "Device ID or Entrant data is null, cannot update Firestore.");
        }
    }
}
