package com.example.coffee2_app.Organizer_ui.myevents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coffee2_app.Event;
import com.example.coffee2_app.EventsAdapter;
import com.example.coffee2_app.Facility;
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
    private Facility facility;
    private String deviceID;
    private EventsAdapter eventsAdapter; // Adapter for RecyclerView
    private List<Event> eventList; // List to hold events

    /**
     * Inflates the layout, initializes Firestore, and sets up the RecyclerView and click listeners.
     *
     * @param inflater  LayoutInflater to inflate views in the fragment.
     * @param container Parent view to contain the fragment's UI.
     * @param savedInstanceState Bundle containing the fragment's saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.d("MyEventsFragment", "onCreateView called");

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        binding.viewEventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(eventList, requireContext());
        binding.viewEventList.setAdapter(eventsAdapter);

        // Back Button functionality
        ImageButton backButton = root.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return root;
    }

    /**
     * Retrieves organizer and deviceID from the parent activity and fetches events.
     */
    @Override
    public void onStart() {
        super.onStart();
        OrganizerHomeActivity activity = (OrganizerHomeActivity) getActivity();
        if (activity != null) {
            facility = activity.getFacility(); // Get the Facility instance
            deviceID = activity.getDeviceID();
            Log.d("MyEventsFragment", "Facility and Device ID retrieved.");

            if (facility != null) {
                Log.d("MyEventsFragment", "Facility ID: " + facility.getUserID());
                fetchEventsFromFacility();
            } else {
                Log.e("MyEventsFragment", "Facility is null.");
                Toast.makeText(getContext(), "Error: Facility data is missing.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("MyEventsFragment", "OrganizerHomeActivity is null.");
        }
    }

    /**
     * Fetches events listed in the Facility's `events` field.
     */
    private void fetchEventsFromFacility() {
        if (facility == null) {
            Log.e("MyEventsFragment", "Facility is null. Cannot fetch events.");
            Toast.makeText(getContext(), "Error: Facility data is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> eventIDs = facility.getEvents();
        if (eventIDs == null || eventIDs.isEmpty()) {
            Log.d("MyEventsFragment", "Event IDs list is empty.");
            Toast.makeText(getContext(), "No events found.", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("MyEventsFragment", "Fetching events with IDs: " + eventIDs);

        // Fetch events in batches of 10
        for (int i = 0; i < eventIDs.size(); i += 10) {
            List<String> batch = eventIDs.subList(i, Math.min(i + 10, eventIDs.size()));
            fetchEventBatch(batch);
        }
    }

    private void fetchEventBatch(List<String> batch) {
        db.collection("events")
                .whereIn("id", batch)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                // Deserialize the Firestore document directly into the Event class
                                Event event = document.toObject(Event.class);

                                // Add the event to the list
                                eventList.add(event);
                            } catch (Exception e) {
                                Log.e("MyEventsFragment", "Error deserializing event data: " + e.getMessage());
                            }
                        }
                        eventsAdapter.notifyDataSetChanged(); // Notify adapter of data changes
                    } else {
                        Log.e("MyEventsFragment", "Error fetching events: ", task.getException());
                        Toast.makeText(getContext(), "Failed to fetch some events.", Toast.LENGTH_SHORT).show();
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
