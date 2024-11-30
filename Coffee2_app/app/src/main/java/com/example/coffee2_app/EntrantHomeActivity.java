package com.example.coffee2_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.coffee2_app.databinding.EntrantMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntrantHomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EntrantMainBinding binding;
    private Entrant entrant;
    private String deviceID;
    private List<Event> eventList = new ArrayList<>();
    private EntrantEventAdapter eventAdapter;
    private RecyclerView eventRecyclerView;
    private List<String> statusList = new ArrayList<>();


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

    /**
     * This method is called when the activity is created. It sets up the view, initializes the database,
     * configures the bottom navigation bar, and fetches the necessary data (events) from Firestore.
     *
     * @param savedInstanceState a Bundle containing any saved instance state (can be null).
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = EntrantMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        Log.d("FirestoreCheck", "Firestore is Initialized");

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

        Toast.makeText(this, "Click on chosen events to accept invitation", Toast.LENGTH_LONG).show();


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
        // Initialize RecyclerView and Adapter
        eventRecyclerView = findViewById(R.id.view_event_list);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EntrantEventAdapter(eventList, this, getSupportFragmentManager(), statusList, entrant.getUserId());
        eventRecyclerView.setAdapter(eventAdapter);

        // Fetch events from Firestore
        fetchEvents(entrant.getUserId());
    }

    /**
     * This method fetches events associated with the current user (entrant) from Firestore.
     * It retrieves event IDs from the user's data, then fetches details for each event and populates
     * the list of events to be displayed in the RecyclerView
     *
     * @param userId user's (entrant's) ID whose events are to be fetched.
    /**
     * This method fetches events associated with the current user (entrant) from Firestore.
     * It retrieves event IDs from the user's data and fetches their details.
     *
     * @param userId The user's (entrant's) ID whose events are to be fetched.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void fetchEvents(String userId) {
        db.collection("users").document(userId).collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> eventIds = new ArrayList<>(); // Store event IDs

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            eventIds.add(document.getId()); // Collect each event ID
                            statusList.add(document.getString("status")); // Add status
                        }

                        if (!eventIds.isEmpty()) {
                            db.collection("events")
                                    .whereIn("id", eventIds)
                                    .get()
                                    .addOnCompleteListener(eventTask -> {
                                        if (eventTask.isSuccessful() && eventTask.getResult() != null) {
                                            eventList.clear(); // Clear existing events
                                            for (QueryDocumentSnapshot eventDoc : eventTask.getResult()) {
                                                eventList.add(eventDoc.toObject(Event.class)); // Add Event object from Firestore
                                                Log.d("FirestoreData", "Loaded Event: " + eventDoc.getString("name"));
                                            }
                                            eventAdapter.notifyDataSetChanged(); // Refresh the adapter after fetching all events
                                        } else {
                                            Log.e("FirestoreError", "Error fetching event details: ", eventTask.getException());
                                            Toast.makeText(this, "Failed to fetch events.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Log.d("FirestoreData", "No events found for this user.");
                            Toast.makeText(this, "No events available.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("FirestoreError", "Error getting user events: ", task.getException());
                        Toast.makeText(this, "Failed to load user events.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}