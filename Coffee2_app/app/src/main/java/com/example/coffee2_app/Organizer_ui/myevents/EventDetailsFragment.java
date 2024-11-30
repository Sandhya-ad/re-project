package com.example.coffee2_app.Organizer_ui.myevents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.coffee2_app.Facility;
import com.example.coffee2_app.OrganizerHomeActivity;
import com.example.coffee2_app.databinding.FragmentOrganizerEventBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Base64;

/**
 * Fragment displaying details for an event. Allows the user to view the poster, generate/share a QR code, and end the draw.
 */
public class EventDetailsFragment extends Fragment {

    private FragmentOrganizerEventBinding binding;
    private Facility facility;
    private String deviceID;
    private String eventID;
    private String eventName;
    private String eventDate;
    private String posterImageID; // Image ID for the event poster
    private int maxEntries;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Inflates the layout, retrieves event details, and sets up click listeners.
     *
     * @param inflater  LayoutInflater to inflate views in the fragment.
     * @param container Parent view to contain the fragment's UI.
     * @param savedInstanceState Bundle containing the fragment's saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("EventDetailsFragment", "onCreateView called");
        binding = FragmentOrganizerEventBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        OrganizerHomeActivity activity = (OrganizerHomeActivity) getActivity();
        if (activity != null) {
            facility = activity.getFacility();  // Get the Facility instance
            deviceID = activity.getDeviceID();
        }

        // Back button listener
        binding.backButton.setOnClickListener(view -> requireActivity().onBackPressed());

        // Create/Share QR Code button click listener
        binding.eventShareQr.setOnClickListener(view -> navigateToQRCodeFragment());

        // Get the event details from arguments
        if (getArguments() != null) {
            eventName = getArguments().getString("name");
            eventID = getArguments().getString("eventID");
            maxEntries = getArguments().getInt("maxEntries");
            eventDate = getArguments().getString("eventDate");
            posterImageID = getArguments().getString("posterImageID"); // Poster image ID from arguments
        }

        binding.eventTitle.setText(eventName);
        binding.eventOrganizerName.setText(facility != null ? facility.getName() : "Organizer");
        binding.eventLocation.setText(facility != null ? facility.getAddress() : "Location");
        binding.eventTimeLeft.setText("Event Date: " + eventDate);
        if (maxEntries != -1) {
            binding.eventEntries.setText("Max Entries: " + maxEntries);
        }
        else {
            binding.eventEntries.setText("Max Entries: Unlimited");
        }

        // Fetch and display the poster image
        if (posterImageID != null && !posterImageID.isEmpty()) {
            fetchEventPosterImage(posterImageID);
        } else {
            Log.e("EventDetailsFragment", "Poster Image ID is null or empty.");
        }

        return root;
    }

    /**
     * Fetches the poster image from Firestore and displays it in the ImageView.
     *
     * @param posterImageID The ID of the poster image to fetch.
     */
    private void fetchEventPosterImage(String posterImageID) {
        db.collection("images").document(posterImageID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String encodedImage = task.getResult().getString("imageData");
                        if (encodedImage != null) {
                            byte[] decodedBytes = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                decodedBytes = Base64.getDecoder().decode(encodedImage);
                            }
                            Bitmap posterBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                            binding.eventImage.setImageBitmap(posterBitmap); // Set the poster in ImageView
                        } else {
                            Log.e("EventDetailsFragment", "Encoded image data is null.");
                            Toast.makeText(getContext(), "Failed to load event poster.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("EventDetailsFragment", "Error fetching poster image: ", task.getException());
                        Toast.makeText(getContext(), "Failed to fetch event poster.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Navigates to the QRCodeFragment to display the event QR code.
     */
    private void navigateToQRCodeFragment() {
        if (eventID == null || eventID.isEmpty()) {
            Log.e("EventDetailsFragment", "Event ID is null or empty. Cannot generate QR code.");
            Toast.makeText(getContext(), "Invalid Event ID", Toast.LENGTH_SHORT).show();
            return;
        }

        QRCodeFragment qrCodeFragment = QRCodeFragment.newInstance(eventID);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, qrCodeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
