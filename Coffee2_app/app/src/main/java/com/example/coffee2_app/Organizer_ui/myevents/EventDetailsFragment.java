package com.example.coffee2_app.Organizer_ui.myevents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coffee2_app.databinding.FragmentOrganizerEventBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDetailsFragment extends Fragment {
    private FragmentOrganizerEventBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrganizerEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Back button listener
        binding.backButton.setOnClickListener(view -> getActivity().onBackPressed());

        // Facility button click listener to view/edit profile
        binding.eventViewEntries.setOnClickListener(view -> showEntriesFragment());

        // Create/Share QR Code button click listener
        binding.eventShareQr.setOnClickListener(view -> showQRCreateFragment());

        // Event Edit button click listener
        binding.eventEndDraw.setOnClickListener(view -> endDraw());

        // Get the event ID from arguments
        if (getArguments() != null) {
            String eventId = getArguments().getString("id");
            fetchEventDetails(eventId); // Fetch details from Firestore
        }

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
