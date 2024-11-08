package com.example.coffee2_app.Organizer_ui.myevents;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.coffee2_app.Organizer;
import com.example.coffee2_app.OrganizerHomeActivity;
import com.example.coffee2_app.databinding.FragmentOrganizerEventBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDetailsFragment extends Fragment {

    private FragmentOrganizerEventBinding binding;
    private Organizer organizer;
    private String deviceID;
    private String eventName;
    private String organizerName;
    private String facilityAddress;
    private String eventDate;
    private String drawDate;
    private int maxEntries;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("EventDetailsFragment", "onCreateView called");
        binding = FragmentOrganizerEventBinding.inflate(inflater, container, false);

        // Check if the layout was successfully inflated
        if (binding != null) {
            Log.d("EventDetailsFragment", "Layout successfully inflated");
        } else {
            Log.e("EventDetailsFragment", "Layout inflation failed");
        }

        View root = binding.getRoot();

        OrganizerHomeActivity activity = (OrganizerHomeActivity) getActivity();
        if (activity != null) {
            organizer = activity.getOrganizer();  // Get the Organizer instance
            deviceID = activity.getDeviceID();
        }

        // Back button listener
        binding.backButton.setOnClickListener(view -> getActivity().onBackPressed());

        // Facility button click listener to view/edit profile
        binding.eventViewEntries.setOnClickListener(view -> showEntriesFragment());

        // Create/Share QR Code button click listener
        binding.eventShareQr.setOnClickListener(view -> showQRCreateFragment());

        // Event Edit button click listener
        binding.eventEndDraw.setOnClickListener(view -> endDraw());

        // Get the event details from arguments
        if (getArguments() != null) {
            eventName = getArguments().getString("name");
            maxEntries = getArguments().getInt("maxEntries");
            eventDate = getArguments().getString("eventDate");
        }

        binding.eventTitle.setText(eventName);
        binding.eventOrganizerName.setText(organizer.getName());
        binding.eventLocation.setText(organizer.getAddress());
        binding.eventTimeLeft.setText("Event Date: " + eventDate);
        binding.eventEntries.setText("Max Entries: " + String.valueOf(maxEntries));

        Log.d("EventDetailsFragment", "onCreateView completed");

        return root;
    }

    private void fetchEventDetails(String eventId) {
        db.collection("events").document(eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String name = document.getString("name");
                            String eventDate = document.getString("eventDate");
                            String drawDate = document.getString("drawDate");
                            int maxEntries = document.getLong("entriesLimit").intValue();

                            binding.eventTitle.setText(name);
                            binding.eventOrganizerName.setText("Sample Organizer");
                            binding.eventTimeLeft.setText(eventDate);
                            binding.eventLocation.setText("Sample Location");
                            binding.eventEntries.setText(String.valueOf(maxEntries));
                        } else {
                            Toast.makeText(getContext(), "Event not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error fetching event details: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEntriesFragment() {
        EntriesFragment entriesFragment = new EntriesFragment();

        Context context = getContext();

        FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, entriesFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showQRCreateFragment() {
    }

    private void endDraw() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
