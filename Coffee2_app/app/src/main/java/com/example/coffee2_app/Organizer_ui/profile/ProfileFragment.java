package com.example.coffee2_app.Organizer_ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.coffee2_app.EntrantHomeActivity;
import com.example.coffee2_app.Organizer;
import com.example.coffee2_app.OrganizerHomeActivity;
import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentOrganizerProfileBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private Organizer organizer;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FragmentOrganizerProfileBinding binding;
    private boolean isEditing = false;
    private FirebaseFirestore db;
    private String deviceID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrganizerProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        OrganizerHomeActivity activity = (OrganizerHomeActivity) getActivity();
        if (activity != null) {
            organizer = activity.getOrganizer();  // Get the Organizer instance
            deviceID = activity.getDeviceID();
        }

        if (organizer != null) {
            displayOrganizerDetails();  // Populate fields with Organizer data
        } else {
            organizer = new Organizer(deviceID); // Case if Organizer is missing
            Log.d("test", "Created new Org");
            Toast.makeText(getActivity(), "Profile Error: Organizer data is missing.", Toast.LENGTH_SHORT).show();
        }

        // Hide Save Button by default
        //binding.editProfileButton.setVisibility(View.GONE);

        // Back button listener
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        // Edit icon click listener to enter edit mode
        // binding.backButton.setOnClickListener(view -> toggleEditMode());

        binding.saveButton.setVisibility(View.GONE);

        // Save button click listener to save profile data
        binding.editProfileButton.setVisibility(View.VISIBLE);
        binding.editProfileButton.setOnClickListener(view -> {
            //saveProfileData(); // Commenting this so you can cancel changes
            toggleEditMode(); // Exit edit mode after saving
        });

        // Profile picture click listener, only triggers in edit mode
        binding.organizerImage.setOnClickListener(view -> {
            if (isEditing) {
                openImagePicker();
            }
        });

        binding.saveButton.setOnClickListener(view -> {
            saveProfileData();
        });

        return root;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void displayOrganizerDetails() {
        if(organizer.getName() != null) { binding.organizerName.setText(organizer.getName()); }
        else { binding.organizerName.setText(""); } // Case where changes are cancelled on a null
        if(organizer.getEmail() != null) { binding.organizerEmail.setText(organizer.getEmail()); }
        else { binding.organizerEmail.setText(""); }
        if(organizer.getAddress() != null) { binding.organizerAddress.setText(organizer.getAddress()); }
        else { binding.organizerAddress.setText(""); }
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
            binding.saveButton.setVisibility(View.VISIBLE); // Show Save button
        } else {
            // Exit edit mode, hide Save button
            setEditMode(false);
            binding.saveButton.setVisibility(View.GONE); // Hide Save button
        }

        // Enable or disable profile picture click based on edit mode
        binding.organizerImage.setClickable(isEditing);
    }

    private void setEditMode(boolean enabled) {
        binding.organizerName.setEnabled(enabled);
        binding.organizerEmail.setEnabled(enabled);
        binding.organizerAddress.setEnabled(enabled);

        int bgColor = enabled ? getResources().getColor(android.R.color.background_light) : getResources().getColor(android.R.color.transparent);

        binding.organizerName.setBackgroundColor(bgColor);
        binding.organizerEmail.setBackgroundColor(bgColor);
        binding.organizerAddress.setBackgroundColor(bgColor);


        if (enabled) {
            binding.organizerName.requestFocus();
        }
        else {
            displayOrganizerDetails();
        }
    }

    private void saveProfileData() {
        boolean ret = true;

        String name = binding.organizerName.getText().toString();
        String email = binding.organizerEmail.getText().toString();
        String address = binding.organizerAddress.getText().toString();

        if (!name.isEmpty()) { organizer.setName(name); }
        else { ret = false; } // Name is a mandatory field so make a popup if empty
        organizer.setEmail(email);
        organizer.setAddress(address);


        //Just to check before the db is implemented
        //String message = "Profile saved:\nName: " + organizer.getName() + "\nEmail: " + organizer.getEmail() + "\nAdr: " + organizer.getAddress();

        //Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        if (ret) {
            toggleEditMode();
        }
        else {
            Toast.makeText(getActivity(), "Please set a name.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
