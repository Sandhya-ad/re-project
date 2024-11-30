package com.example.coffee2_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

/**
 * Activity to set up or update the profile of an Organizer.
 * Allows the organizer to input personal information and select a profile photo.
 */
public class OrganizerProfileSetupActivity extends AppCompatActivity {
    private Facility organizer;
    private FirebaseFirestore db;
    private Bitmap bmp;
    private String deviceID;
    private EditText nameInput, emailInput, addressInput;
    private ImageView profilePhoto;

    /**
     * Initializes the activity, retrieves organizer data, sets up UI components, and defines button actions.
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_profile_setup);

        db = FirebaseFirestore.getInstance();

        // Initialize fields
        profilePhoto = findViewById(R.id.profile_photo);
        Button selectPhotoButton = findViewById(R.id.select_photo_button);
        nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);
        addressInput = findViewById(R.id.input_address);
        Button saveButton = findViewById(R.id.save_button);

        // Retrieve organizer data
        Intent intent = getIntent();
        organizer = (Facility) intent.getSerializableExtra("organizer");
        deviceID = organizer.getUserID();

        // Pre-fill data if available
        if (organizer != null) {
            nameInput.setText(organizer.getName());
            emailInput.setText(organizer.getEmail());
            addressInput.setText(organizer.getAddress());
        }

        // Back button functionality
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish()); // Finishes the activity to return to the previous screen

        // Set up photo selection
        selectPhotoButton.setOnClickListener(v -> selectPhoto());

        // Save data
        saveButton.setOnClickListener(v -> saveProfileData());
    }

    /**
     * Launches an intent to allow the user to pick a profile photo from external storage.
     */
    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerLauncher.launch(intent);
    }

    /**
     * Validates and saves the organizer's profile data.
     * Ensures required fields are filled and saves data to Firestore.
     * Redirects to Organizer home screen if save is successful.
     */
    private void saveProfileData() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();

        // Validate required fields
        if (name.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Name and address are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email if provided
        if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update organizer profile
        organizer.setName(name);
        organizer.setAddress(address);
        if (!email.isEmpty()) {
            organizer.setEmail(email);
        }

        if (bmp != null) {
            Log.d("ProfilePhoto", "Saved");
            organizer.setImage(bmp);
        }

        // Save to Firestore
        DatabaseHelper.updateFacility(organizer);

        redirectToHome(organizer);
    }

    /**
     * Redirects the user to the Organizer home activity after profile setup is complete.
     *
     * @param organizer The Organizer object with updated profile information.
     */
    private void redirectToHome(Facility organizer) {
        Intent intent = new Intent(OrganizerProfileSetupActivity.this, OrganizerHomeActivity.class);
        intent.putExtra("organizer", organizer);
        intent.putExtra("deviceID", deviceID);
        startActivity(intent);
        finish();
    }

    /**
     * ActivityResultLauncher to handle the result of the photo picker.
     * Sets the selected photo Bitmap to the ImageView.
     */
    private final ActivityResultLauncher<Intent> photoPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        try {
                            bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            bmp = Bitmap.createScaledBitmap(bmp, 500, 500, false); // Scale the image
                            profilePhoto.setImageBitmap(bmp); // Set Bitmap to ImageView
                        } catch (IOException e) {
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    /**
     * Overrides the onBackPressed method to ensure the activity finishes when the back button is pressed,
     * providing consistent back-navigation behavior.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Finishes the activity to go back to the previous screen
    }
}
