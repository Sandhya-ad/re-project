package com.example.coffee2_app.Organizer_ui.profile;

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
import com.example.coffee2_app.databinding.FragmentOrganizerProfileBinding;
import com.example.coffee2_app.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private FragmentOrganizerProfileBinding binding;
    private boolean isEditing = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrganizerProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Hide Save Button by default
        binding.editProfileButton.setVisibility(View.GONE);

        // Back button listener
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        // Edit icon click listener to enter edit mode
        binding.backButton.setOnClickListener(view -> toggleEditMode());

        // Save button click listener to save profile data
        binding.editProfileButton.setOnClickListener(view -> {
            saveProfileData();
            toggleEditMode(); // Exit edit mode after saving
        });

        // Profile picture click listener, only triggers in edit mode
        binding.organizerImage.setOnClickListener(view -> {
            if (isEditing) {
                openImagePicker();
            }
        });

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
                binding.organizerImage.setImageURI(selectedImageUri);
                // Save the selected image URI to a database or cloud storage if needed
            }
        }
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
        binding.organizerImage.setClickable(isEditing);
    }

    private void setEditMode(boolean enabled) {
        binding.organizerName.setEnabled(enabled);
        binding.organizerEmail.setEnabled(enabled);
        binding.organizerPhone.setEnabled(enabled);
        binding.facilityName.setEnabled(enabled);

        int bgColor = enabled ? getResources().getColor(android.R.color.background_light) : getResources().getColor(android.R.color.transparent);

        binding.organizerName.setBackgroundColor(bgColor);
        binding.organizerEmail.setBackgroundColor(bgColor);
        binding.organizerPhone.setBackgroundColor(bgColor);
        binding.facilityName.setBackgroundColor(bgColor);

        if (enabled) {
            binding.organizerName.requestFocus();
        }
    }

    private void saveProfileData() {
        String name = binding.organizerName.getText().toString().trim();
        String address = binding.organizerEmail.getText().toString().trim();
        String email = binding.organizerPhone.getText().toString().trim();
        String phone = binding.facilityName.getText().toString().trim();
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
