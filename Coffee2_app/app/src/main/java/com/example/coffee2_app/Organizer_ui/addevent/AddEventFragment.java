package com.example.coffee2_app.Organizer_ui.addevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coffee2_app.Organizer;
import com.example.coffee2_app.OrganizerHomeActivity;
import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentAddEventBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Fragment that allows an organizer to create a new event.
 * Has UI for entering event details. The event details are saved to Firebase Firestore upon submission.
 */
public class AddEventFragment extends Fragment {

    private Organizer organizer;
    private String deviceID;
    private FragmentAddEventBinding binding;
    private FirebaseFirestore db;

    /**
     * Inflates the layout, initializes Firestore and click listeners.
     *
     * @param inflater  LayoutInflater to inflate views in the fragment
     * @param container          Parent view to contain the fragment's UI
     * @param savedInstanceState Bundle containing the fragment's saved state
     * @return The root view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = FirebaseFirestore.getInstance();

        // Back button listener
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        // Save button click listener to save profile data
        binding.saveButton.setOnClickListener(view -> {createEvent();});

        // Set up the button to open the DatePickerDialog
        binding.dateButton.setOnClickListener(v -> openDateTimePicker(binding.eventDate));
        binding.dateDrawButton.setOnClickListener(v -> openDateTimePicker(binding.eventDrawDate));

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
            Log.d("AEF_get", "Activity works");
        } else {
            Log.e("AEF_get", "Activity null");
        }

        if (organizer != null) {
            Log.d("AEF_Org", "Organizer ID: " + organizer.getUserID());
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
                R.style.CustomDatePickerDialog,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // When date is selected, show TimePickerDialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                            (timeView, selectedHour, selectedMinute) -> {
                                // Format the selected date and time
                                String selectedDateTime = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear +
                                        " " + String.format("%02d:%02d", selectedHour, selectedMinute);
                                // Set the formatted date and time to the TextView
                                dateView.setText(selectedDateTime);
                            }, hour, minute, true);
                    timePickerDialog.show();
                }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Creates a new event with details. Validates input fields, writes the event data to Firestore.
     * Displays a Toast message indicating success or failure.
     */
    private void createEvent() {
        String name = binding.eventName.getText().toString().trim();
        String eventDateString = binding.eventDate.getText().toString().trim();
        String drawDateString = binding.eventDrawDate.getText().toString().trim();
        String entriesLimitString = binding.eventEntries.getText().toString().trim();
        int entriesLimit;
        boolean geolocation = binding.eventGeolocation.isChecked();

        // Valid Entry Logic
        if (name.isEmpty() || eventDateString.isEmpty() || drawDateString.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() > 100) {
            Toast.makeText(getContext(), "Please limit book name to 50 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the SimpleDateFormat to parse date and time
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        sdf.setLenient(false);

        Date eventDate, drawDate;
        try {
            eventDate = sdf.parse(eventDateString);
            drawDate = sdf.parse(drawDateString);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Please enter a valid date in MM/dd/yyyy HH:mm format.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert Date objects to Timestamps
        Timestamp eventTimestamp = new Timestamp(eventDate);
        Timestamp drawTimestamp = new Timestamp(drawDate);

        // Initialize Calendars with the Date objects
        Calendar eventCalendar = Calendar.getInstance();
        Calendar drawCalendar = Calendar.getInstance();
        eventCalendar.setTime(eventDate);
        drawCalendar.setTime(drawDate);

        Calendar currentDate = Calendar.getInstance();

        try {
            // Check if either date is in the past
            if (eventCalendar.before(currentDate) || drawCalendar.before(currentDate)) {
                Toast.makeText(getContext(), "Please enter a future date and time", Toast.LENGTH_SHORT).show();
                return;
            }
            // Ensure event date is after draw date
            if (eventCalendar.before(drawCalendar)) {
                Toast.makeText(getContext(), "Please enter an event date AFTER the draw date", Toast.LENGTH_SHORT).show();
                return;
            }
            // Validate the year range for both dates
            int eventYear = eventCalendar.get(Calendar.YEAR);
            int drawYear = drawCalendar.get(Calendar.YEAR);
            if (eventYear > 3000 || drawYear > 3000) {
                Toast.makeText(getContext(), "Please enter a valid year (2024-3000)", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid year", Toast.LENGTH_SHORT).show();
            return;
        }

        if (entriesLimitString.isEmpty()) {
            entriesLimit = -1;
        } else {
            entriesLimit = Integer.parseInt(entriesLimitString);
        }

        //Just to check before the db is implemented
        String message = "Event Created - Name: " + name + ", Event Date: " + eventDateString + ", Draw Date: " + drawDateString + ", Geolocation: " + (geolocation ? "On" : "Off");

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        // Create a map for the event data
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("facilityID", organizer.getUserID());
        eventData.put("eventDate", eventTimestamp);
        eventData.put("drawDate", drawTimestamp);
        eventData.put("entriesLimit", entriesLimit);
        eventData.put("collectGeoStatus", geolocation);

        // Write to Firestore
        db.collection("events") // Change to your collection name
                .add(eventData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FirestoreCheck", "DocumentSnapshot added with ID: " + documentReference.getId());
                    Toast.makeText(getActivity(), "Event Created!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.d("FirestoreCheck", "Error adding document", e);
                    Toast.makeText(getActivity(), "Error creating event.", Toast.LENGTH_SHORT).show();
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
