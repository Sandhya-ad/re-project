package com.example.coffee2_app.Entrant_ui.profile;


import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coffee2_app.Entrant;
import com.example.coffee2_app.EntrantHomeActivity;
import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentEntrantProfileBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Entrant entrant;
    private FragmentEntrantProfileBinding binding;
    private boolean isEditing = false;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEntrantProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        EntrantHomeActivity activity = (EntrantHomeActivity) getActivity();
        if (activity != null) {
            entrant = activity.getEntrant();  // Get the Entrant instance
        }

        if (entrant != null) {
            displayEntrantDetails();  // Populate fields with Entrant data
        } else {
            Toast.makeText(getActivity(), "Error: Entrant data is missing.", Toast.LENGTH_SHORT).show();
        }

        // Save Button functionality
        binding.editProfileButton.setVisibility(View.GONE);  // Hide by default
        binding.editProfileButton.setOnClickListener(view -> {
            if (entrant != null) {
                saveProfileData();
                toggleEditMode();  // Exit edit mode
            }
        });

        binding.editProfileIcon.setOnClickListener(view -> toggleEditMode());
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        // Profile picture click listener
        binding.profilePicture.setOnClickListener(view -> {
            if (isEditing) openImagePicker();
        });

        // Notification settings button listener
        binding.notificationSettingsButton.setOnClickListener(view -> showNotificationSettingsDialog());

        return root;
    }

    private void displayEntrantDetails() {
        binding.entrantName.setText(entrant.getName());
        binding.entrantEmail.setText(entrant.getEmail());
        binding.entrantAddress.setText(entrant.getAddress());
        binding.entrantPhone.setText(entrant.getPhone());
    }

    private void toggleEditMode() {
        isEditing = !isEditing;
        binding.editProfileButton.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        binding.editProfileIcon.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        binding.notificationSettingsButton.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        setEditMode(isEditing);
    }

    private void setEditMode(boolean enabled) {
        binding.entrantName.setEnabled(enabled);
        binding.entrantAddress.setEnabled(enabled);
        binding.entrantEmail.setEnabled(enabled);
        binding.entrantPhone.setEnabled(enabled);

        int bgColor = enabled ? getResources().getColor(android.R.color.background_light) : getResources().getColor(android.R.color.transparent);
        binding.entrantName.setBackgroundColor(bgColor);
        binding.entrantAddress.setBackgroundColor(bgColor);
        binding.entrantEmail.setBackgroundColor(bgColor);
        binding.entrantPhone.setBackgroundColor(bgColor);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void showNotificationSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_notification_settings, null);
        builder.setView(dialogView);

        CheckBox adminCheckBox = dialogView.findViewById(R.id.checkbox_admin);
        CheckBox organizerCheckBox = dialogView.findViewById(R.id.checkbox_organizer);

        builder.setPositiveButton("Save", (dialog, which) -> {
            boolean receiveFromAdmin = adminCheckBox.isChecked();
            boolean receiveFromOrganizer = organizerCheckBox.isChecked();
            Toast.makeText(getActivity(), "Notification preferences saved.", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void saveProfileData() {
        String name = binding.entrantName.getText().toString().trim();
        String address = binding.entrantAddress.getText().toString().trim();
        String email = binding.entrantEmail.getText().toString().trim();
        String phone = binding.entrantPhone.getText().toString().trim();

        // Update Entrant instance and Firestore
        entrant.setName(name);
        entrant.setEmail(email);
        entrant.setAddress(address);
        entrant.setPhone(phone);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
