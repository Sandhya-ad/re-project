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

/**
 * Fragment displaying details for an event. Allows the user to generate/share a QR code, and end the draw.
 */
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

    /**
     * Inflates the layout, retrieves event details, and sets up click listeners
     *
     * @param inflater  LayoutInflater to inflate views in the fragment
     * @param container          Parent view to contain the fragment's UI
     * @param savedInstanceState Bundle containing the fragment's saved state
     * @return The root view of the fragment
     */
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

    /**
     * Goes to EntriesFragment to display a list of entrants
     */
    private void showEntriesFragment() {
        EntriesFragment entriesFragment = new EntriesFragment();

        Context context = getContext();

        FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, entriesFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Displays a QR code creation/sharing fragment.
     */
    private void showQRCreateFragment() {
    }

    /**
     * End the event draw.
     */
    private void endDraw() {
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
