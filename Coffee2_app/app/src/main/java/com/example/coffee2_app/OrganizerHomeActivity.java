package com.example.coffee2_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coffee2_app.databinding.OrganizerMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Activity for organizer home screen. Sets up and manages the navigation bar
 */
public class OrganizerHomeActivity extends AppCompatActivity {

    private OrganizerMainBinding binding;
    private Facility facility;
    private FirebaseFirestore db;
    private String deviceID;

    /**
     * Returns the Facility object for use in fragments.
     *
     * @return Facility object.
     */
    public Facility getFacility() {
        return facility;
    }

    /**
     * Returns the Device ID for use in fragments.
     *
     * @return Device ID string.
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * Refreshes the Facility data from Firestore. Updates the local `facility` object.
     *
     * @param callback Callback to notify when the Facility data is refreshed.
     */
    public void refreshFacility(@NonNull FacilityCallback callback) {
        if (deviceID == null) {
            Log.e("OrganizerHomeActivity", "Device ID is null. Cannot refresh facility data.");
            callback.onFailure(new Exception("Device ID is null."));
            return;
        }

        DocumentReference docRef = db.collection("users").document(deviceID);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null && user.getFacility() != null) {
                    facility = user.getFacility();
                    callback.onSuccess(facility);
                } else {
                    callback.onFailure(new Exception("Facility not found in Firestore."));
                }
            } else {
                callback.onFailure(new Exception("User document does not exist in Firestore."));
            }
        }).addOnFailureListener(e -> {
            Log.e("FirestoreError", "Error refreshing Facility data", e);
            callback.onFailure(e);
        });
    }

    /**
     * Retrieves the list of event IDs from the Facility object.
     *
     * @return List of event IDs, or null if Facility is null.
     */
    public List<String> getEventIDs() {
        if (facility == null) {
            Log.e("OrganizerHomeActivity", "Facility is null. Cannot retrieve event IDs.");
            return null;
        }
        return facility.getEvents();
    }

    /**
     * Initializes the view binding, retrieves the Organizer and device ID, and sets up navigation.
     *
     * @param savedInstanceState Contains data most recently supplied by `onSaveInstanceState`.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up view binding
        binding = OrganizerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        Log.d("OrganizerHomeActivity", "Firestore initialized.");

        // Retrieve Facility and Device ID from the Intent
        Intent intent = getIntent();
        facility = (Facility) intent.getSerializableExtra("organizer");
        deviceID = intent.getStringExtra("deviceID");

        // Debugging Facility and Device ID
        if (facility == null) {
            Log.e("OrganizerHomeActivity", "Facility is null.");
        } else {
            Log.d("OrganizerHomeActivity", "Facility retrieved: " + facility.getUserID());
        }

        if (deviceID == null) {
            Log.e("OrganizerHomeActivity", "Device ID is null.");
        } else {
            Log.d("OrganizerHomeActivity", "Device ID retrieved: " + deviceID);
        }

        // Set up bottom navigation
        setupNavigation();
    }

    /**
     * Configures bottom navigation and the navigation controller for the app.
     */
    private void setupNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.organizer_navigation_home,
                R.id.organizer_navigation_addevent,
                R.id.organizer_navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.organizerNavView, navController);
    }

    /**
     * Callback interface for refreshing Facility data.
     */
    public interface FacilityCallback {
        void onSuccess(Facility facility);

        void onFailure(Exception e);
    }
}
