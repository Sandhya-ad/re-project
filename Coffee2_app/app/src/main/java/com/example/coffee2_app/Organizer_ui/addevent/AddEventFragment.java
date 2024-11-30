package com.example.coffee2_app.Organizer_ui.addevent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coffee2_app.DatabaseHelper;
import com.example.coffee2_app.Event;
import com.example.coffee2_app.Facility;
import com.example.coffee2_app.OrganizerHomeActivity;
import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentAddEventBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Fragment that allows an organizer to create a new event.
 * Includes UI for entering event details and uploading an event poster.
 */
public class AddEventFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Facility facility;
    private String deviceID;
    private FragmentAddEventBinding binding;
    private FirebaseFirestore db;
    private Bitmap eventPosterBitmap;
    private String posterImageID;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    /**
     * Inflates the layout, initializes Firestore, and sets up the image picker.
     *
     * @param inflater  LayoutInflater to inflate views in the fragment.
     * @param container Parent view to contain the fragment's UI.
     * @param savedInstanceState Bundle containing the fragment's saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = FirebaseFirestore.getInstance();

        // Set up the button to open the DatePickerDialog
        binding.dateButton.setOnClickListener(v -> openDateTimePicker(binding.eventDate));
        binding.dateDrawButton.setOnClickListener(v -> openDateTimePicker(binding.eventDrawDate));

        // Set up the Select Poster button
        binding.selectPosterButton.setOnClickListener(v -> selectPosterImage());

        // Save button click listener to save event data
        binding.saveButton.setOnClickListener(view -> createEvent());

        // Back button listener
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        // Set up ActivityResultLauncher for image selection
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            InputStream imageStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
                            eventPosterBitmap = BitmapFactory.decodeStream(imageStream);
                            binding.eventPoster.setImageBitmap(eventPosterBitmap); // Display poster preview
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Failed to load image.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

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
            facility = activity.getFacility(); // Get the Organizer instance
            deviceID = activity.getDeviceID();
            Log.d("AEF_get", "Activity works");
        } else {
            Log.e("AEF_get", "Activity null");
        }

        if (facility != null) {
            Log.d("AEF_Org", "Organizer ID: " + facility.getUserID());
        } else {
            Log.e("AEF_Org", "Organizer is null");
            Toast.makeText(getActivity(), "Profile Error: Organizer data is missing.", Toast.LENGTH_SHORT).show();
        }

        if (deviceID != null) {
            Log.d("AEF_Dev", "DeviceID in MyEvents: " + deviceID);
        } else {
            Log.e("AEF_Dev", "DeviceID is null");
        }
    }

    /**
     * Date and time picker dialog, displays in the TextView
     *
     * @param dateView The TextView where date and time will be displayed.
     */
    private void openDateTimePicker(TextView dateView) {
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Show DatePickerDialog first
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // When date is selected, show TimePickerDialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            requireContext(),
                            (timeView, selectedHour, selectedMinute) -> {
                                // Format the selected date and time
                                String selectedDateTime = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear +
                                        " " + String.format("%02d:%02d", selectedHour, selectedMinute);
                                // Set the formatted date and time to the TextView
                                dateView.setText(selectedDateTime);
                            },
                            hour,
                            minute,
                            true
                    );

                    // Customize TimePickerDialog buttons
                    timePickerDialog.setOnShowListener(dialog -> {
                        Button okButton = timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE);
                        Button cancelButton = timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE);

                        if (okButton != null) {
                            okButton.setTextColor(requireContext().getColor(android.R.color.black)); // Set OK button color to black
                        }
                        if (cancelButton != null) {
                            cancelButton.setTextColor(requireContext().getColor(android.R.color.black)); // Set Cancel button color to black
                        }
                    });

                    timePickerDialog.show();
                },
                year,
                month,
                day
        );

        // Customize DatePickerDialog buttons
        datePickerDialog.setOnShowListener(dialog -> {
            Button okButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE);
            Button cancelButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE);

            if (okButton != null) {
                okButton.setTextColor(requireContext().getColor(android.R.color.black)); // Set OK button color to black
            }
            if (cancelButton != null) {
                cancelButton.setTextColor(requireContext().getColor(android.R.color.black)); // Set Cancel button color to black
            }
        });

        datePickerDialog.show();
    }

    /**
     * Opens an intent to pick an image from the gallery.
     */
    private void selectPosterImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    /**
     * Uploads the event poster to Firestore.
     */
    private void uploadEventPoster() {
        if (eventPosterBitmap == null) {
            Log.e("EventPoster", "Bitmap is null. Cannot upload poster.");
            return;
        }

        try {
            ByteArrayOutputStream converter = new ByteArrayOutputStream();
            eventPosterBitmap.compress(Bitmap.CompressFormat.PNG, 100, converter);
            String encodedImage = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                encodedImage = Base64.getEncoder().encodeToString(converter.toByteArray());
            }
            posterImageID = UUID.nameUUIDFromBytes(encodedImage.getBytes()).toString();

            Map<String, Object> imageData = new HashMap<>();
            imageData.put("imageData", encodedImage);

            db.collection("images")
                    .document(posterImageID)
                    .set(imageData)
                    .addOnSuccessListener(aVoid -> Log.d("EventPoster", "Poster uploaded successfully: " + posterImageID))
                    .addOnFailureListener(e -> Log.e("EventPoster", "Poster upload failed", e));
        } catch (Exception e) {
            Log.e("EventPoster", "Error while uploading poster", e);
        }
    }

    /**
     * Creates a new event and uploads its details to Firestore.
     */
    private void createEvent() {
        String name = binding.eventName.getText().toString().trim();
        String eventDateString = binding.eventDate.getText().toString().trim();
        String drawDateString = binding.eventDrawDate.getText().toString().trim();
        String entriesLimitString = binding.eventEntries.getText().toString().trim();
        String description = binding.eventDescription.getText().toString().trim();
        boolean geolocation = binding.eventGeolocation.isChecked();

        // Validation logic
        if (name.isEmpty() || eventDateString.isEmpty() || drawDateString.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length() > 100) {
            Toast.makeText(getContext(), "Event name cannot exceed 100 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse dates
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        sdf.setLenient(false);
        Date eventDate, drawDate;

        try {
            eventDate = sdf.parse(eventDateString);
            drawDate = sdf.parse(drawDateString);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Invalid date format. Use MM/dd/yyyy HH:mm.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate date logic
        if (drawDate.after(eventDate)) {
            Toast.makeText(getContext(), "Draw date cannot be after the event date.", Toast.LENGTH_SHORT).show();
            return;
        }

        int entriesLimit = entriesLimitString.isEmpty() ? -1 : Integer.parseInt(entriesLimitString);

        // Create Event object
        Event event = new Event(name, facility.getUserID(), entriesLimit, geolocation, new Timestamp(eventDate), new Timestamp(drawDate), description);

        if (eventPosterBitmap != null) {
            uploadEventPoster(); // Upload the poster and set the image ID
            event.setImage(posterImageID);
        }

        Log.d("CreateEvent", "Saving event: " + event.toString());
        DatabaseHelper.addEvent(event,facility);
        Toast.makeText(getContext(), "Event Published.", Toast.LENGTH_SHORT).show();
        resetUI();
    }

    /**
     * Resets all input fields in the UI to their default/blank states.
     */
    private void resetUI() {
        binding.eventName.setText("");         // Clear event name
        binding.eventDate.setText("");         // Clear event date
        binding.eventDrawDate.setText("");     // Clear draw date
        binding.eventEntries.setText("");      // Clear entries limit
        binding.eventGeolocation.setChecked(false); // Uncheck geolocation checkbox
        binding.eventDescription.setText("");  // Clear description
        binding.eventPoster.setImageResource(0); // Clear poster preview
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