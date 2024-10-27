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
import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private FragmentProfileBinding binding;
    private boolean isEditing = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Hide Save Button by default
        binding.editProfileButton.setVisibility(View.GONE);

        // Back button listener
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        // Edit icon click listener to enter edit mode
        binding.editProfileIcon.setOnClickListener(view -> toggleEditMode());

        // Save button click listener to save profile data
        binding.editProfileButton.setOnClickListener(view -> {
            saveProfileData();
            toggleEditMode(); // Exit edit mode after saving
        });

        // Profile picture click listener, only triggers in edit mode
        binding.profilePicture.setOnClickListener(view -> {
            if (isEditing) {
                openImagePicker();
            }
        });

        // Notification settings button click listener
        binding.notificationSettingsButton.setOnClickListener(view -> showNotificationSettingsDialog());

        return root;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                binding.profilePicture.setImageURI(selectedImageUri);
                // Save the selected image URI to a database or cloud storage if needed
            }
        }
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

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
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

        // Enable or disable profile picture click based on edit mode
        binding.profilePicture.setClickable(isEditing);
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

        if (enabled) {
            binding.entrantName.requestFocus();
        }
    }

    private void saveProfileData() {
        String name = binding.entrantName.getText().toString().trim();
        String address = binding.entrantAddress.getText().toString().trim();
        String email = binding.entrantEmail.getText().toString().trim();
        String phone = binding.entrantPhone.getText().toString().trim();
        //Just to check before the db is implemented
        String message = "Profile saved:\nName: " + name + "\nAddress: " + address +
                "\nEmail: " + email + "\nPhone: " + (phone.isEmpty() ? "Not provided" : phone);

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
