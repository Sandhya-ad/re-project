package com.example.coffee2_app.Organizer_ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.coffee2_app.EntrantHomeActivity;
import com.example.coffee2_app.Image;
import com.example.coffee2_app.ImageGenerator;
import com.example.coffee2_app.MainActivity;
import com.example.coffee2_app.Organizer;
import com.example.coffee2_app.OrganizerHomeActivity;
import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentOrganizerProfileBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private Organizer organizer;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FragmentOrganizerProfileBinding binding;
    private boolean isEditing = false;
    private FirebaseFirestore db;
    private String deviceID;
    private Bitmap bmp;

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
                PopupMenu popupMenu = new PopupMenu(this.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.image_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.choose_image) {
                            openImagePicker();
                            return true;
                        }
                        else if (item.getItemId() == R.id.remove_image) {
                            ImageGenerator gen;
                            if (organizer.getName() != null) {
                                gen = new ImageGenerator(organizer.getName());
                            }
                            else {
                                gen = new ImageGenerator("User");
                            }
                            binding.organizerImage.setImageBitmap(gen.getImg());
                            bmp = gen.getImg();
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                });
                popupMenu.show();
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
                try {
                    bmp = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), selectedImageUri);
                    binding.organizerImage.setImageBitmap(bmp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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

        if (bmp != null) {
            Log.d("ProfilePhoto", "Saved");
            organizer.setImage(bmp);
        }

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
