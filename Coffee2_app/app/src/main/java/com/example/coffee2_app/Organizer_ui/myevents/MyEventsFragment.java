package com.example.coffee2_app.Organizer_ui.myevents;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee2_app.EntrantHomeActivity;
import com.example.coffee2_app.Event;
import com.example.coffee2_app.EventsAdapter;
import com.example.coffee2_app.Organizer;
import com.example.coffee2_app.OrganizerHomeActivity;
import com.example.coffee2_app.R;
import com.example.coffee2_app.User;
import com.example.coffee2_app.databinding.FragmentMyEventsBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyEventsFragment extends Fragment {

    private Organizer organizer;
    private FragmentMyEventsBinding binding;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter; // Create an adapter for your RecyclerView
    private List<Event> eventList; // Create a list to hold events
    private String deviceID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false); // Initialize binding
        View root = binding.getRoot();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        OrganizerHomeActivity activity = (OrganizerHomeActivity) getActivity();
        if (activity != null) {
            organizer = activity.getOrganizer();  // Get the Organizer instance
            deviceID = activity.getDeviceID();
        }

        if (organizer == null) {
            Log.d("test", "Organizer is null");
            Toast.makeText(getActivity(), "Profile Error: Organizer data is missing.", Toast.LENGTH_SHORT).show();
        }


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

    private void fetchEvents() {
        db.collection("events") // Use your collection name
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            Organizer organizer = new Organizer("");
                            int maxEntries = document.getLong("entriesLimit").intValue();
                            boolean collectGeo = document.getBoolean("collectGeoStatus");
                            String hashQRData = "";
                            Timestamp eventDate = document.getTimestamp("eventDate");
                            Timestamp drawDate = document.getTimestamp("drawDate");

                            Event event = new Event(name, organizer.getName(), maxEntries, collectGeo, hashQRData, eventDate, drawDate);
                            eventList.add(event);
                        }
                        eventsAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                    } else {
                        Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

//package com.example.coffee2_app.Organizer_ui.myevents;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.coffee2_app.Event;
//import com.example.coffee2_app.EventsAdapter;
//import com.example.coffee2_app.R;
//import com.example.coffee2_app.User;
//import com.example.coffee2_app.databinding.FragmentMyEventsBinding;
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MyEventsFragment extends Fragment {
//
//    private FragmentMyEventsBinding binding;
//    private FirebaseFirestore db;
//    private RecyclerView recyclerView;
//    private EventsAdapter eventsAdapter; // Create an adapter for your RecyclerView
//    private List<Event> eventList; // Create a list to hold events
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentMyEventsBinding.inflate(inflater, container, false); // Initialize binding
//        View root = binding.getRoot();
//
//        // Initialize Firestore
//        db = FirebaseFirestore.getInstance();
//
//        // Set up RecyclerView
//        binding.viewEventList.setLayoutManager(new LinearLayoutManager(getContext())); // Use binding to access the RecyclerView
//        eventList = new ArrayList<>();
//        eventsAdapter = new EventsAdapter(eventList); // Your custom adapter
//        binding.viewEventList.setAdapter(eventsAdapter); // Use binding to set the adapter
//
//        // Find the Back Button from the layout
//        ImageButton backButton = root.findViewById(R.id.back_button);
//
//        // Handle Back Button click
//        backButton.setOnClickListener(v -> getActivity().onBackPressed()); // Navigate back when clicked
//
//        // Fetch events from Firestore
//        fetchEvents();
//
//        return root;
//    }
//
//    private void fetchEvents() {
//        db.collection("events") // Use your collection name
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            String id = "";
//                            String name = document.getString("name");
//                            User organizer = new User("");
//                            int maxEntries = document.getLong("entriesLimit").intValue();
//                            boolean collectGeo = document.getBoolean("collectGeoStatus");
//                            String hashQRData = "";
//                            Timestamp eventDate = document.getTimestamp("eventDate");
//                            Timestamp drawDate = document.getTimestamp("drawDate");
//
//                            Event event = new Event(id, name, organizer, maxEntries, collectGeo, hashQRData, eventDate, drawDate);
//                            eventList.add(event);
//                        }
//                        eventsAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
//                    } else {
//                        Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}