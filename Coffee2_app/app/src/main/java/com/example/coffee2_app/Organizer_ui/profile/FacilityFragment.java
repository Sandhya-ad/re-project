package com.example.coffee2_app.Organizer_ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;

import com.example.coffee2_app.databinding.FragmentFacilityEditBinding;

public class FacilityFragment extends Fragment {

    private FragmentFacilityEditBinding binding;
    private boolean isEditing = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFacilityEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Back button listener
        binding.backButton.setOnClickListener(View -> getActivity().onBackPressed());

        // Hide Save Button by default
        //binding.editProfileButton.setVisibility(View.GONE);

        // Save button click listener to save profile data
        binding.editProfileButton.setOnClickListener(view -> {
            saveProfileData();
            toggleEditMode(); // Exit edit mode after saving
        });

        return root;
    }

    private void toggleEditMode() {
        isEditing = !isEditing;

        if (isEditing) {
            // Enter edit mode, show Save button
            setEditMode(true);
            binding.editProfileButton.setVisibility(View.VISIBLE); // Show Save button
        } else {
            // Exit edit mode, hide Save button
            setEditMode(false);
            binding.editProfileButton.setVisibility(View.GONE); // Hide Save button
        }
    }

    private void setEditMode(boolean enabled) {
        binding.facilityName.setEnabled(enabled);
        binding.facilityAddress.setEnabled(enabled);
        binding.facilityEmail.setEnabled(enabled);
        binding.facilityPhone.setEnabled(enabled);

        int bgColor = enabled ? getResources().getColor(android.R.color.background_light) : getResources().getColor(android.R.color.transparent);

        binding.facilityName.setBackgroundColor(bgColor);
        binding.facilityAddress.setBackgroundColor(bgColor);
        binding.facilityEmail.setBackgroundColor(bgColor);
        binding.facilityPhone.setBackgroundColor(bgColor);

        if (enabled) {
            binding.facilityName.requestFocus();
        }
    }

    private void saveProfileData() {
        String name = binding.facilityName.getText().toString().trim();
        String address = binding.facilityAddress.getText().toString().trim();
        String email = binding.facilityEmail.getText().toString().trim();
        String phone = binding.facilityPhone.getText().toString().trim();
        //Just to check before the db is implemented
        String message = "Profile saved:\nName: " + name + "\nAddress: " + address + "\nEmail: " + email + "\nPhone: " + (phone.isEmpty() ? "Not provided" : phone);

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
