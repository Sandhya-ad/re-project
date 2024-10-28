package com.example.coffee2_app.Organizer_ui.addevent;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentAddEventBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventFragment extends Fragment {

    private FragmentAddEventBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Back button listener
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        // Save button click listener to save profile data
        binding.saveButton.setOnClickListener(view -> {
            createEvent();
        });

        // Set up the button to open the DatePickerDialog
        binding.dateButton.setOnClickListener(v -> openDatePicker());

        return root;
    }

    private void openDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                R.style.CustomDatePickerDialog,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the selected date
                    String selectedDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                    binding.eventDrawDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void createEvent() {
        String name = binding.eventName.getText().toString().trim();
        String date = binding.eventDrawDate.getText().toString().trim();
        String entries = binding.eventEntries.getText().toString().trim();
        boolean geolocation = binding.eventGeolocation.isChecked();

        //Just to check before the db is implemented
        String message = "Event Created:\nName: " + name + "\nDate: " + date + "\nEntries: " + entries + "\nGeolocation: " + (geolocation ? "On" : "Off");

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
