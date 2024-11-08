package com.example.coffee2_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
    private EventsAdapter eventAdapter;
    private RecyclerView eventRecyclerView;


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
        // Initialize RecyclerView and Adapter
        eventRecyclerView = findViewById(R.id.view_event_list);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventsAdapter(eventList, this, getSupportFragmentManager());
        eventRecyclerView.setAdapter(eventAdapter);

        // Fetch events from Firestore
        fetchEvents(entrant.getUserId());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void fetchEvents(String userId) {
        db.collection("users").document(userId).collection("events")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> eventIds = new ArrayList<>();  // Store event IDs

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            eventIds.add(document.getId());  // Collect each event ID
                        }

                        for (String eventId : eventIds) {
                            db.collection("events").document(eventId).get()
                                    .addOnCompleteListener(eventTask -> {
                                        if (eventTask.isSuccessful() && eventTask.getResult() != null) {
                                            DocumentSnapshot eventDocument = eventTask.getResult();
                                            String eventName = eventDocument.getString("name");
                                            String organizerID = eventDocument.getString("userID");
                                            boolean collectGeo = eventDocument.getBoolean("collectGeoStatus") != null && eventDocument.getBoolean("collectGeoStatus");
                                            //String hashQrData = eventDocument.getString("hashQrData");
                                            String hashQrData = "235b";
                                            Timestamp eventDate = eventDocument.getTimestamp("eventDate");
                                            Timestamp drawDate = eventDocument.getTimestamp("drawDate");
                                            int maxEntries = eventDocument.contains("entriesLimit") ? eventDocument.getLong("entriesLimit").intValue() : -1;

                                            Event event;
                                            if (maxEntries != -1) {
                                                event = new Event(eventName, organizerID, maxEntries, collectGeo, hashQrData, eventDate, drawDate);
                                            } else {
                                                event = new Event(eventName, organizerID, collectGeo, hashQrData, eventDate, drawDate);
                                            }
                                            eventList.add(event);
                                            Log.d("FirestoreData", "Added Event: " + event.getName());
                                            eventAdapter.notifyDataSetChanged(); // Refresh the adapter after each fetch
                                        } else {
                                            Log.e("FirestoreError", "Error getting event details: ", eventTask.getException());
                                        }
                                    });
                        }
                    } else {
                        Log.e("FirestoreError", "Error getting user events: ", task.getException());
                    }
                });
    }
}