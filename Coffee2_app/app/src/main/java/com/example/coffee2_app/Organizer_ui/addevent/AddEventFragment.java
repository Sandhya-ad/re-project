package com.example.coffee2_app.Organizer_ui.addevent;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentAddEventBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEventFragment extends Fragment {

    private FragmentAddEventBinding binding;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Back button listener
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        // Save button click listener to save profile data
        binding.saveButton.setOnClickListener(view -> {
            createEvent();
        });

        // Set up the button to open the DatePickerDialog
        binding.dateButton.setOnClickListener(v -> openDatePicker(binding.eventDate));
        binding.dateDrawButton.setOnClickListener(v -> openDatePicker(binding.eventDrawDate));

        return root;
    }

    private void openDatePicker(TextView dateView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                R.style.CustomDatePickerDialog,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                    dateView.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void createEvent() {
        String name = binding.eventName.getText().toString().trim();
        String eventDateString = binding.eventDate.getText().toString().trim();
        String drawDateString = binding.eventDrawDate.getText().toString().trim();
        String entries = binding.eventEntries.getText().toString().trim();
        boolean geolocation = binding.eventGeolocation.isChecked();

        // Valid Entry Logic
        if (name.isEmpty() || eventDateString.isEmpty() || drawDateString.isEmpty() || entries.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() > 100) {
            Toast.makeText(getContext(), "Please limit book name to 50 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        int eventYear, drawYear;
        eventYear = Integer.parseInt(eventDateString.split("/")[2]);
        drawYear = Integer.parseInt(drawDateString.split("/")[2]);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        try {
            Date dateDate = sdf.parse(eventDateString);
            Date dateDate2 = sdf.parse(drawDateString);
            Calendar eventDate = Calendar.getInstance();
            Calendar drawDate = Calendar.getInstance();
            eventDate.setTime(dateDate);
            drawDate.setTime(dateDate2);
            Calendar currentDate = Calendar.getInstance();
            if (eventDate.before(currentDate) || drawDate.before(currentDate)) {
                Toast.makeText(getContext(), "Please enter a future date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (eventDate.before(drawDate)){
                Toast.makeText(getContext(), "Please enter an event date AFTER draw date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (eventYear > 3000 || drawYear > 3000) {
                Toast.makeText(getContext(), "Please enter a valid year (2024-3000)", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Please enter a valid date in MM/dd/yyyy format.", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid year", Toast.LENGTH_SHORT).show();
            return;
        }

        int entrants;
        try {
            entrants = Integer.parseInt(entries);
            if (entrants < 0 || entrants > 1000) {
                Toast.makeText(getContext(), "Please enter a valid number of entrants (0-1000)", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        //Just to check before the db is implemented
        String message = "Event Created - Name: " + name + ", Event Date: " + eventDateString + ", Draw Date: " + drawDateString + ", Entries: " + entries + ", Geolocation: " + (geolocation ? "On" : "Off");

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        // Create a map for the event data
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("Event Name", name);
        eventData.put("Event Date", eventDateString);
        eventData.put("Draw Date", drawDateString);
        eventData.put("Max Entries", entries);
        eventData.put("Geolocation", (geolocation ? "On" : "Off"));

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
