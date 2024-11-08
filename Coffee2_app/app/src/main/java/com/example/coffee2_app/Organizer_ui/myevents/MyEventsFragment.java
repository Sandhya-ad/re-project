package com.example.coffee2_app.Organizer_ui.myevents;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coffee2_app.Event;
import com.example.coffee2_app.EventsAdapter;
import com.example.coffee2_app.Organizer;
import com.example.coffee2_app.OrganizerHomeActivity;
import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentMyEventsBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a list of events created by the current organizer. Gets events from Firestore, displays navigation options.
 */
public class MyEventsFragment extends Fragment {

    private FragmentMyEventsBinding binding;
    private FirebaseFirestore db;
    Organizer organizer;
    public String deviceID;
    private EventsAdapter eventsAdapter; // Create an adapter for your RecyclerView
    private List<Event> eventList; // Create a list to hold events

    /**
     * Inflates the layout, initializes Firestore and click listeners.
     *
     * @param inflater  LayoutInflater to inflate views in the fragment
     * @param container          Parent view to contain the fragment's UI
     * @param savedInstanceState Bundle containing the fragment's saved state
     * @return The root view of the fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false); // Initialize binding
        View root = binding.getRoot();
        Log.d("EventDetailsFragment", "onCreateView called");

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        binding.viewEventList.setLayoutManager(new LinearLayoutManager(getContext())); // Use binding to access the RecyclerView
        eventList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(eventList, this); // Your custom adapter
        binding.viewEventList.setAdapter(eventsAdapter); // Use binding to set the adapter

        // Find the Back Button from the layout
        ImageButton backButton = root.findViewById(R.id.back_button);

        // Handle Back Button click
        backButton.setOnClickListener(v -> getActivity().onBackPressed()); // Navigate back when clicked

        // Fetch events from Firestore
        fetchEvents();

        return root;
    }

    /**
     * Retrieves organizer and deviceID from the parent activity and logs status.
     */
    @Override
    public void onStart() {
        super.onStart();
        OrganizerHomeActivity activity = (OrganizerHomeActivity) getActivity();
        if (activity != null) {
            organizer = activity.getOrganizer(); // Get the Organizer instance
            deviceID = activity.getDeviceID();
            Log.d("MEF_get", "Activity works");
        } else {
            Log.e("MEF_get", "Activity null");
        }

        if (organizer != null) {
            Log.d("MEF_Org", "Organizer ID in MyEvents: " + organizer.getUserID());
        } else {
            Log.e("MEF_Org", "Organizer is null");
            Toast.makeText(getActivity(), "Profile Error: Organizer data is missing.", Toast.LENGTH_SHORT).show();
        }

        if (deviceID != null) {
            Log.d("MEF_Dev", "DeviceID in MyEvents: " + deviceID);
        } else {
            Log.e("MEF_Dev", "DeviceID is null");
        }
    }

    private void fetchEvents() {
        db.collection("events") // Use your collection name
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = "";
                            String name = document.getString("name");
                            String userID = document.getString("facilityID");
                            int maxEntries = document.getLong("entriesLimit").intValue();
                            boolean collectGeo = document.getBoolean("collectGeoStatus");
                            String hashQRData = "";
                            Timestamp eventDate = document.getTimestamp("eventDate");
                            Timestamp drawDate = document.getTimestamp("drawDate");
                            if (userID.equals(organizer.getUserID())){
                                Event event = new Event(name, userID, maxEntries, collectGeo, hashQRData, eventDate, drawDate);
                                eventList.add(event);
                            }
                        }
                        eventsAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                    } else {
                        Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Clears the binding reference when the view is destroyed to prevent memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}