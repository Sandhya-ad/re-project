package com.example.coffee2_app.Organizer_ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coffee2_app.DatabaseHelper;
import com.example.coffee2_app.Facility;
import com.example.coffee2_app.ImageGenerator;
import com.example.coffee2_app.OrganizerHomeActivity;
import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentOrganizerProfileBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Fragment for displaying and editing the organizer's profile and changing profile picture.
 */
public class ProfileFragment extends Fragment {

    private Facility facility;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FragmentOrganizerProfileBinding binding;
    private boolean isEditing = false;
    private FirebaseFirestore db;
    private String deviceID;
    private Bitmap bmp;

    /**
     * Inflates the layout, initializes Firestore and click listeners.
     *
     * @param inflater  LayoutInflater to inflate views in the fragment
     * @param container          Parent view to contain the fragment's UI
     * @param savedInstanceState Bundle containing the fragment's saved state
     * @return The root view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrganizerProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        OrganizerHomeActivity activity = (OrganizerHomeActivity) getActivity();
        if (activity != null) {
            facility = activity.getFacility();  // Get the Organizer instance
            deviceID = activity.getDeviceID();
        }

        if (facility != null) {
            displayOrganizerDetails();  // Populate fields with Organizer data
        } else {
           facility = new Facility(deviceID); // Case if Organizer is missing
            Log.d("test", "Created new Org");
            Toast.makeText(getActivity(), "Profile Error: Organizer data is missing.", Toast.LENGTH_SHORT).show();
        }

        // Hide Save Button by default
        // binding.editProfileButton.setVisibility(View.GONE);

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
                            if (facility.getName() != null) {
                                gen = new ImageGenerator(facility.getName());
                            }
                            else {
                                gen = new ImageGenerator("User");
                            }
                            bmp = gen.getImg();
                            binding.organizerImage.setImageBitmap(bmp);
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

    /**
     * Opens the image picker to select a new profile picture.
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Displays organizer details in the UI fields.
     * Gets profile picture from Firestore, or generates a default image.
     */
    private void displayOrganizerDetails() {
        if(facility.getName() != null) { binding.organizerName.setText(facility.getName()); }
        else { binding.organizerName.setText(""); } // Case where changes are cancelled on a null
        if(facility.getEmail() != null) { binding.organizerEmail.setText(facility.getEmail()); }
        else { binding.organizerEmail.setText(""); }
        if(facility.getAddress() != null) { binding.organizerAddress.setText(facility.getAddress()); }
        else { binding.organizerAddress.setText(""); }
        if (facility.getImageID() != null) {
            // If document doesn't exist, fallback to default photo
            DocumentReference doc = db.collection("images").document(facility.getImageID());
            doc.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("Firestore", "Document exists, loading picture.");

                        // If doc exists
                        String imageData = document.getString("imageData");
                        InputStream inputStream = new ByteArrayInputStream(Base64.decode(imageData, Base64.DEFAULT));
                        binding.organizerImage.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                    }
                    else {
                        // If doc doesn't exist, save default picture
                        Log.d("Firestore", "Document does not exist.");
                        ImageGenerator gen;
                        if (facility.getName() != null) {
                            gen = new ImageGenerator(facility.getName());
                            facility.setImage(gen.getImg());
                        }
                        else {
                            gen = new ImageGenerator("User");
                        }
                        binding.organizerImage.setImageBitmap(gen.getImg());
                    }
                }
                else {
                    Log.e("FirestoreError", "Image Failed: ", task.getException());
                }
            });
        }
        else {
            ImageGenerator gen;
            if (facility.getName() != null) {
                gen = new ImageGenerator(facility.getName());
                facility.setImage(gen.getImg());
            }
            else {
                gen = new ImageGenerator("User");
            }
            binding.organizerImage.setImageBitmap(gen.getImg());
        }
    }

    /**
     * Called when an activity result is returned, such as selecting an image.
     * Processes and displays the selected image if successful.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult().
     * @param resultCode The integer result code returned by the child activity.
     * @param data An Intent containing the data from the activity result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    bmp = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), selectedImageUri);
                    bmp = Bitmap.createScaledBitmap(bmp, 500, 500, false);
                    binding.organizerImage.setImageBitmap(bmp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Toggles between view and edit mode for the profile. Toggles visilibity of Save button.
     */
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

    /**
     * Enables or disables input fields for editing, and sets background colors accordingly.
     *
     * @param enabled True if edit mode is enabled, false otherwise.
     */
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

    /**
     * Validates and saves the profile data entered, updating organizer and Firebase
     */
    private void saveProfileData() {
        boolean ret = true;

        String name = binding.organizerName.getText().toString();
        String email = binding.organizerEmail.getText().toString();
        String address = binding.organizerAddress.getText().toString();

        if (!name.isEmpty()) { facility.setName(name); }
        else { ret = false; } // Name is a mandatory field so make a popup if empty
        facility.setEmail(email);
        facility.setAddress(address);

        if (bmp != null) {
            Log.d("ProfilePhoto", "Saved");
            facility.setImage(bmp);
        }

        if (facility.getImageID() == null) {
            ImageGenerator gen = new ImageGenerator(facility.getName());
            facility.setImage(gen.getImg());
        }

        //Just to check before the db is implemented
        //String message = "Profile saved:\nName: " + organizer.getName() + "\nEmail: " + organizer.getEmail() + "\nAdr: " + organizer.getAddress();

        //Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        if (ret) {
            toggleEditMode();
            DatabaseHelper.updateFacility(facility);
        }
        else {
            Toast.makeText(getActivity(), "Please set a name.", Toast.LENGTH_LONG).show();
        }
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
